package com.tirupati.vendor.fragmnts



import SaveReportRequest
import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import com.tirupati.vendor.adapters.ReportIssueBatchAdapter
import com.tirupati.vendor.adapters.ReportIssueCoilAdapter
import com.tirupati.vendor.databinding.FragmentReportIssueBinding
import com.tirupati.vendor.helper.SessionManager
import com.tirupati.vendor.helper.hidden
import com.tirupati.vendor.helper.showCustomDialog
import com.tirupati.vendor.helper.shown
import com.tirupati.vendor.model.BatchDetail
import com.tirupati.vendor.model.ResponseData
import com.tirupati.vendor.model.ResponseDataCustomer
import com.tirupati.vendor.network.NetworkState
import com.tirupati.vendor.ui.CustomerHomeActivity
import com.tirupati.vendor.utils.ActivityUtils
import com.tirupati.vendor.utils.ImageOrientationHelper
import com.tirupati.vendor.viewmodels.GatekeeperListViewModel
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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReportIssueFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ReportIssueFragment : Fragment() {

    private var responseData: ResponseDataCustomer? = null
    private val gateKeeperVm: GatekeeperListViewModel by viewModels()
    private var currentPictureFile: File? = null
    var latitude:Double?=0.00
    var longitude:Double?=0.00
    var multipart: MultipartBody.Part ?=null
    @Inject
    lateinit var sessionManager: SessionManager
    private lateinit var videoCaptureLauncher: ActivityResultLauncher<Intent>
    var objectResponseData: ResponseData?=null
    private var binding: FragmentReportIssueBinding? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // Retrieve responseData from arguments
            responseData = it.getParcelable("responseData")
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         binding = FragmentReportIssueBinding.inflate(inflater, container, false)
        val spinnerAdapter = ReportIssueBatchAdapter(requireActivity(), R.layout.item_spinner_row, ArrayList(responseData?.batchDetails))
        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner_row)
        binding?.batchNo?.setAdapter(spinnerAdapter);
        // Remove setting key listener to null
        // bindingSecondPage?.statesList?.setKeyListener(null);
        binding?.batchNo?.threshold=1
        binding?.batchNo?.setOnClickListener {
            (it as AutoCompleteTextView).showDropDown()
        }

        binding?.batchNo?.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            val selectedModel = parent.adapter.getItem(position) as BatchDetail
            // Do whatever you want with the selected model object here
            binding?.batchNo?.setText(selectedModel.batchCode,false)

        }


        val batchDetails = responseData?.batchDetails
        val spinnerAdapter1 = ReportIssueCoilAdapter(requireActivity(), R.layout.item_spinner_row, ArrayList(batchDetails?: emptyList()))
        spinnerAdapter1.setDropDownViewResource(R.layout.item_spinner_row)
        binding?.coilNumber?.setAdapter(spinnerAdapter1);
        // Remove setting key listener to null
        // bindingSecondPage?.statesList?.setKeyListener(null);
//        binding.coilNumber?.threshold=1
        binding?.coilNumber?.setOnClickListener {
            (it as AutoCompleteTextView).showDropDown()
        }

        binding?.coilNumber?.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            val selectedModel = parent.adapter.getItem(position) as BatchDetail
            // Do whatever you want with the selected model object here
            binding?.coilNumber?.setText(selectedModel.coilNo,false)
            binding?.grossWeight?.setText(selectedModel.grossWeight)
            binding?.parallelWeight?.setText(selectedModel.palletWeight)
            binding?.netWeight?.setText(selectedModel.netWeight)


        }

        // Create a list of static strings
        val staticStrings = listOf("Quality Issue", "Weight Issue","Others(Please Specify)")

// Initialize a default ArrayAdapter with the list of strings
        val spinnerAdapter2 = ArrayAdapter(requireActivity(), R.layout.item_spinner_row, staticStrings)
        spinnerAdapter2.setDropDownViewResource(R.layout.item_spinner_row)

// Set the adapter to the AutoCompleteTextView
        binding?.issueType?.setAdapter(spinnerAdapter2)

// Ensure the dropdown shows with a threshold of 1 character
        binding?.issueType?.threshold = 1

// Show the dropdown when the field is clicked
        binding?.issueType?.setOnClickListener {
            (it as AutoCompleteTextView).showDropDown()
        }

