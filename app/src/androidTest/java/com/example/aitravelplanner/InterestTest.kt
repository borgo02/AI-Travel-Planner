package com.example.aitravelplanner
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.aitravelplanner.ui.components.interest.InterestComponent
import com.google.android.material.slider.Slider
import org.junit.Rule
import org.junit.Test

class InterestTest {

    @get:Rule
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun test_click_continue(){
        onView(withId(R.id.story))
            .check(matches(isAssignableFrom(InterestComponent::class.java)))

        onView(withId(R.id.story))
            .perform(scrollTo())
            .check { view, _ ->
                if (view is InterestComponent) {
                    val slider = view.findViewById<Slider>(R.id.slider)
                    // Assert the slider's current value
                    assert(slider.value == 5.0f)
                } else {
                    throw AssertionError("Non è un interest component")
                }
            }

        onView(withId(R.id.art))
            .check(matches(isAssignableFrom(InterestComponent::class.java)))

        onView(withId(R.id.art))
            .perform(scrollTo())
            .check { view, _ ->
                if (view is InterestComponent) {
                    val slider = view.findViewById<Slider>(R.id.slider)
                    // Assert the slider's current value
                    assert(slider.value == 5.0f)
                } else {
                    throw AssertionError("Non è un interest component")
                }
            }

        onView(withId(R.id.party))
            .check(matches(isAssignableFrom(InterestComponent::class.java)))

        onView(withId(R.id.party))
            .perform(scrollTo())
            .check { view, _ ->
                if (view is InterestComponent) {
                    val slider = view.findViewById<Slider>(R.id.slider)
                    // Assert the slider's current value
                    assert(slider.value == 5.0f)
                } else {
                    throw AssertionError("Non è un interest component")
                }
            }

        onView(withId(R.id.nature))
            .check(matches(isAssignableFrom(InterestComponent::class.java)))

        onView(withId(R.id.nature))
            .perform(scrollTo())
            .check { view, _ ->
                if (view is InterestComponent) {
                    val slider = view.findViewById<Slider>(R.id.slider)
                    // Assert the slider's current value
                    assert(slider.value == 5.0f)
                } else {
                    throw AssertionError("Non è un interest component")
                }
            }

        onView(withId(R.id.entertainment))
            .check(matches(isAssignableFrom(InterestComponent::class.java)))

        onView(withId(R.id.entertainment))
            .perform(scrollTo())
            .check { view, _ ->
                if (view is InterestComponent) {
                    val slider = view.findViewById<Slider>(R.id.slider)
                    // Assert the slider's current value
                    assert(slider.value == 5.0f)
                } else {
                    throw AssertionError("Non è un interest component")
                }
            }

        onView(withId(R.id.sport))
            .check(matches(isAssignableFrom(InterestComponent::class.java)))

        onView(withId(R.id.sport))
            .perform(scrollTo())
            .check { view, _ ->
                if (view is InterestComponent) {
                    val slider = view.findViewById<Slider>(R.id.slider)
                    // Assert the slider's current value
                    assert(slider.value == 5.0f)
                } else {
                    throw AssertionError("Non è un interest component")
                }
            }

        onView(withId(R.id.shopping))
            .check(matches(isAssignableFrom(InterestComponent::class.java)))

        onView(withId(R.id.shopping))
            .perform(scrollTo())
            .check { view, _ ->
                if (view is InterestComponent) {
                    val slider = view.findViewById<Slider>(R.id.slider)
                    // Assert the slider's current value
                    assert(slider.value == 5.0f)
                } else {
                    throw AssertionError("Non è un interest component")
                }
            }

    }

}
