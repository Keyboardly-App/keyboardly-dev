package app.keyboardly.lib.reflector

import android.view.View
import app.keyboardly.lib.KeyboardActionDependency
import app.keyboardly.lib.navigation.NavigationMenuModel

/**
 * Created by zainal on 6/9/22 - 10:03 AM
 */
interface DynamicFeature {
    fun getView() : View? = null
    fun getTopView() : View? = null
    fun getSubMenus(): MutableList<NavigationMenuModel> = mutableListOf()

    /**
     * DynamicFeature can be instantiated in whatever way the implementer chooses,
     * we just want to have a simple method to get() an instance of it.
     */
    interface Provider {
        fun get(dependencies: Dependencies): DynamicFeature
    }

    /**
     * Dependencies from the main app module that are required by the Feature.
     */
    interface Dependencies {
        fun getFeatureNameId(): String
        fun getKeyboardDependency(): KeyboardActionDependency
    }
}