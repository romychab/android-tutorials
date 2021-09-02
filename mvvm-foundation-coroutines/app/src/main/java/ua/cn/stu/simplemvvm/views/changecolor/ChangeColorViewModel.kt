package ua.cn.stu.simplemvvm.views.changecolor

import androidx.lifecycle.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ua.cn.stu.foundation.model.PendingResult
import ua.cn.stu.foundation.model.Result
import ua.cn.stu.foundation.model.SuccessResult
import ua.cn.stu.foundation.sideeffects.navigator.Navigator
import ua.cn.stu.foundation.sideeffects.resources.Resources
import ua.cn.stu.foundation.sideeffects.toasts.Toasts
import ua.cn.stu.foundation.views.BaseViewModel
import ua.cn.stu.foundation.views.ResultFlow
import ua.cn.stu.foundation.views.ResultMutableStateFlow
import ua.cn.stu.simplemvvm.R
import ua.cn.stu.simplemvvm.model.colors.ColorsRepository
import ua.cn.stu.simplemvvm.model.colors.NamedColor
import ua.cn.stu.simplemvvm.views.changecolor.ChangeColorFragment.Screen

class ChangeColorViewModel(
    screen: Screen,
    private val navigator: Navigator,
    private val toasts: Toasts,
    private val resources: Resources,
    private val colorsRepository: ColorsRepository,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel(), ColorsAdapter.Listener {

    // input sources
    private val _availableColors: ResultMutableStateFlow<List<NamedColor>> = MutableStateFlow(PendingResult())
    private val _currentColorId = savedStateHandle.getStateFlow("currentColorId", screen.currentColorId)
    private val _saveInProgress = MutableStateFlow(false)

    // main destination (contains merged values from _availableColors & _currentColorId & _saveInProgress)
    val viewState: ResultFlow<ViewState> = combine(
        _availableColors,
        _currentColorId,
        _saveInProgress,
        ::mergeSources
    )

    // example of converting Flow into LiveData
    // - incoming flow is Flow<Result<ViewState>>
    // - Flow<Result<ViewState>> is mapped to Flow<String> by using .map() operator
    // - then Flow<String> is converted to LiveData<String> by using .asLiveData()
    val screenTitle: LiveData<String> = viewState
        .map { result ->
            return@map if (result is SuccessResult) {
                val currentColor = result.data.colorsList.first { it.selected }
                resources.getString(R.string.change_color_screen_title, currentColor.namedColor.name)
            } else {
                resources.getString(R.string.change_color_screen_title_simple)
            }
        }
        .asLiveData()

    init {
        load()
    }

    override fun onColorChosen(namedColor: NamedColor) {
        if (_saveInProgress.value) return
        _currentColorId.value = namedColor.id
    }

    fun onSavePressed() = viewModelScope.launch {
        try {
            _saveInProgress.value = true
            val currentColorId = _currentColorId.value
            val currentColor = colorsRepository.getById(currentColorId)

            // here we don't want to listen progress but only wait for operation completion
            // so we use collect() without any inner block:
            colorsRepository.setCurrentColor(currentColor).collect()

            navigator.goBack(currentColor)
        } catch (e: Exception) {
            if (e !is CancellationException) toasts.toast(resources.getString(R.string.error_happened))
        } finally {
            _saveInProgress.value = false
        }
    }

    fun onCancelPressed() {
        navigator.goBack()
    }

    fun tryAgain() {
        load()
    }

    /**
     * Transformation pure method for combining data from several input flows:
     * - the result of fetching colors list (Result<List<NamedColor>>)
     * - current selected color in RecyclerView (Long)
     * - flag whether saving operation is in progress or not (Boolean)
     * All values above are merged into one [ViewState] instance:
     * ```
     * Flow<Result<List<NamedColor>>> ---+
     * Flow<Long> -----------------------|--> Flow<Result<ViewState>>
     * Flow<Boolean> --------------------+
     * ```
     */
    private fun mergeSources(colors: Result<List<NamedColor>>, currentColorId: Long, saveInProgress: Boolean): Result<ViewState> {
        // map Result<List<NamedColor>> to Result<ViewState>
        return colors.map { colorsList ->
            ViewState(
                // map List<NamedColor> to List<NamedColorListItem>
                colorsList = colorsList.map { NamedColorListItem(it, currentColorId == it.id) },

                showSaveButton = !saveInProgress,
                showCancelButton = !saveInProgress,
                showSaveProgressBar = saveInProgress
            )
        }
    }

    private fun load() = into(_availableColors) { colorsRepository.getAvailableColors() }

    data class ViewState(
        val colorsList: List<NamedColorListItem>,
        val showSaveButton: Boolean,
        val showCancelButton: Boolean,
        val showSaveProgressBar: Boolean
    )

}