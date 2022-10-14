package com.derrick.cart.ui

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.derrick.cart.CartApplication
import com.derrick.cart.R
import com.derrick.cart.adapters.ChecklistAdapter
import com.derrick.cart.databinding.ActivityItemsBinding
import com.derrick.cart.models.Checklist
import com.derrick.cart.viewmodels.SearchViewModel
import com.derrick.cart.viewmodels.SearchViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class SearchResultActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener,
    ChecklistAdapter.OnListSelectedListener, CoroutineScope {

    private var job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    /* bindings */
    private lateinit var binding: ActivityItemsBinding
    private lateinit var fab: FloatingActionButton
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var navView: NavigationView
    private lateinit var checklists: RecyclerView
    /* end of bindings */

    //BottomSheetDialogs
    private val bottomSheetDialog by lazy { BottomSheetDialog(this) }

    //ViewModels
//    private val viewModel by lazy {
//        ViewModelProvider(this)[ItemsActivityViewModel::class.java]
//    }

    private val viewModel: SearchViewModel by viewModels {
        SearchViewModelFactory((application as CartApplication).repository)
    }

    //LayoutManagers
    private val checklistLayoutManager by lazy { LinearLayoutManager(this) }
//    private val listItemLayoutManager by lazy { LinearLayoutManager(this) }

    //RecyclerAdapters
    private val checklistAdapter by lazy {
//        val adapter = ListRecyclerAdapter(this, DataManager.listsArray, dialogManageList)
        val adapter = ChecklistAdapter(this)
        adapter.setOnSelectedListener(this)
        adapter
    }
//    private val listItemRecyclerAdapter by lazy { ListItemRecyclerAdapter(this, DataManager.listItems) }
//    private val recentlyViewedListsRecyclerAdapter by lazy {
//        val adapter = ListRecyclerAdapter(this, viewModel.recentlyViewedLists)
//        adapter.setOnSelectedListener(this)
//        adapter
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityItemsBinding.inflate(layoutInflater)
        fab = binding.appBarItems.fab
        drawerLayout = binding.drawerLayout
        toolbar = binding.appBarItems.toolbar
        navView = binding.navView
        checklists = binding.appBarItems.contentItems.checklists /*RecyclerView*/

        setContentView(binding.root)
        fab.visibility = View.INVISIBLE

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""


        navView.setNavigationItemSelectedListener(this)

        viewModel.allChecklists.observe(this) { checklists ->
            checklists?.let {
                checklistAdapter.setChecklists(it)
            }
        }

        checklists.layoutManager = checklistLayoutManager
        checklists.adapter = checklistAdapter

//        if (viewModel.isNewlyCreated && savedInstanceState != null) {
//            viewModel.restoreState(savedInstanceState)
//        }

//        viewModel.isNewlyCreated = false
//        handleDisplaySelection(viewModel.navDrawerDisplaySelection)

        // when there's a configuration change repopulate from viewModel
//        updateNavViewHistory()
        handleIntent(intent)

    }


    override fun onStart() {
        super.onStart()
        // this forces the sheet to appear at max height even on landscape (credits: stackoverflow!)
        bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onResume() {
        super.onResume()
        // retrieve checklist history from SharedPreferences
        val sharedPref = getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        ) ?: return

        launch {
            viewModel.getChecklistHistory(this@SearchResultActivity, sharedPref)

            updateChecklistHistory()
        }


    }

    override fun onPause() {
        bottomSheetDialog.dismiss()

        // save checklist view history
        val sharedPref = getSharedPreferences(
            getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )
        viewModel.saveChecklistHistory(this, sharedPref)

        super.onPause()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.appbar_actions, menu)
        // Hide Action Buttons & Action Overflow
        menu.findItem(R.id.action_settings).isVisible = false
        menu.findItem(R.id.action_search).isVisible = false

        // Configure Search
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView?
        searchView?.queryHint = getString(R.string.search_view_list)
        searchView?.isIconifiedByDefault = false

        val componentName = ComponentName(this, SearchResultActivity::class.java)
        val searchableInfo = searchManager.getSearchableInfo(componentName)

        searchView?.setSearchableInfo(searchableInfo)

        return super.onCreateOptionsMenu(menu)
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.nav_lists,
//            R.id.nav_prices,
//            R.id.nav_recently_notes -> {
//                handleDisplaySelection(item.itemId)
//                viewModel.navDrawerDisplaySelection = item.itemId
//            }
//        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        viewModel.saveState(outState)
//    }


