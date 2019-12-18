package ni.devotion.multipurposedownloader.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.*
import ni.devotion.multipurposedownloader.adapter.interfaces.RecyclerItemInterface
import ni.devotion.multipurposedownloader.models.Information
import ni.devotion.multipurposedownloader.ui.viewHolder.InformationViewHolder

class InformationAdapter(private val recyclerItemInterface: RecyclerItemInterface) : ListAdapter<Information, InformationViewHolder>(DIFF_CALLBACK){
    companion object{
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Information>() {
            override fun areItemsTheSame(oldItem: Information, newItem: Information) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Information, newItem: Information) = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = InformationViewHolder.create(parent)
    override fun onBindViewHolder(holder: InformationViewHolder, position: Int) = holder.bind(getItem(position), recyclerItemInterface)
}