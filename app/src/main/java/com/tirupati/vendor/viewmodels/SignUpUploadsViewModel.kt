package com.tirupati.vendor.viewmodels

import androidx.lifecycle.ViewModel
import com.tirupati.vendor.helper.toRequestBody
import com.tirupati.vendor.model.UploadsDetailResponse
import com.tirupati.vendor.network.ApiService
import com.tirupati.vendor.network.NetworkCall
import com.tirupati.vendor.network.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class SignUpUploadsViewModel @Inject constructor(private val registerUploadVMRepo: SignUpUploadsRepository) :
    ViewModel() {


    suspend fun postSignUpRegistration(
        headers: HashMap<String, String>,
        LATITUDE: String,
        LONGITUDE: String,
        GODOWN_LOCATION: String,
        OTHERVENDORSAPPLICABLE: String,

        V1_NAME: String,
        V1_EMAIL: String,
        V1_CONTACT_NO: String,
        V1_AADHAR_NO: String,

        V2_NAME: String,
        V2_EMAIL: String,
        V2_CONTACT_NO: String,
        V2_AADHAR_NO: String,

        V3_NAME: String,
        V3_EMAIL: String,
        V3_CONTACT_NO: String,
        V3_AADHAR_NO: String,

        OWNER_NAME: String,
        OWNER_CONTACT: String,
        OWNER_EMAIL: String,
        NAME_OF_ORGANISATION: String,
        VENDOR_LEGAL_NAME: String,
        CONTACT_PERSON: String,
        CONTACT_NUMBER: String,
        CONTACT_EMAIL: String,
        POC_NAME: String,
        POC_NO: String,
        WHATSAPP_NO: String,
        ADDRESS: String,
        country: String,
        STATE: String,
        CITY: String,
        PIN_CODE: String,
        GSTIN: String,
        PANCARD_NO: String,
        BANK_NAME: String,
        ACCOUNT_NUMBER: String,
        ACCOUNT_TYPE: String,
        BANK_BRANCH: String,
        IFSC_CODE: String?,
        MSME_APPLICABLE: String?,
        E_INVOICE_APPLICABLE: String?,

        imagelist1: ArrayList<MultipartBody.Part?>,
        imagelist2: ArrayList<MultipartBody.Part?>,
        imagelist3: ArrayList<MultipartBody.Part?>,
        imagelist4: ArrayList<MultipartBody.Part?>,
        imagelist5: ArrayList<MultipartBody.Part?>,
        imagelist6: ArrayList<MultipartBody.Part?>,
        imagelist7: ArrayList<MultipartBody.Part?>,
        imagelist8: ArrayList<MultipartBody.Part?>,
        imagelist9: ArrayList<MultipartBody.Part?>,
        imagelist10: ArrayList<MultipartBody.Part?>,
        imagelist11: ArrayList<MultipartBody.Part?>
    ): NetworkState<UploadsDetailResponse> {
        return registerUploadVMRepo.signUpUploads(
            headers,
            LATITUDE,
            LONGITUDE,
            GODOWN_LOCATION,
            OTHERVENDORSAPPLICABLE,

            V1_NAME,
            V1_EMAIL,
            V1_CONTACT_NO,
            V1_AADHAR_NO,

            V2_NAME,
            V2_EMAIL,
            V2_CONTACT_NO,
            V2_AADHAR_NO,

            V3_NAME,
            V3_EMAIL,
            V3_CONTACT_NO,
            V3_AADHAR_NO,

            OWNER_NAME,
            OWNER_CONTACT,
            OWNER_EMAIL,
            NAME_OF_ORGANISATION,
            VENDOR_LEGAL_NAME,
            CONTACT_PERSON,
            CONTACT_NUMBER,
            CONTACT_EMAIL,
            POC_NAME,
            POC_NO,
            WHATSAPP_NO,
            ADDRESS,
            country,
            STATE,
            CITY,
            PIN_CODE,
            GSTIN,
            PANCARD_NO,
            BANK_NAME,
            ACCOUNT_NUMBER,
            ACCOUNT_TYPE,
            BANK_BRANCH,
            IFSC_CODE!!,
            MSME_APPLICABLE,
            E_INVOICE_APPLICABLE,
            imagelist1,
            imagelist2,
            imagelist3,
            imagelist4,
            imagelist5,
            imagelist6,
            imagelist7,
            imagelist8,
            imagelist9,
            imagelist10,
            imagelist11
        )
    }

}

