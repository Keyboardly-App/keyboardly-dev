package app.keyboardly.lib.navigation

/**
 * Created by zainal on 5/30/21 - 9:41 AM
 */

import android.graphics.*
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import app.keyboardly.actionview.databinding.ItemNavigationMenuBinding
import com.bumptech.glide.Glide


class NavigationMenuAdapter(
    private var listOtherMenu: List<NavigationMenuModel>,
    private var navigationCallback: NavigationCallback
) : RecyclerView.Adapter<NavigationMenuAdapter.ViewHolder>() {

    fun updateList(list: List<NavigationMenuModel>){
        listOtherMenu = list
        notifyDataSetChanged()
    }

    fun updateCallBack(callback: NavigationCallback?){
        callback?.let {
            navigationCallback = it
        }
    }

    class ViewHolder(
        view: ItemNavigationMenuBinding,
    ) : RecyclerView.ViewHolder(view.root) {
        val title: TextView = view.navigationTitle
        val iconMenuIv: ImageView = view.navigationIcon
        val root: LinearLayoutCompat = view.navigationItemParent
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemNavigationMenuBinding.inflate(layoutInflater)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listOtherMenu.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listOtherMenu[position]
        with(holder){
            data.apply {
                val context = holder.itemView.context
                var strName = ""
                if (!nameString.isNullOrEmpty()){
                    strName = nameString.toString()
                } else {
                    name?.let {
                        if (it!=0) {
                            strName = context.getString(it)
                        }
                    }
                }
                title.text = strName
                if (!iconUrl.isNullOrEmpty()){
                    Glide.with(context)
                        .load(iconUrl.toString())
                        .into(iconMenuIv)
                } else {
                    icon?.let {
                        if (it!=0) {
                            iconMenuIv.setImageResource(it)
                        }
                    }
                }

                if (enable) {
                    iconMenuIv.colorFilter = null
                } else {
                    iconMenuIv.toGrayscale()
                }
                root.setOnClickListener {
                    navigationCallback.onClickMenu(data)
                }
            }
        }
    }

    private fun ImageView.toGrayscale(){
        val bmpOriginal = drawable.toBitmap()
        val height: Int = bmpOriginal.height
        val width: Int = bmpOriginal.width
        val bmpGrayscale: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val c = Canvas(bmpGrayscale)
        val paint = Paint()
        val cm = ColorMatrix()
        cm.setSaturation(0f)
        val f = ColorMatrixColorFilter(cm)
        paint.colorFilter = f
        c.drawBitmap(bmpOriginal,Matrix(), paint)
        setImageBitmap(bmpGrayscale)
    }
}