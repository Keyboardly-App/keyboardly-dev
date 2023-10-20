package app.keyboardly.addon.sample.action.province

import android.text.Editable
import android.text.TextWatcher
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
import timber.log.Timber

/**
 * Created by Zainal on 20/10/2023 - 13:44
 */
class ProvinceListActionView(
    dependency: KeyboardActionDependency
) : KeyboardActionView(dependency), IProvincePresenter {

    private var topRecyclerView: RecyclerView? = null
    private var provinceListAdapter: ProvinceListAdapter? = null
    private var listProvinces: MutableList<Province> = mutableListOf()
    private var binding : SampleProvinceListLayoutBinding? = null
    private var repository = ProvinceRepository(getContext(), this)

    override fun onCreate() {
        binding = SampleProvinceListLayoutBinding.inflate(getLayoutInflater())

        binding?.apply {
            list.apply {
                provinceListAdapter = ProvinceListAdapter(context, listProvinces) {
                    toast("Selected province:\n${it.name}")
                }
                layoutManager = LinearLayoutManager(context)
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
                        provinceListAdapter?.updateList(listProvinces)
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
                                val listForFilter = listProvinces
                                val query = s?.toString()
                                if (query!=null){
                                    val filtered = listForFilter.filter { it.name.lowercase().contains(query) }
                                    provinceListAdapter?.updateList(filtered)
                                } else {
                                    provinceListAdapter?.updateList(listProvinces)
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

    override fun onLoading() {
        loading()
    }

    private fun loading(isLoading: Boolean=true) {

        if (isLoading) {
            binding?.progress?.visible()
            binding?.list?.gone()
        } else {
            binding?.progress?.gone()
            binding?.list?.visible()
        }
    }

    override fun onSuccess(list: MutableList<Province>?) {
        loading(false)
        if (list!=null){
            provinceListAdapter?.updateList(list)
        } else {
            toast("List is empty")
        }
    }

    override fun onError(error: String?) {
        loading(false)
    }
}