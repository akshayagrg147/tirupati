package com.tirupati.vendor.fragmnts

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap.CompressFormat
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tirupati.vendor.R
import com.tirupati.vendor.adapters.DeliveryTermsAdapter
import com.tirupati.vendor.adapters.PaymentTermsAdapter
import com.tirupati.vendor.adapters.SpinnerAdapter
import com.tirupati.vendor.databinding.VendorQuotationFormBinding
import com.tirupati.vendor.helper.SessionManager
import com.tirupati.vendor.model.ResponseDataItem
import com.tirupati.vendor.model.UOMData
import com.tirupati.vendor.model.vendorQuoationRequest
import com.tirupati.vendor.network.NetworkState
import com.tirupati.vendor.ui.LandingVendorSActivity
import com.tirupati.vendor.utils.ActivityUtils
import com.tirupati.vendor.utils.ImageOrientationHelper
import com.tirupati.vendor.viewmodels.GatekeeperListViewModel
import com.tirupati.vendor.viewmodels.VendorFormViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.apache.commons.io.FileUtils
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class VendorQotationFragment : Fragment() {
    private var binding: VendorQuotationFormBinding? = null
    private var currentPictureFile: File? = null
    private lateinit var videoCaptureLauncher: ActivityResultLauncher<Intent>
    private val gateKeeperVm: GatekeeperListViewModel by viewModels()
    var multipart: MultipartBody.Part ?=null



    private val vendorFormVM: VendorFormViewModel by viewModels()
    @Inject
    lateinit var sessionManager: SessionManager
    lateinit var deliverTerm:String
    lateinit var itemDetail:String
    lateinit var paymentTerm:String
    lateinit var itemId:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = VendorQuotationFormBinding.inflate(inflater, container, false)
        binding?.btnAddPurchase?.setOnClickListener { onSubmitClicked() }
        binding?.uploadDocument?.setOnClickListener{
            selectImage()

        }
        binding?.thumbnail?.setOnClickListener {
            selectImage()
        }



            return binding!!.root
    }
   var  requestPermissionsLauncher:ActivityResultLauncher<Array<String>> = registerForActivityResult(
    ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.CAMERA] == true &&
            permissions[Manifest.permission.RECORD_AUDIO] == true &&
            permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true) {
            // All permissions are granted
            dispatchTakeVideoIntent()

            // Handle the case where permissions are not granted
        }
    }
    var cameraResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            object : ActivityResultCallback<ActivityResult?> {
                override fun onActivityResult(result: ActivityResult?) {
                    val contentUri: Uri? = if (Build.VERSION.SDK_INT >= 24) {
                        FileProvider.getUriForFile(
                            context!!,
                            context!!.packageName + ".provider",
                            currentPictureFile!!
                        )
                    } else {
                        Uri.fromFile(currentPictureFile)
                    }
                    try {
                        val orientation: Int =
                            ImageOrientationHelper.getOrientation(context, contentUri)
                        var bitmap = MediaStore.Images.Media.getBitmap(
                            activity!!.contentResolver,
                            contentUri
                        )
                        bitmap = ActivityUtils.scaleBitmap(bitmap, 500F, 500F)
                        bitmap = ImageOrientationHelper.rotateBitmap(bitmap, orientation)
                        val compressFormat = CompressFormat.JPEG
                        val quality = 80
                        val byteArrayOutputStream = ByteArrayOutputStream()
                        bitmap.compress(compressFormat, quality, byteArrayOutputStream)
                        var image: String = ActivityUtils.getBASE64Image(bitmap)
                        image = "data:image/png;base64,$image"
                        binding?.thumbnail?.visibility=View.VISIBLE
                        binding?.uploadDocument?.visibility=View.GONE
                        binding?.thumbnail?.setImageBitmap(bitmap)
                        val imageByteArray = byteArrayOutputStream.toByteArray()

                        // Create RequestBody from byte array

                        // MultipartBody.Part is used to send also the actual file name
                        val file2 = getImageFromUri(Uri.fromFile(File(currentPictureFile?.absolutePath)))
                        val requestFileNew = file2?.asRequestBody("image/*".toMediaTypeOrNull())
                        val part = MultipartBody.Part.createFormData("QUOTATION_ATTACHMENT[]", file2?.name, requestFileNew!!)
                        multipart=part


                      //  uploadImageToserver(image)
                    } catch (e: java.lang.Exception) {
                    }
                }
            })

    private fun getImageFromUri(imageUri: Uri?): File? {
        imageUri?.let { uri ->
            val mimeType = getMimeType(requireContext(), uri)
            mimeType?.let {
                val file = createTmpFileFromUri(requireContext(), imageUri, "temp_image", ".$it")
                file?.let { Log.d("image Url = ", file.absolutePath) }
                return file
            }
        }
        return null
    }
    private fun createTmpFileFromUri(
        context: Context,
        uri: Uri,
        fileName: String,
        mimeType: String
    ): File? {
        return try {
            val stream = context.contentResolver.openInputStream(uri)
            val file = File.createTempFile(fileName, mimeType,requireContext().cacheDir)
            FileUtils.copyInputStreamToFile(stream, file)
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    private fun getMimeType(context: Context, uri: Uri): String? {
        //Check uri format to avoid null
        val extension: String? = if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            //If scheme is a content
            val mime = MimeTypeMap.getSingleton()
            mime.getExtensionFromMimeType(context.contentResolver.getType(uri))
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(File(uri.path)).toString())
        }
        return extension
    }

    private fun dispatchTakeVideoIntent() {
        val takeVideoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        if (takeVideoIntent.resolveActivity(requireContext().packageManager) != null) {
            videoCaptureLauncher.launch(takeVideoIntent)
        }
    }
    private fun selectImage() {
        val options = arrayOf<CharSequence>(
            "Choose Video",
          "Choose Camera",
            "Cancel"
        )
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Add Photo/Video!")
        builder.setItems(options) { dialog, item ->
            //if (options[item].equals("Take Photo")) {
            //  Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            //startActivityForResult(cameraIntent, 0);
            /*Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);//zero can be replaced with any action code (called requestCode)
               */
            //}
            if (options[item] ==  "Choose Camera") {

                // Check if permissions are granted
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    // Permission is already granted, proceed with your task
                    openCamera()
                } else {
                    // Permission is not granted, request it
                    requestPermissions()
                }

            }
            else if (options[item]=="Choose Video"){
                checkPermissionsAndLaunch()
            }
            else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }
    private fun checkPermissionsAndLaunch() {
        when {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&

                    ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED -> {
                // Permissions are already granted
                dispatchTakeVideoIntent()
            }
            else -> {
                // Request the permissions
                requestPermissionsLauncher.launch(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                )
            }
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        var contentUri: Uri? = null
        currentPictureFile = getMediaFile(requireContext())
        contentUri = if (Build.VERSION.SDK_INT >= 24) {
            requireContext().packageName
            FileProvider.getUriForFile(
                requireContext(),
                requireContext().packageName + ".provider",
                currentPictureFile!!
            )
        } else {
            Uri.fromFile(currentPictureFile)
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri)
        cameraResultLauncher.launch(intent)
    }
    private val requestPermissionLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { isGranted ->
            if (isGranted.containsValue(false)) {
                // Permission denied, handle accordingly
            } else {
              openCamera()
            }
        }

    private fun requestPermissions() {
        val permissions = arrayOf(Manifest.permission.CAMERA)
        requestPermissionLauncher.launch(permissions)
    }

    fun getMediaFile(context: Context): File? {
        val cw = ContextWrapper(context)
        val mediaStorageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        Log.e("MyCameraApp", mediaStorageDir!!.absolutePath)
        return try {
            if (!mediaStorageDir!!.exists()) {
                if (!mediaStorageDir!!.mkdirs()) {
                    Log.d("MyCameraApp", "failed to create directory")
                    return null
                }
            }
            // Create a media file name
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.UK).format(Date())
            File(mediaStorageDir!!.path + File.separator + "MOXIAM_" + timeStamp + ".jpg") //File.separator + "Images" +
        } catch (e: Exception) {
            null
        }
    }

    private fun onSubmitClicked() {
        val rate = binding?.rate?.text.toString().trim()
        val quantity = binding?.quantity?.text.toString().trim()
        val totalAmount = binding?.amountTotal?.text.toString().trim()
        val remarks = binding?.remarks?.text.toString().trim()

        val header = HashMap<String, String>()
        header["Accept"] = "application/json"
        header["version"] = "1"
        header["Authorization"] = "${sessionManager.loginToken}"
        header["userID"]="${sessionManager.user?.RESPONSEDATA?.USER_ID}"
        lifecycleScope.launch {
            var response = gateKeeperVm.saveVendorQuationR(header, multipart,vendorQuoationRequest(UOM_ID = itemDetail, TOTAL_AMOUNT = totalAmount, DELIVERY_TERMS = deliverTerm, REMARKS = remarks, QUANTITY = quantity, PAYMENT_TERMS = paymentTerm, RATE = rate, ITEM_ID = itemId))

            when (response) {

                is NetworkState.Success -> {
                    if(response.body.STATUS){
                        Toast.makeText(context,"updated",Toast.LENGTH_SHORT).show()
                        findNavController(). popBackStack()

                    }




                }

                is NetworkState.Error<*> -> {
                     Toast.makeText(context,"something went wrong",Toast.LENGTH_SHORT).show()
                }

                is NetworkState.NetworkException -> {
                }

                is NetworkState.HttpErrors.InternalServerError -> {
                }

                is NetworkState.HttpErrors.ResourceNotFound -> {
                }

                else -> {
                }
            }


        }




    }
    private fun getuomList() {

        lifecycleScope.launch {
            var response = vendorFormVM.getUomList()

            when (response) {

                is NetworkState.Success->{

                    itemDetail=response.body.RESPONSEDATA[0].UOMID
                    binding?.itemDetail?.setText(response.body.RESPONSEDATA[0].UOMCODE)

                    val customDropDownAdapter3 =
                        DeliveryTermsAdapter(requireContext(),  response.body.RESPONSEDATA)
                    binding?.itemDetail?.setAdapter(customDropDownAdapter3)


                    binding?.itemDetail?.threshold=1
                    binding?.itemDetail?.setKeyListener(null);
                    binding?.itemDetail?.setOnClickListener {
                        (it as AutoCompleteTextView).showDropDown()
                    }
                    binding?.itemDetail?.setOnFocusChangeListener { v, hasFocus ->
                        if (hasFocus) {
                            (v as AutoCompleteTextView).showDropDown()
                        }
                    }

                    binding?.itemDetail?.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
                        val selectedModel = parent.adapter.getItem(position) as UOMData
                        if (selectedModel != null) {
                            itemDetail=selectedModel.UOMID
                            binding?.itemDetail?.setText(selectedModel.UOMCODE)
                        }
                    }

                }

                is NetworkState.Error<*>->{
                    // Toast.makeText(context,response.msg.toString(),Toast.LENGTH_SHORT).show()
                }

                is NetworkState.NetworkException->{
                }
                is NetworkState.HttpErrors.InternalServerError->{
                }
                is NetworkState.HttpErrors.ResourceNotFound->{
                }
                else->{
                }
            }


        }
    }
    override fun onResume() {
        super.onResume()
        LandingVendorSActivity.showIcon(false)
        LandingVendorSActivity.changeTitle("Vendor Quotation Form")


    }

    override fun onPause() {
        super.onPause()
        LandingVendorSActivity.showIcon(false)
        LandingVendorSActivity.changeTitle("Vendor Quotation Form")

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getuomList()
        getPaymentTerms()
        getItemDetails()

        binding?.vendorName?.setText(sessionManager.user?.RESPONSEDATA?.NAME_OF_OWNER)
        binding?.organisationName?.setText(sessionManager.user?.RESPONSEDATA?.NAME_OF_ORGANIZATION)
        videoCaptureLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val videoUri: Uri? = result.data?.data
                videoUri?.let {
                    displayVideoThumbnail(requireContext(),it, binding?.thumbnail)
                }


                // Handle the videoUri, like playing the video or saving it
            }
        }

    }

    private fun getItemDetails() {
        deliverTerm="Freight Paid"
        val accountType: ArrayList<String> = ArrayList()
        accountType.add( "Freight Paid")
        accountType.add("Freight To Pay")
        binding?.deliveryTerms?.setText(deliverTerm)

        val spinnerAdapter = SpinnerAdapter(requireActivity(), R.layout.item_spinner_row, accountType)
        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner_row)
        binding?.deliveryTerms?.setAdapter(spinnerAdapter);
        // Remove setting key listener to null
        // bindingSecondPage?.statesList?.setKeyListener(null);
        binding?.deliveryTerms?.threshold=1
        binding?.deliveryTerms?.setKeyListener(null);
        binding?.deliveryTerms?.setOnClickListener {
            (it as AutoCompleteTextView).showDropDown()
        }
        binding?.deliveryTerms?.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                (v as AutoCompleteTextView).showDropDown()
            }
        }

        binding?.deliveryTerms?.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            val selectedModel = parent.adapter.getItem(position) as String
            // Do whatever you want with the selected model object here
            binding?.deliveryTerms?.setText(selectedModel)
        }


    }

    private fun displayVideoThumbnail(context: Context, uri: Uri, imageView: ImageView?) {
        binding?.thumbnail?.visibility=View.VISIBLE
        binding?.uploadDocument?.visibility=View.GONE
        try {
            val mediaMetadataRetriever = MediaMetadataRetriever()
            mediaMetadataRetriever.setDataSource(context, uri)
            imageView?.setImageBitmap( mediaMetadataRetriever.frameAtTime)

        } catch (ex: Exception) {
            Toast
                .makeText(context, "Error retrieving bitmap", Toast.LENGTH_SHORT)
                .show()
        }




    }

    private fun getRealPathFromURI(context: Context, contentUri: Uri): String {
        var result: String? = null
        val cursor = context.contentResolver.query(contentUri, null, null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val index = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                if (index != -1) {
                    result = cursor.getString(index)
                }
            }
            cursor.close()
        }
        return result ?: contentUri.path ?: ""
    }

    private fun getPaymentTerms() {
        lifecycleScope.launch {
            val header = HashMap<String, String>()
            header["Accept"] = "application/json"
            header["version"] = "1"
            header["Authorization"] = "${sessionManager.loginToken}"
            header["userID"]="${sessionManager.user?.RESPONSEDATA?.USER_ID}"
            var response = vendorFormVM.getPaymentTermItemList(header)

            when (response) {

                is NetworkState.Success->{
                    var clicked:Boolean=true

                    // Assuming you have a PaymentTermsAdapter that takes a context and a list of ResponseDataItem
                    val customDropDownAdapter3 = PaymentTermsAdapter(requireContext(), response.body.RESPONSEDATA)
                    val initialItem = response.body.RESPONSEDATA[0]

// Set initial values
                    paymentTerm = initialItem.NAME
                    binding?.paymentTerms?.setText(paymentTerm)
                    itemId = initialItem.ITEMID
                    binding?.hsnSacCode?.setText(initialItem.HSNDESCRIPTION)

// Set the adapter to the AutoCompleteTextView
                    binding?.paymentTerms?.setAdapter(customDropDownAdapter3)

                    binding?.paymentTerms?.threshold=1
                    binding?.paymentTerms?.setKeyListener(null);
                    binding?.paymentTerms?.setOnClickListener {
                        (it as AutoCompleteTextView).showDropDown()
                    }

                    binding?.paymentTerms?.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
                        val selectedItem = parent.adapter.getItem(position) as ResponseDataItem
                        // Do whatever you want with the selected model object here
                        if (selectedItem != null) {
                            paymentTerm = selectedItem.NAME
                            clicked=true
                            itemId = selectedItem.ITEMID
                            binding?.paymentTerms?.setText(paymentTerm)
                            binding?.hsnSacCode?.setText(selectedItem.HSNDESCRIPTION)
                        }
                    }










                }

                is NetworkState.Error<*>->{
                    // Toast.makeText(context,response.msg.toString(),Toast.LENGTH_SHORT).show()
                }

                is NetworkState.NetworkException->{
                }
                is NetworkState.HttpErrors.InternalServerError->{
                }
                is NetworkState.HttpErrors.ResourceNotFound->{
                }
                else->{
                }
            }


        }

    }

}