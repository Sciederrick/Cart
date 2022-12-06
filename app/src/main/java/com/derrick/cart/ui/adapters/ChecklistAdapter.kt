package com.derrick.cart.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.derrick.cart.CHECKLIST
import com.derrick.cart.data.local.entities.Checklist
import com.derrick.cart.R
import com.derrick.cart.ui.SubItemsActivity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ChecklistAdapter(private val context: Context)
    : PagedListAdapter<Checklist, ChecklistAdapter.ViewHolder>(CHECKLIST_COMPARATOR) {

    private val layoutInflater = LayoutInflater.from(context)
    private var onListSelectedListener: OnListSelectedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.item_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val checklist = getItem(position)
        val checklistProgress = (checklist?.itemsChecked?.toDouble()?.div(5) ?: 0) as Double
        val checklistProgressPercentage:Int = (checklistProgress * 100).toInt()
        holder.textTitle.text = checklist?.title
        holder.itemsChecked.text = checklist?.itemsChecked.toString().plus("/${5}")
        holder.progressBar.progress = checklistProgressPercentage
        holder.checklistPosition = position
    }

    fun setOnSelectedListener(listener: OnListSelectedListener) {
        onListSelectedListener = listener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textTitle: TextView = itemView.findViewById(R.id.listTitle)
        val itemsChecked: TextView = itemView.findViewById(R.id.itemsChecked)
        val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)

        private val buttonListActions: ImageButton = itemView.findViewById(R.id.imageButtonListActions)
        var checklistPosition = 0

        init {
            itemView.setOnClickListener {
                getItem(checklistPosition)?.let { it1 -> onListSelectedListener?.onListSelected(it1) }

                val intent = Intent(context, SubItemsActivity::class.java)
                val checklist = getItem(checklistPosition)
                checklist?.let { it1 -> intent.putExtra(CHECKLIST, Json.encodeToString(it1)) }
                context.startActivity(intent)
            }

            buttonListActions.setOnClickListener {
                getItem(checklistPosition)?.let { it1 ->
                    onListSelectedListener?.onOverflowOptionsSelected(
                        it1, checklistPosition)
                }
            }
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