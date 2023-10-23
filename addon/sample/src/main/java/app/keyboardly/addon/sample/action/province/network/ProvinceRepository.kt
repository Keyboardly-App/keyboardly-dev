package app.keyboardly.addon.sample.action.province.network

import android.content.Context
import app.keyboardly.addon.sample.action.province.model.Province
import kotlinx.coroutines.*

/**
 * Created by Zainal on 20/10/2023 - 16:07
 */
class ProvinceRepository(
    context: Context,
    private var presenter: IProvincePresenter
) {

    private val service = ProvinceService.client(context)
    private var job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    fun getList(){
        presenter.onLoading()
        scope.launch {
            try {
                val list = service.getList()
                withContext(Dispatchers.Main) {
                    presenter.onSuccess(list)
                }
            } catch (e: Exception){
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    presenter.onError(e.message)
                }
            }
        }
    }

}

interface IProvincePresenter{
    fun onLoading()
    fun onSuccess(list: MutableList<Province>?)
    fun onError(error: String?)
}