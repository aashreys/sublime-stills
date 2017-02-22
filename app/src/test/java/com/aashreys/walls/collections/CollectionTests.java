package com.aashreys.walls.collections;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.aashreys.walls.BaseTestCase;
import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.domain.display.collections.CollectionFactory;
import com.aashreys.walls.domain.display.collections.CollectionValidator;
import com.aashreys.walls.domain.display.collections.CollectionValidatorImpl;
import com.aashreys.walls.domain.display.collections.FavoriteCollection;
import com.aashreys.walls.domain.display.collections.UnsplashCollection;
import com.aashreys.walls.domain.display.collections.UnsplashRecentCollection;
import com.aashreys.walls.domain.display.sources.Source;
import com.aashreys.walls.domain.values.Name;
import com.aashreys.walls.domain.values.ServerId;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static junit.framework.Assert.assertEquals;

/**
 * Created by aashreys on 10/02/17.
 */

public class CollectionTests extends BaseTestCase {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    @SuppressWarnings("WrongConstant")
    public void test_collections_factory_return_type() {
        CollectionFactory factory = new CollectionFactory();

        Collection favoriteCollection = factory.create(Collection.Type.FAVORITE, null, null);
        assertEquals(favoriteCollection instanceof FavoriteCollection, true);

        Collection unsplashRecentCollection = factory.create(
                Collection.Type.UNSPLASH_RECENT,
                null,
                null
        );
        assertEquals(unsplashRecentCollection instanceof UnsplashRecentCollection, true);

        String nameString = "Spacemonkeys!";
        String serverIdString = "42";

        Collection unsplashCollection = factory.create(
                Collection.Type.UNSPLASH_COLLECTION,
                new ServerId(serverIdString),
                new Name(nameString)
        );
        assertEquals(unsplashCollection instanceof UnsplashCollection, true);
        testCommonCollection(
                unsplashCollection,
                nameString,
                serverIdString,
                true,
                UnsplashCollection.class
        );

        exception.expect(IllegalArgumentException.class);
        factory.create("Clearly not an expected collection type", null, null);
    }

    @Test
    public void test_collection_validator_logic() {
        Collection validCollection = new Collection() {
            @NonNull
            @Override
            public ServerId id() {
                return new ServerId("2");
            }

            @NonNull
            @Override
            public Name name() {
                return new Name("Simonella");
            }

            @Override
            public boolean isRemovable() {
                return false;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {

            }
        };
        Collection invalidCollection = new Collection() {
            @NonNull
            @Override
            public ServerId id() {
                return null;
            }

            @NonNull
            @Override
            public Name name() {
                return null;
            }

            @Override
            public boolean isRemovable() {
                return false;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {

            }
        };

        CollectionValidator collectionValidator = new CollectionValidatorImpl();

        assertEquals(collectionValidator.isValid(validCollection), true);
        assertEquals(collectionValidator.isValid(invalidCollection), false);

    }

    @Test
    public void test_favorites_collection() {
        testCommonCollection(
                new FavoriteCollection(),
                "Favorites",
                "1",
                false,
                FavoriteCollection.class
        );
    }

    @Test
    public void test_unsplash_recent_collection() {
        testCommonCollection(
                new UnsplashRecentCollection(),
                "Discover",
                "1",
                false,
                UnsplashRecentCollection.class
        );
    }

    @Test
    public void test_unsplash_collection() {
        String nameString = "Spacemagic!";
        String serverIdString = "32";
        Name name = new Name(nameString);
        ServerId serverId = new ServerId(serverIdString);
        Collection unsplashCollection = new UnsplashCollection(serverId, name);
        testCommonCollection(
                unsplashCollection,
                nameString,
                serverIdString,
                true,
                UnsplashCollection.class
        );
    }

    /** Common tests for all collections */
    static <F extends Collection, T extends Source> void testCommonCollection(
            Collection collection,
            String nameString,
            String serverId,
            boolean isRemovable,
            Class<F> collectionClass
    ) {
        assertEquals(collection.name().value(), nameString);
        assertEquals(collection.id().value(), serverId);
        assertEquals(collection.isRemovable(), isRemovable);
        assertEquals(collection.getClass().isAssignableFrom(collectionClass), true);
    }

    @Test
    public void test_equals_and_hashcode() {

    }

}
