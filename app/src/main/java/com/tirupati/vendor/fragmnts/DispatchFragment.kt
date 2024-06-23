package com.tirupati.vendor.fragmnts

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.tirupati.vendor.R
import com.tirupati.vendor.databinding.FragmentDispatchBinding
import com.tirupati.vendor.databinding.FragmentPurchaseOrderClickBinding
import com.tirupati.vendor.helper.SessionManager
import com.tirupati.vendor.model.PurchaseOrderRequest
import com.tirupati.vendor.model.ResponseData
import com.tirupati.vendor.network.NetworkState
import com.tirupati.vendor.ui.LandingVendorSActivity
import com.tirupati.vendor.utils.ActivityUtils
import com.tirupati.vendor.utils.AddressConverter
import com.tirupati.vendor.utils.ImageOrientationHelper
import com.tirupati.vendor.viewmodels.GatekeeperListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
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
 * Use the [DispatchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class DispatchFragment : Fragment() {

    private val gateKeeperVm: GatekeeperListViewModel by viewModels()
    private var currentPictureFile: File? = null
    var multipart: MultipartBody.Part ?=null
    @Inject
    lateinit var sessionManager: SessionManager
    private lateinit var videoCaptureLauncher: ActivityResultLauncher<Intent>
    var objectResponseData: ResponseData?=null
    private var binding: FragmentDispatchBinding? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getParcelable<ResponseData>("myObjectKey")?.let { myObject ->
            objectResponseData=myObject
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentDispatchBinding.inflate(inflater, container, false)
        return binding?.root
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
                        val part = MultipartBody.Part.createFormData("DISPATCH_DOC[]", file2?.name, requestFileNew!!)
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
        if (takeVideoIntent.resolveActivity(requireContext().packageManager) != null) {
            videoCaptureLauncher.launch(takeVideoIntent)
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



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.purchaseNo?.setText(objectResponseData?.PO_NO)
        binding?.orderDate?.setText(objectResponseData?.PO_DT)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                121
            )
            return
        }
        var latitude:Double?=0.00
        var longitude:Double?=0.00
        fusedLocationClient.lastLocation
            .addOnCompleteListener(requireActivity(), OnCompleteListener<Location> { task ->
                if (task.isSuccessful && task.result != null) {
                    val location = task.result
                     latitude = location.latitude
                     longitude = location.longitude
                    // Use latitude and longitude as needed
                } else {
                    // Handle failure to get location
                }
            })
        binding?.uploadDocument?.setOnClickListener{
            selectImage()

        }
        binding?.thumbnail?.setOnClickListener {
            selectImage()
        }
        binding?.btnGateEntryDone?.setOnClickListener{
            val apiKey = "AIzaSyCw-Je4CcmISXUcgyDBSauoVxcy0HLR4eU"
            val addressConverter = AddressConverter(apiKey)
            var convertAddress:String?=null

            addressConverter.getAddressFromLatLng(latitude?:0.00, longitude?:0.00) { address ->
                if (address != null) {
                    convertAddress=address
                    // Use the address here
                    println(address)
                } else {
                    // Handle error
                }
            }
            val header = HashMap<String, String>()
            header["Accept"] = "application/json"
            header["version"] = "1"
            header["Authorization"] = "${sessionManager.loginToken}"
            header["userID"]="${sessionManager.user?.RESPONSEDATA?.USER_ID}"
            lifecycleScope.launch {
                val orderDate = binding?.orderDate?.text.toString()
                val purchaseNo = binding?.purchaseNo?.text.toString()

                    if (orderDate.isEmpty() || purchaseNo.isEmpty() || convertAddress?.isEmpty()==true || latitude==0.00 || longitude==0.00) {
                        // Return with a message indicating that some fields are empty
                        val emptyFields = mutableListOf<String>()
                        if (orderDate.isEmpty()) emptyFields.add("Order Date")
                        if (purchaseNo.isEmpty()) emptyFields.add("Purchase Number")
                        if (convertAddress?.isEmpty()==true) emptyFields.add("Location Name")
                        if (latitude==0.00) emptyFields.add("Latitude")
                        if (longitude==0.00) emptyFields.add("Longitude")

                        val message = "The following fields are empty: ${emptyFields.joinToString(", ")}"
                        // Show the message to the user (you can use Toast, Snackbar, or any other method)
                        // For example:
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                    } else {
                        var response = gateKeeperVm.passDispatchOrder(
                            header,
                            multipart,
                            PurchaseOrderRequest(
                                PODATE = binding?.orderDate?.text.toString(),
                                LOCATION_NAME = convertAddress?:"",
                                PO_NUMBER = binding?.purchaseNo?.text.toString(),
                                LONGITUDE = latitude.toString(),
                                LATITUDE = longitude.toString()
                            )
                        )


                        when (response) {

                            is NetworkState.Success -> {
                                Toast.makeText(context, "Dispatched", Toast.LENGTH_SHORT).show()
                                findNavController(). popBackStack()


                            }

                            is NetworkState.Error<*> -> {
                                Toast.makeText(context, response.msg.toString(), Toast.LENGTH_SHORT)
                                    .show()
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
            } }
        videoCaptureLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val videoUri: Uri? = result.data?.data
                videoUri?.let {
                    displayVideoThumbnail(requireContext(),it, binding?.thumbnail)
                    val file = File(getPathFromUri(requireContext(), it))
                    val requestFile = file.asRequestBody("video/*".toMediaTypeOrNull())
                    val part = MultipartBody.Part.createFormData("DISPATCH_DOC[]", file.name, requestFile)
                    multipart=part
                }


                // Handle the videoUri, like playing the video or saving it
            }
        }
    }
    private fun getPathFromUri(context: Context, uri: Uri): String {
        var filePath: String? = null
        val cursor = context.contentResolver.query(uri, arrayOf(MediaStore.Video.Media.DATA), null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                filePath = it.getString(it.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))
            }
        }
        return filePath ?: throw IllegalArgumentException("Could not get file path from URI")
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
    override fun onResume() {
        super.onResume()
        LandingVendorSActivity.showIcon(false)
        LandingVendorSActivity.changeTitle("Dispatch Order")


    }

    override fun onPause() {
        super.onPause()
        LandingVendorSActivity.showIcon(false)
        LandingVendorSActivity.changeTitle("Dispatch Order")

    }


}