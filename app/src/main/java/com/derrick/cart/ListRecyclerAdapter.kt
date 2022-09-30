package com.derrick.cart

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog

class ListRecyclerAdapter(private val context: Context,
                          private val lists: List<ListInfo>,
                          private val dialogManageList: BottomSheetDialog? = null)
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
        private val buttonListActions: ImageButton = itemView.findViewById(R.id.imageButtonListActions)
        var listPosition = 0
        init {
            itemView.setOnClickListener {
                onListSelectedListener?.onListSelected(lists[listPosition])
                val intent = Intent(context, SubItemsActivity::class.java)
                intent.putExtra(LIST_ITEM_POSITION, lists[listPosition].listId)
                context.startActivity(intent)
            }

            buttonListActions.setOnClickListener {
                val view = layoutInflater.inflate(R.layout.bottom_sheet_manage_list, null)

                // Title
                val title = view.findViewById<TextView>(R.id.manageList)
                title.text = lists[listPosition].title

                dialogManageList?.setContentView(view)
                dialogManageList?.show()

                // GoTo Rename Button Action
                val actionRename = view.findViewById<Button>(R.id.btnRenameList)
                val renameView = layoutInflater.inflate(R.layout.bottom_sheet_rename_list, null)
                // Title
                val renameTitle = renameView.findViewById<EditText>(R.id.renameList)
                renameTitle.setText(lists[listPosition].title)

                actionRename.setOnClickListener {
                    dialogManageList?.cancel()
                    dialogManageList?.setContentView(renameView)
                    dialogManageList?.show()

                    // Cancel Button Action -- go back to bottom_sheet_manage_list
                    val renameViewCancelBtn = renameView.findViewById<Button>(R.id.btnRenameListCancel)
                    renameViewCancelBtn.setOnClickListener {
                        dialogManageList?.cancel()
                        dialogManageList?.setContentView(view)
                        dialogManageList?.show()
                    }

                    // Rename Button Action
                    val renameViewUpdateBtn = renameView.findViewById<Button>(R.id.btnRenameListUpdate)
                    renameViewUpdateBtn.setOnClickListener {
                        dialogManageList?.cancel()
                        lists[listPosition].title = renameTitle.text.toString()
                        Toast.makeText(context, R.string.toast_rename_success, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    interface OnListSelectedListener {
        fun onListSelected(list: ListInfo)
    }
}