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
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tirupati.vendor.adapters.ImageCamAdapter
import com.tirupati.vendor.databinding.FragmentUploadSupervisorPicturesBinding
import com.tirupati.vendor.helper.SessionManager
import com.tirupati.vendor.helper.hidden
import com.tirupati.vendor.helper.loadFromUrlWithRoundedCorners
import com.tirupati.vendor.helper.showCustomDialog
import com.tirupati.vendor.helper.shown
import com.tirupati.vendor.model.GateRESPONSEDATA
import com.tirupati.vendor.model.ImageItem
import com.tirupati.vendor.network.NetworkState
import com.tirupati.vendor.ui.LandingScreenCustomerActivity
import com.tirupati.vendor.ui.LandingScreenGateKeeperActivity
import com.tirupati.vendor.utils.moveToActivity
import com.tirupati.vendor.utils.toast
import com.tirupati.vendor.viewmodels.GatekeeperListUploadsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.apache.commons.io.FileUtils
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class UploadSupervisorPicturesFragment : Fragment(), ImageCamAdapter.OnClickListener{
    private val uploadViewModel: GatekeeperListUploadsViewModel by viewModels()
    lateinit var bundledData: Bundle


    private val images = mutableListOf<Uri>()
    private lateinit var adapter: ImageCamAdapter
    @Inject
    lateinit var sessionManager: SessionManager

    //    private lateinit var currentPhotoPath: String
    var file: File? = null
    var cacheDir: File? = null
    var selectedPaths: String = ""
    var photoFile: File? = null

    var mCurrentPhotoPath: String? = null
    val RC_TAKE_PHOTO: Int = 1
        var bindingPic:FragmentUploadSupervisorPicturesBinding? = null

    private lateinit var imageAdapter: ImageCamAdapter
    private lateinit var imageItems: MutableList<ImageItem>
    private val images1 = ArrayList<File>()
    private val images2 = ArrayList<File>()
    private val images3 = ArrayList<File>()
    private val images4 = ArrayList<File>()
    private val images5 = ArrayList<File>()
    private val images6 = ArrayList<File>()

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


    private var fifth:ArrayList<MultipartBody.Part?>?=null
    private var sixth:ArrayList<MultipartBody.Part?>?=null
    var supervisorData: GateRESPONSEDATA? = null

    var vendorName=""
    var gateNo=""
    var grossWT=""
    var tareWT=""
    var netWT=""
    var vehNo=""





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        if (args != null) {

            bundledData = args
            Log.d("bndldata", bundledData.toString())
            supervisorData = args.getParcelable("SUPERVISOR_DATA")
         vendorName=   args.getString("SUPERVISORE_VENDOR","")
          gateNo =  args.getString("SUPERVISORE_GATE_NO","")
           grossWT = args.getString("SUPERVISORE_GROSS","")
            tareWT = args.getString("SUPERVISORE_TARE","")
            netWT = args.getString("SUPERVISORE_NET","")
            vehNo = args.getString("VEHICLE_NO","")


        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        bindingPic = FragmentUploadSupervisorPicturesBinding.inflate(inflater, container, false)

        bindingPic!!.imageListRC.layoutManager = LinearLayoutManager(requireContext())

        adapter = ImageCamAdapter(images, this)
        val weightChange = tareWT.toDouble()
        if(weightChange>.00){
            bindingPic!!.tollReceiptLL.visibility = View.GONE
            bindingPic!!.vehicleRcLL.visibility = View.GONE
            bindingPic!!.drivingLicenceLL.visibility = View.GONE
            bindingPic!!.frntBackLL.visibility = View.GONE


        }
        else{

        }
        bindingPic!!.imageListRC.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        bindingPic!!.imageListRC.itemAnimator = null
        println(supervisorData)
        bindingPic!!.imageListRC.adapter = adapter
        if (adapter != null) {
            // do your stuff here
            bindingPic!!.imageListRC.visibility = View.VISIBLE
        }else{
            bindingPic!!.imageListRC.visibility = View.GONE
        }
        // Button to capture image
        /*bindingPic!!.uploadFrntBack.setOnClickListener {
            capturePhoto()

        }*/
       /* if (images.isNullOrEmpty()){
            bindingPic!!.imageListRC.visibility = View.VISIBLE
            bindingPic!!.imageListRC.scrollToPosition(images.size)
        }
        else{
            bindingPic!!.imageListRC.visibility = View.GONE
        }*/


        bindingPic!!.loadVehicle.setOnClickListener {
//            dispatchTakePictureIntent()
            image1clicked=true
            image2clicked=false
            image3clicked=false
            image4clicked=false

//            takePhoto()
//            dispatchTakePictureIntent()
            capturePhoto()
        }

        bindingPic!!.tollReceipt.setOnClickListener{
            image1clicked=false
            image2clicked=true
            image3clicked=false
            image4clicked=false

            capturePhoto()
        }

        bindingPic!!.vehicleRc.setOnClickListener {
            image1clicked=false
            image2clicked=false
            image3clicked = true
            image4clicked=false

            capturePhoto()

        }

        bindingPic!!.driverLicense.setOnClickListener {
            image1clicked=false
            image2clicked=false
            image3clicked = false
            image4clicked=true



            capturePhoto()

        }


            bindingPic!!.btnSupervisorDone.setOnClickListener {
                if (validateUI(bindingPic!!)) {
                    callUploadImageData()
                }
            }


        return bindingPic?.root
    }

    private fun callUploadImageData() {
        lifecycleScope.launch {
            bindingPic!!.loginProgressBar.progressBar.shown()

            val header = HashMap<String, String>()
            header["Accept"] = "application/json"
            header["version"] = "1"
            header["Authorization"] = "${sessionManager.loginToken}"
            header["userID"]="${sessionManager.user?.RESPONSEDATA?.USER_ID}"

            val response =
                uploadViewModel.postUploadsSupervisor(
                    header,supervisorData!!.GEID,supervisorData!!.SLID_REF,vehNo,grossWT,tareWT,netWT,
                    first!!,second!!,third!!,fourth!!,fifth!!
                )

            when (response) {

                is NetworkState.Success -> {
                    bindingPic!!.loginProgressBar.progressBar.hidden()

                    toast("${response.body.MESSAGE}")
                    requireActivity().moveToActivity(LandingScreenGateKeeperActivity::class.java)
                    requireActivity().finish()
                  /*  val args = Bundle()

                    Navigation.findNavController(bindingPic!!.root).
                    navigate(R.id.action_uploadSupervisorPicturesFragment_to_customerFragment, args)*/
                }

                is NetworkState.Error<*> -> {
                    bindingPic!!.loginProgressBar.progressBar.hidden()

                    // Toast.makeText(context,response.msg.toString(),Toast.LENGTH_SHORT).show()
                }

                is NetworkState.NetworkException -> {
                    bindingPic!!.loginProgressBar.progressBar.hidden()

                }

                is NetworkState.HttpErrors.InternalServerError -> {
                    bindingPic!!.loginProgressBar.progressBar.hidden()

                }

                is NetworkState.HttpErrors.ResourceNotFound -> {
                    bindingPic!!.loginProgressBar.progressBar.hidden()

                }

                else -> {
                    bindingPic!!.loginProgressBar.progressBar.hidden()

                }
            }


        }
    }

    private fun validateUI(binding: FragmentUploadSupervisorPicturesBinding): Boolean {

        var status = false
        if (images1.isEmpty()) {
            showCustomDialog(requireContext(), "Image Video of Load vehicle can't be Empty","Error")
            status = false
        }
        else if (images2.isEmpty()) {
            showCustomDialog(requireContext(), "Copy Of Toll Receipt can't be Empty","Error")
            status = false
        }
        else if (images3.isEmpty()) {
            showCustomDialog(requireContext(), "Vehicle RC can't be Empty","Error")
            status = false
        }
        else if (images4.isEmpty()) {
            showCustomDialog(requireContext(), "Driving Licence can't be Empty","Error")
            status = false
        }
        else if (images5.isEmpty()) {
            showCustomDialog(requireContext(), "Front and Back of vehicle with Driver can't be Empty","Error")
            status = false
        }


        else {
            return true
        }
        return status
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
    private fun filesToMultipartParts(keyName:String,imageFiles: List<File>): ArrayList<MultipartBody.Part?> {
        val parts = ArrayList<MultipartBody.Part?>()
        for (file in imageFiles) {
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData(keyName, file.name, requestFile)
            parts.add(part)
        }
        return parts
    }

    override fun onAddImageClick() {
        if (images.size < 4) {
            image1clicked=false
            image2clicked=false
            image3clicked=false
            image4clicked=false
            capturePhoto()
        } else {
            Toast.makeText(requireContext(), "Maximum 4 images allowed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDeleteImageClick(position: Int) {
        if(position==0){

        }
        adapter.removeImage(position)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            photoFile?.let {
//                bindingUploads..loadFromUrlWithRoundedCorners(
//                    it.absolutePath
//                )
                selectedPaths = it.absolutePath

                when {
                    image1clicked -> {
                        val file1 =getImageFromUri(Uri.fromFile(File(selectedPaths)))

                        images1.add(file1!!)
                        first= filesToMultipartParts("VIDEO_LOADVEHICLE[]",images1)
                        bindingPic?.vendorLL?.visibility = View.GONE
                        bindingPic!!.vendorImageRL.visibility = View.VISIBLE
                        bindingPic!!.InvoiceimageView.setImageURI(Uri.fromFile(File(selectedPaths)))

                        bindingPic!!.delete1Button.setOnClickListener {
                            bindingPic?.vendorLL?.visibility = View.VISIBLE
                            bindingPic!!.vendorImageRL.visibility = View.GONE
                        }

                    }
                    image2clicked -> {
            //                    copyTollReceiptRL
            //                    copyTollReceiptRL
            //                    tollReceiptimageView
            //                    deleteToll
            //                    tollReceiptLL

                        val file2 = getImageFromUri(Uri.fromFile(File(selectedPaths)))

                        images2.add(file2!!)
                        second= filesToMultipartParts("TOLL_RECIEPT[]",images2)

                        bindingPic?.tollReceiptLL?.visibility = View.GONE
                        bindingPic!!.copyTollReceiptRL.visibility = View.VISIBLE
                        bindingPic!!.tollReceiptimageView.loadFromUrlWithRoundedCorners(selectedPaths)
                        bindingPic!!.deleteToll.setOnClickListener {
                            bindingPic?.tollReceiptLL?.visibility = View.VISIBLE
                            bindingPic!!.copyTollReceiptRL.visibility = View.GONE
                        }
            //                ewayBilimageView
            //                deleteEwayButton
            //                bindingUploads?.eWayBillRL
            //                bindingUploads?.eWayBillLL
                    }
                    image3clicked -> {
                        val file3 = getImageFromUri(Uri.fromFile(File(selectedPaths)))

                        images3.add(file3!!)
                        third= filesToMultipartParts("VEHICLE_RC[]",images3)
            //                    VehicleRCRL
            //                    vehicleRCimageView
            //                    deleteVehicleRc
            //                    vehicleRcLL


                        bindingPic?.vehicleRcLL?.visibility = View.GONE
                        bindingPic!!.VehicleRCRL.visibility = View.VISIBLE
            //                        bindingUploads!!.eInvoiceimageView.setImageURI(Uri.fromFile(File(selectedPaths)))
                        bindingPic!!.vehicleRCimageView.setImageURI(Uri.fromFile(File(selectedPaths)))
                        bindingPic!!.deleteVehicleRc.setOnClickListener {
                            bindingPic?.vehicleRcLL?.visibility = View.VISIBLE
                            bindingPic!!.VehicleRCRL.visibility = View.GONE
                        }


                    }
                    image4clicked -> {
                        val file4 = getImageFromUri(Uri.fromFile(File(selectedPaths)))

                        images4.add(file4!!)
                        fourth= filesToMultipartParts("DRIVING_LICENSE[]",images4)

            //                    drivingLicenceRL
            //                    uploadDrivinLCImageView
            //                    deleteDrivinLC
            //                    drivingLicenceLL

                        bindingPic?.drivingLicenceLL?.visibility = View.GONE
                        bindingPic!!.drivingLicenceRL.visibility = View.VISIBLE
                        bindingPic!!.uploadDrivinLCImageView.setImageURI(Uri.fromFile(File(selectedPaths)))
                        bindingPic!!.deleteDrivinLC.setOnClickListener {
                            bindingPic?.drivingLicenceLL?.visibility = View.VISIBLE
                            bindingPic!!.drivingLicenceRL.visibility = View.GONE

                        }

                    }
                    else -> {
                        val adapterImages = getImageFromUri(Uri.fromFile(File(selectedPaths)))
                        adapter.addImage(Uri.fromFile(File(selectedPaths)))
                        images5.add(File(selectedPaths))
                         fifth = filesToMultipartParts("FRONT_BACK_PICTURE_OF_VEHICLE_WITH_DRIVER[]",images5)

                    }
                }
            }
        } else {
            toast("Request cancelled or something went wrong.")
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

    override fun onResume() {
        super.onResume()
        LandingScreenCustomerActivity.changeTitle("Upload Documents")


    }

    override fun onPause() {
        super.onPause()
        LandingScreenCustomerActivity.changeTitle("Upload Documents")

    }
    override fun onDestroy() {
        super.onDestroy()
        LandingScreenCustomerActivity.changeIcon()
    }
}