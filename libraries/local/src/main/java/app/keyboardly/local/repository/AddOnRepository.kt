package app.keyboardly.local.repository

import android.content.Context
import app.keyboardly.local.DatabaseManager
import app.keyboardly.local.model.AddOnModel

/**
 * Created by zainal on 12/7/21 - 1:42 PM
 */
class AddOnRepository(
    context: Context
) {
    val dao = DatabaseManager.getInstance(context).addOnDao()

    fun insert(list: List<AddOnModel>) = dao.inserts(list)
    fun insert(data: AddOnModel) {
        dao.insert(data)
    }
    fun delete(data: AddOnModel) = dao.delete(data)
    fun getInstalledAddOn() = dao.getInstalledAddOn()
    fun getInstalledById(data: AddOnModel) = dao.getInstalledById(data.id)
    fun isInstalled(data: AddOnModel): Boolean {
        return dao.getInstalledById(data.id) != null
    }
    fun getInstalledById(id: Int) = dao.getInstalledById(id)
    fun getAddOn() = dao.getAddOn()
    fun getAddOnAsync() = dao.getAddOnAsync()
}