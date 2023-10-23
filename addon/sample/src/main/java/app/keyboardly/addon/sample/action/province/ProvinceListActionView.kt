package app.keyboardly.addon.sample.action.province

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.keyboardly.addon.sample.action.province.model.Province
import app.keyboardly.addon.sample.action.province.network.IProvincePresenter
import app.keyboardly.addon.sample.action.province.network.ProvinceRepository
import app.keyboardly.addon.sample.databinding.SampleProvinceListLayoutBinding
import app.keyboardly.lib.KeyboardActionDependency
import app.keyboardly.lib.KeyboardActionView
import app.keyboardly.lib.helper.InputPresenter
import app.keyboardly.lib.helper.OnViewReady
import app.keyboardly.style.helper.gone
import app.keyboardly.style.helper.visible
import timber.log.Timber

/**
 * Created by Zainal on 20/10/2023 - 13:44
 */
class ProvinceListActionView(
    dependency: KeyboardActionDependency
) : KeyboardActionView(dependency), IProvincePresenter, InputPresenter {

    private var topRecyclerView: RecyclerView? = null
    private var provinceListAdapter: ProvinceListAdapter? = null
    private var listData: MutableList<Province> = mutableListOf()
    private var binding : SampleProvinceListLayoutBinding? = null
    private var repository = ProvinceRepository(getContext(), this)

    override fun onCreate() {
        binding = SampleProvinceListLayoutBinding.inflate(getLayoutInflater())

        binding?.apply {
            provinceListAdapter = ProvinceListAdapter(getContext(), listData) {
                toast("Selected province:\n${it.name}")
            }
            listProvincesRV.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(dependency.getContext())
                adapter = provinceListAdapter
            }

            back.setOnClickListener {
                if (topRecyclerView!=null){
                    dependency.hideTopView()
                    topRecyclerView = null
                }
                dependency.viewAddOnNavigation()
            }

            repository.getList()

            search.setOnClickListener {
                Timber.i("search hitted")
                dependency.requestInput(null, hint = androidx.appcompat.R.string.search_menu_title,
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
                val filtered = listForFilter.filter { it.name.lowercase().contains(query)
                        || it.name.lowercase().startsWith(query)
                }
                Timber.i("filtered="+filtered.size)
                provinceListAdapter?.updateList(filtered)
                dependency.showTopRecyclerView(object : OnViewReady{
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

    private fun loading(isLoading: Boolean=true) {
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
        listData = list?: mutableListOf()
        Timber.i("list="+ listData.size)
        binding?.apply {
            loading(false)
            if (listData.isNotEmpty()){

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

    override fun onError(error: String?) {
        loading(false)
        toast(error?:"Error getting data.")
    }

    override fun onDone(text: String, editText: EditText?) {

    }
}