package ua.cn.stu.multimodule.uialerts

import android.content.Context

interface UiAlerts {

    fun toast(message: String)

    companion object : UiAlerts {

        private lateinit var appContext: Context

        private val instance: UiAlerts by lazy {
            // get an implementation at runtime if possible
            try {
                Class.forName("ua.cn.stu.multimodule.uialerts.EntryPoint")
                    .getDeclaredMethod("get", Context::class.java)
                    .invoke(null, appContext) as UiAlerts
            } catch (e: Exception) {
                EmptyUiAlerts()
            }
        }

        /**
         * Call this e.g. in Application.onCreate or Activity.onCreate before usage of [UiAlerts.toast]
         */
        fun init(appContext: Context) {
            this.appContext = appContext
        }

        /**
         * Show toast message.
         */
        override fun toast(message: String) {
            instance.toast(message)
        }

    }

}