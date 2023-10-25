package app.keyboardly.addon.sample.action.province

import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.keyboardly.addon.sample.data.model.Province
import app.keyboardly.addon.sample.data.remote.IProvincePresenter
import app.keyboardly.addon.sample.data.remote.ProvinceRepository
import app.keyboardly.addon.sample.databinding.SampleProvinceListLayoutBinding
import app.keyboardly.lib.KeyboardActionDependency
import app.keyboardly.lib.KeyboardActionView
import app.keyboardly.lib.helper.OnViewReady
import app.keyboardly.style.helper.gone
import app.keyboardly.style.helper.visible
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

/**
 * Created by Zainal on 20/10/2023 - 13:44
 */
class ProvinceListActionView(
    dependency: KeyboardActionDependency
) : KeyboardActionView(dependency), IProvincePresenter, KoinComponent {

    private var topRecyclerView: RecyclerView? = null
    private var provinceListAdapter: ProvinceListAdapter? = null
    private var listData: MutableList<Province> = mutableListOf()
    private var binding: SampleProvinceListLayoutBinding? = null
    private val repository by inject<ProvinceRepository>()

    override fun onCreate() {
        binding = SampleProvinceListLayoutBinding.inflate(getLayoutInflater())
        provinceListAdapter = ProvinceListAdapter(getContext(), listData) {
            val gson: Gson = GsonBuilder().setPrettyPrinting().create()
            Timber.d("json=\n" + gson.toJson(it))
            toast("Selected province:\n${it.name}")
        }

        binding?.apply {
            listProvincesRV.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                adapter = provinceListAdapter
            }

            local.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    toast("Switch data from local")
                    loading()
                    Handler(Looper.getMainLooper())
                        .postDelayed(
                            {
                                val provinces = repository.getLocal()?.toMutableList()
                                if (provinces != null) {
                                    listData.clear()
                                    listData.addAll(provinces)
                                    provinceListAdapter?.updateList(listData)
                                } else {
                                    toast("Local data is empty")
                                }
                                loading(false)
                            }, 1000
                        )
                } else {
                    toast("Get data from server")
                    repository.getList()
                }
            }

            back.setOnClickListener {
                if (topRecyclerView != null) {
                    dependency.hideTopView()
                    topRecyclerView = null
                }
                dependency.viewAddOnNavigation()
            }

            repository.init(this@ProvinceListActionView)
            repository.getList()

            search.setOnClickListener {
                dependency.requestInput(
                    null, hint = androidx.appcompat.R.string.search_menu_title,
                    onCloseSearch = {
                        topRecyclerView = null
                        provinceListAdapter?.updateList(listData)
                        dependency.viewLayoutAction()
                    },
                    textWatcher = textWatcher()
                )
            }
        }

        viewLayout = binding?.root
    }

    private fun textWatcher() = object : TextWatcher {
        override fun beforeTextChanged(
            s: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {
        }

        override fun onTextChanged(
            s: CharSequence?,
            start: Int,
            before: Int,
            count: Int
        ) {
            val listForFilter = listData
            val query = s?.toString()?.lowercase()
            Timber.d("query=$query | ${listForFilter.size}")
            if (query != null) {
                val filtered = listForFilter.filter {
                    it.name.lowercase().contains(query)
                            || it.name.lowercase().startsWith(query)
                }
                provinceListAdapter?.updateList(filtered)
                dependency.showTopRecyclerView(object : OnViewReady {
                    override fun onRecyclerViewReady(recyclerView: RecyclerView) {
                        recyclerView.apply {
                            layoutManager = LinearLayoutManager(dependency.getContext())
                            adapter = provinceListAdapter

                            topRecyclerView = this
                        }
                    }
                })
            } else {
                topRecyclerView?.gone()
                provinceListAdapter?.updateList(listData)
            }
        }

        override fun afterTextChanged(s: Editable?) {}

    }

    override fun onResume() {
        repository.getList()
        super.onResume()
    }

    override fun onLoading() {
        loading()
    }

    private fun loading(isLoading: Boolean = true) {
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

    override fun onSuccess(list: MutableList<Province>?) {
        listData.clear()
        listData = list ?: mutableListOf()
        binding?.apply {
            loading(false)
            if (listData.isNotEmpty()) {
                provinceListAdapter?.updateList(listData)
            } else {
                toast("List is empty")
            }
        }
    }

    override fun onError(error: String?) {
        loading(false)
        listData.clear()
        provinceListAdapter?.updateList(listData)
        toast(error ?: "Error getting data.")
    }
}