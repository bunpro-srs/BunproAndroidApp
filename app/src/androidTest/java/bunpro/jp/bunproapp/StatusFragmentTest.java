package bunpro.jp.bunproapp;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.web.webdriver.Locator;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import bunpro.jp.bunproapp.ui.home.HomeActivity;

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

public class StatusFragmentTest {
    @Rule
    public ActivityTestRule<HomeActivity> activityRule = new ActivityTestRule<>(HomeActivity.class);

    @Before
    public void setup() {
//      No need to setup the fragment because it is the default one
//        activityRule.getActivity().getSupportFragmentManager().beginTransaction().add(R.id.main_container, new StatusFragment()).commit();
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
