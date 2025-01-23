package com.tirupati.vendor.fragmnts



import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tirupati.vendor.databinding.FragmentAboutBinding
import com.tirupati.vendor.databinding.FragmentPrivacyBinding
import com.tirupati.vendor.databinding.FragmentTermsConditionBinding
import com.tirupati.vendor.helper.hidden
import com.tirupati.vendor.helper.shown
import com.tirupati.vendor.network.ApiService
import com.tirupati.vendor.ui.CustomerHomeActivity
import com.tirupati.vendor.ui.LandingScreenCustomerActivity
import com.tirupati.vendor.ui.LandingScreenGateKeeperActivity
import com.tirupati.vendor.ui.LandingVendorActivity
import com.tirupati.vendor.ui.LandingVendorSActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class PrivacyFragment : Fragment() {

    private var _binding: FragmentPrivacyBinding? = null
    private val binding get() = _binding!!

    private val client = OkHttpClient()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPrivacyBinding.inflate(inflater, container, false)

        // Set title in CustomerHomeActivity
        if(requireActivity() is CustomerHomeActivity){
            CustomerHomeActivity.changeTitle("Privacy Policy")
            CustomerHomeActivity.showIcon(false)

        }
        else if(requireActivity() is LandingScreenGateKeeperActivity){
            LandingScreenGateKeeperActivity.changeTitle("Privacy Policy")
            LandingScreenGateKeeperActivity.showIcon(false)

        }
        else if(requireActivity() is LandingScreenCustomerActivity){
            LandingScreenCustomerActivity.changeTitle("Privacy Policy")
            LandingScreenCustomerActivity.showIcon(false)

        }
        else if(requireActivity() is LandingVendorActivity){
            LandingVendorActivity.changeTitle("Privacy Policy")
            LandingVendorActivity.showIcon(false)

        }
        else if(requireActivity() is LandingVendorSActivity){
            LandingVendorSActivity.changeTitle("Privacy Policy")
            LandingVendorSActivity.showIcon(false)

        }


//        // Set initial placeholder text
//        binding.textViewTitle.text = "Fetching data, please wait..."

        // Fetch data from the API
        fetchMessageTemplate()

        return binding.root
    }

    private fun fetchMessageTemplate() {
        binding.loginProgressBar.progressBar.shown()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Prepare the request
                val request = Request.Builder()
                    .url("https://bsquareappfordemo.com:8095/api/V2/getMessagetemplate")
                    .build()



                // Execute the request
                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val responseData = response.body?.string() ?: ""
                    val messageBody = parseMessageBody(responseData)

                    // Update the UI on the main thread
                    withContext(Dispatchers.Main) {
                        binding.loginProgressBar.progressBar.hidden()
                        binding.textViewTitle.text = Html.fromHtml(messageBody, Html.FROM_HTML_MODE_COMPACT)

                    }
                } else {
                    // Handle error response
                    withContext(Dispatchers.Main) {
                        binding.loginProgressBar.progressBar.hidden()
                        binding.textViewTitle.text = "Error: ${response.message}"
                    }
                }
            } catch (e: Exception) {
                // Handle exception
                withContext(Dispatchers.Main) {
                    binding.loginProgressBar.progressBar.hidden()
                    binding.textViewTitle.text = "Error: ${e.localizedMessage}"
                }
            }
        }
    }

    private fun parseMessageBody(responseData: String): String {
        return try {
            val jsonObject = JSONObject(responseData)
            val responseArray = jsonObject.getJSONArray("RESPONSEDATA")
            val firstTemplate = responseArray.getJSONObject(1)
            firstTemplate.getString("MESSAGE_BODY")
        } catch (e: Exception) {
            e.printStackTrace()
            "Error parsing message body"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}