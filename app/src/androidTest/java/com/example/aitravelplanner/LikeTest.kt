package com.example.aitravelplanner
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule
import org.junit.Test

class LikeTest {

    @get:Rule
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testLike() {
        val previousLikeCount = getLikeCount()
        onView(withId(R.id.likesImage)).perform(click())
        val updatedLikeCount = getLikeCount()

        assert(previousLikeCount != updatedLikeCount)
    }

    private fun getLikeCount(): Int {
        val likeText = onView(withId(R.id.likesNumber))
            .check(matches(isDisplayed()))
            .toString()

        return likeText.filter { it.isDigit() }.toInt()
    }
}
