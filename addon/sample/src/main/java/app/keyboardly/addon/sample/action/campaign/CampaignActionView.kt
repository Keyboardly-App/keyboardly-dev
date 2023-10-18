package app.keyboardly.addon.sample.action.campaign

import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.keyboardly.style.helper.gone
import app.keyboardly.lib.KeyboardActionDependency
import app.keyboardly.lib.KeyboardActionView
import app.keyboardly.lib.helper.InputPresenter
import app.keyboardly.lib.helper.OnViewReady
import app.keyboardly.addon.sample.R
import app.keyboardly.addon.sample.databinding.SampleCampaignLayoutBinding
import timber.log.Timber

/**
 * Created by zainal on 6/8/22 - 2:59 PM
 */
class CampaignActionView (
    dependency: KeyboardActionDependency
) : KeyboardActionView(dependency), InputPresenter {


    private var textWatcher: TextWatcher? = null
    private lateinit var campaignAdapter: CampaignListAdapter
    private var floatingRv: RecyclerView? = null
    private var countDownTimer: CountDownTimer? = null
    private lateinit var binding: SampleCampaignLayoutBinding

    override fun onCreate() {
        binding = SampleCampaignLayoutBinding.inflate(getLayoutInflater())
        viewLayout = binding.root

        initAction()
    }

    private fun initAction() {
        val listCampaign = listCampaign()
        campaignAdapter = CampaignListAdapter(getContext(), listCampaign,
                object : CampaignListAdapter.CampaignCallback {
                    override fun onClick(data: CampaignModel) {
                        dependency.commitText(data.description)
                        floatingRv?.gone()
                        campaignAdapter.updateList(listCampaign)
                        dependency.viewLayoutAction()
                    }
            })
        binding.apply {
            campaignList.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = campaignAdapter
            }
            backBtn.apply {
                setOnClickListener {
                    dependency.viewAddOnNavigation()
                }
            }

            textWatcher = object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    Timber.i("char=$char")
                    if (char.isNullOrEmpty()){
                        dependency.loadingOnInput(false)
                        Timber.e("list is null")
                        floatingRv?.gone()
                    } else {
                        dependency.loadingOnInput(true)
                        countDownTimer?.cancel()
                        countDownTimer = object : CountDownTimer(1500, 1000) {
                            override fun onTick(tick: Long) {}
                            override fun onFinish() {
                                val textLowerCase = char.toString().lowercase()
                                val filter = listCampaign.filter {
                                    it.description.lowercase().contains(textLowerCase)
                                            || it.title.lowercase().contains(textLowerCase)
                                }
                                Timber.i("filter result=${filter.size}")
                                showFloatingRecyclerView(filter)
                                dependency.loadingOnInput(false)
                            }
                        }.start()
                    }
                }

                override fun afterTextChanged(p0: Editable?) {}
            }

            search.setOnClickListener {
                dependency.requestInput(null,this@CampaignActionView,
                    hint = R.string.search_campaign, textWatcher = textWatcher,
                onCloseSearch = {
                    campaignAdapter.updateList(listCampaign)
                    floatingRv?.gone()
                    dependency.viewLayoutAction()
                })
            }

            fullList.setOnClickListener {
                showFloatingRecyclerView(listCampaign, false)
            }
        }
    }

    private fun showFloatingRecyclerView(list: List<CampaignModel>, inputMode: Boolean=true) {
        if (list.isEmpty()) {
            floatingRv?.gone()
        } else {
            dependency.showTopRecyclerView(object : OnViewReady {
                override fun onRecyclerViewReady(recyclerView: RecyclerView) {
                    Timber.i("recyclerView="+recyclerView.isVisible)
                    recyclerView.apply {
                        floatingRv = this
                        layoutManager = LinearLayoutManager(context)
                        adapter = campaignAdapter
                        campaignAdapter.updateList(list)

                        if (!inputMode){
                            dependency.showTitleAboveList(true,"Choose Campaign", true)
                        }
                    }
                }
            }, inputMode)


        }
    }

    private fun listCampaign(): List<CampaignModel> {
        val list = mutableListOf<CampaignModel>()
        list.add(CampaignModel(4,"Flash Discount","Claim discount with special price on website."))
        list.add(CampaignModel(1,"January Promo","Buy 2 get 1 Free.\n\n*Terms and Conditions apply"))
        list.add(CampaignModel(3,"Flash Sale","Get product with special price on website."))
        list.add(CampaignModel(5,"Free Shipping","Claim free shipping on website."))
        list.add(CampaignModel(2,"Weekend Discount","Get 35% discount for all product.\n\n*Terms and Conditions apply"))
        list.add(CampaignModel(6,"Free Gift","Get free gift with minimum purchase."))
        return list
    }

    override fun onDone(text: String, editText: EditText?) {

    }

}