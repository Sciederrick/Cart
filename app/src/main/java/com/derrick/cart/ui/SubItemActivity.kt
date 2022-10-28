package com.derrick.cart.ui

import android.app.Activity
import com.derrick.cart.*
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.google.android.material.imageview.ShapeableImageView
import com.derrick.cart.databinding.ActivityMainBinding
import com.derrick.cart.models.Checklist
import com.derrick.cart.models.ChecklistItem
import com.derrick.cart.viewmodels.SubItemActivityViewModel
import com.derrick.cart.viewmodels.SubItemActivityViewModelFactory
import kotlinx.coroutines.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.coroutines.CoroutineContext

class SubItemActivity : AppCompatActivity(), CoroutineScope {
    private var checklistItem: ChecklistItem? = null
    private var checklists: List<Checklist>? = null
    private var currentChecklist: Checklist? = null

    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var spinnerLists: Spinner
    private lateinit var checklistItemTitle: EditText
    private lateinit var checklistItemDescription: EditText
    private var checklistItemQuantity: EditText? = null
    private var checklistItemPrice: EditText? = null
    private lateinit var btnExpand: ImageButton
    private lateinit var imageViewSmall: ShapeableImageView
    private lateinit var imageViewLarge: ShapeableImageView

    private val adapterLists by lazy {
        ArrayAdapter<Checklist>(
            this,
            android.R.layout.simple_spinner_item,
            mutableListOf()
        )
    }

    private var job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private val viewModel: SubItemActivityViewModel by viewModels {
        SubItemActivityViewModelFactory((application as CartApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        toolbar = binding.toolbar
        spinnerLists = binding.contentMain.spinnerLists
        checklistItemTitle = binding.contentMain.editListItemTitle
        checklistItemDescription = binding.contentMain.editListItemDescription
        checklistItemQuantity = binding.contentMain.editListItemQuantity
        checklistItemPrice = binding.contentMain.editListItemPrice
        btnExpand = binding.contentMain.btnExpand!!
        imageViewSmall = binding.contentMain.itemImage2Small!!
        imageViewLarge = binding.contentMain.itemImage2!!

        setContentView(binding.root)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        populateAdapterList()
        adapterLists.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLists.adapter = adapterLists

        val currentChecklistJSON = savedInstanceState?.getString(CHECKLIST)
            ?: intent.getStringExtra(CHECKLIST)
        currentChecklist = currentChecklistJSON?.let { it -> Json.decodeFromString(it) }

        Log.d(this::class.simpleName, "current checklist: $currentChecklist")

        val checklistItemJSON =
            savedInstanceState?.getString(CHECKLIST_ITEM)
                ?: intent.getStringExtra(CHECKLIST_ITEM)
        checklistItem = checklistItemJSON?.let { Json.decodeFromString(it) }

        if (checklistItem != null) displayChecklistItem()

        btnExpand.setOnClickListener { toggleItemImage() }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val selectedChecklist = spinnerLists.selectedItem as Checklist
        val newChecklistItem = ChecklistItem(
            id = checklistItem?.id ?: 0,
            checklistId = selectedChecklist.id,
            title = checklistItemTitle.text.toString(),
            description = checklistItemDescription.text.toString(),
            quantity = (checklistItemQuantity as TextView?)?.text?.toString()?.toFloat(),
            price = (checklistItemPrice as TextView?)?.text?.toString()?.toDouble(),
            hasSublist = false,
            isDone = false
        )
        outState.putString(CHECKLIST_ITEM, Json.encodeToString(newChecklistItem))
        outState.putString(CHECKLIST, Json.encodeToString(selectedChecklist))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val checklist = getChecklist()
                if (checklist != null) {
                    val replyIntent = Intent()
                    // TODO: Add Update && Delete functionality
                    replyIntent.putExtra(CHECKLIST, Json.encodeToString(checklist))
                    setResult(Activity.RESULT_OK, replyIntent)
                    finish()
                } else {
                    startActivity(Intent(this, ItemsActivity::class.java))
                }
            }
            R.id.action_home -> {
                startActivity(Intent(this, ItemsActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()
        saveListItem()
    }

    private fun saveListItem() {
        val selectedChecklist = spinnerLists.selectedItem as Checklist

        val newChecklistItem = ChecklistItem(
            id = checklistItem?.id ?: 0,
            checklistId = selectedChecklist.id,
            title = checklistItemTitle.text.toString(),
            description = checklistItemDescription.text.toString(),
            quantity = (checklistItemQuantity as TextView?)?.text?.toString()?.toFloat() ?: 0F,
            price = (checklistItemPrice as TextView?)?.text?.toString()?.toDouble() ?: 0.00,
            hasSublist = false,
            isDone = false
        )

        viewModel.insertChecklistItem(newChecklistItem)
    }

    private fun displayChecklistItem() {
        val checklist = getChecklist()

        (checklistItemTitle as TextView).text = checklistItem?.title
        (checklistItemDescription as TextView).text = checklistItem?.description
        (checklistItemQuantity as TextView?)?.text = checklistItem?.quantity.toString()
        (checklistItemPrice as TextView?)?.text = checklistItem?.price.toString()

        if (checklists == null) return

        if (checklists!!.isNotEmpty()) {
            checklist?.let { it ->
                spinnerLists.setSelection(checklists!!.indexOf(it))
            }
        }
    }

    private fun toggleItemImage() {
        if (imageViewLarge.visibility == View.VISIBLE) {
            btnExpand.setImageDrawable(
                AppCompatResources.getDrawable(
                    this,
                    R.drawable.ic_caret_down_bold_24dp
                )
            )
            imageViewLarge.visibility = View.GONE
            imageViewSmall.visibility = View.VISIBLE
        } else {
            btnExpand.setImageDrawable(
                AppCompatResources.getDrawable(
                    this,
                    R.drawable.ic_caret_up_bold_24dp
                )
            )
            imageViewLarge.visibility = View.VISIBLE
            imageViewSmall.visibility = View.GONE
        }
    }

    private fun populateAdapterList() = launch {
        val deferred: Deferred<List<Checklist>?> = async {
            viewModel.getAllChecklistsAsync().await()
        }

        deferred.join()

        checklists = deferred.await()

        checklists?.let {
            if (checklists!!.isNotEmpty()) adapterLists.addAll(checklists!!)
        }

        val parent = checklists?.find { it -> it.id == checklistItem?.checklistId }
        Log.d(this::class.simpleName, "parent: $parent, current checklist: $currentChecklist")
        checklists?.let {
            spinnerLists.setSelection(checklists!!.indexOf(currentChecklist ?: parent))
        }
    }

    private fun getChecklist() :Checklist?{
        return checklists?.let { it ->
            it.find { it2 ->
                it2.title == spinnerLists.selectedItem.toString() }
        }
    }

}
