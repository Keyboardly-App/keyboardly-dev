package app.keyboardly.sample.action.campaign

import app.keyboardly.sample.databinding.CampaignLayoutBinding
import app.keyboardly.lib.KeyboardActionDependency
import app.keyboardly.lib.KeyboardActionView

/**
 * Created by zainal on 6/8/22 - 2:59 PM
 */
class CampaignActionView (
    dependency: KeyboardActionDependency
) : KeyboardActionView(dependency){


    private lateinit var binding: CampaignLayoutBinding

    override fun onCreate() {
        binding = CampaignLayoutBinding.inflate(getLayoutInflater())
        viewLayout = binding.root

        initFooter()
    }

    private fun initFooter() {
        binding.campaignFooter.apply {
            footerBackBtn.setOnClickListener {
                dependency.viewAddOnNavigation()
            }
        }
    }

}