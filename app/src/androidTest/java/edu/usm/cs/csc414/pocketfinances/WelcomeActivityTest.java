package edu.usm.cs.csc414.pocketfinances;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class WelcomeActivityTest {

    private WelcomeActivity activity;
    private FrameLayout fragmentHolder;
    private TextView nextButton;
    private ImageView background;

    @Rule
    public ActivityTestRule<WelcomeActivity> rule  = new  ActivityTestRule<>(WelcomeActivity.class);


    @Before
    public void setUp() {
        activity = rule.getActivity();

        fragmentHolder = activity.findViewById(R.id.activity_welcome_framelayout);
        background = activity.findViewById(R.id.activity_welcome_background);
        nextButton = activity.findViewById(R.id.activity_welcome_next_textview);

    }

    @Test
    public void testPreConditions() {
        assertNotNull(fragmentHolder);
        assertNotNull(background);
        assertNotNull(nextButton);
    }
}
