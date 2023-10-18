package app.keyboardly.addon.sample.action.campaign

/**
 * Created by zainal on 5/30/21 - 9:41 AM
 */

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.keyboardly.addon.sample.databinding.SampleItemDataTextBinding


class CampaignListAdapter(
    private val context: Context,
    private var list: List<CampaignModel>,
    private var navigationCallback: CampaignCallback
) : RecyclerView.Adapter<CampaignListAdapter.ViewHolder>() {

    fun updateList(list: List<CampaignModel>){
        this.list = list
        notifyDataSetChanged()
    }

    class ViewHolder(
        view: SampleItemDataTextBinding,
    ) : RecyclerView.ViewHolder(view.root) {
        val titleTv: TextView = view.title
        val descTv: TextView = view.description
        val root: LinearLayout = view.parentCampaign
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val binding = SampleItemDataTextBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        with(holder){
            data.apply {
                titleTv.text = title
                descTv.text = description
                root.setOnClickListener {
                    navigationCallback.onClick(data)
                }
            }
        }
    }

    interface CampaignCallback {
        fun onClick(data: CampaignModel)
    }
}