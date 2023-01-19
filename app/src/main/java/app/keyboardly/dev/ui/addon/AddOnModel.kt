package app.keyboardly.dev.ui.addon

import app.keyboardly.lib.navigation.NavigationMenuModel
import java.io.Serializable

/**
 * Created by zainal on 6/14/22 - 10:06 AM
 */
data class AddOnModel(
    val id: Int,
    val featureNameId: String,
    val name: String,
    val description: String,
    val developer: String,
    val linkDetail: String,
    val iconUrl: String,
    val price: Int?=0,
    val featurePackageId: String?=null,
    val developerLink: String?=null,
    val downloadCount: Int?=0,
    val helpLink: String?=null,
    val authorLink: String?=null,
    val termLink: String?=null,
    val privacyLink: String?=null,
    var installed: Boolean?=false,
    var thumbnail: List<Thumbnail>?=null,
) : Serializable {

    fun mapToNavigationModel() : NavigationMenuModel {
        return NavigationMenuModel(
            id,
            nameString = name,
            iconUrl = iconUrl,
            featureNameId = featureNameId,
            featurePackageId = featurePackageId
        )
    }
}