package com.softwarica.sondr.model

data class ReportModel(
    val reportId: String = "",
    val postId: String = "",
    val reportedByUserId: String = "",
    val reason: String = "",
    val additionalDetails: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
