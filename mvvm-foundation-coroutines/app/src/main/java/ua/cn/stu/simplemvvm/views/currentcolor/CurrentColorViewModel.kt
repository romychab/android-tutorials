package ua.cn.stu.simplemvvm.views.currentcolor

import android.Manifest
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ua.cn.stu.foundation.model.PendingResult
import ua.cn.stu.foundation.model.SuccessResult
import ua.cn.stu.foundation.model.takeSuccess
import ua.cn.stu.foundation.sideeffects.dialogs.Dialogs
import ua.cn.stu.foundation.sideeffects.dialogs.plugin.DialogConfig
import ua.cn.stu.foundation.sideeffects.intents.Intents
import ua.cn.stu.foundation.sideeffects.navigator.Navigator
import ua.cn.stu.foundation.sideeffects.permissions.Permissions
import ua.cn.stu.foundation.sideeffects.permissions.plugin.PermissionStatus
import ua.cn.stu.foundation.sideeffects.resources.Resources
import ua.cn.stu.foundation.sideeffects.toasts.Toasts
import ua.cn.stu.foundation.views.BaseViewModel
import ua.cn.stu.foundation.views.LiveResult
import ua.cn.stu.foundation.views.MutableLiveResult
import ua.cn.stu.simplemvvm.R
import ua.cn.stu.simplemvvm.model.colors.ColorsRepository
import ua.cn.stu.simplemvvm.model.colors.NamedColor
import ua.cn.stu.simplemvvm.views.changecolor.ChangeColorFragment

class CurrentColorViewModel(
    private val navigator: Navigator,
    private val toasts: Toasts,
    private val resources: Resources,
    private val permissions: Permissions,
    private val intents: Intents,
    private val dialogs: Dialogs,
    private val colorsRepository: ColorsRepository,
) : BaseViewModel() {

    private val _currentColor = MutableLiveResult<NamedColor>(PendingResult())
    val currentColor: LiveResult<NamedColor> = _currentColor

    // --- example of listening results via model layer

    init {
        viewModelScope.launch {
            // as listenCurrentColor() returns infinite flow,
            // collecting is cancelled when view-model is going to be destroyed
            // (because collect() is executed inside viewModelScope)
            colorsRepository.listenCurrentColor().collect {
                _currentColor.postValue(SuccessResult(it))
            }
        }
        load()
    }

    // --- example of listening results directly from the screen

    override fun onResult(result: Any) {
        super.onResult(result)
        if (result is NamedColor) {
            val message = resources.getString(R.string.changed_color, result.name)
            toasts.toast(message)
        }
    }

    // ---

    fun changeColor() {
        val currentColor = currentColor.value.takeSuccess() ?: return
        val screen = ChangeColorFragment.Screen(currentColor.id)
        navigator.launch(screen)
    }

    /**
     * Example of using side-effect plugins
     */
    fun requestPermission() = viewModelScope.launch {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        val hasPermission = permissions.hasPermissions(permission)
        if (hasPermission) {
            dialogs.show(createPermissionAlreadyGrantedDialog())
        } else {
            when (permissions.requestPermission(permission)) {
                PermissionStatus.GRANTED -> {
                    toasts.toast(resources.getString(R.string.permissions_grated))
                }
                PermissionStatus.DENIED -> {
                    toasts.toast(resources.getString(R.string.permissions_denied))
                }
                PermissionStatus.DENIED_FOREVER -> {
                    if (dialogs.show(createAskForLaunchingAppSettingsDialog())) {
                        intents.openAppSettings()
                    }
                }
            }
        }
    }

    fun tryAgain() {
        load()
    }

    private fun load() = into(_currentColor) { colorsRepository.getCurrentColor() }

    private fun createPermissionAlreadyGrantedDialog() = DialogConfig(
        title = resources.getString(R.string.dialog_permissions_title),
        message = resources.getString(R.string.permissions_already_granted),
        positiveButton = resources.getString(R.string.action_ok)
    )

    private fun createAskForLaunchingAppSettingsDialog() = DialogConfig(
        title = resources.getString(R.string.dialog_permissions_title),
        message = resources.getString(R.string.open_app_settings_message),
        positiveButton = resources.getString(R.string.action_open),
        negativeButton = resources.getString(R.string.action_cancel)
    )
}