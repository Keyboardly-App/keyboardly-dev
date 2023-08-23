package app.keyboardly.lib

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import app.keyboardly.style.helper.showToast
import app.keyboardly.style.helper.showToastLong

/**
 * Created by zainal on 6/25/22 - 4:32 PM
 */
abstract class KeyboardActionView (
    val dependency: KeyboardActionDependency
) {

    private val packageIdMainApp = "app.keyboardly.android"

    // should declare this onCreate method
    var viewLayout: View? = null

    abstract fun onCreate()

    open fun getView(): View? {
        instanceLogic()
        return view()
    }

    fun view() = viewLayout

    fun instanceLogic() {
        if (viewLayout == null) {
            onCreate()
        } else {
            onResume()
        }
    }

    open fun onResume() {}

    fun getContext(): Context {
        return dependency.getContext()
    }

    fun getLayoutInflater(): LayoutInflater {
        return LayoutInflater.from(getContext())
    }

    fun toast(message: String) {
        if (getPackageName() == packageIdMainApp) {
            showToast(getContext(), message)
        } else {
            showToast(message)
        }
    }

    fun toast(message: Int) {
        toast(getContext().getString(message))
    }

    private fun getPackageName(): String? {
        return dependency.getCurrentEditorInfo().packageName
    }

    private fun showToastLong(message: Int) {
        showToastLong(getContext().getString(message))
    }

    private fun showToast(message: String) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showToastLong(message: String) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show()
    }

    fun toastLong(message: Int) {
        if (getPackageName() == packageIdMainApp){
            showToastLong(getContext(), message)
        } else {
            showToastLong(message)
        }
    }

    fun toastLong(message: String) {
        if (getPackageName() == packageIdMainApp) {
            showToastLong(getContext(), message)
        } else {
            showToastLong(message)
        }
    }

    fun getSelectedTags(chipGroup: ChipGroup): String {
        val list = mutableListOf<String>()
        chipGroup.checkedChipIds.forEach { id ->
            val name = chipGroup.findViewById<Chip>(id).text.toString()
            list.add(name)
        }
        return list.joinToString(",")
    }

    fun Int.toPx(): Int = (this * getContext().resources.displayMetrics.density).toInt()

}