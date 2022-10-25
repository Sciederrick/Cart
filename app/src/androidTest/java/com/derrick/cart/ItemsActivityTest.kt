package com.derrick.cart

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.derrick.cart.ui.ItemsActivity
import org.hamcrest.Matchers.containsString
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ItemsActivityTest{

    @Rule
    @JvmField
    val itemsActivity = ActivityTestRule(ItemsActivity::class.java)

    @Test
    fun selectNoteAfterNavigationDrawerChange(){
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_prices))

        val coursePosition = 0
        onView(withId(R.id.checklists)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ListItemRecyclerAdapter.ViewHolder>(coursePosition, click())
        )

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_lists))

        val notePosition = 0
        onView(withId(R.id.checklists)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ListItemRecyclerAdapter.ViewHolder>(coursePosition, click())
        )

        val note = DataManager.lists[notePosition]
        onView(withId(R.id.spinnerLists)).check(matches(withSpinnerText(containsString(note.course?.title))))
        onView(withId(R.id.editListItemTitle)).check(matches(withText(containsString(note.title))))
        onView(withId(R.id.editListItemDescription)).check(matches(withText(containsString(note.text))))

    }

}