package app.keyboardly.local.repository

import android.content.Context
import app.keyboardly.local.DatabaseManager
import app.keyboardly.local.model.ClipboardData

/**
 * Created by zainal on 12/7/21 - 1:42 PM
 */
class ClipboardRepository(
    context: Context
) {
    val dao = DatabaseManager.getInstance(context).clipboardDao()

    fun insert(data: ClipboardData){

        val clipboards = dao.getClipboards()
        if (clipboards.isNullOrEmpty()){
            dao.insert(data)
        } else {
            val size = clipboards.size
            if (size >= 20){
                dao.delete(clipboards.last())
            }
            dao.insert(data)
        }
    }

    fun getClipBoards(): MutableList<ClipboardData>? {
        return dao.getClipboards()
    }

    fun isNotInsertedYet(data: ClipboardData): Boolean {
        return dao.isSaved(data.text.toString()) == null
    }
}