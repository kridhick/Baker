package com.example.udacity.karthikeyan.baker;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import com.example.udacity.karthikeyan.baker.UI.RecipeDetailListActivity;
import com.example.udacity.karthikeyan.baker.UI.RecipeMainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class IntentCallTest  {

    @Rule
    public IntentsTestRule<RecipeMainActivity> recipeMainIntentsTestRule = new IntentsTestRule<>(RecipeMainActivity.class);

    private IdlingResource mIdlingResource;
    private static IdlingRegistry mIdlingRegistry = IdlingRegistry.getInstance();

    @Before
    public void registerIdlingResource() {
        mIdlingResource = recipeMainIntentsTestRule.getActivity().getIdlingResource();
        mIdlingRegistry.register(mIdlingResource);
    }

    @Before
    public void stubIntending() {
        // By default Espresso Intents does not stub any Intents. Stubbing needs to be setup before
        // every test run.
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void checkIntent_RecipeDetailActivity() {
        onView(ViewMatchers.withId(R.id.rv_recipe_main)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        intended(hasComponent(RecipeDetailListActivity.class.getName()));

    }

    @After
    public void unregisterIdlingResource(){
        if(mIdlingResource != null)
        {
            mIdlingRegistry.unregister(mIdlingResource);
        }
    }


}
