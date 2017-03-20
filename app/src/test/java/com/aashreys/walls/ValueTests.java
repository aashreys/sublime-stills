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

import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.domain.values.Pixel;
import com.aashreys.walls.domain.values.Id;
import com.aashreys.walls.domain.values.Url;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by aashreys on 10/02/17.
 */

public class ValueTests extends BaseTestCase {

    @Test
    public void test_invalid_constructor_args() {
        Name name = new Name("");
        Name name2 = new Name("null");
        Pixel pixel = new Pixel(-2);
        Id id = new Id("");
        Url url = new Url("not a url, duh!");

        assertFalse(name.isValid());
        assertFalse(name2.isValid());
        assertFalse(pixel.isValid());
        assertFalse(id.isValid());
        assertFalse(url.isValid());
    }

    @Test
    public void test_null_constructor_args() {
        Name name = new Name(null);
        Pixel pixel = new Pixel(null);
        Id id = new Id(null);
        Url url = new Url(null);

        assertFalse(name.isValid());
        assertFalse(pixel.isValid());
        assertFalse(id.isValid());
        assertFalse(url.isValid());
    }

    @Test
    public void test_valid_constructor_args() {
        String nameInput = "Ash Ketchum";
        Integer pixelInput = 2;
        String serverIdInput = "23424";
        String urlInput = "https://somewebsite.com";


        Name name = new Name(nameInput);
        Pixel pixel = new Pixel(pixelInput);
        Id id = new Id(serverIdInput);
        Url url = new Url(urlInput);

        assertTrue(name.isValid());
        assertTrue(pixel.isValid());
        assertTrue(id.isValid());
        assertTrue(url.isValid());

        assertEquals(name.value(), nameInput);
        assertEquals(pixel.value(), pixelInput);
        assertEquals(id.value(), serverIdInput);
        assertEquals(url.value(), urlInput);
    }

    @Test
    public void test_equals_hashcode() {
        Name name1 = new Name("Master Chief");
        Name name1Again = new Name("Master Chief");
        assertEquals(name1, name1Again);
        assertEquals(name1.hashCode(), name1Again.hashCode());

        Name name2 = new Name("Cortana");
        assertEquals(name1.equals(name2), false);
        assertEquals(name1.hashCode() == name2.hashCode(), false);

        Pixel pixel = new Pixel(42);
        assertEquals(name1.equals(pixel), false);
        assertEquals(name1.hashCode() == pixel.hashCode(), false);
    }
}
