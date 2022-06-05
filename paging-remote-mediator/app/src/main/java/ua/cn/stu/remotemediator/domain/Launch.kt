package ua.cn.stu.remotemediator.domain

interface Launch {
    val id: Long
    val name: String
    val description: String
    val imageUrl: String
    val year: Int
    val launchTimestamp: Long
    val isSuccess: Boolean
}
