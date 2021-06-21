package ua.cn.stu.recyclerview

import android.app.Application
import ua.cn.stu.recyclerview.model.UsersService

class App : Application() {

    val usersService = UsersService()
}