package app.keyboardly.addon.sample.action.province.network

import android.content.Context
import app.keyboardly.addon.sample.action.province.model.Province
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by Zainal on 20/10/2023 - 16:07
 */
class ProvinceRepository(
    context: Context,
    private val presenter: IProvincePresenter
) {

    private val service = ProvinceService.client(context)
    private val scope = CoroutineScope(Dispatchers.Default)

    fun getList(){
        presenter.onLoading()
        scope.launch {
            try {
                val list = service.getList()
                presenter.onSuccess(list)
            } catch (e: Exception){
                e.printStackTrace()
                presenter.onError(e.message)
            }
        }
    }

}

interface IProvincePresenter{
    fun onLoading()
    fun onSuccess(list: MutableList<Province>?)
    fun onError(error: String?)
}