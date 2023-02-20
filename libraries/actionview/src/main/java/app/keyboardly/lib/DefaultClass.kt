package app.keyboardly.lib

import app.keyboardly.lib.navigation.NavigationMenuModel

/**
 * Created by Zainal on 20/02/2023 - 11:40
 */
abstract class DefaultClass(
    dependency: KeyboardActionDependency
) : KeyboardActionView(dependency) {

    abstract fun getSubmenus() : MutableList<NavigationMenuModel>
}