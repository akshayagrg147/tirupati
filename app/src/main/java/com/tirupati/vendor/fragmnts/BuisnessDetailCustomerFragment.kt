package com.tirupati.vendor.fragmnts

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Looper
import android.provider.MediaStore
import android.provider.Settings
import android.text.InputFilter
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import android.widget.RadioGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.tirupati.vendor.R
import com.tirupati.vendor.adapters.CityAdapter
import com.tirupati.vendor.adapters.SpinnerAdapter
import com.tirupati.vendor.adapters.StateAdapter
import com.tirupati.vendor.databinding.FragmentBuisnessDetailCustomerBinding
import com.tirupati.vendor.databinding.FragmentSecondDetailPageBinding
import com.tirupati.vendor.helper.hidden
import com.tirupati.vendor.helper.isValidGST
import com.tirupati.vendor.helper.isValidPINcode
import com.tirupati.vendor.helper.isValidPhoneNumber
import com.tirupati.vendor.helper.showCustomDialog
import com.tirupati.vendor.helper.shown
import com.tirupati.vendor.model.CityList
import com.tirupati.vendor.model.ResponseDataPo
import com.tirupati.vendor.network.NetworkState
import com.tirupati.vendor.ui.LandingVendorSActivity
import com.tirupati.vendor.utils.AddressConverter
import com.tirupati.vendor.utils.first
import com.tirupati.vendor.utils.isValidEmail
import com.tirupati.vendor.utils.isValidPANNumber
import com.tirupati.vendor.utils.second
import com.tirupati.vendor.utils.third
import com.tirupati.vendor.utils.toast
import com.tirupati.vendor.viewmodels.SignUpUploadsViewModel
import com.tirupati.vendor.viewmodels.statesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BuisnessDetailCustomerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class BuisnessDetailCustomerFragment : Fragment() {


    private var bindingFirstPage: FragmentBuisnessDetailCustomerBinding? = null
    var org_name = ""
    var org_contact = ""
    var locationName = "hardcoded test"
    var latitude = 0.0
    var longitude = 0.0
    var selectedPaths: String = ""
    private val signUpVM: SignUpUploadsViewModel by viewModels()

    var photoFile: File? = null
    private val pdf1 = ArrayList<File>()
    private val pdf2 = ArrayList<File>()
    private val pdf3 = ArrayList<File>()
    var mCurrentPhotoPath: String? = null
    var org_email = ""
    private var image1clicked:Boolean = false
    private var image1clicked2:Boolean = false
    private var image1clicked3:Boolean = false
    val RC_TAKE_PHOTO: Int = 1
    private val PICK_PDF_REQUEST_CODE = 101
    private val statesviewModel: statesViewModel by viewModels()

    private var bindingSecondPage: FragmentSecondDetailPageBinding? = null
    var stateName: ArrayList<ResponseDataPo> = ArrayList()
    var cityList: ArrayList<CityList> = ArrayList()




    var stateCode = ""
    var cityCode = ""
    val firstMp: ArrayList<MyMultipartData?> = ArrayList()
    val secondMp: ArrayList<MyMultipartData?> = ArrayList()
    val thirdMp: ArrayList<MyMultipartData?> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args=arguments
        if(args!=null){
            org_name = args.getString("ORG_NAME","")
            org_contact = args.getString("ORG_CONTACT","")
            org_email = args.getString("ORG_EMAIL","")

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        bindingFirstPage = FragmentBuisnessDetailCustomerBinding.inflate(inflater, container, false)
        bindingFirstPage!!.ownerPanET.setFilters(bindingFirstPage!!.ownerPanET.getFilters() + InputFilter.AllCaps())
        legalEntity()

        bindingFirstPage?.btnBackForm?.setOnClickListener{
            findNavController(). popBackStack()

        }
        bindingFirstPage?.inputUserFirstName?.setText(org_name)
        bindingFirstPage?.btnFirstDone?.setOnClickListener {
            if(validateUI(bindingFirstPage!!)){
                val apiKey = "AIzaSyBPlX3AuJuNr8bsZaaL2QQOI4weEkZkBb0"
                val addressConverter = AddressConverter(apiKey)

                addressConverter.getAddressFromLatLng(latitude?:0.00, longitude?:0.00) { address ->
                    if (address != null) {
                        locationName = address

                        val args = Bundle()
                        args.putString("ORG_NAME",org_name)
                        args.putString("ORG_CONTACT",org_contact)
                        args.putString("ORG_EMAIL",org_email)
//                NEW ADDED

                        args.putString("NAME_OF_ORG",bindingFirstPage!!.inputUserFirstName.text.toString())
                        args.putString("LEGAL_ENTITY",bindingFirstPage!!.legalEntity.text.toString())
                        args.putString("POC_NAME",bindingFirstPage!!.ownerFullNameET.text.toString())
                        args.putString("POC_CONTACT",bindingFirstPage!!.ownerContactET.text.toString())
                        args.putString("POC_EMAIL",bindingFirstPage!!.emailOwnerET.text.toString())
                        args.putString("orgGstNumber",bindingFirstPage!!.orgGstNumber.text.toString())
                        args.putString("OWNER_PAN",bindingFirstPage!!.ownerPanET.text.toString())
                        args.putString("addressET",bindingFirstPage!!.addressET.text.toString())
                        args.putString("countrySpinner",bindingFirstPage!!.countrySpinner.text.toString())
                        args.putString("statesList",bindingFirstPage!!.statesList.text.toString())
                        args.putString("citiesList",bindingFirstPage!!.citiesList.text.toString())
                        args.putString("pincode",bindingFirstPage!!.etPinCode.text.toString())

                        args.putString("otherApplicable","0")
                        args.putString("C1_NAME","")
                        args.putString("C1_EMAIL","")
                        args.putString("C1_CONTACT_NO","")
                        args.putString("C1_PAN_NO","")

                        args.putString("C2_NAME","")
                        args.putString("C2_EMAIL","")
                        args.putString("C2_CONTACT_NO","")
                        args.putString("C2_PAN_NO","")

                        args.putString("C3_NAME","")
                        args.putString("C3_EMAIL","")
                        args.putString("C3_CONTACT_NO","")
                        args.putString("C3_PAN_NO","")
                        args.putString("latititude",latitude.toString())
                        args.putString("longitude",longitude.toString())
                        args.putString("GODOWN_LOCATION",locationName)
                        args.putParcelableArrayList("firstImage", firstMp)
                        args.putParcelableArrayList("secondImage", secondMp)
                        args.putParcelableArrayList("thirdImage", thirdMp)



                        val radioGroup: RadioGroup = bindingFirstPage!!.radioGroup
                        val selectedId = radioGroup.checkedRadioButtonId
                        if (selectedId == -1) {
                            Toast.makeText(context, "Please select yes or no", Toast.LENGTH_SHORT).show()
                        } else {
                            val selection = if (selectedId == R.id.radio_yes) {
                                "1"
                            } else {
                                "2"
                            }
                            if (selection == "1") {
                                Navigation.findNavController(bindingFirstPage!!.root)
                                    .navigate(
                                        R.id.action_firstDetailPageFragment_to_multipleAccountFragment,
                                        args
                                    )
                            } else {
                                Navigation.findNavController(bindingFirstPage!!.root)
                                    .navigate(
                                        R.id.action_firstDetailPageFragment_to_bankAccountScreen,
                                        args
                                    )


                            }


                        }}

                        else{
                            setUpLocationListener()
                        }

                    }




                    // You can perform your navigation or further logic here based on the selection




            }
        }

        bindingFirstPage!!.countrySpinner.setText("India")
        getStatesfromServer()
        bindingFirstPage!!.gstLl.setOnClickListener {
            image1clicked=true
            image1clicked2=false
            image1clicked3=false
            capturePhoto("pdf")
        }
        bindingFirstPage!!.llPan.setOnClickListener {
            image1clicked=false
            image1clicked2=true
            image1clicked3=false
            capturePhoto("pdf")
        }
        bindingFirstPage!!.addDocumentLl.setOnClickListener {
            image1clicked=false
            image1clicked2=false
            image1clicked3=true
            capturePhoto("pdf")
        }




        return bindingFirstPage!!.root
    }

    private fun apiCallForCustomer() {

    }
    override fun onResume() {
        super.onResume()
        showSettingsAlert()
        setUpLocationListener()



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
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                121
            )
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
                            fusedLocationProviderClient.removeLocationUpdates(this)

//
                        } else {
                            fusedLocationProviderClient.removeLocationUpdates(this)
                            // showSettingsAlert()
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
        val locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            return

        val alertDialog = AlertDialog.Builder(context,R.style.DialogTheme)
        alertDialog.setTitle("SETTINGS")
        alertDialog.setMessage("Enable Location Provider! Go to settings menu?")
        alertDialog.setPositiveButton("Settings")
        { dialog, which ->
            dialog.cancel()
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
                try {

                    photoFile = createImageFile()
                    if (photoFile != null) {
                        val photoURI = if (Build.VERSION.SDK_INT >= 24) {
                            requireContext().packageName
                            FileProvider.getUriForFile(
                                requireContext(),
                                requireContext().packageName + ".provider",
                                photoFile!!
                            )
                        } else {
                            Uri.fromFile(photoFile)
                        }

                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, RC_TAKE_PHOTO)
                    } else {
                        Log.e("CameraError", "Photo file is null")
                    }
                } catch (ex: Exception) {
                    Log.e("CameraError", "Exception: ${ex.message}")
                    Toast.makeText(requireContext(), ex.message.toString(), Toast.LENGTH_SHORT).show()
                }


            }
            else{
                val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                    type = "application/pdf"
                    addCategory(Intent.CATEGORY_OPENABLE)
                }
                startActivityForResult(Intent.createChooser(intent, "Select PDF"), PICK_PDF_REQUEST_CODE)
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
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_PDF_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            data?.data?.let { uri1 ->
                // The user selected a PDF file.
                // You can use this URI to read or display the PDF.
                val selectedPdfUri: Uri = uri1
                val uriString = selectedPdfUri.toString()
                val myFile = File(uriString)
                val path = myFile.absolutePath
                var displayName: String? = null


                val file = getImageFromUri(uri=selectedPdfUri ?: return)

                when {
                    image1clicked -> {


                        pdf1.add(file)
                        first = filesToMultipartParts("GST_CERTIFICATE[]", pdf1)
                        bindingFirstPage?.gstLl?.visibility = View.GONE
                        bindingFirstPage!!.gstCustBusiness.visibility = View.VISIBLE
                        bindingFirstPage!!.gstIv1.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.pdf))

                        bindingFirstPage!!.deleteGst1.setOnClickListener {
                            bindingFirstPage?.gstLl?.visibility = View.VISIBLE
                            bindingFirstPage!!.gstCustBusiness.visibility = View.GONE
                        }
                    }
                    image1clicked2 -> {
                        pdf2.add(file)
                        second = filesToMultipartParts("GST_CERTIFICATE[]", pdf2)
                        bindingFirstPage?.llPan?.visibility = View.GONE
                        bindingFirstPage!!.rlPanCard.visibility = View.VISIBLE
                        bindingFirstPage!!.panIv.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.pdf))

                        bindingFirstPage!!.deletePan.setOnClickListener {
                            bindingFirstPage?.llPan?.visibility = View.VISIBLE
                            bindingFirstPage!!.rlPanCard.visibility = View.GONE
                        }
                    }
                    image1clicked3 -> {
                        pdf3.add(file)
                        third = filesToMultipartParts("ELECTRIC_BILL[]", pdf3)
                        bindingFirstPage?.addDocumentLl?.visibility = View.GONE
                        bindingFirstPage!!.gstCerRL.visibility = View.VISIBLE
                        bindingFirstPage!!.adIv.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.pdf))

                        bindingFirstPage!!.deleteAdd.setOnClickListener {
                            bindingFirstPage?.addDocumentLl?.visibility = View.VISIBLE
                            bindingFirstPage!!.gstCerRL.visibility = View.GONE
                        }
                    }

                }
            }
        }

        else {
            toast("Request cancelled or something went wrong.")
        }
    }
    private fun filesToMultipartParts(keyName: String, pdfFiles: List<File>): ArrayList<MultipartBody.Part?> {
        val parts = ArrayList<MultipartBody.Part?>()
        for (file in pdfFiles) {
            val requestFile = file.asRequestBody("application/pdf".toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData(keyName, file.name, requestFile)
            parts.add(part)
        }
        return parts
    }
    private fun compressFile(inputFile: File, context: Context): File {

        return inputFile
    }

    fun getImageFromUri(context: Context?=requireContext(), uri: Uri): File {
        val inputStream: InputStream? = context?.contentResolver?.openInputStream(uri)
        val outputFile = File(context?.cacheDir, "${System.currentTimeMillis()}.pdf") // Replace with desired file name and location

        inputStream?.use { input ->
            FileOutputStream(outputFile).use { output ->
                input.copyTo(output)
            }
        }

        return outputFile
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

//    inputUserFirstName
//    legalEntity
//    ownerFullNameET
//    ownerContactET
//    emailOwnerET
//    ownerPanET
//    POCWhatsAppET
    /*
    * if self.organizationNameTextField.text == ""{
            self.showAlertWithViewController(self, title: "Error", message: "Name of the organisation can't be empty!")
        }else if self.legalEntityTextField.text == ""{
            self.showAlertWithViewController(self, title: "Error", message: "Legal entity type can't be empty!")
        }else if self.ownerNameTextField.text == ""{
            self.showAlertWithViewController(self, title: "Error", message: "Owner's Full Name can't be empty!")
        }else if self.legalEntityTextField.text == ""{
            self.showAlertWithViewController(self, title: "Error", message: "Legal entity type can't be empty!")
        }else if self.contactNumberTextField.text == ""{
            self.showAlertWithViewController(self, title: "Error", message: "Owner's Contact Number can't be empty!")
        }else if self.contactNumberTextField.text?.isValidPhone == false{
            self.showAlertWithViewController(self, title: "Error", message: "Owner's Contact Number is incorrect")
        }
        else if ((self.emailIDTextField.text?.count ?? 0) > 0) && (self.emailIDTextField.text?.isValidEmail == false){
            self.showAlertWithViewController(self, title: "Error", message: "Owner's Email ID is incorrect")
        }

        else if ((self.whatappNumberTextField.text?.count ?? 0) > 0) && (self.whatappNumberTextField.text?.isValidPhone == false){
            self.showAlertWithViewController(self, title: "Error", message: "POC Whatsapp Number is incorrect")
        }
        else if self.pocNumberTextField.text == ""{
            self.showAlertWithViewController(self, title: "Error", message: "Owner's PAN Card can't be empty!")
        }
        else if self.pocNumberTextField.text?.isValidPanNumber == false{
            self.showAlertWithViewController(self, title: "Error", message: "Owner's PAN Card is incorrect")
        }
    *
    * */




    private fun validateUI(binding: FragmentBuisnessDetailCustomerBinding): Boolean {


        var status = false
        if (binding.inputUserFirstName.text.isNullOrEmpty()) {
            showCustomDialog(requireContext(),"Name of Organisation can't be empty!","Error")
            status = false
        }
        else if(binding.legalEntity.text.isNullOrEmpty()){
            showCustomDialog(requireContext(),"Legal entity type can't be empty!","Error")
            status = false

        }

        else if(binding.ownerFullNameET.text.toString().isNullOrEmpty()){
            showCustomDialog(requireContext(),"Owner's Full Name can't be empty!","Error")
            status = false

        }
        else if (binding.ownerContactET.text.isNullOrEmpty()) {
            showCustomDialog(requireContext(), "Owner's Contact Number can't be empty!", "Error")
            status = false
        } else if (binding.ownerContactET.text?.length != 10) {
            showCustomDialog(requireContext(), "Owner's Contact Number is Incorrect", "Error")
            status = false
        }
        else if(!binding.ownerContactET.text.toString().isValidPhoneNumber()){
            showCustomDialog(requireContext(),"Owner's Contact Number is Incorrect","Error")
            status = false

        }

        else if(binding.emailOwnerET.text.length>0 && !binding.emailOwnerET.text.toString().isValidEmail()){

            showCustomDialog(requireContext(),"Owner's Email ID is Incorrect","Error")
            status = false

        }

        else if(binding.ownerPanET.text.isNullOrEmpty()){
            showCustomDialog(requireContext(),"Owner's PAN Number can't be empty!","Error")
            status = false

        }
        else if(!binding.ownerPanET.text.toString().isValidPANNumber()){
            showCustomDialog(requireContext(),"Owner's PAN Number is Incorrect","Error")
            status = false

        }
        else if (binding.addressET.text.isNullOrEmpty()) {
            showCustomDialog(requireContext(),"Address can't be empty!","Error")
            status = false
        }
        else if(binding.countrySpinner.text.isNullOrEmpty()){
            showCustomDialog(requireContext(),"Country can't be empty!","Error")
            status = false

        }

        else if(binding.statesList.text.toString().isNullOrEmpty()){
            showCustomDialog(requireContext(),"State can't be empty!","Error")
            status = false

        }
        else if(binding.citiesList.text.isNullOrEmpty()){
            showCustomDialog(requireContext(),"City can't be empty!","Error")
            status = false

        }
        else if(binding.etPinCode.text.toString().isNullOrEmpty()){
            showCustomDialog(requireContext(),"Pincode can't be empty!","Error")
            status = false

        }

        else if(!binding.etPinCode.text.toString().isValidPINcode()){
            showCustomDialog(requireContext(),"Pincode is Incorrect","Error")
            status = false

        }
        else if(binding.orgGstNumber.text.toString().isNullOrEmpty()){
            showCustomDialog(requireContext(),"Organisation’s GST Number can't be empty!","Error")
            status = false

        }

        else if(!binding.orgGstNumber.text.toString().isValidGST()){
            showCustomDialog(requireContext(),"Organisation’s GST Number is Incorrect","Error")
            status = false

        }
       else if (pdf1.isEmpty()) {
            showCustomDialog(requireContext(), "Please upload GST certificate","Error")
            status = false
        }
        else if (pdf2.isEmpty()) {
            showCustomDialog(requireContext(), "Please upload Owner's Pancard","Error")
            status = false
        }
        else if (pdf3.isEmpty()) {
            showCustomDialog(requireContext(), "Please upload Electricity bill","Error")
            status = false
        }



        else {
            return true
        }
        return status
    }

    fun UpdateData(){


        Log.d("TV",bindingFirstPage?.legalEntity?.text.toString())
    }

    fun legalEntity() {
        val legalEntityArray: ArrayList<String> = ArrayList()
//        "Proprietorship (Individual/HUF)","Firm/LLP","Company (OPC/Pvt Ltd/Ltd)"]
        legalEntityArray.add("Proprietorship (Individual/HUF)")
        legalEntityArray.add("Firm/LLP")
        legalEntityArray.add("Company (OPC/Pvt Ltd/Ltd)")
        var spinnerAdapter = SpinnerAdapter(requireActivity(), R.layout.item_spinner_row, legalEntityArray)
        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner_row)
        /*bindingFirstPage!!.spinner.adapter = spinnerAdapter
        bindingFirstPage!!.spinner.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {


            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != 0) {
                    when (position) {
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        })*/
        bindingFirstPage?.legalEntity?.setAdapter(spinnerAdapter);
        bindingFirstPage?.legalEntity?.setKeyListener(null);
        bindingFirstPage?.legalEntity?.setOnTouchListener(View.OnTouchListener { v, event ->
            (v as AutoCompleteTextView).showDropDown()
            false
        })
        UpdateData()
    }
    fun getStates(stateName: ArrayList<ResponseDataPo>) {
        val spinnerAdapter =
            StateAdapter(requireActivity(), R.layout.item_spinner_row, stateName)
        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner_row)
        bindingFirstPage!!.spinner.adapter = spinnerAdapter
        bindingFirstPage!!.statesList.setOnClickListener {
//        bindingSecondPage!!.spinner.visibility = View.VISIBLE
            bindingFirstPage!!.spinner.visibility = View.VISIBLE
            bindingFirstPage!!.spinner.performClick()

        }
        bindingFirstPage!!.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view:View?, position: Int, id: Long) {
                // Handle spinner item selection here
//            val selectedItem = parent?.getItemAtPosition(position).toString()
                // Update EditText text
                bindingFirstPage!!.spinner.visibility=View.GONE

                bindingFirstPage!!.statesList.setText(spinnerAdapter.getItem(position).NAME)
                getCities(spinnerAdapter.getItem(position).STID)

//            bindingSecondPage!!.statesList.setText(spinnerAdapter.getItem(position).NAME)
//            bindingSecondPage!!.spinner.visibility = View.GONE
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

    }

    private fun getStatesfromServer() {
        bindingFirstPage!!.loginProgressBar.progressBar.shown()
        lifecycleScope.launch {
            var response = statesviewModel.getStatesList()

            when (response) {

                is NetworkState.Success->{
                    bindingFirstPage!!.loginProgressBar.progressBar.hidden()
                    stateName= response.body.RESPONSEDATA
//                    initializeAdapter(branchList)
                    getStates(stateName)
                }

                is NetworkState.Error<*>->{
                    bindingFirstPage!!.loginProgressBar.progressBar.hidden()
                    showCustomDialog(requireContext(), response.msg.toString(),"Error")
                    // Toast.makeText(context,response.msg.toString(),Toast.LENGTH_SHORT).show()
                }

                is NetworkState.NetworkException->{
                    bindingFirstPage!!.loginProgressBar.progressBar.hidden()
                }
                is NetworkState.HttpErrors.InternalServerError->{
                    bindingFirstPage!!.loginProgressBar.progressBar.hidden()
                }
                is NetworkState.HttpErrors.ResourceNotFound->{
                    bindingFirstPage!!.loginProgressBar.progressBar.hidden()
                }
                else->{
                    bindingFirstPage!!.loginProgressBar.progressBar.hidden()
                }
            }


        }
    }
    private fun getCities(Stateid: String) {
        bindingFirstPage!!.loginProgressBar.progressBar.shown()
        stateCode = Stateid
        bindingFirstPage!!.citiesList.text.clear()
        lifecycleScope.launch {
            var response = statesviewModel.getCitiesList(Stateid)

            when (response) {

                is NetworkState.Success->{
                    bindingFirstPage!!.loginProgressBar.progressBar.hidden()
                    cityList= response.body.RESPONSEDATA
//                    initializeAdapter(branchList)

                    cityAdapter(cityList)

                }

                is NetworkState.Error<*>->{
                    bindingFirstPage!!.loginProgressBar.progressBar.hidden()
                    showCustomDialog(requireContext(), response.msg.toString(),"Error")
                    // Toast.makeText(context,response.msg.toString(),Toast.LENGTH_SHORT).show()
                }

                is NetworkState.NetworkException->{
                    bindingFirstPage!!.loginProgressBar.progressBar.hidden()
                }
                is NetworkState.HttpErrors.InternalServerError->{
                    bindingFirstPage!!.loginProgressBar.progressBar.hidden()
                }
                is NetworkState.HttpErrors.ResourceNotFound->{
                    bindingFirstPage!!.loginProgressBar.progressBar.hidden()
                }
                else->{
                    bindingFirstPage!!.loginProgressBar.progressBar.hidden()
                }
            }


        }
    }
    private fun cityAdapter(cityList: ArrayList<CityList>) {

        val spinnerAdapter =
            CityAdapter(requireActivity(), R.layout.item_spinner_row, cityList)
        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner_row)

        bindingFirstPage?.citiesList?.setAdapter(spinnerAdapter);
        // Remove setting key listener to null
        // bindingSecondPage?.statesList?.setKeyListener(null);
        bindingFirstPage?.citiesList?.threshold=1
        bindingFirstPage?.citiesList?.setOnClickListener {
            (it as AutoCompleteTextView).showDropDown()
        }

        bindingFirstPage?.citiesList?.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            val selectedModel = parent.adapter.getItem(position) as CityList
            // Do whatever you want with the selected model object here
            bindingFirstPage?.citiesList?.setText(selectedModel.NAME,false)
            cityCode= selectedModel.CITYID
//            getCities(stateId)
        }

        /*   bindingSecondPage!!.citiesList.setOnItemSelectedListener(object :
               AdapterView.OnItemSelectedListener {

               override fun onItemSelected(
                   parent: AdapterView<*>?,
                   view: View?,
                   position: Int,
                   id: Long
               ) {
                   if (position != 0) {
                       when (position) {
                           position ->{

                           }

                       }
                   }
               }

               override fun onNothingSelected(parent: AdapterView<*>?) {

               }
           })*/
    }


}