// Handle item selection
        binding?.issueType?.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            val selectedString = parent.adapter.getItem(position) as String
            // Set the selected value in the text field
            binding?.issueType?.setText(selectedString,false)
        }
        binding?.submitButton?.setOnClickListener{
            onSubmitClicked()

        }




        // Use the responseData to update UI
//        responseData?.let { data ->
//            // Example of using responseData
//            binding.textViewIssueDescription.text = data.salesOrderDate // Example of data usage
//        }

        return binding?.root
    }
    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    private fun onSubmitClicked() {


        val batchNoValue = binding!!.batchNo.getText().toString().trim()
        val coilNoValue = binding!!.coilNumber.getText().toString().trim()
        val grossWeightValue = binding!!.grossWeight.getText().toString().trim()
        val parallelWeightValue = binding!!.parallelWeight.getText().toString().trim()
        val netWeightValue = binding!!.netWeight.getText().toString().trim()
        val issueTypeValue = binding!!.issueType.getText().toString().trim()
        val serialNumberValue = binding!!.serialNumber.getText().toString().trim()

        // Validate if any field is empty and show toast messages

        // Validate if any field is empty and show toast messages
        if (batchNoValue.isEmpty()) {
            showCustomDialog(requireContext(), "Batch Number can't be empty!","Error")

            return  // Exit method if validation fails
        }

        if (coilNoValue.isEmpty()) {
            showCustomDialog(requireContext(), "Coil Number can't be empty!","Error")
            return
        }
        if (grossWeightValue.isEmpty()) {

            showCustomDialog(requireContext(), "Gross weight can't be empty!","Error")
            return
        }
        if (parallelWeightValue.isEmpty()) {
            showCustomDialog(requireContext(), "Parallel weight can't be empty!","Error")

            return
        }
        if (netWeightValue.isEmpty()) {
            showCustomDialog(requireContext(), "Net weight can't be empty!","Error")
            return
        }
        if (issueTypeValue.isEmpty()) {
            showCustomDialog(requireContext(), "Select issue type can't be empty!","Error")
            showToast("Issue type is required")
            return
        }
        if (serialNumberValue.isEmpty()) {
            showCustomDialog(requireContext(), "Please Describe your issue","Error")
            return
        }
        binding!!.loginProgressBar.progressBar.shown()
        val header = HashMap<String, String>()
        header["Accept"] = "application/json"
        header["version"] = "1"
        header["Authorization"] = "${sessionManager.loginToken}"
        header["userID"]="${sessionManager.user?.RESPONSEDATA?.USER_ID}"
        lifecycleScope.launch {
            val saveReportRequest = SaveReportRequest(
                SOID_REF = batchNoValue,
                BATCH_NO = batchNoValue,
                COIL_NO = coilNoValue,
                GROSS_WEIGHT = grossWeightValue,
                PALLET_WEIGHT = parallelWeightValue,
                NET_WEIGHT = netWeightValue,
                ISSUE_TYPE = issueTypeValue,
                ISSUE_DESCRIPTION = serialNumberValue
            )
            var response = gateKeeperVm.setReportIssueR(header, multipart,saveReportRequest)

            when (response) {

                is NetworkState.Success -> {
                    binding!!.loginProgressBar.progressBar.hidden()
                    if(response.body.STATUS){
                        Toast.makeText(context,"updated",Toast.LENGTH_SHORT).show()
                        Handler(Looper.getMainLooper()).postDelayed({
                            findNavController().popBackStack()
                        }, 1000)

                    }
                    else{
                        Toast.makeText(context,"not authorized",Toast.LENGTH_SHORT).show()
                    }




                }

                is NetworkState.Error<*> -> {
                    binding!!.loginProgressBar.progressBar.hidden()
                    showCustomDialog(requireContext(), response.msg.toString(),"Error")
                }

                is NetworkState.NetworkException -> {
                    binding!!.loginProgressBar.progressBar.hidden()
                    showCustomDialog(requireContext(), response.msg.toString(),"Error")
                }

                is NetworkState.HttpErrors.InternalServerError -> {
                    binding!!.loginProgressBar.progressBar.hidden()
                    showCustomDialog(requireContext(), response.msg.toString(),"Error")
                }

                is NetworkState.HttpErrors.ResourceNotFound -> {
                    binding!!.loginProgressBar.progressBar.hidden()
                    showCustomDialog(requireContext(), response.msg.toString(),"Error")
                }

                else -> {
                    binding!!.loginProgressBar.progressBar.hidden()
                    showCustomDialog(requireContext(), "something went wrong","Error")
                }
            }


        }




    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        videoCaptureLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val videoUri: Uri? = result.data?.data
                videoUri?.let {
                    displayVideoThumbnail(requireContext(),it, binding?.thumbnail)

                    val videoFile1 = createTempFileFromUri(requireContext(), it)
                    videoFile1?.let { file ->
                        val requestFileNew = file.asRequestBody("video/*".toMediaTypeOrNull())
                        val part = MultipartBody.Part.createFormData("ISSUE_ATTACHMENT[]", file.name, requestFileNew)

                        multipart = part
                    }
                }


                // Handle the videoUri, like playing the video or saving it
            }
        }

        binding?.uploadDocument?.setOnClickListener{
            selectImage()

        }
        binding?.thumbnail?.setOnClickListener {
            selectImage()
        }
    }


    private var requestPermissionsLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val cameraPermissionGranted = permissions[Manifest.permission.CAMERA] == true
            val recordAudioPermissionGranted = permissions[Manifest.permission.RECORD_AUDIO] == true

            // Check appropriate storage permission based on API level
            val storagePermissionGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissions[Manifest.permission.READ_MEDIA_VIDEO] == true
            } else {
                permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true
            }

            if (cameraPermissionGranted && recordAudioPermissionGranted && storagePermissionGranted) {
                // All required permissions are granted
                dispatchTakeVideoIntent()
            } else {
                // Handle the case where permissions are not granted
                Toast.makeText(requireContext(), "Permissions are required to capture video", Toast.LENGTH_SHORT).show()
            }
        }

    var cameraResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            object : ActivityResultCallback<ActivityResult?> {
                override fun onActivityResult(result: ActivityResult?) {
                    val bitmapImage: Bitmap? = null
                    var contentUri: Uri? = null
                    contentUri = if (Build.VERSION.SDK_INT >= 24) {
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
                        val compressFormat = Bitmap.CompressFormat.JPEG
                        val quality = 80
                        val byteArrayOutputStream = ByteArrayOutputStream()
                        bitmap.compress(compressFormat, quality, byteArrayOutputStream)
                        val file2 = getImageFromUri(Uri.fromFile(File(currentPictureFile?.absolutePath)))
                        val requestFileNew = file2?.asRequestBody("image/*".toMediaTypeOrNull())
                        val part = MultipartBody.Part.createFormData("ISSUE_ATTACHMENT[]", file2?.name, requestFileNew!!)
                        multipart=part
                        binding?.thumbnail?.visibility=View.VISIBLE
                        binding?.uploadDocument?.visibility=View.GONE


//                        var image: String = ActivityUtils.getBASE64Image(bitmap)
//                        image = "data:image/png;base64,$image"


                        binding?.thumbnail?.setImageBitmap(bitmap)


                        //  uploadImageToserver(image)
                    } catch (e: java.lang.Exception) {
                    }
                }
            })

    private fun dispatchTakeVideoIntent() {
        val takeVideoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        try {
            videoCaptureLauncher.launch(takeVideoIntent)
        }
        catch (e:Exception){
            e.message
        }
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
        val requiredPermissions = mutableListOf(
            Manifest.permission.CAMERA
        )

        // Add appropriate storage permission based on Android version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requiredPermissions.add(Manifest.permission.READ_MEDIA_VIDEO)
        } else {
            requiredPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        // Check if all required permissions are granted
        val allPermissionsGranted = requiredPermissions.all { permission ->
            ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED
        }

        if (allPermissionsGranted) {
            // Permissions are already granted
            dispatchTakeVideoIntent()
        } else {
            // Request missing permissions
            requestPermissionsLauncher.launch(requiredPermissions.toTypedArray())
        }
    }
    override fun onResume() {
        super.onResume()

        CustomerHomeActivity.showIcon(false)
        CustomerHomeActivity.changeTitle("Report an issue")


    }

    override fun onPause() {
        super.onPause()
        CustomerHomeActivity.showIcon(false)
        CustomerHomeActivity.changeTitle("Report an issue")

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

    fun createTempFileFromUri(context: Context, uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val tempFile = File.createTempFile("temp_video", ".mp4", context.cacheDir)
            inputStream?.use { input ->
                tempFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }



    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReportIssueFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
