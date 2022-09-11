package com.israteneda.notekeeper

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.israteneda.notekeeper.databinding.ActivityItemsBinding


class ItemsActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener,
    ListRecyclerAdapter.OnListSelectedListener{
    private val tag = "ItemsActivity"
    /* bindings */
    private lateinit var binding: ActivityItemsBinding

    private lateinit var fab: FloatingActionButton
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var navView: NavigationView
    private lateinit var lists: RecyclerView
    /* bindings */

    private val listLayoutManager by lazy { LinearLayoutManager(this) }

    private val listRecyclerAdapter by lazy {
        val adapter = ListRecyclerAdapter(this, DataManager.loadLists())
        adapter.setOnSelectedListener(this)
        adapter
    }

    private val listItemLayoutManager by lazy { LinearLayoutManager(this) }

    private val listItemRecyclerAdapter by lazy { ListItemRecyclerAdapter(this, DataManager.listItems) }

    private val recentlyViewedListsRecyclerAdapter by lazy {
        val adapter = ListRecyclerAdapter(this, viewModel.recentlyViewedLists)
        adapter.setOnSelectedListener(this)
        adapter
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[ItemsActivityViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityItemsBinding.inflate(layoutInflater)

        fab = binding.appBarItems.fab
        drawerLayout = binding.drawerLayout
        toolbar = binding.appBarItems.toolbar
        navView = binding.navView
        lists = binding.appBarItems.contentItems.lists

        setContentView(binding.root)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""


        fab.setOnClickListener {
            startActivity(Intent(this, NoteActivity::class.java))
        }

        if (viewModel.isNewlyCreated && savedInstanceState != null) {
            viewModel.restoreState(savedInstanceState)
        }

        viewModel.isNewlyCreated = false
        handleDisplaySelection(viewModel.navDrawerDisplaySelection)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // when there's a configuration change repopulate from viewModel
        updateNavViewHistory()

        navView.setNavigationItemSelectedListener(this)

    }


    private fun displayNotes() {
        lists.layoutManager = listLayoutManager
        lists.adapter = listRecyclerAdapter

        navView.menu.findItem(R.id.nav_notes).isCheckable = true
    }

    private fun displayCourses() {
        lists.layoutManager = listItemLayoutManager
        lists.adapter = listItemRecyclerAdapter

        navView.menu.findItem(R.id.nav_courses).isCheckable = true
    }

    private fun displayRecentlyViewedLists() {
        lists.layoutManager = listLayoutManager
        lists.adapter = recentlyViewedListsRecyclerAdapter

        navView.menu.findItem(R.id.nav_courses).isCheckable = true
    }

    override fun onResume() {
        super.onResume()
        lists.adapter?.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.items, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_notes,
            R.id.nav_courses,
            R.id.nav_recently_notes -> {
                handleDisplaySelection(item.itemId)
                viewModel.navDrawerDisplaySelection = item.itemId
            }
//            R.id.nav_share -> {
//                handleSelection(R.string.nav_share_message)
//            }
//            R.id.nav_send -> {
//                handleSelection(R.string.nav_send_message)
//            }
//            R.id.nav_how_many -> {
//                val message = getString(R.string.nav_how_many_message_format,
//                DataManager.lists.size, DataManager.courses.size)
//                Snackbar.make(listItems, message, Snackbar.LENGTH_LONG).show()
//            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun handleDisplaySelection(itemId: Int) {
        when(itemId){
            R.id.nav_notes -> {
                displayNotes()
            }
            R.id.nav_courses -> {
                displayCourses()
            }
            R.id.nav_recently_notes -> {
                displayRecentlyViewedLists()
            }
        }
    }

//    private fun handleSelection(stringId: Int) {
//        Snackbar.make(listItems, stringId, Snackbar.LENGTH_LONG).show()
//    }

    override fun onListSelected(list: ListInfo) {
        viewModel.addToRecentlyViewedLists(list)
        updateNavViewHistory()
    }

    private fun updateNavViewHistory() {
        lateinit var title: String
        lateinit var titleCondensed: String
        lateinit var itemHistory: MenuItem

        val target = navView.menu.findItem(R.id.nav_item_history).subMenu
        target.clear()

        for (listItem in viewModel.recentlyViewedLists) {
            title = listItem.title!!
            titleCondensed = if (listItem.title?.length!! > 20) listItem.title?.slice(0..20)
                .plus("...") else title
            itemHistory = target.add(Menu.NONE, Menu.NONE, Menu.NONE, titleCondensed)
            itemHistory.icon = AppCompatResources.getDrawable(this, R.drawable.ic_history_24dp)

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(outState != null)
            viewModel.saveState(outState)
    }
}
