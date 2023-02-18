package app.keyboardly.sample.app

/**
 * Created by zainal on 4/30/21 - 9:31 AM
 */

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.keyboardly.sample.databinding.SampleLayoutItemMenuBinding

class SubMenuAdapter(
    private var listMenu: List<SubMenuModel>,
    private val onClick: (data: Int) -> Unit
) : RecyclerView.Adapter<SubMenuAdapter.ViewHolder>() {

    class ViewHolder(
        view: SampleLayoutItemMenuBinding,
    ) :
        RecyclerView.ViewHolder(view.root) {
        val title: TextView = view.itemMenuTv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = SampleLayoutItemMenuBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listMenu.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listMenu[position]
        with(holder){
            title.text =data.name
            data.iconLeft?.let {
                title.setCompoundDrawablesWithIntrinsicBounds(
                    it,0,0,0
                )
            }
            title.setOnClickListener {
                onClick(position)
            }
        }
    }
}

data class SubMenuModel(
    val name:String,
    val iconLeft: Int?=null
)