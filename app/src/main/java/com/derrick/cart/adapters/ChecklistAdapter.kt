package com.derrick.cart.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.derrick.cart.models.Checklist
import com.derrick.cart.R

class ChecklistAdapter
    : ListAdapter<Checklist, ChecklistAdapter.ChecklistViewHolder>(CHECKLIST_COMPARATOR) {

    private var onListSelectedListener: OnListSelectedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChecklistViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list, parent, false)
        return ChecklistViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChecklistViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
        holder.listPosition = position
    }

    fun setOnSelectedListener(listener: OnListSelectedListener) {
        onListSelectedListener = listener
    }

    inner class ChecklistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textTitle: TextView = itemView.findViewById(R.id.listTitle)
        private val itemsChecked: TextView = itemView.findViewById(R.id.itemsChecked)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)

        private val buttonListActions: ImageButton = itemView.findViewById(R.id.imageButtonListActions)
        var listPosition = 0

        init {
            itemView.setOnClickListener {
                onListSelectedListener?.onListSelected(getItem(listPosition))
//                val intent = Intent(context, SubItemsActivity::class.java)
//                intent.putExtra(LIST_ITEM_POSITION, checkLists!![listPosition].id)
//                context.startActivity(intent)
            }

            buttonListActions.setOnClickListener {
                onListSelectedListener?.onOverflowOptionsSelected(getItem(listPosition), listPosition)
            }
        }

        fun bind(checklist: Checklist) {
            val checklistProgress = (checklist.itemsChecked?.toDouble()?.div(5) ?: 0) as Double
            val checklistProgressPercentage = (checklistProgress * 100).toInt()

            textTitle.text = checklist.title
            itemsChecked.text = checklist.itemsChecked.toString().plus("/${5}")
            progressBar.progress = checklistProgressPercentage
        }

    }

    interface OnListSelectedListener {
        fun onListSelected(checklist: Checklist)
        fun onOverflowOptionsSelected(checklist: Checklist, checklistPosition: Int)
    }

    companion object {
        private val CHECKLIST_COMPARATOR = object : DiffUtil.ItemCallback<Checklist>() {
            override fun areItemsTheSame(oldItem: Checklist, newItem: Checklist): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Checklist, newItem: Checklist): Boolean {
                return oldItem.title == newItem.title &&
                        oldItem.tags == newItem.tags &&
                        oldItem.itemsChecked == newItem.itemsChecked
            }
        }
    }
}