class SignUpUploadsRepository @Inject constructor(private val apiService: ApiService) :
    NetworkCall() {


    suspend fun signUpUploads(
        headers: HashMap<String, String>,
        LATITUDE: String,
        LONGITUDE: String,
        GODOWN_LOCATION: String,
        OTHERVENDORSAPPLICABLE: String,

        V1_NAME: String,
        V1_EMAIL: String,
        V1_CONTACT_NO: String,
        V1_AADHAR_NO: String,

        V2_NAME: String,
        V2_EMAIL: String,
        V2_CONTACT_NO: String,
        V2_AADHAR_NO: String,

        V3_NAME: String,
        V3_EMAIL: String,
        V3_CONTACT_NO: String,
        V3_AADHAR_NO: String,

        OWNER_NAME: String,
        OWNER_CONTACT: String,
        OWNER_EMAIL: String,
        NAME_OF_ORGANISATION: String,
        VENDOR_LEGAL_NAME: String,
        CONTACT_PERSON: String,
        CONTACT_NUMBER: String,
        CONTACT_EMAIL: String,
        POC_NAME: String,
        POC_NO: String,
        WHATSAPP_NO: String,
        ADDRESS: String,
        country: String,
        STATE: String,
        CITY: String,
        PIN_CODE: String,
        GSTIN: String,
        PANCARD_NO: String,
        BANK_NAME: String,
        ACCOUNT_NUMBER: String,
        ACCOUNT_TYPE: String,
        BANK_BRANCH: String,
        IFSC_CODE: String?,
        MSME_APPLICABLE: String?,
        E_INVOICE_APPLICABLE: String?,

        imagelist1: ArrayList<MultipartBody.Part?>,
        imagelist2: ArrayList<MultipartBody.Part?>,
        imagelist3: ArrayList<MultipartBody.Part?>,
        imagelist4: ArrayList<MultipartBody.Part?>,
        imagelist5: ArrayList<MultipartBody.Part?>,
        imagelist6: ArrayList<MultipartBody.Part?>,
        imagelist7: ArrayList<MultipartBody.Part?>,
        imagelist8: ArrayList<MultipartBody.Part?>,
        imagelist9: ArrayList<MultipartBody.Part?>,
        imagelist10: ArrayList<MultipartBody.Part?>,
        imagelist11: ArrayList<MultipartBody.Part?>
    ): NetworkState<UploadsDetailResponse> {

        return safeApiCall {

            apiService.signUpCall(
                headers,
                LATITUDE.toRequestBody(),
                LONGITUDE.toRequestBody(),
                GODOWN_LOCATION.toRequestBody(),
                OTHERVENDORSAPPLICABLE.toRequestBody(),

                V1_NAME.toRequestBody(),
                V1_EMAIL.toRequestBody(),
                V1_CONTACT_NO.toRequestBody(),
                V1_AADHAR_NO.toRequestBody(),

                V2_NAME.toRequestBody(),
                V2_EMAIL.toRequestBody(),
                V2_CONTACT_NO.toRequestBody(),
                V2_AADHAR_NO.toRequestBody(),

                V3_NAME.toRequestBody(),
                V3_EMAIL.toRequestBody(),
                V3_CONTACT_NO.toRequestBody(),
                V3_AADHAR_NO.toRequestBody(),

                OWNER_NAME.toRequestBody(),
                OWNER_CONTACT.toRequestBody(),
                OWNER_EMAIL.toRequestBody(),
                NAME_OF_ORGANISATION.toRequestBody(),
                VENDOR_LEGAL_NAME.toRequestBody(),
                CONTACT_PERSON.toRequestBody(),
                CONTACT_NUMBER.toRequestBody(),
                CONTACT_EMAIL.toRequestBody(),
                POC_NAME.toRequestBody(),
                POC_NO.toRequestBody(),
                WHATSAPP_NO.toRequestBody(),
                ADDRESS.toRequestBody(),
                country.toRequestBody(),
                STATE.toRequestBody(),
                CITY.toRequestBody(),
                PIN_CODE.toRequestBody(),
                GSTIN.toRequestBody(),
                PANCARD_NO.toRequestBody(),
                BANK_NAME.toRequestBody(),
                ACCOUNT_NUMBER.toRequestBody(),
                ACCOUNT_TYPE.toRequestBody(),
                BANK_BRANCH.toRequestBody(),
                IFSC_CODE!!.toRequestBody(),
            MSME_APPLICABLE?.toRequestBody(),
            E_INVOICE_APPLICABLE?.toRequestBody(),
                imagelist1,
                imagelist2,
                imagelist3,
                imagelist4,
                imagelist5,
                imagelist6,
                imagelist7,
                imagelist8,
                imagelist9,
                imagelist10,
                imagelist11
            )
        }
    }

}
