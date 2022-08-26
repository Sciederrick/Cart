package com.israteneda.notekeeper

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.SubMenu
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.children
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
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
    private lateinit var listItems: RecyclerView
    /* bindings */

    private val noteLayoutManager by lazy { LinearLayoutManager(this) }

    private val noteRecycleAdapter by lazy {
        val adapter = ListRecyclerAdapter(this, DataManager.loadLists())
        adapter.setOnSelectedListener(this)
        adapter
    }

    private val courseLayoutManager by lazy { GridLayoutManager(this, resources.getInteger(R.integer.course_grid_span)) }

    private val courseRecycleAdapter by lazy { CourseRecycleAdapter(this, DataManager.courses.values.toList()) }

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
        listItems = binding.appBarItems.contentItems.listItems

        setContentView(binding.root)
        setSupportActionBar(toolbar)


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
        listItems.layoutManager = noteLayoutManager
        listItems.adapter = noteRecycleAdapter

        navView.menu.findItem(R.id.nav_notes).isCheckable = true
    }

    private fun displayCourses() {
        listItems.layoutManager = courseLayoutManager
        listItems.adapter = courseRecycleAdapter

        navView.menu.findItem(R.id.nav_courses).isCheckable = true
    }

    private fun displayRecentlyViewedLists() {
        listItems.layoutManager = noteLayoutManager
        listItems.adapter = recentlyViewedListsRecyclerAdapter

        navView.menu.findItem(R.id.nav_courses).isCheckable = true
    }

    override fun onResume() {
        super.onResume()
        listItems.adapter?.notifyDataSetChanged()
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
        viewModel.addToRecentlyViewedNotes(list)
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
            itemHistory = target.add(Menu.NONE, Menu.NONE, Menu.NONE, "$titleCondensed")
            itemHistory.icon = getDrawable(R.drawable.ic_history_24dp)

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(outState != null)
            viewModel.saveState(outState)
    }
}
