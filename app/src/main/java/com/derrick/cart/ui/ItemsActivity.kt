package com.derrick.cart.ui

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.derrick.cart.CartApplication
import com.derrick.cart.R
import com.derrick.cart.adapters.ChecklistAdapter
import com.derrick.cart.databinding.ActivityItemsBinding
import com.derrick.cart.models.Checklist
import com.derrick.cart.viewmodels.ItemsActivityViewModel
import com.derrick.cart.viewmodels.ItemsActivityViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class ItemsActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener,
    ChecklistAdapter.OnListSelectedListener, CoroutineScope {

    private var _checklists: List<Checklist>? = null

    private var job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    /* bindings */
    private lateinit var binding: ActivityItemsBinding
    private lateinit var fab: FloatingActionButton
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var navView: NavigationView
    private lateinit var recyclerView: RecyclerView
    /* end of bindings */

    //BottomSheetDialogs
    private val dialogNewList by lazy { BottomSheetDialog(this) }
    private val dialogManageList by lazy { BottomSheetDialog(this) }

    //ViewModels
    private val viewModel: ItemsActivityViewModel by viewModels {
        ItemsActivityViewModelFactory((application as CartApplication).repository)
    }

    //LayoutManagers
    private val checklistLayoutManager by lazy { LinearLayoutManager(this) }

    //RecyclerAdapters
    private val checklistAdapter by lazy {
        val adapter = ChecklistAdapter(this)
        adapter.setOnSelectedListener(this)
        adapter
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityItemsBinding.inflate(layoutInflater)
        fab = binding.appBarItems.fab
        drawerLayout = binding.drawerLayout
        toolbar = binding.appBarItems.toolbar
        navView = binding.navView
        recyclerView = binding.appBarItems.contentItems.checklists /*RecyclerView*/

        setContentView(binding.root)

        setSupportActionBar(toolbar)
        supportActionBar?.title = ""

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Modify default hamburger icon
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(AppCompatResources.getDrawable(this, R.drawable.ic_menu_2_24dp))

        navView.setNavigationItemSelectedListener(this)

        viewModel.allChecklists.observe(this) { checklists ->
            checklists?.let {
                checklistAdapter.setChecklists(it)
                _checklists = it
            }
        }

        recyclerView.layoutManager = checklistLayoutManager
        recyclerView.adapter = checklistAdapter

        fab.setOnClickListener {
            addNewList()
        }

        itemTouchHelper.attachToRecyclerView(recyclerView)
    }


    override fun onStart() {
        super.onStart()
        // this forces the sheet to appear at max height even on landscape (credits: stackoverflow!)
        dialogNewList.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialogManageList.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onResume() {
        super.onResume()
        // retrieve checklist history from SharedPreferences
        val sharedPref = getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        ) ?: return

        launch {
            viewModel.getChecklistHistory(this@ItemsActivity, sharedPref)

            updateChecklistHistory()
        }


    }

    override fun onPause() {
        dialogManageList.dismiss()
        dialogManageList.dismiss()

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

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView?

        searchView?.isIconifiedByDefault = false

        val componentName = ComponentName(this, SearchResultActivity::class.java)
        val searchableInfo = searchManager.getSearchableInfo(componentName)

        searchView?.setSearchableInfo(searchableInfo)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_settings -> gotoSettings()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.nav_settings -> gotoSettings()
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

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

    private fun gotoSettings() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    override fun onListSelected(checklist: Checklist) {
        viewModel.addToRecentlyViewedChecklists(checklist)
        updateChecklistHistory()
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
                viewModel.updateChecklist(checklist)

                Toast.makeText(this, R.string.toast_rename_success, Toast.LENGTH_LONG).show()
            }
        }

        // Delete Button Action
        val deleteBtn = view.findViewById<Button>(R.id.btnDeleteList)
        deleteBtn.setOnClickListener {
            dialogManageList.dismiss()
            viewModel.deleteChecklist(checklist)
            Toast.makeText(this, getString(R.string.toast_delete_success, checklist.title), Toast.LENGTH_SHORT).show()
        }
    }

    private fun addNewList() {
        val view = layoutInflater.inflate(R.layout.bottom_sheet_new_list, null)

        dialogNewList.setCancelable(false)
        dialogNewList.setContentView(view)
        dialogNewList.show()

        // action listeners
        val btnCancel = view.findViewById<Button>(R.id.btnRenameListCancel)
        btnCancel.setOnClickListener { dialogNewList.dismiss() }

        val btnCreateNewList = view.findViewById<Button>(R.id.btnRenameListUpdate)
        btnCreateNewList.setOnClickListener {
            val txtInput = view.findViewById<EditText>(R.id.renameList).text.toString()
            if (viewModel.isEntryValid(txtInput)) {
                viewModel.addNewChecklist(title = txtInput)
            }

            dialogNewList.dismiss()
        }
    }

    private val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
        0, ItemTouchHelper.RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            val deletedChecklist = _checklists?.get(position)
            deletedChecklist?.let { it -> viewModel.deleteChecklist(it) }

            Snackbar.make(recyclerView, "Deleted", Snackbar.LENGTH_LONG)
                .setAction("UNDO") {
                    deletedChecklist?.let { _it -> viewModel.insertChecklist(_it) }
                }
                .show()
        }
    })

}
