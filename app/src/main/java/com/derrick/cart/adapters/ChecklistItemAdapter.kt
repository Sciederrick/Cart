package com.derrick.cart.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.derrick.cart.CHECKLIST_ITEM
import com.derrick.cart.CHECKLIST_ITEM_POSITION
import com.derrick.cart.R
import com.derrick.cart.models.ChecklistItem
import com.derrick.cart.ui.SubItemActivity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.w3c.dom.Text

class ChecklistItemAdapter(private val context: Context) :
    RecyclerView.Adapter<ChecklistItemAdapter.ViewHolder>() {

    private var checklistItems: List<ChecklistItem>? = null
    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.item_sublist, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int = checklistItems?.size ?: 0

    fun setChecklistItems(checklistItems: List<ChecklistItem>) {
        this.checklistItems = checklistItems
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val checklistItem = checklistItems?.get(position)
        holder.checklistItemTitle.text = checklistItem?.title
        holder.checklistItemDesc.text = checklistItem?.description
        holder.checklistItemQuantity.text = checklistItem?.quantity.toString()
        holder.checklistItemPrice.text = checklistItem?.price.toString()
        holder.checklistItemPosition = position
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val checklistItemTitle: TextView = itemView.findViewById(R.id.listItemTitle)
        val checklistItemDesc: TextView = itemView.findViewById(R.id.listItemDesc)
        val checklistItemQuantity: TextView = itemView.findViewById(R.id.listItemQuantity)
        val checklistItemPrice: TextView = itemView.findViewById(R.id.listItemPrice)
        var checklistItemPosition = 0

        init {
            itemView.setOnClickListener {
                val intent = Intent(context, SubItemActivity::class.java)
                intent.putExtra(CHECKLIST_ITEM, Json.encodeToString(checklistItems?.get(checklistItemPosition)))
                context.startActivity(intent)
            }

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
