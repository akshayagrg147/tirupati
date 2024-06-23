package com.tirupati.vendor.fragmnts

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.FileUtils.copy
import android.os.Looper
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.shockwave.pdfium.PdfiumCore
import com.tirupati.vendor.R
import com.tirupati.vendor.adapters.ImageCamAdapter
import com.tirupati.vendor.adapters.MenuAdapter
import com.tirupati.vendor.databinding.FragmentUploadsBinding
import com.tirupati.vendor.helper.SessionManager
import com.tirupati.vendor.helper.hidden
import com.tirupati.vendor.helper.interfaces.OnItemClickListGateKeeper
import com.tirupati.vendor.helper.showCustomDialog
import com.tirupati.vendor.helper.shown
import com.tirupati.vendor.model.VendorRESPONSEDATAX
import com.tirupati.vendor.network.NetworkState
import com.tirupati.vendor.ui.LandingScreenGateKeeperActivity
import com.tirupati.vendor.utils.toast
import com.tirupati.vendor.viewmodels.GatekeeperListViewModel
import com.tirupati.vendor.viewmodels.SignUpUploadsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.apache.commons.io.FileUtils
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.inject.Inject

@AndroidEntryPoint
class UploadsFragment : Fragment(), ImageCamAdapter.OnClickListener {

    private var bindingUploads: FragmentUploadsBinding? = null
    @Inject
    lateinit var sessionManager: SessionManager
    private val images = mutableListOf<Uri>()
    private lateinit var adapter: ImageCamAdapter
    var locationName = ""
    var latitude = 0.0
    var longitude = 0.0
    private val signUpVM: SignUpUploadsViewModel by viewModels()
    private val LOCATION_PERMISSION_REQUEST = 100

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    var selectedPaths: String = ""
    var photoFile: File? = null

    var mCurrentPhotoPath: String? = null
    val RC_TAKE_PHOTO: Int = 1
    private val PICK_PDF_REQUEST_CODE = 101

    var allowLocation = false

    private val images1 = ArrayList<File>()
    private val images2 = ArrayList<File>()
    private val images3 = ArrayList<File>()
    private val images4 = ArrayList<File>()
    private val images5 = ArrayList<File>()
    private val images6 = ArrayList<File>()
    private val images7 = ArrayList<File>()
    private val images8 = ArrayList<File>()
    private val images9 = ArrayList<File>()
    private val images10 = ArrayList<File>()
    private val images11 = ArrayList<File>()

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


    private var image5:String=""
    private var image5clicked:Boolean = false
    private var fifth:ArrayList<MultipartBody.Part?>?=null

    private var image6:String=""
    private var image6clicked:Boolean = false
    private var sixth:ArrayList<MultipartBody.Part?>?=null


    private var image7:String=""
    private var image7clicked:Boolean = false
    private var seventh:ArrayList<MultipartBody.Part?>?=null

    private var image8:String=""
    private var image8clicked:Boolean = false
    private var eighth:ArrayList<MultipartBody.Part?>?=null

    private var image9:String=""
    private var image9clicked:Boolean = false
    private var ninth:ArrayList<MultipartBody.Part?>?=null

    private var image10:String=""
    private var image10clicked:Boolean = false
    private var tenth:ArrayList<MultipartBody.Part?>?=null

    private var image11:String=""
    private var image11clicked:Boolean = false
    private var elevnth:ArrayList<MultipartBody.Part?>?=null

    var org_name =""
    var org_contact = ""
    var org_email = ""
    var business_org_name = ""
    private var ownerName = ""
    var legalName = ""
    var ownerContact = ""
    var ownerEmail =""
    var podName = ""
    var ownerPan = ""
    var podWhatsapp = ""
    var addressName = ""
    var country = ""
    var state = ""
    var city = ""
    var pinCode = ""
    var orgGst = ""
    var orgPAN = ""
    var orgBank = ""
    var accountNumber = ""
    var accountType = ""
    var ifsc = ""
    var branchName = ""
    var branchPin = ""


    var MSME= ""
    var EINVOICE = ""
    var MULTIPLE_ACCOUNTS=""

    var v1Name=""
    var v1Contact=""
    var v1Email=""
    var v1Adhar=""

    var v2Name=""
    var v2Contact=""
    var v2Email=""
    var v2Adhar=""

    var v3Name=""
    var v3Contact=""
    var v3Email=""
    var v3Adhar=""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        adapter = ImageCamAdapter(images, this)

