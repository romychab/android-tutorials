# MVVM Navigation Example

This example describes how to implement navigation handled from `ViewModel`.

For example:

```kotlin
class MyViewModel(
    private val navigator: Navigator,
    private val screen: MyCurrentScreenArgs
) : BaseViewModel {
    
    fun onEditButtonPressed() {
        if (!canEdit()) return
        navigator.launch(OtherFragment.Screen("arg1"))
    }

    override fun onResult(result: Any) {
        // results from other screens can be listened here using IF or WHEN operator
        if (result is SomeResult1) {
            // process result from screen 1 here
        } else if (result is SomeResult2) {
            // process result from screen 2 here
        } else {
            // ...
        }
    }

}
```

- Entry point: `MainActivity.kt`
- Initial screen: `screens.hello.HelloFragment`, `screens.hello.HelloViewModel`
- Second screen: `screens.edit.EditFragment`, `screens.edit.EditViewModel`