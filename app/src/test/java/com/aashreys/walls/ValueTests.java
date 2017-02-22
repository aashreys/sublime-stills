package com.aashreys.walls;

import com.aashreys.walls.domain.values.LocalId;
import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.domain.values.Pixel;
import com.aashreys.walls.domain.values.ServerId;
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
        LocalId localId = new LocalId((long) -32);
        Name name = new Name("");
        Name name2 = new Name("null");
        Pixel pixel = new Pixel(-2);
        ServerId serverId = new ServerId("");
        Url url = new Url("not a url, duh!");

        assertFalse(localId.isValid());
        assertFalse(name.isValid());
        assertFalse(name2.isValid());
        assertFalse(pixel.isValid());
        assertFalse(serverId.isValid());
        assertFalse(url.isValid());
    }

    @Test
    public void test_null_constructor_args() {
        LocalId localId = new LocalId(null);
        Name name = new Name(null);
        Pixel pixel = new Pixel(null);
        ServerId serverId = new ServerId(null);
        Url url = new Url(null);

        assertFalse(localId.isValid());
        assertFalse(name.isValid());
        assertFalse(pixel.isValid());
        assertFalse(serverId.isValid());
        assertFalse(url.isValid());
    }

    @Test
    public void test_valid_constructor_args() {
        Long localIdInput = 23L;
        String nameInput = "Ash Ketchum";
        Integer pixelInput = 2;
        String serverIdInput = "23424";
        String urlInput = "https://somewebsite.com";


        LocalId localId = new LocalId(localIdInput);
        Name name = new Name(nameInput);
        Pixel pixel = new Pixel(pixelInput);
        ServerId serverId = new ServerId(serverIdInput);
        Url url = new Url(urlInput);

        assertTrue(localId.isValid());
        assertTrue(name.isValid());
        assertTrue(pixel.isValid());
        assertTrue(serverId.isValid());
        assertTrue(url.isValid());

        assertEquals(localId.value(), localIdInput);
        assertEquals(name.value(), nameInput);
        assertEquals(pixel.value(), pixelInput);
        assertEquals(serverId.value(), serverIdInput);
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