        val args= arguments
        if(args!=null){
            if(args.getString("FROM_MULTI").isNullOrEmpty()){

            org_name =  args.getString("ORG_NAME","")
            org_contact= args.getString("ORG_CONTACT","")
            org_email =args.getString("ORG_EMAIL","")
            business_org_name= args.getString("NAME_OF_ORG","")
            legalName = args.getString("LEGAL_ENTITY","")
            ownerName= args.getString("OWNER_NAME","")
            ownerContact = args.getString("OWNER_CONTACT","")
            ownerEmail =args.getString("OWNER_EMAIL","")
            podName= args.getString("POD_NAME","")
            ownerPan = args.getString("OWNER_PAN","")
            podWhatsapp =args.getString("POCWhatsAppNumber","")
            addressName =args.getString("ADDRESS","")
            country= args.getString("COUNTRY","")
            state = args.getString("STATE","")
            city = args.getString("CITY","")
            pinCode =args.getString("PIN","")
            orgGst =args.getString("ORG_GST","")
            orgPAN =args.getString("ORG_PAN","")
            //NEW ADDED
            orgBank= args.getString("BANK_NAME","")
            accountNumber =args.getString("ACCOUNT_NUMBER","")
            accountType = args.getString("ACCOUNT_TYPE","")
            ifsc= args.getString("IFSC","")
            branchName =args.getString("BRANCH_NAME","")
            branchPin =args.getString("PINCODE","")
            }
            else{

                org_name =  args.getString("ORG_NAME","")
                org_contact= args.getString("ORG_CONTACT","")
                org_email =args.getString("ORG_EMAIL","")
                business_org_name= args.getString("NAME_OF_ORG","")
                legalName = args.getString("LEGAL_ENTITY","")
                ownerName= args.getString("OWNER_NAME","")
                ownerContact = args.getString("OWNER_CONTACT","")
                ownerEmail =args.getString("OWNER_EMAIL","")
                podName= args.getString("POD_NAME","")
                ownerPan = args.getString("OWNER_PAN","")
                podWhatsapp =args.getString("POCWhatsAppNumber","")
                addressName =args.getString("ADDRESS","")
                country= args.getString("COUNTRY","")
                state = args.getString("STATE","")
                city = args.getString("CITY","")
                pinCode =args.getString("PIN","")
                orgGst =args.getString("ORG_GST","")
                orgPAN =args.getString("ORG_PAN","")
                //NEW ADDED
                orgBank= args.getString("BANK_NAME","")
                accountNumber =args.getString("ACCOUNT_NUMBER","")
                accountType = args.getString("ACCOUNT_TYPE","")
                ifsc= args.getString("IFSC","")
                branchName =args.getString("BRANCH_NAME","")
                branchPin =args.getString("PINCODE","")



                args.putString("MSME","")
                args.putString("EINVOICE","")
                args.putString("OTHERAPPLICABLE","")


                v1Name =args.getString("V1_NAME","")
                v1Contact=args.getString("V1_CONTACT","")
                v1Email= args.getString("V1_EMAIL","")
                v1Adhar =args.getString("V1_ADHAR","")

                v2Name= args.getString("V2_NAME","")
                v2Contact= args.getString("V2_CONTACT","")
                v2Email =args.getString("V2_EMAIL","")
                v2Adhar = args.getString("V2_ADHAR","")


                v3Name=  args.getString("V3_NAME","")
                v3Contact =args.getString("V3_CONTACT","")
                v3Email =args.getString("V3_EMAIL","")
                v3Adhar=args.getString("V3_ADHAR","")


            }
        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission granted, open camera
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                setUpLocationListener()

            } else {
                // Location permission denied, show a message
                Toast.makeText(
                    requireContext(),
                    "Location permission is required to open the camera.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setUpLocationListener() {
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        // for getting the current location update after every 1 minute with high accuracy
        val locationRequest = LocationRequest().setInterval(2000).setFastestInterval(30000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    for (location in locationResult.locations) {
                        if (location != null) {
                            latitude = location.latitude
                            longitude = location.longitude
                            getCompleteAddressString(
                                latitude,
                                longitude
                            )
//
                        } else {
                            showSettingsAlert()
                        }
                    }
                    // Few more things we can do here:
                    // For example: Update the location of user on server
                }
            },
            Looper.myLooper()
        )
    }
    fun showSettingsAlert() {
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle("SETTINGS")
        alertDialog.setMessage("Enable Location Provider! Go to settings menu?")
        alertDialog.setPositiveButton("Settings")
        { dialog, which ->
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }
        alertDialog.setNegativeButton("Cancel",
            object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    dialog.cancel()
                }
            })
        alertDialog.show()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        bindingUploads = FragmentUploadsBinding.inflate(inflater, container, false)
        bindingUploads?.btnBackForm?.setOnClickListener{
            findNavController(). popBackStack()

        }

        bindingUploads!!.imageListRCPDF.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        bindingUploads!!.imageListRCPDF.itemAnimator = null
        bindingUploads!!.imageListRCPDF.adapter = adapter
        if (adapter != null) {
            // do your stuff here
            bindingUploads!!.imageListRCPDF.visibility = View.VISIBLE
        }else{
            bindingUploads!!.imageListRCPDF.visibility = View.GONE
        }
        bindingUploads!!.gstCertificate.setOnClickListener {
//            dispatchTakePictureIntent()
            image1clicked=true
            image2clicked=false
            image3clicked=false
            image4clicked=false
            image5clicked=false
            image6clicked=false
            image7clicked=false
            image8clicked=false
            image9clicked=false
            image10clicked=false
            image11clicked=false

//            takePhoto()
//            dispatchTakePictureIntent()
            capturePhoto("pdf")
        }

        bindingUploads!!.panCardUpload.setOnClickListener{
            image1clicked=false
//            image2clicked=true
            image3clicked=false
            image4clicked=false
            image5clicked=false
            image6clicked=false
            image7clicked=false
            image8clicked=false
            image9clicked=false
            image10clicked=false
            image11clicked=false

            capturePhoto("pdf")
        }
