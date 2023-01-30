package app.keyboardly.lib.helper

import android.widget.EditText

/**
 * Created by zainal on 11/23/21 - 2:23 PM
 */
interface InputPresenter {

    /**
     * triggered when user end the input mode / click the done button
     * @param text: result text input
     * @param editText: edittext that active on input mode
     */
    fun onDone(text: String, editText: EditText?)
}