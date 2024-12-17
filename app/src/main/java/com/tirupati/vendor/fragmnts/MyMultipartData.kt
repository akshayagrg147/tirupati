package com.tirupati.vendor.fragmnts

import java.io.Serializable
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class MyMultipartData(
    val fileName: String?,
    val headers: Map<String, List<String>>?
) :  Parcelable
