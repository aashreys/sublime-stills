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

package com.aashreys.walls.collections;

import com.aashreys.walls.BaseTestCase;
import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.domain.display.collections.Collection.Type;
import com.aashreys.walls.domain.display.collections.CollectionFactory;
import com.aashreys.walls.domain.display.collections.DiscoverCollection;
import com.aashreys.walls.domain.display.collections.FavoriteCollection;
import com.aashreys.walls.domain.display.collections.UnsplashCollection;
import com.aashreys.walls.domain.values.Id;
import com.aashreys.walls.domain.values.Name;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by aashreys on 10/02/17.
 */

public class CollectionTests extends BaseTestCase {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    /** Common tests for all collections */
    static <T extends Collection> void testCollection(
            Collection collection,
            String id,
            String name,
            String type,
            boolean isRemovable,
            Class<T> collectionClass
    ) {
        assertEquals(collection.getName().value(), name);
        assertEquals(collection.getId().value(), id);
        assertTrue(collection.getType() != null);
        assertEquals(collection.getType(), type);
        assertEquals(collection.isRemovable(), isRemovable);
        assertEquals(collection.getClass().isAssignableFrom(collectionClass), true);
    }

    @Test
    @SuppressWarnings("WrongConstant")
    public void test_collections() {
        String idString = "42";
        String nameString = "Spacemonkeys!";
        Id id = new Id(idString);
        Name name = new Name(nameString);

        CollectionFactory factory = new CollectionFactory();
        Collection unsplashCollection = factory.create(Type.UNSPLASH_COLLECTION, id, name);
        Collection discoverCollection = factory.create(Type.DISCOVER, null, null);
        Collection favoriteCollection = factory.create(Type.FAVORITE, null, null);


        assertTrue(unsplashCollection instanceof UnsplashCollection);
        assertTrue(discoverCollection instanceof DiscoverCollection);
        assertTrue(favoriteCollection instanceof FavoriteCollection);

        testCollection(
                unsplashCollection,
                idString,
                nameString,
                Type.UNSPLASH_COLLECTION,
                true,
                UnsplashCollection.class
        );

        testCollection(
                discoverCollection,
                "1",
                "Discover",
                Type.DISCOVER,
                false,
                DiscoverCollection.class
        );

        testCollection(
                favoriteCollection,
                "1",
                "Favorites",
                Type.FAVORITE,
                false,
                FavoriteCollection.class
        );

        exception.expect(IllegalArgumentException.class);
        factory.create("Clearly not an expected collection type string", null, null);
    }

    @Test
    public void test_equals_and_hashcode() {

    }

}
