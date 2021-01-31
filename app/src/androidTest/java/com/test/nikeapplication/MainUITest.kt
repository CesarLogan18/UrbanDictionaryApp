package com.test.nikeapplication

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4

import androidx.test.filters.LargeTest

import com.test.nikeapplication.ui.MainActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainUITest {

    @ExperimentalCoroutinesApi
    @FlowPreview
    @get:Rule
    var activityScenarioRule: ActivityScenarioRule<MainActivity?>? = ActivityScenarioRule(
        MainActivity::class.java
    )

    @Test
    fun initialState() {
        onView(withId(R.id.et_search)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_word)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_filter_thumbs_up)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_filter_thumbs_down)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_background)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_filter_label)).check(matches(isDisplayed()))

        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))

        onView(withId(R.id.rv_word)).check(RecyclerViewItemCountAssertion(0))


    }
}