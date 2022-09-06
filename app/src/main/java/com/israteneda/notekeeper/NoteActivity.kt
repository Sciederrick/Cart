package com.israteneda.notekeeper

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.israteneda.notekeeper.databinding.ActivityMainBinding

class NoteActivity : AppCompatActivity() {
    private val tag = this::class.simpleName
    private var listItemPosition = POSITION_NOT_SET

//    private val noteGetTogetherHelper = NoteGetTogetherHelper(this, lifecycle)

//    private val locManager = PseudoLocationManager(this) { lat, lon ->
//        Log.d(tag, "Location Callback Lat:$lat Lon:$lon")
//    }

    /* bindings */
    private lateinit var binding: ActivityMainBinding

    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var spinnerLists: Spinner
    private lateinit var listItemTitle: EditText
    private lateinit var listItemDescription: EditText

    /* bindings */

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        toolbar = binding.toolbar
        spinnerLists = binding.contentMain.spinnerLists
        listItemTitle = binding.contentMain.newListItemTitle
        listItemDescription = binding.contentMain.newListItemDescription

        setContentView(binding.root)
        setSupportActionBar(toolbar)

        val adapterLists = ArrayAdapter(this,
            android.R.layout.simple_spinner_item,
            DataManager.lists.values as ArrayList)
        adapterLists.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerLists.adapter = adapterLists

        listItemPosition = savedInstanceState?.getInt(LIST_ITEM_POSITION, POSITION_NOT_SET) ?:
            intent.getIntExtra(LIST_ITEM_POSITION, POSITION_NOT_SET)


        if(listItemPosition != POSITION_NOT_SET) {
            displayListItem()
        }
        else {
            createListItem()
        }
        Log.d(tag, "onCreate")
    }

//    override fun onStart() {
//        super.onStart()
//        locManager.start()
//    }
//
//    override fun onStop() {
//        locManager.stop()
//        super.onStop()
//    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(LIST_POSITION, listItemPosition)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (listItemPosition >= DataManager.listItems.lastIndex){
            val menuItem = menu?.findItem(R.id.action_next)
            if (menuItem != null){
                menuItem.icon = getDrawable(R.drawable.ic_block_with_24dp)
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_settings -> true
            R.id.action_next -> {
                if(listItemPosition < DataManager.listItems.lastIndex){
                    moveNext()
                } else {
                    val message = "No more notes"
                    showMessage(message).show()
                }
                true
            }
//            R.id.action_get_together -> {
//                noteGetTogetherHelper.sendMessage(DataManager.loadNote(listItemPosition))
//                true
//            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPause() {
        super.onPause()
        saveListItem()
        Log.d(tag, "onPause")
    }

    private fun saveListItem() {
        val listItem = DataManager.listItems[listItemPosition]
        listItem.title = listItemTitle.text.toString()
        listItem.description = listItemDescription.text.toString()
        listItem.list = spinnerLists.selectedItem as ListInfo
    }

    private fun displayListItem() {
        if(listItemPosition > DataManager.listItems.lastIndex) {
            showMessage("Note not found").show()
            Log.e(tag, "Invalid note position $listItemPosition, max valid position ${DataManager.listItems.lastIndex}")
            return
        }

        Log.i(tag, "Displaying note for position $listItemPosition")
        val listItem = DataManager.listItems[listItemPosition]
        (listItemTitle as TextView).text = listItem.title
        (listItemDescription as TextView).text = listItem.description

        val listPosition = DataManager.lists.values.indexOf(listItem.list)
        spinnerLists.setSelection(listPosition)
    }

    private fun moveNext() {
        ++listItemPosition
        displayListItem()
        invalidateOptionsMenu()
    }

    private fun createListItem() {
        DataManager.listItems.add(ListItem())
        listItemPosition = DataManager.listItems.lastIndex
    }

    private fun showMessage(message: String) =
        Snackbar.make(listItemTitle, message, Snackbar.LENGTH_LONG)

}
