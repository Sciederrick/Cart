package com.derrick.cart.ui

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.derrick.cart.*
import com.derrick.cart.adapters.ChecklistItemAdapter
import com.derrick.cart.contracts.SubItemActivityContract
import com.derrick.cart.databinding.ActivitySubItemsBinding
import com.derrick.cart.models.Checklist
import com.derrick.cart.models.ChecklistItem
import com.derrick.cart.viewmodels.SubItemsActivityViewModel
import com.derrick.cart.viewmodels.SubItemsActivityViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

class SubItemsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySubItemsBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var fab: FloatingActionButton
    private var checklist: Checklist? = null
    private var _checklistItems: List<ChecklistItem>? = null

    private val checklistItemLayoutManager by lazy { LinearLayoutManager(this) }
    private val checklistItemAdapter by lazy {
        checklist?.id?.let {
            val adapter = ChecklistItemAdapter(this)
            adapter
        }
    }

    private val viewModel: SubItemsActivityViewModel by viewModels {
        SubItemsActivityViewModelFactory((application as CartApplication).repository)
    }

    private val startForResult =
        registerForActivityResult(SubItemActivityContract()) { result: Checklist? ->
            result?.let {
                checklist = result
                displayChecklistItems()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySubItemsBinding.inflate(layoutInflater)
        recyclerView = binding.contentSubItems.checklists
        fab = binding.fab
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        checklist =
            savedInstanceState?.getString(CHECKLIST)?.let { it -> Json.decodeFromString(it) }
                ?: intent.getStringExtra(CHECKLIST)?.let { Json.decodeFromString(it) }

        supportActionBar?.title = checklist?.title ?: ""

        displayChecklistItems()

        fab.setOnClickListener {
            startForResult.launch(checklist)
        }

        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.appbar_actions, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView?
        searchView?.queryHint = getString(R.string.search_view_list_items)
        searchView?.isIconifiedByDefault = false

        val componentName = ComponentName(this, SearchResultActivity::class.java)
        val searchableInfo = searchManager.getSearchableInfo(componentName)

        searchView?.setSearchableInfo(searchableInfo)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(CHECKLIST, Json.encodeToString(checklist))
    }

    private fun displayChecklistItems() {
        checklist?.id?.let { it ->
            viewModel.getChecklistItemsByChecklistId(it).observe(this) { checklistItems ->
                checklistItems?.let { it ->
                    checklistItemAdapter?.setChecklistItems(it)
                    _checklistItems = it
                }
            }
        }

        recyclerView.layoutManager = checklistItemLayoutManager
        recyclerView.adapter = checklistItemAdapter
    }

    private val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT
    ) {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            val deletedChecklistItem = _checklistItems?.get(position)
            deletedChecklistItem?.let { it -> viewModel.deleteChecklistItem(it) }

            Snackbar.make(recyclerView, "Deleted", Snackbar.LENGTH_LONG)
                .setAction("UNDO") {
                    //undoDelete(position, deletedChecklistItem)
                    //updateChecklistItem(deletedChecklistItem, true)
                    deletedChecklistItem?.let { it -> viewModel.insertChecklistItem(it) }
                }
                .show()
        }
    })

}