//==================================================
        bindingUploads!!.electricityBill.setOnClickListener {
            image1clicked=false
            image2clicked=false
            image3clicked=true
            image4clicked=false
            image5clicked=false
            image6clicked=false
            image7clicked=false
            image8clicked=false
            image9clicked=false
            image10clicked=false
            image11clicked=false
            capturePhoto("pdf")

        }

        bindingUploads!!.rentDeadUpload.setOnClickListener {
            image1clicked=false
            image2clicked=false
            image3clicked=false
            image4clicked=true
            image5clicked=false
            image6clicked=false
            image7clicked=false
            image8clicked=false
            image9clicked=false
            image10clicked=false
            image11clicked=false



            capturePhoto("pdf")

        }

        bindingUploads!!.godwnPhoto.setOnClickListener {

            if(ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Request location permission if not granted
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION),
                    LOCATION_PERMISSION_REQUEST
                )
            } else {
                setUpLocationListener()
            if(locationName.isNotEmpty()){
                image1clicked=false
                image2clicked=false
                image3clicked=false
                image4clicked=false
                image5clicked=true
                image6clicked=false
                image7clicked=false
                image8clicked=false
                image9clicked=false
                image10clicked=false
                image11clicked=false


                capturePhoto("cam")

            }

                // Permission is already granted, open camera

            }



        }

        bindingUploads!!.cancelledChequeUpload.setOnClickListener {
            image1clicked=false
            image2clicked=false
            image3clicked=false
            image4clicked=false
            image5clicked=false
            image6clicked=true
            image7clicked=false
            image8clicked=false
            image9clicked=false
            image10clicked=false
            image11clicked=false


            capturePhoto("pdf")

        }

        bindingUploads!!.adhaarUpload.setOnClickListener {
            image1clicked=false
            image2clicked=false
            image3clicked=false
            image4clicked=false
            image5clicked=false
            image6clicked=false
            image7clicked=true
            image8clicked=false
            image9clicked=false
            image10clicked=false
            image11clicked=false


            capturePhoto("pdf")

        }

        bindingUploads!!.bankConfirmationUpload.setOnClickListener {
            image1clicked=false
            image2clicked=false
            image3clicked=false
            image4clicked=false
            image5clicked=false
            image6clicked=false
            image7clicked=false
            image8clicked=true
            image9clicked=false
            image10clicked=false
            image11clicked=false


            capturePhoto("pdf")

        }


        bindingUploads!!.uploadITR.setOnClickListener {
            image1clicked=false
            image2clicked=false
            image3clicked=false
            image4clicked=false
            image5clicked=false
            image6clicked=false
            image7clicked=false
            image8clicked=false
            image9clicked=true
            image10clicked=false
            image11clicked=false

            capturePhoto("pdf")

        }


        bindingUploads!!.uploadMSME.setOnClickListener {
            image1clicked=false
            image2clicked=false
            image3clicked=false
            image4clicked=false
            image5clicked=false
            image6clicked=false
            image7clicked=false
            image8clicked=false
            image9clicked=false
            image10clicked=true
            image11clicked=false


            capturePhoto("pdf")

        }
        bindingUploads!!.eInvoiceApplicableUpload.setOnClickListener {
            image1clicked=false
            image2clicked=false
            image3clicked=false
            image4clicked=false
            image5clicked=false
            image6clicked=false
            image7clicked=false
            image8clicked=false
            image9clicked=false
            image10clicked=false
            image11clicked=true



            capturePhoto("pdf")

        }



        bindingUploads!!.btnSignUpDone.setOnClickListener {
            if (validateUI(bindingUploads!!)) {
                callUploadImageData()
            }
        }


        return bindingUploads!!.root
    }


    override fun onAddImageClick() {
        if (images.size < 4) {
            image1clicked=false
            image2clicked=true
            image3clicked=false
            image4clicked=false
            capturePhoto("pdf")
        } else {
            Toast.makeText(requireContext(), "Maximum 4 images allowed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDeleteImageClick(position: Int) {
        if(position==0){

        }
        adapter.removeImage(position)
    }
    private fun getCompleteAddressString(LATITUDE: Double, LONGITUDE: Double) {
        var strAdd = ""
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            val addresses: List<Address>? = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1)
            if (addresses != null) {
                val returnedAddress: Address = addresses[0]
                val strReturnedAddress = StringBuilder()
                for (i in 0..returnedAddress.getMaxAddressLineIndex()) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
                }
                strAdd = strReturnedAddress.toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        locationName = strAdd
        latitude = LATITUDE
        longitude =LONGITUDE


    }


    private fun callUploadImageData() {

        println("$latitude,$longitude,$locationName,$org_name,$org_contact, $org_email,$business_org_name,$ownerName,$legalName,$ownerContact,$ownerEmail,$podName,$ownerPan,$podWhatsapp,$addressName$country,$state,$city,$pinCode,$orgGst,$orgPAN$orgBank,$accountNumber,$accountType,$ifsc,$branchName,$branchPin ,$v1Name,$v1Contact,$v1Email,$v1Adhar,$v2Name,$v2Contact,$v2Email,$v2Adhar,$v3Name,$v3Contact,$v3Email,$v3Adhar")
        bindingUploads!!.loginProgressBar.progressBar.shown()

            lifecycleScope.launch {
                val header = HashMap<String, String>()
                header["Accept"] = "application/json"
                header["version"] = "1"
                var response = signUpVM.postSignUpRegistration(header,latitude.toString(),
                    longitude.toString(),locationName,MULTIPLE_ACCOUNTS,
                    v1Name,v1Email,v1Contact,v1Adhar,
                    v2Name,v2Email,v2Contact,v2Adhar,
                    v3Name,v3Email,v3Contact,v3Adhar,
                    ownerName,ownerContact,ownerEmail,
                    org_name,legalName,ownerName,ownerContact,ownerEmail,podName,ownerPan,podWhatsapp,
                    addressName,"1",state,city,pinCode,orgGst,orgPAN,
                    orgBank,accountNumber,accountType,branchName,ifsc,MSME,EINVOICE,
                    first!!,second!!,third!!,fourth!!,fifth!!,sixth!!,seventh!!,eighth!!,ninth!!,tenth!!,elevnth!!

                )

                when (response) {

                    is NetworkState.Success -> {
                        bindingUploads!!.loginProgressBar.progressBar.hidden()
                        toast("Registered Successfully!!")
                        findNavController().navigate(R.id.logInFragment2)

//                    binding?.listOpts?.adapter!!.notifyDataSetChanged()



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

    private fun validateUI(binding: FragmentUploadsBinding): Boolean {

        var status = false
        if (images1.isEmpty()) {
            showCustomDialog(requireContext(), "Please upload GST certificate","Error")
            status = false
        }
        else if (images2.isEmpty()) {
            showCustomDialog(requireContext(), "Please upload Owner's Pancard","Error")
            status = false
        }
        else if (images3.isEmpty()) {
            showCustomDialog(requireContext(), "Please upload Electricity bill","Error")
            status = false
        }
        else if (images4.isEmpty()) {
            showCustomDialog(requireContext(), "Please upload Rent dead","Error")
            status = false
        }
        else if (images5.isEmpty()) {
            showCustomDialog(requireContext(), "Please upload Real time godown","Error")
            status = false
        }
//        ==========================================================
        else if (images6.isEmpty()) {
            showCustomDialog(requireContext(), "Please upload cancelled cheque","Error")
            status = false
        }
        else if (images7.isEmpty()) {
            showCustomDialog(requireContext(), "Please upload Organisation PAN card","Error")
            status = false
        }
        else if (images8.isEmpty()) {
            showCustomDialog(requireContext(), "Please upload Bank confirmation letter","Error")
            status = false
        }
        else if (images9.isEmpty()) {
            showCustomDialog(requireContext(), "Please upload ITR Acknowledgement","Error")
            status = false
        }
        else if (images10.isEmpty()) {
            showCustomDialog(requireContext(), "Please upload MSME","Error")
            status = false
        }
        else if (images11.isEmpty()) {
            showCustomDialog(requireContext(), "Please upload E-Invoice Applicable","Error")
            status = false
        }



        else {
            return true
        }
        return status
    }



    private fun capturePhoto(typeOfStroge:String) {
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
            if(typeOfStroge=="cam"){
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
            else{
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "application/pdf"
                startActivityForResult(intent, PICK_PDF_REQUEST_CODE)
                /*var intentPDF=Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    type = "application/pdf"
                    addCategory(Intent.CATEGORY_OPENABLE)
                    putExtra(DocumentsContract.EXTRA_INITIAL_URI, true)
                }
                startActivityForResult(
                    Intent.createChooser(intentPDF, "Open with"),
                    1001
                )*/
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


    private fun filesToMultipartPartsPDF(keyName: String, imageFiles: List<File>, context: Context): ArrayList<MultipartBody.Part?> {
        val parts = ArrayList<MultipartBody.Part?>()
        for (file in imageFiles) {
            val uri = Uri.fromFile(file)
            val inputStream = context.contentResolver.openInputStream(uri)
            val bytes = inputStream?.readBytes()
            bytes?.let {
                val requestBody = RequestBody.create("application/pdf".toMediaTypeOrNull(), it)
                val part = MultipartBody.Part.createFormData(keyName, file.name, requestBody)
                parts.add(part)
            }
            inputStream?.close()
        }
        return parts
    }

    @RequiresApi(Build.VERSION_CODES.Q)
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


//                        gstCerRL
//                        gstIv
//                        deleteGst
//                        gstCerLL

                        images1.add(compressFile(file1!!,requireContext()))
                        first= filesToMultipartParts("GST_CERTIFICATE[]",images1)
                        bindingUploads?.gstCerLL?.visibility = View.GONE
                        bindingUploads!!.gstCerRL.visibility = View.VISIBLE
                        bindingUploads!!.gstIv.setImageURI(Uri.fromFile(File(selectedPaths)))

                        bindingUploads!!.deleteGst.setOnClickListener {
                            bindingUploads?.gstCerLL?.visibility = View.VISIBLE
                            bindingUploads!!.gstCerRL.visibility = View.GONE
                        }

                    }
                  /*  image2clicked -> {
//                        panRL
//                        panIV
//                        deletePanIV
//                        panLL

                        val file2 = getImageFromUri(Uri.fromFile(File(selectedPaths)))

                        images2.add(file2!!)
                        second= filesToMultipartParts("",images2)

                        bindingUploads?.panLL?.visibility = View.GONE
                        bindingUploads!!.panRL.visibility = View.VISIBLE

                       *//* bindingUploads!!.panIV.loadFromUrlWithRoundedCorners(selectedPaths)
                        bindingUploads!!.deletePanIV.setOnClickListener {
                            bindingUploads?.panLL?.visibility = View.VISIBLE
                            bindingUploads!!.panRL.visibility = View.GONE
                        }*//*
                        //                ewayBilimageView
                        //                deleteEwayButton
                        //                bindingUploads?.eWayBillRL
                        //                bindingUploads?.eWayBillLL
                    }*/
                    image3clicked -> {



                        val file3 = getImageFromUri(Uri.fromFile(File(selectedPaths)))

                        images3.add(compressFile(file3!!,requireContext()))
                        third= filesToMultipartParts("ELECTRIC_BILL[]",images3)

//                        eleBillRL
//                        IVelecBill
//                        deleteIVElecBill
//                        eleBillLL

                        bindingUploads?.eleBillLL?.visibility = View.GONE
                        bindingUploads!!.eleBillRL.visibility = View.VISIBLE
                        //                        bindingUploads!!.eInvoiceimageView.setImageURI(Uri.fromFile(File(selectedPaths)))
                        bindingUploads!!.IVelecBill.setImageURI(Uri.fromFile(File(selectedPaths)))
                        bindingUploads!!.deleteIVElecBill.setOnClickListener {
                            bindingUploads?.eleBillLL?.visibility = View.VISIBLE
                            bindingUploads!!.eleBillRL.visibility = View.GONE
                        }


                    }
                    image4clicked -> {

                        val file4 = getImageFromUri(Uri.fromFile(File(selectedPaths)))

                        images4.add(compressFile(file4!!,requireContext()))
                        fourth= filesToMultipartParts("RENT_DOC[]",images4)

//                        rentDeaRL
//                        IVrentDead
//                        deleteRentDead
//                        rentDeaLL
                        bindingUploads?.rentDeaLL?.visibility = View.GONE
                        bindingUploads!!.rentDeaRL.visibility = View.VISIBLE
                        bindingUploads!!.IVrentDead.setImageURI(Uri.fromFile(File(selectedPaths)))
                        bindingUploads!!.deleteRentDead.setOnClickListener {
                            bindingUploads?.rentDeaLL?.visibility = View.VISIBLE
                            bindingUploads!!.rentDeaRL.visibility = View.GONE

                        }

                    }
                    image5clicked -> {

                        val file5 = getImageFromUri(Uri.fromFile(File(selectedPaths)))

                        images5.add(compressFile(file5!!,requireContext()))
                        fifth= filesToMultipartParts("GODOWN_PIC[]",images5)


//                        godwonRL
//                        IVgodownImg
//                        IVdeletegodownImg
//                        godwonLL
                        Log.d("kakjkldjakdsjds","$locationName     $latitude    $longitude")
                        bindingUploads?.godwonLL?.visibility = View.GONE
                        bindingUploads!!.godwonRL.visibility = View.VISIBLE
                        bindingUploads!!.IVgodownImg.setImageURI(Uri.fromFile(File(selectedPaths)))
                        bindingUploads!!.IVdeletegodownImg.setOnClickListener {
                            bindingUploads?.godwonLL?.visibility = View.VISIBLE
                            bindingUploads!!.godwonRL.visibility = View.GONE

                        }

                    }


                    image6clicked -> {

                        val file6 = getImageFromUri(Uri.fromFile(File(selectedPaths)))

                        images6.add(compressFile(file6!!,requireContext()))
                        sixth= filesToMultipartParts("CANCEL_CHEQUE[]",images6)
//                        ccRL
//                        cancelledCIv
//                        deleteChequeIV
//                        ccLL

                        bindingUploads?.ccLL?.visibility = View.GONE
                        bindingUploads!!.ccRL.visibility = View.VISIBLE
                        bindingUploads!!.cancelledCIv.setImageURI(Uri.fromFile(File(selectedPaths)))
                        bindingUploads!!.deleteChequeIV.setOnClickListener {
                            bindingUploads?.ccLL?.visibility = View.VISIBLE
                            bindingUploads!!.ccRL.visibility = View.GONE

                        }

                    }
                    image7clicked -> {

                        val file7 = getImageFromUri(Uri.fromFile(File(selectedPaths)))

                        images7.add(compressFile(file7!!,requireContext()))
                        seventh= filesToMultipartParts("OWNER_AADHAR[]",images7)

//                        adharRL
//                        adhaarIV
//                        deleteAdharIv
//                        adharLL
                        bindingUploads?.adharLL?.visibility = View.GONE
                        bindingUploads!!.adharRL.visibility = View.VISIBLE
                        bindingUploads!!.adhaarIV.setImageURI(Uri.fromFile(File(selectedPaths)))
                        bindingUploads!!.deleteAdharIv.setOnClickListener {
                            bindingUploads?.adharLL?.visibility = View.VISIBLE
                            bindingUploads!!.adharRL.visibility = View.GONE
                            seventh!!.clear()

                        }

                    }
                    image8clicked -> {

                        val file8 = getImageFromUri(Uri.fromFile(File(selectedPaths)))

                        images8.add(compressFile(file8!!,requireContext()))

                        eighth= filesToMultipartParts("BANK_CONF_LETTER[]",images8)
//                        BCRL
//                        bankConfirmIV
//                        deleteBankConfirmIV
//                        BCLL
                        bindingUploads?.BCLL?.visibility = View.GONE
                        bindingUploads!!.BCRL.visibility = View.VISIBLE
                        bindingUploads!!.bankConfirmIV.setImageURI(Uri.fromFile(File(selectedPaths)))
                        bindingUploads!!.deleteBankConfirmIV.setOnClickListener {
                            bindingUploads?.BCLL?.visibility = View.VISIBLE
                            bindingUploads!!.BCRL.visibility = View.GONE

                        }

                    }
                    image9clicked -> {
//                        itrRL
//                        ITRimageView
//                        deleteITRIV
//                        itrLL
                        val file9 = getImageFromUri(Uri.fromFile(File(selectedPaths)))

                        images9.add(compressFile(file9!!,requireContext()))
                        ninth= filesToMultipartParts("ITR_ACK[]",images9)

                        bindingUploads?.itrLL?.visibility = View.GONE
                        bindingUploads!!.itrRL.visibility = View.VISIBLE
                        bindingUploads!!.ITRimageView.setImageURI(Uri.fromFile(File(selectedPaths)))
                        bindingUploads!!.deleteITRIV.setOnClickListener {
                            bindingUploads?.itrLL?.visibility = View.VISIBLE
                            bindingUploads!!.itrRL.visibility = View.GONE

                        }

                    }
                    image10clicked -> {

                        val file10 = getImageFromUri(Uri.fromFile(File(selectedPaths)))

                        images10.add(compressFile(file10!!,requireContext()))
                        tenth= filesToMultipartParts("MSME[]",images10)
//                        MsmeRL
//                        MSMEImageView
//                        deleteMSMEImageView
//                        MsmeLL
                        bindingUploads?.MsmeLL?.visibility = View.GONE
                        bindingUploads!!.MsmeRL.visibility = View.VISIBLE
                        bindingUploads!!.MSMEImageView.setImageURI(Uri.fromFile(File(selectedPaths)))
                        bindingUploads!!.deleteMSMEImageView.setOnClickListener {
                            bindingUploads?.MsmeLL?.visibility = View.VISIBLE
                            bindingUploads!!.MsmeRL.visibility = View.GONE

                        }

                    }
                    image11clicked -> {

                        val file11 = getImageFromUri(Uri.fromFile(File(selectedPaths)))

                        images11.add(compressFile(file11!!,requireContext()))

                        elevnth= filesToMultipartParts("E_INVOICE[]",images11)
//                        eInvRL
//                        eInvoiceimageView
//                        deleteEInvoice
//                        eInvLL

                        bindingUploads?.eInvLL?.visibility = View.GONE
                        bindingUploads!!.eInvRL.visibility = View.VISIBLE
                        bindingUploads!!.eInvoiceimageView.setImageURI(Uri.fromFile(File(selectedPaths)))
                        bindingUploads!!.deleteEInvoice.setOnClickListener {
                            bindingUploads?.eInvLL?.visibility = View.VISIBLE
                            bindingUploads!!.eInvRL.visibility = View.GONE

                        }

                    }

                    else -> {

                    }
                }
            }
        }
        else if (requestCode == PICK_PDF_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            data?.data?.let { uri1 ->
                // The user selected a PDF file.
                // You can use this URI to read or display the PDF.
                val selectedPdfUri: Uri = uri1

                val uri: Uri = selectedPdfUri
                val uriString = uri.toString()
                val myFile = File(uriString)
                val path = myFile.absolutePath
                var displayName: String? = null

                when{
                    image1clicked -> {

                        if (uriString.startsWith("content://")) {
                            var cursor: Cursor? = null
                            try {
                                cursor = requireActivity().contentResolver.query(uri, null, null, null, null)
                                if (cursor != null && cursor.moveToFirst()) {
                                    displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                                    Log.d("lllllllllll",displayName)
                                    val uri = Uri.parse(("android.resource://" + requireContext().packageName).toString() + "/drawable/pdficon")
                                    val path=fileFromContentUri(requireContext(),uri)
                                    images1.add(compressFile(path,requireContext()))
                                    first= filesToMultipartParts("GST_CERTIFICATE[]",images1)
                                    bindingUploads?.gstCerLL?.visibility = View.GONE
                                    bindingUploads!!.gstCerRL.visibility = View.VISIBLE
                                    bindingUploads!!.gstIv.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.pdf))

                                    bindingUploads!!.deleteGst.setOnClickListener {
                                        bindingUploads?.gstCerLL?.visibility = View.VISIBLE
                                        bindingUploads!!.gstCerRL.visibility = View.GONE
                                    }
                                }
                            } finally {

                                cursor!!.close()
                            }
                        } else if (uriString.startsWith("file://")) {
                            displayName = myFile.name
                            Log.d("lllllllllll",displayName)

                        }
                    }
                    image2clicked -> {
                        if (uriString.startsWith("content://")) {
                            var cursor: Cursor? = null
                            try {
                                cursor = requireActivity().contentResolver.query(uri, null, null, null, null)
                                if (cursor != null && cursor.moveToFirst()) {
                                    displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                                    Log.d("lllllllllll",displayName)
                                    val uri = Uri.parse(("android.resource://" + requireContext().packageName).toString() + "/drawable/pdficon")

                                    adapter.addImage(uri)
                                    val path=fileFromContentUri(requireContext(),uri)
////
                                    images2.add(compressFile(path,requireContext()))
                                    second = filesToMultipartPartsPDF("COM_PAN_CARD[]",images2,requireContext())
                                }
                            } finally {

                                cursor!!.close()
                            }
                        } else if (uriString.startsWith("file://")) {
                            displayName = myFile.name
                            Log.d("lllllllllll",displayName)

                        }
                    }
                    image3clicked -> {

                        if (uriString.startsWith("content://")) {
                            var cursor: Cursor? = null
                            try {
                                cursor = requireActivity().contentResolver.query(uri, null, null, null, null)
                                if (cursor != null && cursor.moveToFirst()) {
                                    displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                                    Log.d("lllllllllll",displayName)
                                    val uri = Uri.parse(("android.resource://" + requireContext().packageName).toString() + "/drawable/pdficon")
                                    val path=fileFromContentUri(requireContext(),uri)
                                    images3.add(compressFile(path,requireContext()))
                                    third= filesToMultipartParts("ELECTRIC_BILL[]",images3)
                                    bindingUploads?.eleBillLL?.visibility = View.GONE
                                    bindingUploads!!.eleBillRL.visibility = View.VISIBLE
                                    bindingUploads!!.IVelecBill.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.pdf))
                                    bindingUploads!!.deleteIVElecBill.setOnClickListener {
                                        bindingUploads?.eleBillLL?.visibility = View.VISIBLE
                                        bindingUploads!!.eleBillRL.visibility = View.GONE
                                    }
                                }
                            } finally {
                                cursor!!.close()
                            }
                        } else if (uriString.startsWith("file://")) {
                            displayName = myFile.name
                            Log.d("lllllllllll",displayName)
                        }


                    }
                    image4clicked -> {

                        if (uriString.startsWith("content://")) {
                            var cursor: Cursor? = null
                            try {
                                cursor = requireActivity().contentResolver.query(uri, null, null, null, null)
                                if (cursor != null && cursor.moveToFirst()) {
                                    displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                                    Log.d("lllllllllll",displayName)
                                    val uri = Uri.parse(("android.resource://" + requireContext().packageName).toString() + "/drawable/pdficon")
                                    val path=fileFromContentUri(requireContext(),uri)
                                    images4.add(compressFile(path,requireContext()))
                                    fourth= filesToMultipartParts("RENT_DOC[]",images4)

//                        rentDeaRL
//                        IVrentDead
//                        deleteRentDead
//                        rentDeaLL
                                    bindingUploads?.rentDeaLL?.visibility = View.GONE
                                    bindingUploads!!.rentDeaRL.visibility = View.VISIBLE
                                    bindingUploads!!.IVrentDead.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.pdf))
                                    bindingUploads!!.deleteRentDead.setOnClickListener {
                                        bindingUploads?.rentDeaLL?.visibility = View.VISIBLE
                                        bindingUploads!!.rentDeaRL.visibility = View.GONE

                                    }
                                }
                            } finally {

                                cursor!!.close()
                            }
                        } else if (uriString.startsWith("file://")) {
                            displayName = myFile.name
                            Log.d("lllllllllll",displayName)

                        }



                    }
                    image6clicked -> {

                        if (uriString.startsWith("content://")) {
                            var cursor: Cursor? = null
                            try {
                                cursor = requireActivity().contentResolver.query(uri, null, null, null, null)
                                if (cursor != null && cursor.moveToFirst()) {
                                    displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                                    Log.d("lllllllllll",displayName)
                                    val uri = Uri.parse(("android.resource://" + requireContext().packageName).toString() + "/drawable/pdficon")
                                    val path=fileFromContentUri(requireContext(),uri)
                                    images6.add(compressFile(path,requireContext()))
                                    sixth= filesToMultipartParts("CANCEL_CHEQUE[]",images6)
//                        ccRL
//                        cancelledCIv
//                        deleteChequeIV
//                        ccLL

                                    bindingUploads?.ccLL?.visibility = View.GONE
                                    bindingUploads!!.ccRL.visibility = View.VISIBLE
                                    bindingUploads!!.cancelledCIv.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.pdf))
                                    bindingUploads!!.deleteChequeIV.setOnClickListener {
                                        bindingUploads?.ccLL?.visibility = View.VISIBLE
                                        bindingUploads!!.ccRL.visibility = View.GONE

                                    }
                                }
                            } finally {

                                cursor!!.close()
                            }
                        } else if (uriString.startsWith("file://")) {
                            displayName = myFile.name
                            Log.d("lllllllllll",displayName)

                        }



                    }
                    image7clicked -> {
                        if (uriString.startsWith("content://")) {
                            var cursor: Cursor? = null
                            try {
                                cursor = requireActivity().contentResolver.query(uri, null, null, null, null)
                                if (cursor != null && cursor.moveToFirst()) {
                                    displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                                    Log.d("lllllllllll",displayName)
                                    val uri = Uri.parse(("android.resource://" + requireContext().packageName).toString() + "/drawable/pdficon")
                                    val path=fileFromContentUri(requireContext(),uri)
                                    images7.add(compressFile(path,requireContext()))
                                    seventh= filesToMultipartParts("OWNER_AADHAR[]",images7)

//                        adharRL
//                        adhaarIV
//                        deleteAdharIv
//                        adharLL
                                    bindingUploads?.adharLL?.visibility = View.GONE
                                    bindingUploads!!.adharRL.visibility = View.VISIBLE
                                    bindingUploads!!.adhaarIV.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.pdf))
                                    bindingUploads!!.deleteAdharIv.setOnClickListener {
                                        bindingUploads?.adharLL?.visibility = View.VISIBLE
                                        bindingUploads!!.adharRL.visibility = View.GONE
                                        seventh!!.clear()

                                    }
                                }
                            } finally {

                                cursor!!.close()
                            }
                        } else if (uriString.startsWith("file://")) {
                            displayName = myFile.name
                            Log.d("lllllllllll",displayName)

                        }



                    }
                    image8clicked -> {
                        if (uriString.startsWith("content://")) {
                            var cursor: Cursor? = null
                            try {
                                cursor = requireActivity().contentResolver.query(uri, null, null, null, null)
                                if (cursor != null && cursor.moveToFirst()) {
                                    displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                                    Log.d("lllllllllll",displayName)
                                    val uri = Uri.parse(("android.resource://" + requireContext().packageName).toString() + "/drawable/pdficon")
                                    val path=fileFromContentUri(requireContext(),uri)
                                    images8.add(compressFile(path,requireContext()))

                                    eighth= filesToMultipartParts("BANK_CONF_LETTER[]",images8)
                                    bindingUploads?.BCLL?.visibility = View.GONE
                                    bindingUploads!!.BCRL.visibility = View.VISIBLE
                                    bindingUploads!!.bankConfirmIV.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.pdf))
                                    bindingUploads!!.deleteBankConfirmIV.setOnClickListener {
                                        bindingUploads?.BCLL?.visibility = View.VISIBLE
                                        bindingUploads!!.BCRL.visibility = View.GONE

                                    }
                                }
                            } finally {

                                cursor!!.close()
                            }
                        } else if (uriString.startsWith("file://")) {
                            displayName = myFile.name
                            Log.d("lllllllllll",displayName)

                        }



                    }
                    image9clicked -> {
                        if (uriString.startsWith("content://")) {
                            var cursor: Cursor? = null
                            try {
                                cursor = requireActivity().contentResolver.query(uri, null, null, null, null)
                                if (cursor != null && cursor.moveToFirst()) {
                                    displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                                    Log.d("lllllllllll",displayName)
                                    val uri = Uri.parse(("android.resource://" + requireContext().packageName).toString() + "/drawable/pdficon")
                                    val path=fileFromContentUri(requireContext(),uri)
                                    images9.add(compressFile(path,requireContext()))
                                    ninth= filesToMultipartParts("ITR_ACK[]",images9)

                                    bindingUploads?.itrLL?.visibility = View.GONE
                                    bindingUploads!!.itrRL.visibility = View.VISIBLE
                                    bindingUploads!!.ITRimageView.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.pdf))
                                    bindingUploads!!.deleteITRIV.setOnClickListener {
                                        bindingUploads?.itrLL?.visibility = View.VISIBLE
                                        bindingUploads!!.itrRL.visibility = View.GONE

                                    }
                                }
                            } finally {

                                cursor!!.close()
                            }
                        } else if (uriString.startsWith("file://")) {
                            displayName = myFile.name
                            Log.d("lllllllllll",displayName)

                        }



                    }
                    image10clicked -> {
                        if (uriString.startsWith("content://")) {
                            var cursor: Cursor? = null
                            try {
                                cursor = requireActivity().contentResolver.query(uri, null, null, null, null)
                                if (cursor != null && cursor.moveToFirst()) {
                                    displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                                    Log.d("lllllllllll",displayName)
                                    val uri = Uri.parse(("android.resource://" + requireContext().packageName).toString() + "/drawable/pdficon")
                                    val path=fileFromContentUri(requireContext(),uri)
                                    images10.add(compressFile(path,requireContext()))
                                    tenth= filesToMultipartParts("MSME[]",images10)
//                        MsmeRL
//                        MSMEImageView
//                        deleteMSMEImageView
//                        MsmeLL
                                    bindingUploads?.MsmeLL?.visibility = View.GONE
                                    bindingUploads!!.MsmeRL.visibility = View.VISIBLE
                                    bindingUploads!!.MSMEImageView.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.pdf))
                                    bindingUploads!!.deleteMSMEImageView.setOnClickListener {
                                        bindingUploads?.MsmeLL?.visibility = View.VISIBLE
                                        bindingUploads!!.MsmeRL.visibility = View.GONE

                                    }
                                }
                            } finally {

                                cursor!!.close()
                            }
                        } else if (uriString.startsWith("file://")) {
                            displayName = myFile.name
                            Log.d("lllllllllll",displayName)

                        }



                    }
                    image11clicked -> {

                        if (uriString.startsWith("content://")) {
                            var cursor: Cursor? = null
                            try {
                                cursor = requireActivity().contentResolver.query(uri, null, null, null, null)
                                if (cursor != null && cursor.moveToFirst()) {
                                    displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                                    Log.d("lllllllllll",displayName)
                                    val uri = Uri.parse(("android.resource://" + requireContext().packageName).toString() + "/drawable/pdficon")
                                    val path=fileFromContentUri(requireContext(),uri)
                                    images11.add(compressFile(path,requireContext()))

                                    elevnth= filesToMultipartParts("E_INVOICE[]",images11)
//                        eInvRL
//                        eInvoiceimageView
//                        deleteEInvoice
//                        eInvLL

                                    bindingUploads?.eInvLL?.visibility = View.GONE
                                    bindingUploads!!.eInvRL.visibility = View.VISIBLE
                                    bindingUploads!!.eInvoiceimageView.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.pdf))
                                    bindingUploads!!.deleteEInvoice.setOnClickListener {
                                        bindingUploads?.eInvLL?.visibility = View.VISIBLE
                                        bindingUploads!!.eInvRL.visibility = View.GONE

                                    }
                                }
                            } finally {

                                cursor!!.close()
                            }
                        } else if (uriString.startsWith("file://")) {
                            displayName = myFile.name
                            Log.d("lllllllllll",displayName)

                        }




                    }
                }


