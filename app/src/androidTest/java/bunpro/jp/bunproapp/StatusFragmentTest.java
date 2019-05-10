package bunpro.jp.bunproapp;

import androidx.fragment.app.Fragment;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.web.webdriver.Locator;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import bunpro.jp.bunproapp.ui.home.HomeActivity;
import bunpro.jp.bunproapp.ui.status.StatusFragment;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.web.assertion.WebViewAssertions.webMatches;
import static androidx.test.espresso.web.sugar.Web.onWebView;
import static androidx.test.espresso.web.webdriver.DriverAtoms.findElement;
import static androidx.test.espresso.web.webdriver.DriverAtoms.getText;
import static org.hamcrest.CoreMatchers.containsString;

@RunWith(AndroidJUnit4.class)
public class StatusFragmentTest {
    private Fragment statusFragment;

    @Rule
    public ActivityTestRule<HomeActivity> activityRule = new ActivityTestRule<>(HomeActivity.class);

    @Before
    public void setup() {
        Fragment fragment = new Fragment();
        statusFragment = new StatusFragment();
        activityRule.getActivity().getSupportFragmentManager().beginTransaction().add(R.id.main_container, fragment).commit();
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
        onWebView().withElement(findElement(Locator.CLASS_NAME, "cram-start")).check(webMatches(getText(), containsString("N5")));
        onWebView().withElement(findElement(Locator.CLASS_NAME, "cram-start")).check(webMatches(getText(), containsString("N4")));
        onWebView().withElement(findElement(Locator.CLASS_NAME, "cram-start")).check(webMatches(getText(), containsString("N3")));
        onWebView().withElement(findElement(Locator.CLASS_NAME, "cram-start")).check(webMatches(getText(), containsString("N2")));
    }

    @Test
    public void testStudyLink() {
        onView(withText(R.string.study)).perform(ViewActions.click());
        onView(withId(R.id.status_fragment)).check(doesNotExist());
        onWebView().withElement(findElement(Locator.CLASS_NAME, "grammar-point")).check(webMatches(getText(), containsString("Meaning")));
        onWebView().withElement(findElement(Locator.CLASS_NAME, "grammar-point")).check(webMatches(getText(), containsString("Examples")));
        onWebView().withElement(findElement(Locator.CLASS_NAME, "grammar-point")).check(webMatches(getText(), containsString("Readings")));
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
