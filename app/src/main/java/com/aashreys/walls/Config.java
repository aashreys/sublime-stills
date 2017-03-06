package com.aashreys.walls;

/**
 * Created by aashreys on 01/02/17.
 */

public interface Config {

    interface Unsplash {

        String API_VERSION = "v1";

        int ITEMS_PER_PAGE = 30;
    }

    interface Flickr {

        int ITEMS_PER_PAGE = 250;
    }


}
