package app.keyboardly.lib.helper

import androidx.recyclerview.widget.RecyclerView

/**
 * interface class for handle callback recycler view
 * */
interface OnViewReady {
    /**
     * for showing default options with RecyclerView
     * triggered when RecyclerView ready to set the adapter
     */
    fun onRecyclerViewReady(recyclerView: RecyclerView)
}