package app.keyboardly.dev.ui.addon

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddOnViewModel : ViewModel() {

    private val _list = MutableLiveData<List<AddOnModel>>().apply {
        value = listAddOn()
    }
    val list: LiveData<List<AddOnModel>> = _list


    private fun listAddOn(): List<AddOnModel> {
        val list = mutableListOf<AddOnModel>()

        list.add(
            AddOnModel(
                21,
                "sample",
                "Sample",
                "a sample add on",
                "Keyboardly",
                "https://keyboardly.app",
                /*dummy icon*/
                "https://img.icons8.com/external-flaticons-flat-flat-icons/344/external-dummy-robotics-flaticons-flat-flat-icons.png",
                0,
                SAMPLE_ID,
            )
        )

        return list
    }
}