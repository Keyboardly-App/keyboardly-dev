package app.keyboardly.lib.helper

import android.widget.EditText

/**
 * Created by zainal on 11/23/21 - 2:23 PM
 */
interface InputPresenter {
    fun onDone(text: String, editText: EditText?)
}