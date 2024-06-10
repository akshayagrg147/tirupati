package com.tirupati.vendor.network.models

data class ActivityModel(
	val result: Int? = null,
	val msg: String? = null,
	val data: Any? = null
)

data class ActivityDataItem(
	val profession: String? = null,
	val datetime: String? = null,
	val image_url: String? = null,
	val last_name: String? = null,
	val createdAt: String? = null,
	val id: String? = null,
	val first_name: String? = null,
	val country_flag: Any? = null,
	val age: String? = null
)

