/*
 * Copyright {2017} {Aashrey Kamal Sharma}
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.aashreys.walls;

import android.support.annotation.CallSuper;

import com.aashreys.walls.utils.LogWrapper;

import org.junit.Before;

import java.io.InputStream;
import java.util.Scanner;

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

    protected final String readFile(String filepath) {
        InputStream inputStream = ClassLoader.getSystemResourceAsStream(filepath);
        Scanner s = new Scanner(inputStream).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";
        return result;
    }

}