//                adapter.addImage(toShowImg)
//
//                images2.add(FileUtilsPdf.getPathFromUri(requireContext(),selectedPdfUri))
//                second = filesToMultipartParts("FRONT_BACK_PICTURE_OF_VEHICLE_WITH_DRIVER[]",images2)
                }
            }
        else {
            toast("Request cancelled or something went wrong.")
        }
    }
    private fun getFileExtension(context: Context, uri: Uri): String? {
        val fileType: String? = context.contentResolver.getType(uri)
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType)
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    fun fileFromContentUri(context: Context, contentUri: Uri): File {
        // Preparing Temp file name
        val fileExtension = getFileExtension(context, contentUri)
        val fileName = "temp_file" + if (fileExtension != null) ".$fileExtension" else ""

        // Creating Temp file
        val tempFile = File(context.cacheDir, fileName)
        tempFile.createNewFile()

        try {
            val oStream = FileOutputStream(tempFile)
            val inputStream = context.contentResolver.openInputStream(contentUri)

            inputStream?.let {
                copy(inputStream, oStream)
            }

            oStream.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return tempFile
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


    suspend fun compressUris(uris: ArrayList<Uri>, context: Context): ArrayList<Uri> = withContext(
        Dispatchers.IO) {
        val compressedUris = ArrayList<Uri>()

        for (uri in uris) {
            val inputFile = File(uri.path)
            if (inputFile.exists()) {
                val compressedFile = compressFile(inputFile, context)
                compressedUris.add(Uri.fromFile(compressedFile))
            }
        }

        return@withContext compressedUris
    }

    private fun compressFile(inputFile: File, context: Context): File {
        val outputDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val outputFile = File.createTempFile("compressed_", ".zip", outputDir)

        val buffer = ByteArray(1024)

        try {
            val fileOutputStream = FileOutputStream(outputFile)
            val zipOutputStream = ZipOutputStream(fileOutputStream)

            val fileInputStream = FileInputStream(inputFile)
            zipOutputStream.putNextEntry(ZipEntry(inputFile.name))

            var length: Int
            while (fileInputStream.read(buffer).also { length = it } > 0) {
                zipOutputStream.write(buffer, 0, length)
            }

            zipOutputStream.closeEntry()
            zipOutputStream.close()
            fileInputStream.close()
            fileOutputStream.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return outputFile
    }

}

