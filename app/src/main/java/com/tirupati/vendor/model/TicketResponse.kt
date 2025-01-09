package com.tirupati.vendor.model

data class TicketResponse(
    val STATUS: Boolean,
    val RESPONSEDATA: List<Ticket>
)

data class Ticket(
    val TICKET_NO: String,
    val SONO: String,
    val BATCH_NO: String,
    val COIL_NO: String,
    val GROSS_WEIGHT: String,
    val PALLET_WEIGHT: String,
    val NET_WEIGHT: String,
    val ISSUE_TYPE: String,
    val ISSUE_DESCRIPTION: String,
    val ACTION_REMARKS: String?,
    val CREATE_DATE: String,
    val CREATE_TIME: String,
    val ACTION_DATE: String?,
    val STATUS: String
)
