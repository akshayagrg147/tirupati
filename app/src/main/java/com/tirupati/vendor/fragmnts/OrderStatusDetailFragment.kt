package com.tirupati.vendor.fragmnts


import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tirupati.vendor.R
import com.tirupati.vendor.databinding.FragmentOrderStatusDetailBinding
import com.tirupati.vendor.model.ResponseDataCustomer
import com.tirupati.vendor.ui.CustomerHomeActivity
import com.tirupati.vendor.utils.toast


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private val STORAGE_PERMISSION_CODE = 101

/**
 * A simple [Fragment] subclass.
 * Use the [OrderStatusDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OrderStatusDetailFragment : Fragment() {

    private var _binding: FragmentOrderStatusDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var responseData: ResponseDataCustomer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderStatusDetailBinding.inflate(inflater, container, false)

        // Retrieve the Parcelable object from arguments
        arguments?.getParcelable<ResponseDataCustomer>("responseData")?.let {
            responseData = it
        }

        // Use the responseData for UI updates
        setupUI()

        return binding.root
    }
    override fun onResume() {
        super.onResume()

        CustomerHomeActivity.showIcon(false)
        CustomerHomeActivity.changeTitle("Sales Order")


    }

    override fun onPause() {
        super.onPause()
        CustomerHomeActivity.showIcon(false)
        CustomerHomeActivity.changeTitle("Sales Order")

    }

private fun downloadFileUsingDownloadManager(  url: String, fileName: String, context: Context) {
    // Set up the DownloadManager
    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    // Set the download URL and destination path
    val request = DownloadManager.Request(Uri.parse(url))
        .setTitle(fileName)
        .setDescription("Downloading file...")
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        .setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, fileName)

    // Enqueue the download request
    val downloadId = downloadManager.enqueue(request)

    // You can use the download ID to track the download if needed.
    // For example, to listen for download completion, you can query the DownloadManager
    // and check the status of the download by its ID.

    // Toast message to indicate the download has started
    Toast.makeText(context, "Download started...", Toast.LENGTH_SHORT).show()

    // Optionally, you can track the download completion using a BroadcastReceiver or querying the DownloadManager
}

    private fun setupUI() {

        // Update UI elements using responseData
        binding.valueSalesOrderDate.text = responseData.salesOrderDate
        binding.valueCustomerPoNumber.text = responseData.customerPONo
        binding.valueBookingType.text = responseData.directBooking
        binding.valueNameOfBroker.text = responseData.broker

        binding.labelBillingAddressValue.text= responseData.billTo
        binding.labelShippingAddressValue.text= responseData.shipTo
        binding.labelStateValue.text= responseData.state
        binding.labelPaymentTermsValue.text= responseData.paymentTerms
        binding.labelPaymentCreditDaysValue.text= responseData.paymentCreditDays
        binding.labelOfferValidityValue.text= responseData.orderValidity

        binding.valueItemName.text = responseData.itemName
        binding.valueQuantity.text = responseData.quantity
        binding.valueRate.text = responseData.rate
        binding.labelUomVALUE.text = responseData.uom

        binding.valueTaxableAmount.text = responseData.taxableAmount
        binding.labelTaxValue.text = responseData.taxAmount
        binding.valueTotalAmount.text = responseData.totalAmount
        binding.labelBatchNumberValue.text = responseData.batchDetails.firstOrNull()?.batchCode ?: "N/A"  // Handle batch details

        val initialBatch = responseData.batchDetails.firstOrNull()
        binding.labelGrossWeightValue.text = initialBatch?.grossWeight ?: ""
        binding.labelPalletWeightValue.text = initialBatch?.palletWeight ?: ""
        binding.labelNetWeightValue.text = initialBatch?.netWeight ?: ""

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, responseData.batchDetails.mapNotNull { it.coilNo })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.coilNumberValue.adapter = adapter

        binding.coilNumberValue.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Get the selected coil number
                val selectedCoilNo = adapter.getItem(position)

                // Find the corresponding BatchDetail object
                val selectedBatch = responseData.batchDetails.find { it.coilNo == selectedCoilNo }

                // Update the labels with the selected batch details
                binding.labelGrossWeightValue.text = selectedBatch?.grossWeight ?: ""
                binding.labelPalletWeightValue.text = selectedBatch?.palletWeight ?: ""
                binding.labelNetWeightValue.text = selectedBatch?.netWeight ?: ""
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case where nothing is selected (optional)
            }
        }





        binding.labelVechileNumberValue.text = responseData.truckNo ?: "N/A" // Handle nullable field
        binding.driverNameLabelValue.text = responseData.driverName ?: "N/A"  // Handle nullable field
        binding.labelContactNumberValue.text = responseData.driverContactNo ?: "N/A"  // Handle nullable field
        binding.quantityDoucumentImage.setOnClickListener{

            val qualityDocumentUrl = responseData.otherDetails.get(0).qualityDocument

            if (qualityDocumentUrl?.isNotBlank()==true) {
                if (qualityDocumentUrl.startsWith("http://") || qualityDocumentUrl.startsWith("https://")) {
                    downloadFileUsingDownloadManager(qualityDocumentUrl, "Quality Document", requireContext())
                } else {
                    toast("Invalid URL")
                }
            } else {
                toast("Empty file")
            }



        }

        binding.imgSalesInvoic.setOnClickListener{
            if(responseData.otherDetails.get(0).salesInvoice?.isNotBlank()==true)
                downloadFileUsingDownloadManager(responseData.otherDetails.get(0).salesInvoice!!,"Sales Invoice",requireContext(),)
            else{
                toast("empty file")
            }


        }
        binding.imgEwaybill.setOnClickListener{
            if(responseData?.otherDetails?.get(0)?.eWayBill?.isNotBlank()==true)
            downloadFileUsingDownloadManager(responseData.otherDetails?.get(0)?.eWayBill?:"","E way bill",requireContext(),)
            else{
                toast("empty file")
            }


        }

        binding.imgPlatformInvoice.setOnClickListener{
            if(responseData.otherDetails.get(0).proformaInvoice?.isNotBlank()==true)
            downloadFileUsingDownloadManager(responseData.otherDetails.get(0).proformaInvoice!!,"Proforma Invoice",requireContext(),)

            else{
                toast("empty file")
            }

        }

        binding.contactNumberToggle.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.contactNumberToggle.isChecked=false
                val bundle = Bundle().apply {
                    putParcelable("responseData", responseData)
                }

                // Navigate to the other screen and pass the data
                findNavController().navigate(R.id.action_customerFragment_to_reportIssueFragment, bundle)

                // Handle toggle on
            } else {
                // Handle toggle off
            }
        })
        // Add more UI updates as needed
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

