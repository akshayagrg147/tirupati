package com.tirupati.vendor.fragmnts

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.tirupati.vendor.R
import com.tirupati.vendor.databinding.FragmentUploadPhotosPOIDBinding
import com.tirupati.vendor.helper.SessionManager
import com.tirupati.vendor.helper.hidden
import com.tirupati.vendor.helper.loadFromUrlWithRoundedCorners
import com.tirupati.vendor.helper.showCustomDialog
import com.tirupati.vendor.helper.shown
import com.tirupati.vendor.model.VendorRESPONSEDATAX
import com.tirupati.vendor.network.NetworkState
import com.tirupati.vendor.utils.toast
import com.tirupati.vendor.viewmodels.GatekeeperListUploadsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class UploadPhotosPOIDFragment : Fragment() {
    private var bindingUploads: FragmentUploadPhotosPOIDBinding? = null
    private val uploadViewModel: GatekeeperListUploadsViewModel by viewModels()
    lateinit var bundledData: Bundle
    @Inject
    lateinit var sessionManager: SessionManager

    //    private lateinit var currentPhotoPath: String
    var file: File? = null
    var cacheDir: File? = null
    var selectedPaths: String = ""
    var photoFile: File? = null

    var mCurrentPhotoPath: String? = null
    val RC_TAKE_PHOTO: Int = 1
    private var petProfileImageMultipart: MultipartBody.Part? = null



    private val images1 = ArrayList<File>()
    private val images2 = ArrayList<File>()
    private val images3 = ArrayList<File>()
    private val images4 = ArrayList<File>()
    private val images5 = ArrayList<File>()
    private val images6 = ArrayList<File>()
    private val images7 = ArrayList<File>()

    private var image1:String = ""
    private var image1clicked:Boolean = false
    private var first:ArrayList<MultipartBody.Part?>?=null

    private var image2:String = ""
    private var image2clicked:Boolean = false
    private var second:ArrayList<MultipartBody.Part?>?=null

    private var image3:String = ""
    private var image3clicked:Boolean = false
    private var third:ArrayList<MultipartBody.Part?>?=null

    private var image4:String = ""
    private var image4clicked:Boolean = false
    private var fourth:ArrayList<MultipartBody.Part?>?=null

    private var image5:String = ""
    private var image5clicked:Boolean = false
    private var fifth:ArrayList<MultipartBody.Part?>?=null

    private var image6:String = ""
    private var image6clicked:Boolean = false
    private var sixth:ArrayList<MultipartBody.Part?>?=null


    private var image7:String = ""
    private var image7clicked:Boolean = false
    private var seventh:ArrayList<MultipartBody.Part?>?=null

    var userForm:VendorRESPONSEDATAX? =null
    var purchaseNo= ""
    var billNo=""
    var veh_No= ""
    var billDate=""

    private var imageCapture: ImageCapture? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        if (args != null) {

            bundledData = args
            Log.d("bndldata", bundledData.toString())

            userForm = args.getParcelable<VendorRESPONSEDATAX>("user_selected")
            purchaseNo=args.getString("vendor_pod","")
            billNo = args.getString("bill_no","")
            veh_No=args.getString("vehicle_no","")
            billDate=   args.getString("dateOfEntry","")
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingUploads = FragmentUploadPhotosPOIDBinding.inflate(inflater, container, false)


        bindingUploads!!.btnSaveDone.setOnClickListener {

            if (validateUI(bindingUploads!!)) {
//                toast("Hit Server")
            uploadALlToServer()
               }
        }

        bindingUploads!!.uploadVInvoice.setOnClickListener {
//            dispatchTakePictureIntent()
            image1clicked=true
            image2clicked=false
            image3clicked=false
            image4clicked=false
            image5clicked=false
            image6clicked=false
            image7clicked = false
//            takePhoto()
//            dispatchTakePictureIntent()
            capturePhoto()
        }

        bindingUploads!!.eWayBillUpload.setOnClickListener{
            image1clicked=false
            image2clicked=true
            image3clicked=false
            image4clicked=false
            image5clicked=false
            image6clicked=false
            image7clicked=false
            capturePhoto()
        }

        bindingUploads!!.eInvoice.setOnClickListener {
            image1clicked=false
            image2clicked=false
            image3clicked = true
            image4clicked=false
            image5clicked=false
            image6clicked=false
            image7clicked=false
            capturePhoto()

        }

        bindingUploads!!.vWeightSlip.setOnClickListener {
            image1clicked=false
            image2clicked=false
            image3clicked = false
            image4clicked=true
            image5clicked=false
            image6clicked=false
            image7clicked=false


            capturePhoto()

        }

        bindingUploads!!.slipBhiwadi.setOnClickListener {
            image5clicked = true
            image1clicked=false
            image2clicked=false
            image3clicked = false
            image4clicked=false
            image5clicked=true
            image6clicked=false
            image7clicked=false

            capturePhoto()
        }

        bindingUploads!!.transporterBitly.setOnClickListener {
            image1clicked=false
            image2clicked=false
            image3clicked = false
            image4clicked=false
            image5clicked=false
            image6clicked = true
            image7clicked = false
            capturePhoto()
//            dispatchTakePictureIntent()
        }


        bindingUploads!!.tollReceipt.setOnClickListener {
            image1clicked=false
            image2clicked=false
            image3clicked = false
            image4clicked=false
            image5clicked=false
            image6clicked = false
            image7clicked = true
            capturePhoto()
//            dispatchTakePictureIntent()
        }


        // Set up RecyclerView
       /* val layoutManager = LinearLayoutManager(requireContext())
        bindingUploads!!.vendorInvoice.layoutManager = layoutManager
        val adapter = ImageAdapter(images)
        bindingUploads!!.vendorInvoice.adapter = adapter

*/

        // Inflate the layout for this fragment
        return bindingUploads!!.root
    }


    private fun validateUI(binding: FragmentUploadPhotosPOIDBinding): Boolean {

        var status = false
        if (images1.isEmpty()) {
            showCustomDialog(requireContext(),"Please Upload Invoice of Vendor","Error!")
            status = false
        }
        else if(images2.isEmpty()){
            showCustomDialog(requireContext(),"Please Upload E-Way Bill","Error!")
            status = false
        }
        else if(images3.isEmpty()){
            showCustomDialog(requireContext(),"Please Upload E-Invoice", "Error!")
            status = false
        }
        else if(images4.isEmpty()){
            showCustomDialog(requireContext(),"Please Upload Vendor Weighting Slip","Error!")
            status = false
        }
        else if(images5.isEmpty()){
            showCustomDialog(requireContext(),"Please Upload Weighting Slip of Bhiwadi","Error!")
            status = false
        }
        else if(images6.isEmpty()){
            showCustomDialog(requireContext(),"Please Upload Transporter Bilty","Error!")
            status = false
        }
        else if(images7.isEmpty()){
            showCustomDialog(requireContext(),"Please Upload Toll Receipt","Error!")
            status = false
        }
        else {
            return true
        }
        return status
    }

    fun filesToMultipartParts(keyName:String,imageFiles: List<File>): ArrayList<MultipartBody.Part?> {
        val parts = ArrayList<MultipartBody.Part?>()
        for (file in imageFiles) {
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData(keyName, file.name, requestFile)
            parts.add(part)
        }
        return parts
    }
    private fun uploadALlToServer() {

            lifecycleScope.launch {
                bindingUploads!!.loginProgressBar.progressBar.shown()

                val header = HashMap<String, String>()
                header["Accept"] = "application/json"
                header["version"] = "1"
                header["Authorization"] = "${sessionManager.loginToken}"
                header["userID"]="${sessionManager.user?.RESPONSEDATA?.USER_ID}"
                val response =
                    uploadViewModel.postUploadsGatekeeper(
                        header,
                        userForm?.VID!!,purchaseNo,billNo,veh_No,billDate,
                       first!!,second!!,third!!,fourth!!,fifth!!,sixth!!,seventh!!
                        )

                when (response) {

                    is NetworkState.Success -> {
                        bindingUploads!!.loginProgressBar.progressBar.hidden()

                        toast("${response.body.MESSAGE}")
                        val args = Bundle()

                        Navigation.findNavController(bindingUploads!!.root).
                        navigate(R.id.action_photoFragment_to_gateEntryFragment, args)
                    }

                    is NetworkState.Error<*> -> {
                        bindingUploads!!.loginProgressBar.progressBar.hidden()

                        // Toast.makeText(context,response.msg.toString(),Toast.LENGTH_SHORT).show()
                    }

                    is NetworkState.NetworkException -> {
                        bindingUploads!!.loginProgressBar.progressBar.hidden()

                    }

                    is NetworkState.HttpErrors.InternalServerError -> {
                        bindingUploads!!.loginProgressBar.progressBar.hidden()

                    }

                    is NetworkState.HttpErrors.ResourceNotFound -> {
                        bindingUploads!!.loginProgressBar.progressBar.hidden()

                    }

                    else -> {
                        bindingUploads!!.loginProgressBar.progressBar.hidden()

                    }
                }


            }


    }


    /*  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
          super.onActivityResult(requestCode, resultCode, data)
          if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
              images.add(Uri.parse(currentPhotoPath))
              bindingUploads!!.vendorInvoice.adapter?.notifyItemInserted(images.size - 1)
          }
      }*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            photoFile?.let {
//                bindingUploads..loadFromUrlWithRoundedCorners(
//                    it.absolutePath
//                )
                selectedPaths = it.absolutePath
                if(image1clicked){
                    val file1 =getImageFromUri(Uri.fromFile(File(selectedPaths)))

                    images1.add(file1!!)
                   first= filesToMultipartParts("UPLOAD_INVOICEOFVENDOR[]",images1)
                    bindingUploads?.vendorInvoice?.visibility = View.GONE
                    bindingUploads!!.vendorImageRL.visibility = View.VISIBLE
                    bindingUploads!!.InvoiceimageView.setImageURI(Uri.fromFile(File(selectedPaths)))

                    bindingUploads!!.delete1Button.setOnClickListener {
                        bindingUploads?.vendorInvoice?.visibility = View.VISIBLE
                        bindingUploads!!.vendorImageRL.visibility = View.GONE
                    }

                }
                else if(image2clicked){
                    val file2 = getImageFromUri(Uri.fromFile(File(selectedPaths)))

                    images2.add(file2!!)
                    second= filesToMultipartParts("UPLOAD_EWAYBILL[]",images2)

                    bindingUploads?.eWayBillLL?.visibility = View.GONE
                    bindingUploads!!.eWayBillRL.visibility = View.VISIBLE
                    bindingUploads!!.ewayBilimageView.loadFromUrlWithRoundedCorners(selectedPaths)
                    bindingUploads!!.deleteEwayButton.setOnClickListener {
                        bindingUploads?.eWayBillRL?.visibility = View.VISIBLE
                        bindingUploads!!.eWayBillLL.visibility = View.GONE
                    }
//                ewayBilimageView
//                deleteEwayButton
//                bindingUploads?.eWayBillRL
//                bindingUploads?.eWayBillLL
                }
                else if (image3clicked){
                    val file3 = getImageFromUri(Uri.fromFile(File(selectedPaths)))

                    images3.add(file3!!)
                    third= filesToMultipartParts("UPLOAD_EINVOICE[]",images3)

                    bindingUploads?.eInvLl?.visibility = View.GONE
                    bindingUploads!!.eInvoiceRL.visibility = View.VISIBLE
//                        bindingUploads!!.eInvoiceimageView.setImageURI(Uri.fromFile(File(selectedPaths)))
                    bindingUploads!!.eInvoiceimageView.setImageURI(Uri.fromFile(File(selectedPaths)))
                    bindingUploads!!.deleteeInvoice.setOnClickListener {
                        bindingUploads?.eInvLl?.visibility = View.VISIBLE
                        bindingUploads!!.eInvoiceRL.visibility = View.GONE
                    }


                }
                else if(image4clicked){
                    val file4 = getImageFromUri(Uri.fromFile(File(selectedPaths)))

                    images4.add(file4!!)
                    fourth= filesToMultipartParts("UPLOAD_VENDORWEIGHTINGSLIP[]",images4)

                    bindingUploads?.vendorWeightLL?.visibility = View.GONE
                    bindingUploads!!.vendorWeightRL.visibility = View.VISIBLE
                    bindingUploads!!.weightSlip.setImageURI(Uri.fromFile(File(selectedPaths)))
                    bindingUploads!!.deleteWeightSlip.setOnClickListener {
                        bindingUploads?.vendorWeightLL?.visibility = View.VISIBLE
                        bindingUploads!!.vendorWeightRL.visibility = View.GONE

                    }

                }
                else if (image5clicked){
                    val file5 = getImageFromUri(Uri.fromFile(File(selectedPaths)))

                    images5.add(file5!!)
//                imageslipBhiwadi
//                deleteBhiwadi
//                bindingUploads?.vendorWeightBhiRL
//                bindingUploads?.vWeightBhiLL
                    fifth= filesToMultipartParts("UPLOAD_WEIGHTINGSLIPOFBHIWADI[]",images5)


                    bindingUploads?.vWeightBhiLL?.visibility = View.GONE
                    bindingUploads!!.vendorWeightBhiRL.visibility = View.VISIBLE
                    bindingUploads!!.imageslipBhiwadi.setImageURI(Uri.fromFile(File(selectedPaths)))
                    bindingUploads!!.deleteBhiwadi.setOnClickListener {
                        bindingUploads?.vWeightBhiLL?.visibility = View.VISIBLE
                        bindingUploads!!.vendorWeightBhiRL.visibility = View.GONE

                    }

                }
                else if (image6clicked){
//                removeImgtransportBilty
//                ImgtransportBilty
//                bindingUploads?.transporterBiltyRL
//                bindingUploads?.transporterBiltyLL
                    val file6 = getImageFromUri(Uri.fromFile(File(selectedPaths)))

                    images6.add(file6!!)
                    sixth= filesToMultipartParts("UPLOAD_TRANSPORTBILTY[]",images6)

                    bindingUploads?.transporterBiltyLL?.visibility = View.GONE
                    bindingUploads!!.transporterBiltyRL.visibility = View.VISIBLE
                    bindingUploads!!.ImgtransportBilty.setImageURI(Uri.fromFile(File(selectedPaths)))
                    bindingUploads!!.removeImgtransportBilty.setOnClickListener {
                        bindingUploads?.vWeightBhiLL?.visibility = View.GONE
                        bindingUploads!!.transporterBiltyRL.visibility = View.VISIBLE

                    }

                }
                else if (image7clicked){

//
//                    tollReceiptRL
//                    ImgTollReceipt
//                    removeImgTollReceipt
//                    tollReceiptLL
                    val file7 = getImageFromUri(Uri.fromFile(File(selectedPaths)))

                    images7.add(file7!!)
                    sixth= filesToMultipartParts("UPLOAD_TOLLRECEIPT[]",images6)

                    bindingUploads?.tollReceiptLL?.visibility = View.GONE
                    bindingUploads!!.tollReceiptRL.visibility = View.VISIBLE
                    bindingUploads!!.ImgTollReceipt.setImageURI(Uri.fromFile(File(selectedPaths)))
                    bindingUploads!!.removeImgTollReceipt.setOnClickListener {
                        bindingUploads?.tollReceiptRL?.visibility = View.GONE
                        bindingUploads!!.tollReceiptLL.visibility = View.VISIBLE

                    }

                }


                else{
                    //Do nothing
                }



//                bindingUploads!!.vendorInvoice.adapter?.notifyItemInserted(images.size - 1)





            }
        } else {
            toast("Request cancelled or something went wrong.")
        }
    }

    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }



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

    private fun deleteTempFiles(file: File = requireContext().cacheDir!!): Boolean {
        if (file.isDirectory) {
            val files = file.listFiles()
            if (files != null) {
                for (f in files) {
                    if (f.isDirectory) {
                        deleteTempFiles(f)
                    } else {
                        f.delete()
                    }
                }
            }
        }
        return file.delete()
    }

    private fun capturePhoto() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                0
            )
        } else {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
                try {
                    photoFile = createImageFile()
                    if (photoFile != null) {
                        val photoURI = FileProvider.getUriForFile(
                            requireContext(),
                            requireContext().applicationContext.packageName + ".provider_paths",
                            photoFile!!
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, RC_TAKE_PHOTO)
                    } else {
                        Log.e("CameraError", "Photo file is null")
                    }
                } catch (ex: Exception) {
                    Log.e("CameraError", "Exception: ${ex.message}")
                    toast(ex.message.toString())
                }
            } else {
                Log.e("CameraError", "No camera app found")
                toast("Error")
            }
        }

    }
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireContext().packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.tirupati.vendor.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, RC_TAKE_PHOTO)
                }
            }
        }
    }

    private val selectImagesActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                //If single image selected
                if (data?.data != null) {
                    val imageUri: Uri? = data.data
                    val file = getImageFromUri(imageUri)
                    file?.let {

                        selectedPaths= it.absolutePath
                    }
                }
            }
        }

}