//  Custom -----------------------------------------------------------------------------------------

/*Navigation*/
//    private fun handleDisplaySelection(itemId: Int) {
//        when(itemId){
//            R.id.nav_lists -> {
//                displayLists()
//            }
//            R.id.nav_prices -> {
//                displayListItems()
//            }
//            R.id.nav_recently_notes -> {
//                displayRecentlyViewedLists()
//            }
//        }
//    }


    private fun updateChecklistHistory() {
        lateinit var title: String
        lateinit var titleCondensed: String
        lateinit var itemHistory: MenuItem

        val target = navView.menu.findItem(R.id.nav_item_history).subMenu
        target?.clear()

        for (listItem in viewModel.recentlyViewedChecklists) {
            title = listItem.title!!
            titleCondensed = if (listItem.title?.length!! > 20) listItem.title?.slice(0..20)
                .plus("...") else title
            itemHistory = target!!.add(Menu.NONE, Menu.NONE, Menu.NONE, titleCondensed)
            itemHistory.icon = AppCompatResources.getDrawable(this, R.drawable.ic_history_24dp)

        }
    }


//    private fun displayRecentlyViewedLists() {
//        lists.layoutManager = listLayoutManager
//        lists.adapter = recentlyViewedListsRecyclerAdapter
//
//        navView.menu.findItem(R.id.nav_prices).isCheckable = true
//    }
/*End of Navigation*/


    /*Listeners*/
    override fun onListSelected(checklist: Checklist) {
        viewModel.addToRecentlyViewedChecklists(checklist)
        updateChecklistHistory()
    }

    override fun onOverflowOptionsSelected(checklist: Checklist, checklistPosition: Int) {
        val view = layoutInflater.inflate(R.layout.bottom_sheet_manage_list, null)

        // Title
        val title = view.findViewById<TextView>(R.id.manageList)
        title.text = checklist.title

        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()

        // GoTo Rename Button Action
        val actionRename = view.findViewById<Button>(R.id.btnRenameList)
        val renameView = layoutInflater.inflate(R.layout.bottom_sheet_rename_list, null)
        // Title
        val renameTitle = renameView.findViewById<EditText>(R.id.renameList)
        renameTitle.setText(checklist.title)

        actionRename.setOnClickListener {
            bottomSheetDialog.cancel()
            bottomSheetDialog.setContentView(renameView)
            bottomSheetDialog.show()

            // Cancel Button Action -- go back to bottom_sheet_manage_list
            val renameViewCancelBtn = renameView.findViewById<Button>(R.id.btnRenameListCancel)
            renameViewCancelBtn.setOnClickListener {
                bottomSheetDialog.cancel()
                bottomSheetDialog.setContentView(view)
                bottomSheetDialog.show()
            }

            // Rename Button Action
            val renameViewUpdateBtn = renameView.findViewById<Button>(R.id.btnRenameListUpdate)
            renameViewUpdateBtn.setOnClickListener {
                bottomSheetDialog.cancel()
                checklist.title = renameTitle.text.toString()
                viewModel.update(checklist)

                Toast.makeText(this, R.string.toast_rename_success, Toast.LENGTH_LONG).show()
            }
        }

        // Delete Button Action
        val deleteBtn = view.findViewById<Button>(R.id.btnDeleteList)
        deleteBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
            viewModel.delete(checklist)
            Toast.makeText(this, getString(R.string.toast_delete_success, checklist.title), Toast.LENGTH_SHORT).show()
        }
    }
/*End of Listeners*/

    /*Search*/
    private fun handleIntent(intent: Intent) {
        if (intent.action == Intent.ACTION_SEARCH) {
            val searchQuery = intent.getStringExtra(SearchManager.QUERY)

            searchQuery?.let {
                viewModel.getChecklistsByTitleOrItemsCheckedOrTags(searchQuery).observe(this) {
                    it?.let {
                        checklistAdapter.setChecklists(it)
                    }
                }
            }
        }
    }
    /*Search*/
/*Display*/
//    private fun displayLists() {
//        lists.layoutManager = listLayoutManager
//        lists.adapter = listRecyclerAdapter
//
//        navView.menu.findItem(R.id.nav_lists).isCheckable = true
//    }

//    private fun displayListItems() {
//        lists.layoutManager = listItemLayoutManager
//        lists.adapter = listItemRecyclerAdapter
//
//        navView.menu.findItem(R.id.nav_prices).isCheckable = true
//    }
/*Display*/



}
