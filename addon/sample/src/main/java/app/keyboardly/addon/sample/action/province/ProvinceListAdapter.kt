package app.keyboardly.addon.sample.action.province

/**
 * Created by zainal on 5/30/21 - 9:41 AM
 */

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.keyboardly.addon.sample.data.model.Province
import app.keyboardly.addon.sample.databinding.SampleItemDataTextBinding
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import timber.log.Timber


class ProvinceListAdapter(
    private val context: Context,
    private var list: List<Province>,
    private var onClick: (Province) -> Unit
) : RecyclerView.Adapter<ProvinceListAdapter.ViewHolder>() {

    fun updateList(list: List<Province>){
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
                val isDateNull = date_time == null
                val date = if (isDateNull) "" else date_time
                titleTv.text = name
                descTv.text = buildString {
                                append("geo location : ")
                                append(latitude)
                                append(",")
                                append(longitude)
                                if (!date.isNullOrEmpty()){
                                    appendLine()
                                    append(date)
                                }

                }
                root.setOnClickListener {
                    onClick(data)
                }
            }
        }
    }
}