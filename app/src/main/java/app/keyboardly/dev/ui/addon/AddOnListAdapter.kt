package app.keyboardly.dev.ui.addon

/**
 * Created by zainal on 5/30/21 - 9:41 AM
 */

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.keyboardly.dev.databinding.ItemAddOnListBinding
import com.bumptech.glide.Glide


class AddOnListAdapter(
    private val context: Context,
    private var list: List<AddOnModel>,
    private var navigationCallback: OnClickCallback
) : RecyclerView.Adapter<AddOnListAdapter.ViewHolder>() {

    fun updateList(list: List<AddOnModel>){
        this.list = list
        notifyDataSetChanged()
    }

    class ViewHolder(
        view: ItemAddOnListBinding,
    ) : RecyclerView.ViewHolder(view.root) {
        val titleTv: TextView = view.featureName
        val descTv: TextView = view.featureDesc
        val imageTv: ImageView = view.imageview
        val root: View = view.click
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val binding = ItemAddOnListBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        with(holder){
            data.apply {
                titleTv.text = name
                descTv.text = description
                Glide.with(context)
                    .load(iconUrl)
                    .placeholder(app.keyboardly.style.R.drawable.placeholder_round_image_24)
                    .centerInside()
                    .into(imageTv)
                root.setOnClickListener {
                    navigationCallback.onClick(data)
                }
            }
        }
    }

    interface OnClickCallback {
        fun onClick(data: AddOnModel)
    }
}