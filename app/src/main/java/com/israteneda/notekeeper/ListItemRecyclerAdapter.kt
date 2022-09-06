package com.israteneda.notekeeper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
        holder.listItemPosition = position
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val listItemTitle: TextView = itemView.findViewById(R.id.listItemTitle)
        var listItemPosition = 0
    }
}
