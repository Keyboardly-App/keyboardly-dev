package app.keyboardly.dev.ui.addon

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddOnViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Add On Fragment"
    }
    val text: LiveData<String> = _text
}