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
            mod = (int) (num - (BASE_COUNT * div));
            result = ALPHABET[mod] + result;
            num = div;
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
