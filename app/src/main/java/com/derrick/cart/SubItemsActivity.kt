package com.derrick.cart

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.derrick.cart.DataManager.lists
import com.derrick.cart.databinding.ActivitySubItemsBinding

class SubItemsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySubItemsBinding
    private var listItemPosition: String? = null
    private lateinit var listItems: RecyclerView


    private val listItemLayoutManager by lazy { LinearLayoutManager(this) }

    private val listItemRecyclerAdapter by lazy {
        listItemPosition?.let { DataManager.loadListItems(it) }?.let {
            ListItemRecyclerAdapter(this,
                it
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySubItemsBinding.inflate(layoutInflater)
        listItems = binding.contentSubItems.lists
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        listItemPosition = savedInstanceState?.getString(LIST_ITEM_POSITION) ?:
                intent.getStringExtra(LIST_ITEM_POSITION)

        supportActionBar?.title = lists[listItemPosition]?.title
        displayListItems()

        binding.fab.setOnClickListener {
            val intent = Intent(this, SubItemActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.items, menu)

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

    private fun displayListItems() {
        listItems.layoutManager = listItemLayoutManager
        listItems.adapter = listItemRecyclerAdapter
    }



}