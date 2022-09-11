package com.israteneda.notekeeper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class ListItemRecyclerAdapter(private val context: Context, private val listItems: List<ListItem>) :
    RecyclerView.Adapter<ListItemRecyclerAdapter.ViewHolder>() {

    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.item_course_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int = listItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = listItems[position]
        holder.listItemTitle.text = list.title
        holder.listItemDesc.text = list.description
        holder.listItemPosition = position
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val listItemTitle: TextView = itemView.findViewById(R.id.listItemTitle)
        val listItemDesc: TextView = itemView.findViewById(R.id.listItemDesc)
        var listItemPosition = 0

        init {
            val expandButton = itemView.findViewById<ImageButton>(R.id.imageButtonExpand)
            val groupExpand = itemView.findViewById<Group>(R.id.listItemsExpandGroup)
            expandButton.setOnClickListener {
                groupExpand.visibility = if (groupExpand.visibility != View.GONE) {
                    expandButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_expand_down_24dp))
                    View.GONE
                } else {
                    expandButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_expand_up_24dp))
                    View.VISIBLE
                }
            }

        }
    }
}
