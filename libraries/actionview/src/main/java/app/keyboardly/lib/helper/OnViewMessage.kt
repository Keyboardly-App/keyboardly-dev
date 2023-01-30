package app.keyboardly.lib.helper

import android.widget.TextView

interface OnViewMessage {
    /**
     * for showing message on options area like default RecyclerView and ChipGroup options
     * triggered after textview appeared but no text,
     * @param textView: default textView that ready to set some message on it
     */
    fun onTextViewReady(textView: TextView)
}