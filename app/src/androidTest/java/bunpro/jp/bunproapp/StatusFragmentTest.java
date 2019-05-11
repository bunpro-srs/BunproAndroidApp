package bunpro.jp.bunproapp;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import bunpro.jp.bunproapp.ui.home.HomeActivity;
import bunpro.jp.bunproapp.ui.login.LoginPresenter;
import bunpro.jp.bunproapp.utils.EspressoTestingIdlingResource;
import bunpro.jp.bunproapp.utils.SimpleCallbackListener;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class StatusFragmentTest {
    @Rule
    public ActivityTestRule<HomeActivity> activityRule = new ActivityTestRule<>(HomeActivity.class);

    @Before
    public void login() {
        IdlingRegistry.getInstance().register(EspressoTestingIdlingResource.getIdlingResource("login_and_loading"));
        // Getting username and password from environment variable
        String username = BuildConfig.test_bunpro_login;
        String password = BuildConfig.test_bunpro_password;
        // Executing login
        LoginPresenter loginPresenter = new LoginPresenter(activityRule.getActivity());
        loginPresenter.login(username, password, new SimpleCallbackListener() {
            @Override
            public void success() {
                EspressoTestingIdlingResource.decrement("login_and_loading");
            }
            @Override
            public void error(String errorMessage) {
                EspressoTestingIdlingResource.decrement("login_and_loading");
            }
        });
    }

    @Test
    public void testSectionsVisibility() {
        onView(withId(R.id.cram)).check(matches(isDisplayed()));
        onView(withId(R.id.study)).check(matches(isDisplayed()));
        onView(withId(R.id.llReview)).check(matches(isDisplayed()));
    }

    @Test
    public void testCramLink() {
        onView(withText(R.string.cram)).perform(ViewActions.click());
        onView(withId(R.id.status_fragment)).check(doesNotExist());
    }

    @Test
    public void testStudyLink() {
        onView(withText(R.string.study)).perform(ViewActions.click());
        onView(withId(R.id.status_fragment)).check(doesNotExist());
    }

    @Test
    public void testReviewLink() {
        onView(withId(R.id.tvReviews)).perform(ViewActions.click());
        onView(withId(R.id.status_fragment)).check(doesNotExist());
    }

    @Test
    public void testStatusTab() {
        onView(withId(R.id.action_status)).perform(ViewActions.click());
        onView(withId(R.id.search_fragment)).check(doesNotExist());
        onView(withId(R.id.settings_fragment)).check(doesNotExist());
        onView(withId(R.id.status_fragment)).check(matches(isDisplayed()));
    }

    @Test
    public void testSearchTab() {
        onView(withId(R.id.action_search)).perform(ViewActions.click());
        onView(withId(R.id.status_fragment)).check(doesNotExist());
        onView(withId(R.id.settings_fragment)).check(doesNotExist());
        onView(withId(R.id.search_fragment)).check(matches(isDisplayed()));
    }

    @Test
    public void testSettingsTab() {
        onView(withId(R.id.action_settings)).perform(ViewActions.click());
        onView(withId(R.id.status_fragment)).check(doesNotExist());
        onView(withId(R.id.search_fragment)).check(doesNotExist());
        onView(withId(R.id.settings_fragment)).check(matches(isDisplayed()));
    }
}
