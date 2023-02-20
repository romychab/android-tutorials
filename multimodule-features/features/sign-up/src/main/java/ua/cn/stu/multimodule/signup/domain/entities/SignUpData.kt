package ua.cn.stu.multimodule.signup.domain.entities

data class SignUpData(
    val email: String,
    val username: String,
    val password: String,
    val repeatPassword: String,
)