package com.israteneda.notekeeper

import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.israteneda.notekeeper.DataManager.lists
import com.israteneda.notekeeper.databinding.ActivitySubItemsBinding

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
//        Toast.makeText(this, "listItemPosition: $listItemPosition", Toast.LENGTH_LONG).show()
        displayListItems()

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.items, menu)
        return true
    }

    private fun displayListItems() {
        listItems.layoutManager = listItemLayoutManager
        listItems.adapter = listItemRecyclerAdapter
    }

}