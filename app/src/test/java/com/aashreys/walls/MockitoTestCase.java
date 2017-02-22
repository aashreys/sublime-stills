package com.aashreys.walls;

import org.mockito.MockitoAnnotations;

/**
 * Created by aashreys on 10/02/17.
 */

public class MockitoTestCase extends BaseTestCase {

    @Override
    public void init() {
        super.init();
        MockitoAnnotations.initMocks(this);
    }

}
