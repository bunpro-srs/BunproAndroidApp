package bunpro.jp.bunproapp;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import bunpro.jp.bunproapp.ui.login.LoginActivity;
import bunpro.jp.bunproapp.utils.EspressoTestingIdlingResource;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    @Rule
    public ActivityTestRule activityRule = new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoTestingIdlingResource.getIdlingResource("login_and_loading"));
    }

    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoTestingIdlingResource.getIdlingResource("login_and_loading"));
    }

    @Test
    public void testFieldsAndButtonVisibility() {
        // Checking that fields are displayed
        onView(withId(R.id.etEmail)).check(matches(isDisplayed()));
        onView(withId(R.id.etPassword)).check(matches(isDisplayed()));
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()));
    }

    @Test
    public void testWrongUserCombination() {
        // Entering user/password and try login
        onView(withId(R.id.etEmail)).perform(typeText("wrongUsername@gmail.com"));
        onView(withId(R.id.etPassword)).perform(typeText("wrongPassword"));
        onView(withText(R.string.login)).perform(ViewActions.click());
        // Checking that main activity is not displayed
        onView(withId(R.id.login_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.main_container)).check(doesNotExist());
    }

    @Test
    public void testRightUserCombination() {
        // Getting username and password from environment variable
        String username = BuildConfig.test_bunpro_login;
        String password = BuildConfig.test_bunpro_password;
        // Entering user/password and try login
        onView(withId(R.id.etEmail)).perform(typeText(!username.isEmpty() ? username : "username"));
        onView(withId(R.id.etPassword)).perform(typeText(!password.isEmpty() ? password : "password"));
        onView(withText(R.string.login)).perform(ViewActions.click());
        // Checking that main activity is displayed
        onView(withId(R.id.login_layout)).check(doesNotExist());
        onView(withId(R.id.main_container)).check(matches(isDisplayed()));
    }
}
