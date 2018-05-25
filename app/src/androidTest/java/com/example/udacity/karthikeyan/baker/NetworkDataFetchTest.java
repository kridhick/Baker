package com.example.udacity.karthikeyan.baker;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.udacity.karthikeyan.baker.UI.RecipeMainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class NetworkDataFetchTest {

    @Rule
    public ActivityTestRule<RecipeMainActivity> recipeMainActivityTestRule = new ActivityTestRule<>(RecipeMainActivity.class);

    private IdlingResource mIdlingResource;
    private static IdlingRegistry mIdlingRegistry = IdlingRegistry.getInstance();

    @Before
    public void registerIdlingResource() {
        mIdlingResource = recipeMainActivityTestRule.getActivity().getIdlingResource();
        mIdlingRegistry.register(mIdlingResource);
    }

    @Test
    public void checkText_RecipeActivity() {
        onView(withId(R.id.rv_recipe_main)).perform(RecyclerViewActions.scrollToPosition(1));
        onView(withText("Brownies")).check(matches(isDisplayed()));
    }

    @Test
    public void checkpreviousStepIsVisible_RecipeDetailActivity() {
        onView(ViewMatchers.withId(R.id.rv_recipe_main)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(ViewMatchers.withId(R.id.recipedetail_list)).perform(RecyclerViewActions.actionOnItemAtPosition(1,click()));
        onView(withId(R.id.previousStep)).check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResource(){
        if(mIdlingResource != null)
        {
            mIdlingRegistry.unregister(mIdlingResource);
        }
    }




}
