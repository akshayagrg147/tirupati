package com.tirupati.vendor.fragmnts

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tirupati.vendor.R
import com.tirupati.vendor.adapters.CityAdapter
import com.tirupati.vendor.adapters.StateAdapter
import com.tirupati.vendor.databinding.FragmentAddressDetailsCustomerBinding
import com.tirupati.vendor.databinding.FragmentBuisnessDetailCustomerBinding
import com.tirupati.vendor.databinding.FragmentSecondDetailPageBinding
import com.tirupati.vendor.helper.hidden
import com.tirupati.vendor.helper.showCustomDialog
import com.tirupati.vendor.helper.shown
import com.tirupati.vendor.model.CityList
import com.tirupati.vendor.model.ResponseDataPo
import com.tirupati.vendor.network.NetworkState
import com.tirupati.vendor.utils.first
import com.tirupati.vendor.utils.second
import com.tirupati.vendor.utils.third
import com.tirupati.vendor.utils.toast
import com.tirupati.vendor.viewmodels.statesViewModel
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
 * Use the [AddressDetailsCustomerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddressDetailsCustomerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var image1clicked:Boolean = false
    private var image1clicked2:Boolean = false
    private var image1clicked3:Boolean = false
    private var param1: String? = null
    var mCurrentPhotoPath: String? = null
    val RC_TAKE_PHOTO: Int = 1
    private val PICK_PDF_REQUEST_CODE = 101
    private val statesviewModel: statesViewModel by viewModels()

    private var bindingSecondPage: FragmentSecondDetailPageBinding? = null
    var stateName: ArrayList<ResponseDataPo> = ArrayList()
    var cityList: ArrayList<CityList> = ArrayList()




    var stateCode = ""
    var cityCode = ""
    var photoFile: File? = null
    private var param2: String? = null
    private val pdf1 = ArrayList<File>()
    private val pdf2 = ArrayList<File>()
    private val pdf3 = ArrayList<File>()
    private var binfingAddress: FragmentAddressDetailsCustomerBinding? = null

//    else if (binding.addressET.text.isNullOrEmpty()) {
//        showCustomDialog(requireContext(),"Address can't be empty","Error")
//        status = false
//    }
//    else if(binding.countrySpinner.text.isNullOrEmpty()){
//        showCustomDialog(requireContext(),"Country can't be empty","Error")
//        status = false
//
//    }
//
//    else if(binding.statesList.text.toString().isNullOrEmpty()){
//        showCustomDialog(requireContext(),"States can't be empty!!","Error")
//        status = false
//
//    }
//    else if(binding.citiesList.text.isNullOrEmpty()){
//        showCustomDialog(requireContext(),"City can't be empty","Error")
//        status = false
//
//    }
//    else if(binding.etPinCode.text.toString().isNullOrEmpty()){
//        showCustomDialog(requireContext(),"PIN Code can't be empty","Error")
//        status = false
//
//    }
//
//    else if(!binding.etPinCode.text.toString().isValidPINcode()){
//        showCustomDialog(requireContext(),"PIN code not Valid","Error")
//        status = false
//
//    }



//    args.putString("addressET",bindingFirstPage!!.addressET.text.toString())
//    args.putString("countrySpinner",bindingFirstPage!!.countrySpinner.text.toString())
//    args.putString("statesList",bindingFirstPage!!.statesList.text.toString())
//    args.putString("citiesList",bindingFirstPage!!.citiesList.text.toString())
//    args.putString("pincode",bindingFirstPage!!.etPinCode.text.toString())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
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
                        binfingAddress?.gstLl?.visibility = View.GONE
                        binfingAddress!!.gstCustBusiness.visibility = View.VISIBLE
                        binfingAddress!!.gstIv1.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.pdf))

                        binfingAddress!!.deleteGst1.setOnClickListener {
                            binfingAddress?.gstLl?.visibility = View.VISIBLE
                            binfingAddress!!.gstCustBusiness.visibility = View.GONE
                        }
                    }
                    image1clicked2 -> {
                        pdf2.add(file)
                        second = filesToMultipartParts("GST_CERTIFICATE[]", pdf2)
                        binfingAddress?.llPan?.visibility = View.GONE
                        binfingAddress!!.rlPanCard.visibility = View.VISIBLE
                        binfingAddress!!.panIv.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.pdf))

                        binfingAddress!!.deletePan.setOnClickListener {
                            binfingAddress?.llPan?.visibility = View.VISIBLE
                            binfingAddress!!.rlPanCard.visibility = View.GONE
                        }
                    }
                    image1clicked3 -> {
                        pdf3.add(file)
                        third = filesToMultipartParts("ELECTRIC_BILL[]", pdf3)
                        binfingAddress?.addDocumentLl?.visibility = View.GONE
                        binfingAddress!!.gstCerRL.visibility = View.VISIBLE
                        binfingAddress!!.adIv.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.pdf))

                        binfingAddress!!.deleteAdd.setOnClickListener {
                            binfingAddress?.addDocumentLl?.visibility = View.VISIBLE
                            binfingAddress!!.gstCerRL.visibility = View.GONE
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binfingAddress = FragmentAddressDetailsCustomerBinding.inflate(inflater, container, false)
        binfingAddress!!.countrySpinner.setText("India")
        getStatesfromServer()
        binfingAddress!!.gstLl.setOnClickListener {
            image1clicked=true
            image1clicked2=false
            image1clicked3=false
            capturePhoto("pdf")
        }
        binfingAddress!!.llPan.setOnClickListener {
            image1clicked=false
            image1clicked2=true
            image1clicked3=false
            capturePhoto("pdf")
        }
        binfingAddress!!.addDocumentLl.setOnClickListener {
            image1clicked=false
            image1clicked2=false
            image1clicked3=true
            capturePhoto("pdf")
        }

        return binfingAddress!!.root
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
    private fun getStatesfromServer() {
        binfingAddress!!.loginProgressBar.progressBar.shown()
        lifecycleScope.launch {
            var response = statesviewModel.getStatesList()

            when (response) {

                is NetworkState.Success->{
                    binfingAddress!!.loginProgressBar.progressBar.hidden()
                    stateName= response.body.RESPONSEDATA
//                    initializeAdapter(branchList)
                    getStates(stateName)
                }

                is NetworkState.Error<*>->{
                    binfingAddress!!.loginProgressBar.progressBar.hidden()
                    showCustomDialog(requireContext(), response.msg.toString(),"Error")
                    // Toast.makeText(context,response.msg.toString(),Toast.LENGTH_SHORT).show()
                }

                is NetworkState.NetworkException->{
                    binfingAddress!!.loginProgressBar.progressBar.hidden()
                }
                is NetworkState.HttpErrors.InternalServerError->{
                    binfingAddress!!.loginProgressBar.progressBar.hidden()
                }
                is NetworkState.HttpErrors.ResourceNotFound->{
                    binfingAddress!!.loginProgressBar.progressBar.hidden()
                }
                else->{
                    binfingAddress!!.loginProgressBar.progressBar.hidden()
                }
            }


        }
    }
    private fun getCities(Stateid: String) {
        binfingAddress!!.loginProgressBar.progressBar.shown()
        stateCode = Stateid
        binfingAddress!!.citiesList.text.clear()
        lifecycleScope.launch {
            var response = statesviewModel.getCitiesList(Stateid)

            when (response) {

                is NetworkState.Success->{
                    binfingAddress!!.loginProgressBar.progressBar.hidden()
                    cityList= response.body.RESPONSEDATA
//                    initializeAdapter(branchList)

                    cityAdapter(cityList)

                }

                is NetworkState.Error<*>->{
                    binfingAddress!!.loginProgressBar.progressBar.hidden()
                    showCustomDialog(requireContext(), response.msg.toString(),"Error")
                    // Toast.makeText(context,response.msg.toString(),Toast.LENGTH_SHORT).show()
                }

                is NetworkState.NetworkException->{
                    binfingAddress!!.loginProgressBar.progressBar.hidden()
                }
                is NetworkState.HttpErrors.InternalServerError->{
                    binfingAddress!!.loginProgressBar.progressBar.hidden()
                }
                is NetworkState.HttpErrors.ResourceNotFound->{
                    binfingAddress!!.loginProgressBar.progressBar.hidden()
                }
                else->{
                    binfingAddress!!.loginProgressBar.progressBar.hidden()
                }
            }


        }
    }
    private fun cityAdapter(cityList: ArrayList<CityList>) {

        val spinnerAdapter =
            CityAdapter(requireActivity(), R.layout.item_spinner_row, cityList)
        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner_row)

        binfingAddress?.citiesList?.setAdapter(spinnerAdapter);
        // Remove setting key listener to null
        // bindingSecondPage?.statesList?.setKeyListener(null);
        binfingAddress?.citiesList?.threshold=1
        binfingAddress?.citiesList?.setOnClickListener {
            (it as AutoCompleteTextView).showDropDown()
        }

        binfingAddress?.citiesList?.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            val selectedModel = parent.adapter.getItem(position) as CityList
            // Do whatever you want with the selected model object here
            binfingAddress?.citiesList?.setText(selectedModel.NAME,false)
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
    fun getStates(stateName: ArrayList<ResponseDataPo>) {
        val spinnerAdapter =
            StateAdapter(requireActivity(), R.layout.item_spinner_row, stateName)
        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner_row)
        binfingAddress!!.spinner.adapter = spinnerAdapter
        binfingAddress!!.statesList.setOnClickListener {
//        bindingSecondPage!!.spinner.visibility = View.VISIBLE
            binfingAddress!!.spinner.visibility = View.VISIBLE
            binfingAddress!!.spinner.performClick()

        }
        binfingAddress!!.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view:View?, position: Int, id: Long) {
                // Handle spinner item selection here
//            val selectedItem = parent?.getItemAtPosition(position).toString()
                // Update EditText text
                binfingAddress!!.spinner.visibility=View.GONE

                binfingAddress!!.statesList.setText(spinnerAdapter.getItem(position).NAME)
                getCities(spinnerAdapter.getItem(position).STID)

//            bindingSecondPage!!.statesList.setText(spinnerAdapter.getItem(position).NAME)
//            bindingSecondPage!!.spinner.visibility = View.GONE
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

    }


}