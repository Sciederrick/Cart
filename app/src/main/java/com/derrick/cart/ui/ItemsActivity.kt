package com.derrick.cart.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.derrick.cart.CartApplication
import com.derrick.cart.models.Checklist
import com.derrick.cart.viewmodels.ItemsActivityViewModel
import com.derrick.cart.adapters.ChecklistAdapter
import com.derrick.cart.R
import com.derrick.cart.databinding.ActivityItemsBinding
import com.derrick.cart.viewmodels.ItemsActivityViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ItemsActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener,
    ChecklistAdapter.OnListSelectedListener {
    private val tag = "ItemsActivity"

    /* bindings */
    private lateinit var binding: ActivityItemsBinding
    private lateinit var fab: FloatingActionButton
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var navView: NavigationView
    private lateinit var checklists: RecyclerView
    /* end of bindings */

    //BottomSheetDialogs
    private val dialogNewList by lazy { BottomSheetDialog(this) }
    private val dialogManageList by lazy { BottomSheetDialog(this) }

    //ViewModels
//    private val viewModel by lazy {
//        ViewModelProvider(this)[ItemsActivityViewModel::class.java]
//    }

    private val viewModel: ItemsActivityViewModel by viewModels {
        ItemsActivityViewModelFactory((application as CartApplication).repository)
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
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""

        viewModel.allChecklists.observe(this) { checklists ->
            checklists?.let {
                checklistAdapter.setChecklists(it)
            }
        }

        checklists.layoutManager = checklistLayoutManager
        checklists.adapter = checklistAdapter

        fab.setOnClickListener {
            showModalBottomSheet()
        }

//        if (viewModel.isNewlyCreated && savedInstanceState != null) {
//            viewModel.restoreState(savedInstanceState)
//        }

//        viewModel.isNewlyCreated = false
//        handleDisplaySelection(viewModel.navDrawerDisplaySelection)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // when there's a configuration change repopulate from viewModel
//        updateNavViewHistory()

        navView.setNavigationItemSelectedListener(this)

    }

    private fun showModalBottomSheet() {
        val view = layoutInflater.inflate(R.layout.bottom_sheet_new_list, null)

        dialogNewList.setCancelable(false)
        dialogNewList.setContentView(view)
        dialogNewList.show()

        // action listeners
        val btnCancel = view.findViewById<Button>(R.id.btnRenameListCancel)
        btnCancel.setOnClickListener { dialogNewList.dismiss() }

        val btnCreateNewList = view.findViewById<Button>(R.id.btnRenameListUpdate)
        btnCreateNewList.setOnClickListener {
            val txtInput = view.findViewById<EditText>(R.id.renameList)
            viewModel.insert(Checklist(0, txtInput.text.toString(), null, 0))
            dialogNewList.dismiss()
        }
    }


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

//    private fun displayRecentlyViewedLists() {
//        lists.layoutManager = listLayoutManager
//        lists.adapter = recentlyViewedListsRecyclerAdapter
//
//        navView.menu.findItem(R.id.nav_prices).isCheckable = true
//    }

    override fun onStart() {
        super.onStart()
        // this forces the sheet to appear at max height even on landscape (credits: stackoverflow!)
        dialogNewList.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialogManageList.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

//    override fun onResume() {
//        super.onResume()
//        lists.adapter?.notifyDataSetChanged()
//    }

    override fun onPause() {
        dialogManageList.dismiss()
        dialogManageList.dismiss()
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
        menuInflater.inflate(R.menu.items, menu)

//        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
//        val searchItem = menu.findItem(R.id.action_search)
//        val searchView = searchItem.actionView as SearchView?
//        searchView?.queryHint = getString(R.string.search_view_list)
//        searchView?.isIconifiedByDefault = false
//
//        val componentName = ComponentName(this, SearchResultActivity::class.java)
//        val searchableInfo = searchManager.getSearchableInfo(componentName)
//
//        searchView?.setSearchableInfo(searchableInfo)

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


    override fun onListSelected(checklist: Checklist) {
//        viewModel.addToRecentlyViewedLists(list)
//        updateNavViewHistory()
    }

    override fun onOverflowOptionsSelected(checklist: Checklist, checklistPosition: Int) {
        val view = layoutInflater.inflate(R.layout.bottom_sheet_manage_list, null)

        // Title
        val title = view.findViewById<TextView>(R.id.manageList)
        title.text = checklist.title

        dialogManageList.setContentView(view)
        dialogManageList.show()

        // GoTo Rename Button Action
        val actionRename = view.findViewById<Button>(R.id.btnRenameList)
        val renameView = layoutInflater.inflate(R.layout.bottom_sheet_rename_list, null)
        // Title
        val renameTitle = renameView.findViewById<EditText>(R.id.renameList)
        renameTitle.setText(checklist.title)

        actionRename.setOnClickListener {
            dialogManageList.cancel()
            dialogManageList.setContentView(renameView)
            dialogManageList.show()

            // Cancel Button Action -- go back to bottom_sheet_manage_list
            val renameViewCancelBtn = renameView.findViewById<Button>(R.id.btnRenameListCancel)
            renameViewCancelBtn.setOnClickListener {
                dialogManageList.cancel()
                dialogManageList.setContentView(view)
                dialogManageList.show()
            }

            // Rename Button Action
            val renameViewUpdateBtn = renameView.findViewById<Button>(R.id.btnRenameListUpdate)
            renameViewUpdateBtn.setOnClickListener {
                dialogManageList.cancel()
                checklist.title = renameTitle.text.toString()
                viewModel.update(checklist)

                Toast.makeText(this, R.string.toast_rename_success, Toast.LENGTH_LONG).show()
            }
        }

        // Delete Button Action
        val deleteBtn = view.findViewById<Button>(R.id.btnDeleteList)
        deleteBtn.setOnClickListener {
            dialogManageList.dismiss()

            viewModel.delete(checklist)

//            checklistAdapter.notifyItemRemoved(checklistPosition)

            Log.d(this::class.java.name, "checklist position: $checklistPosition")

            Toast.makeText(this, getString(R.string.toast_delete_success, checklist.title), Toast.LENGTH_SHORT).show()
        }
    }

//    private fun updateNavViewHistory() {
//        lateinit var title: String
//        lateinit var titleCondensed: String
//        lateinit var itemHistory: MenuItem
//
//        val target = navView.menu.findItem(R.id.nav_item_history).subMenu
//        target?.clear()
//
//        for (listItem in viewModel.recentlyViewedLists) {
//            title = listItem.title!!
//            titleCondensed = if (listItem.title?.length!! > 20) listItem.title?.slice(0..20)
//                .plus("...") else title
//            itemHistory = target!!.add(Menu.NONE, Menu.NONE, Menu.NONE, titleCondensed)
//            itemHistory.icon = AppCompatResources.getDrawable(this, R.drawable.ic_history_24dp)
//
//        }
//    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        viewModel.saveState(outState)
//    }
}
