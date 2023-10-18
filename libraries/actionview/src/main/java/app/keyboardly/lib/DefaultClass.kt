package app.keyboardly.lib

import app.keyboardly.lib.navigation.NavigationMenuModel

/**
 * Created by Zainal on 20/02/2023 - 11:40
 */
abstract class DefaultClass(
    dependency: KeyboardActionDependency
) : KeyboardActionView(dependency) {

    open fun getSubmenus() : MutableList<NavigationMenuModel> = mutableListOf()
}