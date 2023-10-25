package app.keyboardly.addon.sample.data.remote

import app.keyboardly.addon.sample.action.province.ProvinceListActionView
import app.keyboardly.addon.sample.data.local.SampleDbManager
import app.keyboardly.addon.sample.data.model.Province
import app.keyboardly.style.helper.getCurrentDateTime
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.*
import timber.log.Timber

/**
 * Created by Zainal on 20/10/2023 - 16:07
 */
class ProvinceRepository(
    private val service: ProvinceService,
    private var database: SampleDbManager
) {

    private lateinit var presenter: IProvincePresenter
    private var job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    fun init(param: ProvinceListActionView){
        presenter = param
    }

    fun getLocal() = provinceDao().getProvinces()
    fun clearProvinces() = provinceDao().deleteAll()

    private fun provinceDao() = database.provinceDao()

    fun getList(
    ){
        presenter.onLoading()
        scope.launch {
            try {
                val list = service.getList()
                withContext(Dispatchers.Main) {
                    presenter.onSuccess(list)
                }
                insertLocal(list)
            } catch (e: Exception){
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    presenter.onError(e.message)
                }
            }
        }
    }

    private fun insertLocal(list: List<Province>?) {
        list?.let { it ->
            val provinceList = mutableListOf<Province>()
            provinceList.addAll(it.map { it.copy() })
            provinceList.forEach {data ->
                data.date_time = getCurrentDateTime()
            }
            provinceDao().inserts(provinceList)


            val gson: Gson = GsonBuilder().setPrettyPrinting().create()
            Timber.d("json=\n" + gson.toJson(provinceList))
        }
    }

}

interface IProvincePresenter{
    fun onLoading()
    fun onSuccess(list: MutableList<Province>?)
    fun onError(error: String?)
}