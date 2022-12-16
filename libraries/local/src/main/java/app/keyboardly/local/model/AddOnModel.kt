package app.keyboardly.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import app.keyboardly.lib.navigation.NavigationMenuModel
import java.io.Serializable

/**
 * Created by zainal on 6/14/22 - 10:06 AM
 */
@Entity(tableName = "table_addon")
data class AddOnModel(
    @PrimaryKey
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
    var installed: Boolean?=false
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