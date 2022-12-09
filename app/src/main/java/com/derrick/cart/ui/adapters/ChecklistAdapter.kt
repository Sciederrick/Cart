package com.derrick.cart.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.derrick.cart.CHECKLIST
import com.derrick.cart.data.local.entities.Checklist
import com.derrick.cart.R
import com.derrick.cart.ui.SubItemsActivity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ChecklistAdapter(private val context: Context)
    : PagingDataAdapter<Checklist, ChecklistAdapter.ChecklistViewHolder>(CHECKLIST_COMPARATOR) {

    private val layoutInflater = LayoutInflater.from(context)
    private var onListSelectedListener: OnListSelectedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChecklistViewHolder {
        val itemView = layoutInflater.inflate(R.layout.item_list, parent, false)
        return ChecklistViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChecklistViewHolder, position: Int) {
        val checklist = getItem(position)
        checklist?.let { holder.bind(it) }
    }

    fun setOnSelectedListener(listener: OnListSelectedListener) {
        onListSelectedListener = listener
    }

    inner class ChecklistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var checklist: Checklist? = null

        private val textTitle: TextView = itemView.findViewById(R.id.listTitle)
        private val itemsChecked: TextView = itemView.findViewById(R.id.itemsChecked)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
        private val buttonListActions: ImageButton = itemView.findViewById(R.id.imageButtonListActions)

        init {
            itemView.setOnClickListener {
                checklist?.let { it1 -> onListSelectedListener?.onListSelected(it1) }

                val intent = Intent(context, SubItemsActivity::class.java)
                checklist?.let { it1 -> intent.putExtra(CHECKLIST, Json.encodeToString(it1)) }
                context.startActivity(intent)
            }

            buttonListActions.setOnClickListener {
                checklist?.let { it1 ->
                    onListSelectedListener?.onOverflowOptionsSelected(it1)
                }
            }
        }

        fun bind(checklist: Checklist) {
            this.checklist = checklist
//            val checklistProgress = (checklist?.itemsChecked?.toDouble()?.div(5) ?: 0) as Double
            val checklistProgress = 0
            val checklistProgressPercentage:Int = (checklistProgress * 100).toInt()
            textTitle.text = checklist.title
            itemsChecked.text = checklist.itemsChecked.toString().plus("/${5}")
            progressBar.progress = checklistProgressPercentage
        }
    }

    interface OnListSelectedListener {
        fun onListSelected(checklist: Checklist)
        fun onOverflowOptionsSelected(checklist: Checklist)
    }

    companion object {
        private val CHECKLIST_COMPARATOR = object : DiffUtil.ItemCallback<Checklist>() {
            override fun areItemsTheSame(oldItem: Checklist, newItem: Checklist): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Checklist, newItem: Checklist): Boolean {
                return oldItem === newItem
            }
        }
    }
}