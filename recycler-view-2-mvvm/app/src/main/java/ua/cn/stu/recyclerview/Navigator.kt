package ua.cn.stu.recyclerview

import ua.cn.stu.recyclerview.model.User

interface Navigator {

    fun showDetails(user: User)

    fun goBack()

    fun toast(messageRes: Int)

}