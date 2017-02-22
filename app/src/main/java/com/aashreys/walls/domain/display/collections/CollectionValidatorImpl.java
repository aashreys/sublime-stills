package com.aashreys.walls.domain.display.collections;

/**
 * Created by aashreys on 08/02/17.
 */

public class CollectionValidatorImpl implements CollectionValidator {

    public CollectionValidatorImpl() {}

    @Override
    public boolean isValid(Collection collection) {
        return collection.name() != null && collection.id() != null &&
                collection.name().isValid() && collection.id().isValid();
    }

}
