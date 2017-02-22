package com.aashreys.walls;

import android.support.annotation.CallSuper;

import org.junit.Before;

/**
 * Created by aashreys on 11/02/17.
 */

public class BaseTestCase {

    @Before
    @CallSuper
    public void init() {
        // Disable Android logging so we don't run into mocking errors
        LogWrapper.setEnabled(false);
    }

}
