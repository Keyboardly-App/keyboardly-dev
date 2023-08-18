package app.keyboardly.sample

import app.keyboardly.lib.DefaultClass
import app.keyboardly.lib.KeyboardActionDependency
import app.keyboardly.lib.navigation.NavigationCallback
import app.keyboardly.lib.navigation.NavigationMenuModel
import app.keyboardly.sample.action.campaign.CampaignActionView
import app.keyboardly.sample.action.profile.WelcomeActionView
import app.keyboardly.sample.action.register.RegisterActionView
import app.keyboardly.sample.action.shopping.ShoppingActionView
import app.keyboardly.sample.action.top.TopActionView
import timber.log.Timber

/**
 * Created by zainal on 6/8/22 - 2:59 PM
 */
class SampleDefaultClass(
    dependency: KeyboardActionDependency
) : DefaultClass(dependency), NavigationCallback {

    private val registerView = RegisterActionView(dependency)
    private val campaignActionView = CampaignActionView(dependency)
    private val shoppingActionView = ShoppingActionView(dependency)
    private val welcomeActionView = WelcomeActionView(dependency)
    private val topActionView = TopActionView(dependency)
    private var menu = mutableListOf<NavigationMenuModel>()

    override fun onCreate() {
        menu = mutableListOf()
        initMenuList()
    }

    private fun initMenuList() {
        menu.add(
            NavigationMenuModel(
                WELCOME,
                nameString = "Welcome",
                icon = R.drawable.sample_ic_round_account_circle_24_bot_feature,
            )
        )
        menu.add(
            NavigationMenuModel(
                DISCOUNT,
                nameString = "Register",
                icon = R.drawable.sample_ic_round_discount_24
            )
        )
        menu.add(
            NavigationMenuModel(
                CAMPAIGN,
                nameString = "Campaign",
                icon = R.drawable.sample_ic_round_campaign_24
            )
        )
        menu.add(
            NavigationMenuModel(
                SHOPPING,
                nameString = "Shopping",
                icon = R.drawable.sample_ic_round_shopping_cart_24,
            )
        )

        menu.add(
            NavigationMenuModel(
                TOP_VIEW,
                nameString = "Top View",
                icon = R.drawable.sample_ic_round_settings_24_bot,
                enable = true,
                topView = true
            )
        )
        menu.add(
            NavigationMenuModel(
                6,
                nameString = "Dashboard",
                icon = R.drawable.sample_ic_round_bedroom_parent_24,
                enable = false
            )
        )
    }

    override fun getSubmenus(): MutableList<NavigationMenuModel> {
        if (menu.isEmpty()) {
            initMenuList()
        }
        dependency.setNavigationCallback(this)
        return menu
    }

    override fun onClickMenu(data: NavigationMenuModel) {
        Timber.d("data="+data.featureNameId+"|id="+data.id)
        val view = when (data.id) {
            DISCOUNT -> registerView
            CAMPAIGN -> campaignActionView
            SHOPPING -> shoppingActionView
            WELCOME -> welcomeActionView
            TOP_VIEW -> topActionView
            else -> null
        }

        if (view != null) {
            val isTopView = data.topView
            Timber.d("topview=$isTopView / view=$view")
            if (isTopView !=null && isTopView){
                dependency.setTopActionView(view)
            } else {
                dependency.setActionView(view)
            }
        } else {
            if (!data.enable) {
                toast("Feature on development")
            } else {
                Timber.w("enable but nothing to parse")
            }
        }
    }

    companion object {
        private const val DISCOUNT = 1
        private const val CAMPAIGN = 2
        private const val SHOPPING = 3
        private const val WELCOME = 4
        private const val TOP_VIEW = 5
    }
}