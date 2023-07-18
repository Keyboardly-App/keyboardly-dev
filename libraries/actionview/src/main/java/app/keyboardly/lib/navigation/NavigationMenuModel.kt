package app.keyboardly.lib.navigation

/**
 * It will be used for navigation keyboard
 * Created by zainal on 12/4/21 - 3:42 PM
 */
data class NavigationMenuModel(
    /* navigation id */
    val id: Int,
    /* name with resource id */
    val name: Int?=0,
    /* icon with drawable resource id */
    val icon: Int?=0,
    /* icon with drawable resource id */
    var enable: Boolean=true,
    /* icon url will be priority rather than icon from resource id */
    val iconUrl: String?="",
    /* name string will be priority rather than name from resource id */
    val nameString: String?=null,
    /* feature package id for add on. example : com.abc.sample */
    val featurePackageId: String?=null,
    /* feature name id for add on. example : sample */
    val featureNameId: String?=null,
    /* type of action view, will show on top or not. default : false */
    val topView: Boolean?=false
)
