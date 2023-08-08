package app.keyboardly.sample.action.shopping

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.keyboardly.style.helper.gone
import app.keyboardly.lib.KeyboardActionDependency
import app.keyboardly.lib.KeyboardActionView
import app.keyboardly.lib.helper.OnViewReady
import app.keyboardly.sample.action.campaign.CampaignListAdapter
import app.keyboardly.sample.action.campaign.CampaignModel
import app.keyboardly.sample.databinding.SampleShoppingLayoutBinding

/**
 * Created by zainal on 6/8/22 - 2:59 PM
 */
class ShoppingActionView (
    dependency: KeyboardActionDependency
) : KeyboardActionView(dependency)
{


    private lateinit var campaignAdapter: CampaignListAdapter
    private var floatingRv: RecyclerView? = null
    private lateinit var binding: SampleShoppingLayoutBinding

    override fun onCreate() {
        binding = SampleShoppingLayoutBinding.inflate(getLayoutInflater())
        viewLayout = binding.root

        initAction()
    }

    private fun initAction() {
        val listCampaign = listCampaign()
        campaignAdapter = CampaignListAdapter(getContext(), listCampaign,
                object : CampaignListAdapter.CampaignCallback {
                    @SuppressLint("SetTextI18n")
                    override fun onClick(data: CampaignModel) {
                        floatingRv?.gone()
                        dependency.showTitleAboveList(false)

                        binding.voucherTv.text =
                            "${data.title}\n\n"+data.description
                        dependency.viewLayoutAction()
                    }
            })
        binding.apply {
            backBtn.apply {
                setOnClickListener {
                    dependency.viewAddOnNavigation()
                }
            }

            search.setOnClickListener {
                dependency.loadingMain(true)
                Handler(Looper.getMainLooper()).postDelayed({
                    dependency.loadingMain(false)
                    dependency.showRecyclerViewOptions(object : OnViewReady {
                        override fun onRecyclerViewReady(recyclerView: RecyclerView) {

                        dependency.showTitleAboveList(true,"Choose 1 promo")
//                        Timber.i("recyclerView="+recyclerView.isVisible)
                            recyclerView.apply {
                                floatingRv = this
                                layoutManager = LinearLayoutManager(context)
                                adapter = campaignAdapter
                            }
                        }
                    })
                }, 3000)
            }
        }
    }

    private fun listCampaign(): List<CampaignModel> {
        val list = mutableListOf<CampaignModel>()
        list.add(CampaignModel(4,"AA Flash Discount","Claim discount with special price on website."))
        list.add(CampaignModel(1,"BB January Promo","Buy 2 get 1 Free.\n\n*Terms and Conditions apply"))
        list.add(CampaignModel(3,"CC Flash Sale","Get product with special price on website."))
        list.add(CampaignModel(5,"DD Free Shipping","Claim free shipping on website."))
        list.add(CampaignModel(2,"EE Weekend Discount","Get 35% discount for all product.\n\n*Terms and Conditions apply"))
        list.add(CampaignModel(6,"FF Free Gift","Get free gift with minimum purchase."))
        return list
    }

}