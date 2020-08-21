package knnekt.shared.data.entity

data class Attachment (
    val name: String?,
    val contentType: String?,
    val type: String,
    val url: String?,
    val id: String,
    val data: String?,
    val size: Double,
    val height: Int,
    val width: Int,
    val duration: Int
)