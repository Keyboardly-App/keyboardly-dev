package app.keyboardly.dev.keyboard.keypad.keyboardaction

import android.text.TextWatcher
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import app.keyboardly.dev.R
import app.keyboardly.dev.keyboard.keypad.KokoKeyboardView
import app.keyboardly.dev.keyboard.utils.DynamicModuleHelper
import app.keyboardly.style.helper.gone
import app.keyboardly.style.helper.invisible
import app.keyboardly.style.helper.visible
import app.keyboardly.lib.navigation.NavigationCallback
import app.keyboardly.lib.navigation.NavigationMenuAdapter
import app.keyboardly.lib.navigation.NavigationMenuModel
import app.keyboardly.lib.reflector.DynamicFeature
import timber.log.Timber

/**
 * Created by Zainal on 09/01/2023 - 19:19
 */
open class KeyboardNavigation(
    val view: View,
    private val moduleHelper: DynamicModuleHelper,
    private val kokoKeyboardView: KokoKeyboardView
) : KeyboardBaseId(view), NavigationCallback {

    private var adapterNavigation: NavigationMenuAdapter? = null
    private var defaultHeader: Boolean = true
    private var subMenuAddOnActive: Boolean = false
    var textWatcher: TextWatcher? = null

    fun Int.toPx(): Int = (this * view.context.resources.displayMetrics.density).toInt()

    init {
        val defaultMenuList = defaultNavigation()
        @Suppress("LeakingThis")
        adapterNavigation = NavigationMenuAdapter(defaultMenuList, this)
        navigationView.apply {
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = adapterNavigation
        }
    }


    /**
     * to set main keyboard view
     * */
    open fun setActionView(view: View?) {
        try {
            frame.removeAllViews()
            frame.addView(view)
        } catch (e: Exception) {
            toast("error:\n" + e.message)
        }
        viewLayoutAction()
    }

    /**
     * make the layout action visible
     * called after data input or other
     */
    fun viewLayoutAction() {
        goneOptionsView()
        defaultInputLayout.gone()
        mainHeader.gone()
        headerShadowAction.gone()
        navigationView.invisible()
        headerWrapper.invisible()
        getKeyboardViewWrapper().invisible()

        frame.visible()
        frame.invalidate()
        keyboardActionWrapper.visible()
        resetInputConnection()
        // reset keyboard to alphabet
        Timber.i("----------keyboard switcher back to action---")
    }

    private fun resetInputConnection() {
        mEditField.removeTextChangedListener(textWatcher)
        textWatcher = null
        mEditField.clearFocus()
        mEditField.text = null
        mEditFieldLong.text = null
        Timber.e("reset input connection.")
    }

    fun getKeyboardViewWrapper(): View {
        return keyboardWrapper
    }

    fun goneOptionsView() {
        if (mEditField.isFocused && floatingRecyclerView.isShown) {
            val layoutParams =
                RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 200.toPx())
            floatingRecyclerView.layoutParams = layoutParams
        } else {
            floatingRecyclerView.gone()
        }
        recyclerView.gone()
        chipGroupOnFrame.gone()
        datePickerOnFrame.gone()
        messageOnFrame.gone()
        keyboardActionWrapper.gone()
        progressMain.gone()
    }

    fun viewNavigation() {
        mainHeader.gone()
        navigationParentLayout.visible()
        headerWrapper.visible()
        navigationBack.visible()
        defaultHeader = false
    }

    private fun defaultHeaderView() {
        mainHeader.visible()
        headerWrapper.visible()
        navigationParentLayout.gone()
        defaultHeader = true
    }

    private fun navigationOnClick(data: NavigationMenuModel) {
        val featureName = data.featureNameId
        if (featureName != null) {
            // check is feature installed
            if (moduleHelper.isFeatureInstalled(featureName)) {
                data.featurePackageId?.let {
                    // open sub menu
                    openSubMenuFeature(it)
                }
            } else {
                toast("Feature ${data.nameString} not installed yet.")
            }
        } else {
            toast("It's ${data.nameString} menu.")
            Timber.e("Error. Something wrong on the feature.")
        }
    }

    fun toast(message: String) {
        Toast.makeText(view.context, message, Toast.LENGTH_SHORT).show()
    }

    private fun openSubMenuFeature(featureNameId: String) {
        try {
            // load add on
            val dynamicModule: DynamicFeature? = moduleHelper.initialize(featureNameId)
            if (dynamicModule != null) {
                // get submenu is exist
                val subsMenu = dynamicModule.getSubMenus()
                if (subsMenu.isNotEmpty()) {
                    // show submenu on keyboard
                    switchAddOnNavigationView(subsMenu)
                } else {
                    // if submenu empty, load the view
                    // get view from add on
                    val view = dynamicModule.getView()
                    val topView = dynamicModule.getTopView()
                    Timber.d("top view=$topView | view=$view")
                    if (topView != null) {
                        KokoKeyboardView.dependency?.setTopActionView(topView)
                    } else if (view != null) {
                        KokoKeyboardView.dependency?.setActionView(view)
                    } else {
                        Timber.e("nothing to do.")
                    }
                }
            } else {
                Timber.e("dynamic module null")
                toast("Failed open feature, something wrong.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            toast("Failed open feature.\ne:${e.message}")
        }
    }

    fun switchAddOnNavigationView(list: MutableList<NavigationMenuModel>) {
        subMenuAddOnActive = true
        navigationBack.visible()
        adapterNavigation?.updateList(list)
        navigationView.scrollToPosition(0)
        // set visibility gone for button more key when submenu active.
    }


    private fun defaultNavigation(): MutableList<NavigationMenuModel> {
        val navigationList = mutableListOf<NavigationMenuModel>()
        navigationList.add(NavigationMenuModel(
            0,
            R.string.dummy,
            R.drawable.round_emoji_food_beverage_24,
            nameString = "Dummy1"
        ))
        navigationList.add(NavigationMenuModel(
            0,
            R.string.dummy2,
            R.drawable.round_emoji_food_beverage_24,
            nameString = "Dummy2"
        ))
        navigationList.add(
            NavigationMenuModel(
            1,
            R.string.nav_sample,
            null,
            featurePackageId = "app.keyboardly.addon.sample",
            featureNameId = "sample",
            nameString = "Sample",
            iconUrl = "https://img.icons8.com/external-flaticons-flat-flat-icons/344/external-dummy-robotics-flaticons-flat-flat-icons.png")
        )
        return navigationList
    }


    fun onLogoButtonClicked() {
        Timber.i("default Header=$defaultHeader")
        if (defaultHeader) {
            viewNavigation()
        } else {
            defaultHeaderView()
        }
    }

    fun onBackButtonClicked() {
        Timber.i("submenu = $subMenuAddOnActive // defaultHeader=$defaultHeader")
        if (subMenuAddOnActive) {
            if (titleHeader.isVisible){
                showTitle(false)
                goneOptionsView()
                viewLayoutAction()
            } else {
                kokoKeyboardView.resetHeight()
                viewDefaultNavigation(defaultNavigation())
            }
        } else {
            if (!defaultHeader) {
                defaultHeaderView()
                kokoKeyboardView.resetHeight()
            } else {
                Timber.w("default header is true.")
            }
        }
    }

    fun showTitle(show: Boolean, text: String? = "", asFooter: Boolean?=false) {
        messageOnFrame.gone()

        if (show) {
            if (text != null) {
                titleHeader.text = text
            }

            if (frame.isVisible){
                frame.gone()
            }
            headerShadowAction.visible()
            mainHeader.gone()
            navigationView.gone()

            defaultInputLayout.gone()

            navigationParentLayout.visible()
            navigationBack.visible()
            headerWrapper.visible()
            titleHeader.visible()
            if (asFooter!!) {
                keyboardWrapper.gone()
                keyboardActionWrapper.gone()
            }

            defaultHeader = false
            subMenuAddOnActive = true

        } else {
            if (titleHeader.isVisible) {
                titleHeader.gone()
            }
            if (!defaultHeader
                && !subMenuAddOnActive) {
                mainHeader.visible()
                headerWrapper.visible()
                navigationView.visible()
            }
        }
    }
    private fun viewDefaultNavigation(defaultMenuList: MutableList<NavigationMenuModel>) {
        adapterNavigation?.updateList(defaultMenuList)
        adapterNavigation?.updateCallBack(this)
        subMenuAddOnActive = false
    }

    fun viewDefault() {
        if (defaultHeader) {
            defaultHeaderView()
        } else {
            viewNavigation()
        }
    }

    fun updateNavigationCallBack(navigationCallback: NavigationCallback) {
        adapterNavigation?.updateCallBack(navigationCallback)
    }

    fun viewAddOnSubmenuNavigation() {
        defaultInputLayout.gone()
        mainHeader.gone()
//        floatingRoot.gone()
        keyboardActionWrapper.gone()
        frame.removeAllViews()
        navigationParentLayout.visible()
        navigationView.visible()
        headerShadowAction.visible()
        headerWrapper.visible()

        // skip reset adapter navigation
        /*if (adapterNavigation != null) {
            val menuList: List<NavigationMenuModel> = getListMenu(getContext())
            adapterNavigation?.updateList(menuList)
        }*/

        getKeyboardViewWrapper().visible()
    }

    /**
     * method for back to submenu's addon navigation, after view from submenu
     */
    fun viewAddOnNavigation() {
        kokoKeyboardView.resetHeight()
        defaultInputLayout.gone()
        mainHeader.gone()
//            titleHeader.gone()
//        floatingRoot.gone()
        keyboardActionWrapper.gone()
        frame.removeAllViews()

        navigationParentLayout.visible()
        navigationView.visible()
        headerShadowAction.visible()
        headerWrapper.visible()

        // skip reset adapter navigation
        /*if (adapterNavigation != null) {
            val menuList: List<NavigationMenuModel> = getListMenu(getContext())
            adapterNavigation?.updateList(menuList)
        }*/
        getKeyboardViewWrapper().visible()
        resetInputConnection()
//            keyboardSwitcher.apply {
//                getmain.visible()
//                resetKeyboardAlpahabet(Settings.getInstance())
//            }
    }

    override fun onClickMenu(data: NavigationMenuModel) {
        navigationOnClick(data)
        Timber.d("onClickMenu: data=${data.name}")
    }
}