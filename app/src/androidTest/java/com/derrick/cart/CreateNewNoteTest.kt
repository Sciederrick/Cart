package com.derrick.cart

import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Matchers.*
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CreateNewNoteTest{

//    @Rule @JvmField
//    val noteListActivity = ActivityTestRule(NoteListActivity::class.java)

//    @Test
//    fun createNewNote() {
//        val course = DataManager.courses["android_async"]
//        val noteTile = "This is a test note"
//        val noteText = "This is the body of our test note"
//
//        onView(withId(R.id.fab)).perform(click())
//
//        onView(withId(R.id.spinnerLists)).perform(click())
//        onData(allOf(instanceOf(CourseInfo::class.java), equalTo(course))).perform(click())
//
//        onView(withId(R.id.newListItemTitle)).perform(typeText(noteTile))
//        onView(withId(R.id.newListItemDescription)).perform(typeText(noteText), closeSoftKeyboard())
//
//        pressBack()
//
//        val note = DataManager.lists.last()
//        assertEquals(course, note.course)
//        assertEquals(noteTile, note.title)
//        assertEquals(noteText, note.text)
//    }
}