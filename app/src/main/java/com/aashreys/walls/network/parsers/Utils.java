package com.aashreys.walls.network.parsers;

/**
 * Created by aashreys on 18/03/17.
 */

public class Utils {

    public static String removeFlickrResponseBrackets(String flickrResponse) {
        return flickrResponse.substring(
                flickrResponse.indexOf("{"),
                flickrResponse.lastIndexOf("}") + 1
        );
    }

}
