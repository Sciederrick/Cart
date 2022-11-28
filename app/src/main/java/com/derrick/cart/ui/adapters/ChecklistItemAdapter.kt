package com.derrick.cart.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.derrick.cart.CHECKLIST_ITEM
import com.derrick.cart.R
import com.derrick.cart.data.local.entities.Checklist
import com.derrick.cart.data.local.entities.ChecklistItem
import com.derrick.cart.data.local.entities.formattedPrice
import com.derrick.cart.data.local.entities.formattedQuantity
import com.derrick.cart.ui.SubItemActivity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ChecklistItemAdapter(private val context: Context) :
    ListAdapter<ChecklistItem, ChecklistItemAdapter.ViewHolder>(CHECKLIST_ITEM_COMPARATOR) {

    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.item_sublist, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val checklistItem = getItem(position)
        holder.checklistItemTitle.text = checklistItem?.title
        holder.checklistItemDesc.text = checklistItem?.description
        holder.checklistItemQuantity.text = checklistItem?.formattedQuantity()
        holder.checklistItemPrice.text = checklistItem?.formattedPrice()
        holder.checklistItemPosition = position
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val checklistItemTitle: TextView = itemView.findViewById(R.id.listItemTitle)
        val checklistItemDesc: TextView = itemView.findViewById(R.id.listItemDesc)
        val checklistItemQuantity: TextView = itemView.findViewById(R.id.listItemQuantity)
        val checklistItemPrice: TextView = itemView.findViewById(R.id.listItemPrice)
        val checklistItemImage: ImageView = itemView.findViewById(R.id.itemImage)
        var checklistItemPosition = 0

        init {
            itemView.setOnClickListener {
                val intent = Intent(context, SubItemActivity::class.java)
                intent.putExtra(CHECKLIST_ITEM, Json.encodeToString(getItem(checklistItemPosition)))
                context.startActivity(intent)
            }

            val expandButton = itemView.findViewById<ImageButton>(R.id.imageButtonExpand)
            val groupExpand = itemView.findViewById<Group>(R.id.listItemsExpandGroup)
            expandButton.setOnClickListener {
                groupExpand.visibility = if (groupExpand.visibility != View.GONE) {
                    expandButton.rotation = 0f
                    View.GONE
                } else {
                    expandButton.rotation = 180f
                    View.VISIBLE
                }
            }

        }
    }

    companion object {
        private val CHECKLIST_ITEM_COMPARATOR = object : DiffUtil.ItemCallback<ChecklistItem>() {
            override fun areItemsTheSame(oldItem: ChecklistItem, newItem: ChecklistItem): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: ChecklistItem, newItem: ChecklistItem): Boolean {
                return oldItem.title == newItem.title &&
                        oldItem.description == newItem.description &&
                        oldItem.imageURI == newItem.imageURI
            }
        }
    }
}
