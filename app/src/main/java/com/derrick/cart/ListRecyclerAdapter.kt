package com.derrick.cart

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListRecyclerAdapter(private val context: Context, private val lists: List<ListInfo>)
    : RecyclerView.Adapter<ListRecyclerAdapter.ViewHolder>() {

    private val layoutInflater = LayoutInflater.from(context)
    private var onListSelectedListener: OnListSelectedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.item_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = lists.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = lists[position]
        holder.textTitle.text = list.title
        holder.listPosition = position
    }

    fun setOnSelectedListener(listener: OnListSelectedListener) {
        onListSelectedListener = listener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textTitle: TextView = itemView.findViewById(R.id.listTitle)
        var listPosition = 0
        init {
            itemView.setOnClickListener {
                onListSelectedListener?.onListSelected(lists[listPosition])
                val intent = Intent(context, SubItemsActivity::class.java)
                intent.putExtra(LIST_ITEM_POSITION, lists[listPosition].listId)
                context.startActivity(intent)
            }
        }
    }

    interface OnListSelectedListener {
        fun onListSelected(list: ListInfo)
    }
}