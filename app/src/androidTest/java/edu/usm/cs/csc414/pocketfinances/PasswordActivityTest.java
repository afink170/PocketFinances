package edu.usm.cs.csc414.pocketfinances;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.FrameLayout;
import android.widget.ImageView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class PasswordActivityTest {

    private PasswordActivity activity;
    private FrameLayout fragmentHolder;
    private ImageView background;

    @Rule
    public ActivityTestRule<PasswordActivity> rule  = new  ActivityTestRule<>(PasswordActivity.class);


    @Before
    public void setUp() {
        activity = rule.getActivity();

        fragmentHolder = activity.findViewById(R.id.activity_password_framelayout);
        background = activity.findViewById(R.id.activity_password_background);
    }

    @Test
    public void testPreConditions() {
        assertNotNull(fragmentHolder);
        assertNotNull(background);
    }
}
