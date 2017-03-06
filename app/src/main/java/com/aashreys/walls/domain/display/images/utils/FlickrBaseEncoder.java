package com.aashreys.walls.domain.display.images.utils;

/**
 * A class to encode number for use with Flickr's short url template.
 * <p>
 * Created by aashreys on 02/03/17.
 */

public class FlickrBaseEncoder {

    private static final String ALPHABET_STRING =
            "123456789abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ";

    private static final char[] ALPHABET = ALPHABET_STRING.toCharArray();

    private static final int BASE_COUNT = ALPHABET.length;

    public static String encode(long num) {
        String result = "";
        long div;
        int mod = 0;

        while (num >= BASE_COUNT) {
            div = num / BASE_COUNT;
            mod = (int) (num - (BASE_COUNT * (long) div));
            result = ALPHABET[mod] + result;
            num = (long) div;
        }
        if (num > 0) {
            result = ALPHABET[(int) num] + result;
        }
        return result;
    }

    public static long decode(String link) {
        long result = 0;
        long multi = 1;
        while (link.length() > 0) {
            String digit = link.substring(link.length() - 1);
            result = result + multi * ALPHABET_STRING.lastIndexOf(digit);
            multi = multi * BASE_COUNT;
            link = link.substring(0, link.length() - 1);
        }
        return result;
    }
}
