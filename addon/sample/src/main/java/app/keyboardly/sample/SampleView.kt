package app.keyboardly.sample

import android.util.Log
import app.keyboardly.lib.KeyboardActionDependency
import app.keyboardly.lib.KeyboardActionView
import app.keyboardly.lib.navigation.NavigationCallback
import app.keyboardly.lib.navigation.NavigationMenuModel
import app.keyboardly.sample.action.campaign.CampaignActionView
import app.keyboardly.sample.action.discount.DiscountView

/**
 * Created by zainal on 6/8/22 - 2:59 PM
 */
class SampleView(
    dependency: KeyboardActionDependency
) : KeyboardActionView(dependency), NavigationCallback {

    private val discountView = DiscountView(dependency)
    private val campaignActionView = CampaignActionView(dependency)
    private var menu = mutableListOf<NavigationMenuModel>()

    override fun onCreate() {
        menu = mutableListOf()
        initMenuList()
    }

    private fun initMenuList() {
        menu.add(
            NavigationMenuModel(
                DISCOUNT,
                nameString = "Discount",
                icon = R.drawable.ic_round_discount_24
            )
        )
        menu.add(
            NavigationMenuModel(
                CAMPAIGN,
                nameString = "Campaign",
                icon = R.drawable.ic_round_campaign_24
            )
        )
        menu.add(
            NavigationMenuModel(
                3,
                nameString = "Shopping",
                icon = R.drawable.ic_round_shopping_cart_24,
                enable = false
            )
        )
        menu.add(
            NavigationMenuModel(
                4,
                nameString = "Account",
                icon = R.drawable.ic_round_account_circle_24_bot_feature,
                enable = false
            )
        )
        menu.add(
            NavigationMenuModel(
                5,
                nameString = "Setting",
                icon = R.drawable.ic_round_settings_24_bot,
                enable = false
            )
        )
        menu.add(
            NavigationMenuModel(
                6,
                nameString = "Dashboard",
                icon = R.drawable.ic_round_bedroom_parent_24,
                enable = false
            )
        )
    }

    fun subMenus(): MutableList<NavigationMenuModel> {
        if (menu.isEmpty()) {
            initMenuList()
        }
        dependency.setNavigationCallback(this)
        return menu
    }

    override fun onClickMenu(data: NavigationMenuModel) {
        Log.d("BotFeature", "clicked=${data.nameString} =====")

        val view = when (data.id) {
            DISCOUNT -> discountView
            CAMPAIGN -> campaignActionView
            else -> null
        }

        if (view != null) {
            dependency.setActionView(view)
        } else {
            toast("Feature on development")
        }
    }

    companion object {
        private const val DISCOUNT = 1
        private const val CAMPAIGN = 2
    }
}