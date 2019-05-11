package bunpro.jp.bunproapp;

import android.view.View;
import android.widget.TextView;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import bunpro.jp.bunproapp.ui.home.HomeActivity;
import bunpro.jp.bunproapp.ui.login.LoginPresenter;
import bunpro.jp.bunproapp.ui.settings.SettingFragment;
import bunpro.jp.bunproapp.utils.EspressoTestingIdlingResource;
import bunpro.jp.bunproapp.utils.SimpleCallbackListener;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class SettingFragmentTest {
    @Rule
    public ActivityTestRule<HomeActivity> activityRule = new ActivityTestRule<>(HomeActivity.class);

    @Before
    public void login() {
        IdlingRegistry.getInstance().register(EspressoTestingIdlingResource.getIdlingResource("login_and_loading"));
        IdlingRegistry.getInstance().register(EspressoTestingIdlingResource.getIdlingResource("logout"));
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
        // Going to setting fragment
        activityRule.getActivity().replaceFragment(new SettingFragment());
    }

    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoTestingIdlingResource.getIdlingResource("login_and_loading"));
        IdlingRegistry.getInstance().unregister(EspressoTestingIdlingResource.getIdlingResource("logout"));
    }

    @Test
    public void testSectionsVisibility() {
        onView(withId(R.id.rlFurigana)).perform(scrollTo()).check(matches(isDisplayed()));
        onView(withId(R.id.rlHideEnglish)).perform(scrollTo()).check(matches(isDisplayed()));
        onView(withId(R.id.rlBunnyMode)).perform(scrollTo()).check(matches(isDisplayed()));
        onView(withId(R.id.rlSubscription)).perform(scrollTo()).check(matches(isDisplayed()));
        onView(withId(R.id.rlCommunity)).perform(scrollTo()).check(matches(isDisplayed()));
        onView(withId(R.id.rlAbout)).perform(scrollTo()).check(matches(isDisplayed()));
        onView(withId(R.id.rlContact)).perform(scrollTo()).check(matches(isDisplayed()));
        onView(withId(R.id.rlPrivacy)).perform(scrollTo()).check(matches(isDisplayed()));
        onView(withId(R.id.rlTerms)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void testToggleFurigana() {
        onView(withId(R.id.rlFurigana)).perform(scrollTo()).check(matches(isDisplayed()));
        String furiganaText = getTextFromMatcher(withId(R.id.tvFurigana));
        // Hitting cancel
        onView(withId(R.id.rlFurigana)).perform(ViewActions.click());
        onView(withId(R.id.furigana_layout)).check(matches(isDisplayed()));
        onView(allOf(withText(R.string.cancel), isDescendantOfA(withId(R.id.furigana_layout)))).perform(ViewActions.click());
        onView(withId(R.id.tvFurigana)).check(matches(withText(furiganaText)));
        // Toggling to always
        onView(withId(R.id.rlFurigana)).perform(ViewActions.click());
        onView(allOf(withText(R.string.always), isDescendantOfA(withId(R.id.furigana_layout)))).perform(ViewActions.click());
        onView(withId(R.id.tvFurigana)).check(matches(withText(R.string.always)));
        // Toggling to never
        onView(withId(R.id.rlFurigana)).perform(ViewActions.click());
        onView(allOf(withText(R.string.never), isDescendantOfA(withId(R.id.furigana_layout)))).perform(ViewActions.click());
        onView(withId(R.id.tvFurigana)).check(matches(withText(R.string.never)));
        // Toggling to wanikani
        onView(withId(R.id.rlFurigana)).perform(ViewActions.click());
        onView(allOf(withText(R.string.wanikani), isDescendantOfA(withId(R.id.furigana_layout)))).perform(ViewActions.click());
        onView(withId(R.id.tvFurigana)).check(matches(withText(R.string.wanikani)));
        // Resetting to default
        onView(withId(R.id.rlFurigana)).perform(ViewActions.click());
        onView(allOf(withText(furiganaText), isDescendantOfA(withId(R.id.furigana_layout)))).perform(ViewActions.click());
    }

    @Test
    public void testHideEnglish() {
        onView(withId(R.id.rlHideEnglish)).perform(scrollTo()).check(matches(isDisplayed()));
        String hideEnglishText = getTextFromMatcher(withId(R.id.tvHideEnglish));
        // Hitting cancel
        onView(withId(R.id.rlHideEnglish)).perform(ViewActions.click());
        onView(withId(R.id.hide_english_layout)).check(matches(isDisplayed()));
        onView(allOf(withText(R.string.cancel), isDescendantOfA(withId(R.id.hide_english_layout)))).perform(ViewActions.click());
        onView(withId(R.id.tvHideEnglish)).check(matches(withText(hideEnglishText)));
        // Toggling to yes
        onView(withId(R.id.rlHideEnglish)).perform(ViewActions.click());
        onView(allOf(withText(R.string.yes), isDescendantOfA(withId(R.id.hide_english_layout)))).perform(ViewActions.click());
        onView(withId(R.id.tvHideEnglish)).check(matches(withText(R.string.yes)));
        // Toggling to no
        onView(withId(R.id.rlHideEnglish)).perform(ViewActions.click());
        onView(allOf(withText(R.string.no), isDescendantOfA(withId(R.id.hide_english_layout)))).perform(ViewActions.click());
        onView(withId(R.id.tvHideEnglish)).check(matches(withText(R.string.no)));
        // Resetting to default
        onView(withId(R.id.rlHideEnglish)).perform(ViewActions.click());
        onView(allOf(withText(hideEnglishText), isDescendantOfA(withId(R.id.hide_english_layout)))).perform(ViewActions.click());
    }

    @Test
    public void testBunnyMode() {
        onView(withId(R.id.rlBunnyMode)).perform(scrollTo()).check(matches(isDisplayed()));
        String bunnyModeText = getTextFromMatcher(withId(R.id.tvBunnyMode));
        // Hitting cancel
        onView(withId(R.id.rlBunnyMode)).perform(ViewActions.click());
        onView(withId(R.id.bunny_mode_layout)).check(matches(isDisplayed()));
        onView(allOf(withText(R.string.cancel), isDescendantOfA(withId(R.id.bunny_mode_layout)))).perform(ViewActions.click());
        onView(withId(R.id.tvBunnyMode)).check(matches(withText(bunnyModeText)));
        // Toggling to yes
        onView(withId(R.id.rlBunnyMode)).perform(ViewActions.click());
        onView(allOf(withText(R.string.on), isDescendantOfA(withId(R.id.bunny_mode_layout)))).perform(ViewActions.click());
        onView(withId(R.id.tvBunnyMode)).check(matches(withText(R.string.on)));
        // Toggling to no
        onView(withId(R.id.rlBunnyMode)).perform(ViewActions.click());
        onView(allOf(withText(R.string.off), isDescendantOfA(withId(R.id.bunny_mode_layout)))).perform(ViewActions.click());
        onView(withId(R.id.tvBunnyMode)).check(matches(withText(R.string.off)));
        // Resetting to default
        onView(withId(R.id.rlBunnyMode)).perform(ViewActions.click());
        onView(allOf(withText(bunnyModeText), isDescendantOfA(withId(R.id.bunny_mode_layout)))).perform(ViewActions.click());
    }

    @Test
    public void testCommunityLink() {
        onView(withText(R.string.community)).perform(scrollTo(), ViewActions.click());
        onView(withId(R.id.settings_fragment)).check(doesNotExist());
    }

    @Test
    public void testAboutLink() {
        onView(withText(R.string.about)).perform(scrollTo(), ViewActions.click());
        onView(withId(R.id.settings_fragment)).check(doesNotExist());
    }

    @Test
    public void testContactLink() {
        onView(withText(R.string.contact)).perform(scrollTo(), ViewActions.click());
        onView(withId(R.id.settings_fragment)).check(doesNotExist());
    }

    @Test
    public void testPrivacyLink() {
        onView(withText(R.string.privacy)).perform(scrollTo(), ViewActions.click());
        onView(withId(R.id.settings_fragment)).check(doesNotExist());
    }

    @Test
    public void testTermsLink() {
        onView(withText(R.string.terms_and_conditions)).perform(scrollTo(), ViewActions.click());
        onView(withId(R.id.settings_fragment)).check(doesNotExist());
    }

    @Test
    public void testLogout() {
        onView(withId(R.id.rlLogout)).perform(scrollTo()).check(matches(isDisplayed()));
        // Hitting cancel
        onView(withText(R.string.logout)).perform(ViewActions.click());
        onView(withId(R.id.logout_layout)).check(matches(isDisplayed()));
        onView(allOf(withText(R.string.cancel), isDescendantOfA(withId(R.id.logout_layout)))).perform(ViewActions.click());
        onView(withId(R.id.settings_fragment)).check(matches(isDisplayed()));
        // Executing logout
        onView(withText(R.string.logout)).perform(ViewActions.click());
        onView(allOf(withText(R.string.logout), isDescendantOfA(withId(R.id.logout_layout)))).perform(ViewActions.click());
        onView(withId(R.id.settings_fragment)).check(doesNotExist());
        onView(withId(R.id.login_layout)).check(matches(isDisplayed()));
    }

    String getTextFromMatcher(final Matcher<View> matcher) {
        final String[] stringHolder = { null };
        onView(matcher).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "getting text from a TextView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView tv = (TextView)view; //Save, because of check in getConstraints()
                stringHolder[0] = tv.getText().toString();
            }
        });
        return stringHolder[0];
    }
}
