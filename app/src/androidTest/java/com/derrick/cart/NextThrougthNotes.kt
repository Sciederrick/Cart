package com.derrick.cart

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.derrick.cart.ui.adapters.ChecklistAdapter
import com.derrick.cart.ui.ItemsActivity
import org.hamcrest.Matchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NextThroughNotes{

    @Rule @JvmField
    val itemsActivity = ActivityTestRule(ItemsActivity::class.java)

    @Test
    fun nextThroughNotes() {
        onView(withId(R.id.checklists)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ChecklistAdapter.ViewHolder>(0, click())
        )

        for(index in 0..DataManager.lists.lastIndex){
            val note = DataManager.lists[index]

            onView(withId(R.id.spinnerLists)).check(
                matches(withSpinnerText(note.course?.title))
            )
            onView(withId(R.id.editListItemTitle)).check(
                matches(withText(note.title))
            )
            onView(withId(R.id.editListItemDescription)).check(
                matches(withText(note.text))
            )

            if(index != DataManager.lists.lastIndex)
                onView(allOf(withId(R.id.action_next), isEnabled())).perform(click())
        }

        onView(withId(R.id.action_next)).check(matches(isEnabled()))

    }


}