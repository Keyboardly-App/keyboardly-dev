package app.keyboardly.addon.sample.action.province

import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.keyboardly.addon.sample.action.province.model.Province
import app.keyboardly.addon.sample.action.province.network.IProvincePresenter
import app.keyboardly.addon.sample.action.province.network.ProvinceRepository
import app.keyboardly.addon.sample.databinding.SampleProvinceListLayoutBinding
import app.keyboardly.lib.KeyboardActionDependency
import app.keyboardly.lib.KeyboardActionView
import app.keyboardly.lib.helper.OnViewReady
import app.keyboardly.style.helper.gone
import app.keyboardly.style.helper.visible
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import timber.log.Timber

/**
 * Created by Zainal on 20/10/2023 - 13:44
 */
class ProvinceListActionView(
    dependency: KeyboardActionDependency
) : KeyboardActionView(dependency), IProvincePresenter {

    private var topRecyclerView: RecyclerView? = null
    private var provinceListAdapter: ProvinceListAdapter? = null
    private var listData: MutableList<Province> = mutableListOf()
    private var binding : SampleProvinceListLayoutBinding? = null
    private var job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)
    private var repository = ProvinceRepository(getContext(), this,scope)

    override fun onCreate() {
        binding = SampleProvinceListLayoutBinding.inflate(getLayoutInflater())

        binding?.apply {
            listProvincesRV.apply {
                provinceListAdapter = ProvinceListAdapter(context, listData) {
                    toast("Selected province:\n${it.name}")
                }
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(dependency.getContext())
                adapter = provinceListAdapter
            }

            back.setOnClickListener {
                dependency.viewAddOnNavigation()
            }

            repository.getList()

            search.setOnClickListener {
                dependency.showTopRecyclerView(object : OnViewReady{
                    override fun onRecyclerViewReady(recyclerView: RecyclerView) {
                        recyclerView.apply {
                            layoutManager = LinearLayoutManager(context)
                            adapter = provinceListAdapter

                            topRecyclerView = this
                        }
                    }
                })

                dependency.requestInput(hint = androidx.appcompat.R.string.search_menu_title,
                    inputOnFloatingView = true,
                    onCloseSearch = {
                        topRecyclerView = null
                        provinceListAdapter?.updateList(listData)
                    },
                    textWatcher = object :TextWatcher{
                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {}

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                            if (topRecyclerView!=null){
                                val listForFilter = listData
                                val query = s?.toString()
                                if (query!=null){
                                    val filtered = listForFilter.filter { it.name.lowercase().contains(query) }
                                    provinceListAdapter?.updateList(filtered)
                                } else {
                                    provinceListAdapter?.updateList(listData)
                                }
                            } else {
                                Timber.e("empty top recyclerview")
                            }
                        }

                        override fun afterTextChanged(s: Editable?) {}

                    })
            }
        }

        viewLayout = binding?.root
    }

    override fun onResume() {
        repository.getList()
        super.onResume()
    }

    override fun onLoading() {
        loading()
    }

    private fun loading(isLoading: Boolean=true) {
        Handler(Looper.getMainLooper()).post {
            binding?.apply {
                if (isLoading) {
                    progress.visible()
                    listProvincesRV.gone()
                } else {
                    progress.gone()
                    listProvincesRV.visible()
                }
            }
        }
    }

    override fun onSuccess(listData: MutableList<Province>?) {
        Timber.i("list="+listData?.size)
        binding?.apply {
            Handler(Looper.getMainLooper()).post {
                loading(false)
                if (listData!=null){
                    provinceListAdapter?.updateList(listData)
                    listProvincesRV.visible()
                    listProvincesRV.layoutManager = LinearLayoutManager(getContext())
                    listProvincesRV.adapter = provinceListAdapter

                    Timber.i("list child=${listProvincesRV.childCount}")
                    Timber.i("list visible=${listProvincesRV.isVisible}")
                    Timber.i("adapter=${provinceListAdapter?.itemCount}")
                } else {
                    toast("List is empty")
                }
            }
        }
    }

    override fun onError(error: String?) {
        loading(false)
        Handler(Looper.getMainLooper()).post {
            toast(error?:"Error getting data.")
        }
    }
}