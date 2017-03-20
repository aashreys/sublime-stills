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

package com.aashreys.walls.responses;

/**
 * Created by aashreys on 12/02/17.
 */

public interface UnsplashSourceResponse {

    String INVALID_RESPONSE = "tests if malformed JSON throws the expected exception";

    // Contains 2 images, one with valid and one with invalid data.
    String VALID_RESPONSE =
            "[\n" +
                    "  {\n" +
                    "    \"id\": \"7j3SK5tI7Pw\",\n" +
                    "    \"created_at\": \"2017-02-16T10:54:11-05:00\",\n" +
                    "    \"width\": 3632,\n" +
                    "    \"height\": 5113,\n" +
                    "    \"color\": \"#3F3529\",\n" +
                    "    \"likes\": 8,\n" +
                    "    \"liked_by_user\": false,\n" +
                    "    \"user\": {\n" +
                    "      \"id\": \"Db1vO_F3uog\",\n" +
                    "      \"username\": \"keap\",\n" +
                    "      \"name\": \"Keap\",\n" +
                    "      \"first_name\": \"Keap\",\n" +
                    "      \"last_name\": null,\n" +
                    "      \"portfolio_url\": \"https://www.keapbk.com/\",\n" +
                    "      \"bio\": \"Candles made better.\\r\\nPure, sustainable, simple\",\n" +
                    "      \"location\": \"Brooklyn\",\n" +
                    "      \"total_likes\": 0,\n" +
                    "      \"total_photos\": 3,\n" +
                    "      \"total_collections\": 0,\n" +
                    "      \"profile_image\": {\n" +
                    "        \"small\": \"https://images.unsplash.com/profile-1487260308515-67205289d8a5?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=32&w=32&s=739d8b547b2da6192c9638e06be0e362\",\n" +
                    "        \"medium\": \"https://images.unsplash.com/profile-1487260308515-67205289d8a5?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=64&w=64&s=e8082799279fc295f1809fbc6f569bf7\",\n" +
                    "        \"large\": \"https://images.unsplash.com/profile-1487260308515-67205289d8a5?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=128&w=128&s=9b2438faaac7ed261250548dfe59ccf4\"\n" +
                    "      },\n" +
                    "      \"links\": {\n" +
                    "        \"self\": \"https://api.unsplash.com/users/keap\",\n" +
                    "        \"html\": \"http://unsplash.com/@keap\",\n" +
                    "        \"photos\": \"https://api.unsplash.com/users/keap/photos\",\n" +
                    "        \"likes\": \"https://api.unsplash.com/users/keap/likes\",\n" +
                    "        \"portfolio\": \"https://api.unsplash.com/users/keap/portfolio\",\n" +
                    "        \"following\": \"https://api.unsplash.com/users/keap/following\",\n" +
                    "        \"followers\": \"https://api.unsplash.com/users/keap/followers\"\n" +
                    "      }\n" +
                    "    },\n" +
                    "    \"current_user_collections\": [],\n" +
                    "    \"urls\": {\n" +
                    "      \"raw\": \"https://images.unsplash.com/photo-1487260324984-beef041e54ab\",\n" +
                    "      \"full\": \"https://images.unsplash.com/photo-1487260324984-beef041e54ab?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&s=13cb6816b03a6c1b0deae7ffb8691cf3\",\n" +
                    "      \"regular\": \"https://images.unsplash.com/photo-1487260324984-beef041e54ab?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&s=766bfd9c91667371df15460ecaedd3a9\",\n" +
                    "      \"small\": \"https://images.unsplash.com/photo-1487260324984-beef041e54ab?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max&s=de78ad845ef3ac9705864c1c336cdd4e\",\n" +
                    "      \"thumb\": \"https://images.unsplash.com/photo-1487260324984-beef041e54ab?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max&s=5d1f7963f8dd032d56e0f9c292a38d32\"\n" +
                    "    },\n" +
                    "    \"categories\": [],\n" +
                    "    \"links\": {\n" +
                    "      \"self\": \"https://api.unsplash.com/photos/7j3SK5tI7Pw\",\n" +
                    "      \"html\": \"http://unsplash.com/photos/7j3SK5tI7Pw\",\n" +
                    "      \"download\": \"http://unsplash.com/photos/7j3SK5tI7Pw/download\",\n" +
                    "      \"download_location\": \"https://api.unsplash.com/photos/7j3SK5tI7Pw/download\"\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"id\": \"\",\n" +
                    "    \"created_at\": \"\",\n" +
                    "    \"width\": 3228,\n" +
                    "    \"height\": 4912,\n" +
                    "    \"color\": \"#1E2427\",\n" +
                    "    \"likes\": 15,\n" +
                    "    \"liked_by_user\": false,\n" +
                    "    \"user\": {\n" +
                    "      \"id\": \"NBE_dppnu-0\",\n" +
                    "      \"username\": \"joelfilip\",\n" +
                    "      \"name\": \"Joel Filipe\",\n" +
                    "      \"first_name\": \"Joel\",\n" +
                    "      \"last_name\": \"Filipe\",\n" +
                    "      \"portfolio_url\": \"HTTP://joelfilipe.com\",\n" +
                    "      \"bio\": \"I am driven by a hunger for pixels, interfaces and beautiful web-based experiences. It would be awesome to see where my photos are being used. Show me: joelfilip@gmail.com Instagram: @joelfilip\",\n" +
                    "      \"location\": \"Madrid\",\n" +
                    "      \"total_likes\": 102,\n" +
                    "      \"total_photos\": 53,\n" +
                    "      \"total_collections\": 5,\n" +
                    "      \"profile_image\": {\n" +
                    "        \"small\": \"https://images.unsplash.com/profile-1486986704908-49d160f92a3f?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=32&w=32&s=a69b6ee63a795061bc1715eb69bf2b91\",\n" +
                    "        \"medium\": \"https://images.unsplash.com/profile-1486986704908-49d160f92a3f?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=64&w=64&s=8993ad6dc4347fad53f7bb07cf897021\",\n" +
                    "        \"large\": \"https://images.unsplash.com/profile-1486986704908-49d160f92a3f?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=128&w=128&s=ab9f63180f2b7670eeeb1372209f02ab\"\n" +
                    "      },\n" +
                    "      \"links\": {\n" +
                    "        \"self\": \"https://api.unsplash.com/users/joelfilip\",\n" +
                    "        \"html\": \"http://unsplash.com/@joelfilip\",\n" +
                    "        \"photos\": \"https://api.unsplash.com/users/joelfilip/photos\",\n" +
                    "        \"likes\": \"https://api.unsplash.com/users/joelfilip/likes\",\n" +
                    "        \"portfolio\": \"https://api.unsplash.com/users/joelfilip/portfolio\",\n" +
                    "        \"following\": \"https://api.unsplash.com/users/joelfilip/following\",\n" +
                    "        \"followers\": \"https://api.unsplash.com/users/joelfilip/followers\"\n" +
                    "      }\n" +
                    "    },\n" +
                    "    \"current_user_collections\": [],\n" +
                    "    \"urls\": {\n" +
                    "      \"raw\": \"https://images.unsplash.com/photo-1487246675088-81eae6c46eba\",\n" +
                    "      \"full\": \"https://images.unsplash.com/photo-1487246675088-81eae6c46eba?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&s=85601bf6d42daf85b659e74ac8863e6e\",\n" +
                    "      \"regular\": \"https://images.unsplash.com/photo-1487246675088-81eae6c46eba?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&s=f947b307319e3780a8acedff18626a05\",\n" +
                    "      \"small\": \"https://images.unsplash.com/photo-1487246675088-81eae6c46eba?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max&s=91cfb77b071fcf646c858c367324fcfc\",\n" +
                    "      \"thumb\": \"https://images.unsplash.com/photo-1487246675088-81eae6c46eba?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max&s=766c4d54d2a3691e1823b27646510521\"\n" +
                    "    },\n" +
                    "    \"categories\": [],\n" +
                    "    \"links\": {\n" +
                    "      \"self\": \"https://api.unsplash.com/photos/JK7RP-YtSac\",\n" +
                    "      \"html\": \"http://unsplash.com/photos/JK7RP-YtSac\",\n" +
                    "      \"download\": \"http://unsplash.com/photos/JK7RP-YtSac/download\",\n" +
                    "      \"download_location\": \"https://api.unsplash.com/photos/JK7RP-YtSac/download\"\n" +
                    "    }\n" +
                    "  }\n" +
                    "]";

}