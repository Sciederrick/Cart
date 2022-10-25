package com.derrick.cart.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.derrick.cart.CHECKLIST
import com.derrick.cart.models.Checklist
import com.derrick.cart.R
import com.derrick.cart.ui.SubItemsActivity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ChecklistAdapter(private val context: Context)
    : RecyclerView.Adapter<ChecklistAdapter.ViewHolder>() {

    private var checklists: List<Checklist>? = null
    private val layoutInflater = LayoutInflater.from(context)
    private var onListSelectedListener: OnListSelectedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.item_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = checklists?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val checklist = checklists?.get(position)
        val checklistProgress = (checklist?.itemsChecked?.toDouble()?.div(5) ?: 0) as Double
        val checklistProgressPercentage = (checklistProgress * 100).toInt()

        holder.textTitle.text = checklist?.title
        holder.itemsChecked.text = checklist?.itemsChecked.toString().plus("/${5}")
        holder.progressBar.progress = checklistProgressPercentage
        holder.checklistPosition = position
    }

    fun setOnSelectedListener(listener: OnListSelectedListener) {
        onListSelectedListener = listener
    }

    fun setChecklists(checklists: List<Checklist>) {
        this.checklists = checklists
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textTitle: TextView = itemView.findViewById(R.id.listTitle)
        val itemsChecked: TextView = itemView.findViewById(R.id.itemsChecked)
        val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)

        private val buttonListActions: ImageButton = itemView.findViewById(R.id.imageButtonListActions)
        var checklistPosition = 0

        init {
            itemView.setOnClickListener {
                onListSelectedListener?.onListSelected(checklists!![checklistPosition])

                val intent = Intent(context, SubItemsActivity::class.java)
                val checklist = checklists?.get(checklistPosition)
                checklist?.tags = ""
                checklist?.let { it1 -> intent.putExtra(CHECKLIST, Json.encodeToString(it1)) }
                context.startActivity(intent)
            }

            buttonListActions.setOnClickListener {
                onListSelectedListener?.onOverflowOptionsSelected(checklists!![checklistPosition], checklistPosition)
            }
        }
    }

    interface OnListSelectedListener {
        fun onListSelected(checklist: Checklist)
        fun onOverflowOptionsSelected(checklist: Checklist, checklistPosition: Int)
    }
}