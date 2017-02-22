package com.aashreys.walls.samples;

/**
 * Created by aashreys on 12/02/17.
 */

public interface CollectionSearcherSamples {

    // Contains 2 collections, one with valid and one with invalid data.
    String VALID_RESPONSE =
            "{\n" +
                    "  \"total\": 237,\n" +
                    "  \"total_pages\": 12,\n" +
                    "  \"results\": [\n" +
                    "    {\n" +
                    "      \"id\": 193913,\n" +
                    "      \"title\": \"Office\",\n" +
                    "      \"description\": null,\n" +
                    "      \"published_at\": \"2016-04-15T21:05:44-04:00\",\n" +
                    "      \"curated\": false,\n" +
                    "      \"featured\": true,\n" +
                    "      \"total_photos\": 60,\n" +
                    "      \"private\": false,\n" +
                    "      \"share_key\": \"79ec77a237f014935eddc774f6aac1cd\",\n" +
                    "      \"cover_photo\": {\n" +
                    "        \"id\": \"pb_lF8VWaPU\",\n" +
                    "        \"created_at\": \"2015-02-12T18:39:43-05:00\",\n" +
                    "        \"width\": 5760,\n" +
                    "        \"height\": 3840,\n" +
                    "        \"color\": \"#1F1814\",\n" +
                    "        \"likes\": 786,\n" +
                    "        \"liked_by_user\": false,\n" +
                    "        \"user\": {\n" +
                    "          \"id\": \"tkoUSod3di4\",\n" +
                    "          \"username\": \"gilleslambert\",\n" +
                    "          \"name\": \"Gilles Lambert\",\n" +
                    "          \"portfolio_url\": \"http://www.gilleslambert.be/photography\",\n" +
                    "          \"profile_image\": {\n" +
                    "            \"small\": \"https://images.unsplash.com/profile-1445832407811-c04ed64d238b?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=32&w=32&s=4bb8fad0dcba43c46491c6fd0b92f537\",\n" +
                    "            \"medium\": \"https://images.unsplash.com/profile-1445832407811-c04ed64d238b?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=64&w=64&s=a6d8602c855914fe13650eedd5996cb5\",\n" +
                    "            \"large\": \"https://images.unsplash.com/profile-1445832407811-c04ed64d238b?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=128&w=128&s=26099ca5069692aac6973d08ae02dd71\"\n" +
                    "          },\n" +
                    "          \"links\": {\n" +
                    "            \"self\": \"https://api.unsplash.com/users/gilleslambert\",\n" +
                    "            \"html\": \"http://unsplash.com/@gilleslambert\",\n" +
                    "            \"photos\": \"https://api.unsplash.com/users/gilleslambert/photos\",\n" +
                    "            \"likes\": \"https://api.unsplash.com/users/gilleslambert/likes\"\n" +
                    "          }\n" +
                    "        },\n" +
                    "        \"urls\": {\n" +
                    "          \"raw\": \"https://images.unsplash.com/photo-1423784346385-c1d4dac9893a\",\n" +
                    "          \"full\": \"https://hd.unsplash.com/photo-1423784346385-c1d4dac9893a\",\n" +
                    "          \"regular\": \"https://images.unsplash.com/photo-1423784346385-c1d4dac9893a?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&s=d60d527cb347746ab3abf5fccecf0271\",\n" +
                    "          \"small\": \"https://images.unsplash.com/photo-1423784346385-c1d4dac9893a?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max&s=0bf0c97abca8b2741380f38d3debd45f\",\n" +
                    "          \"thumb\": \"https://images.unsplash.com/photo-1423784346385-c1d4dac9893a?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max&s=9bc3a6d42a16809b735c22720de3fb13\"\n" +
                    "        },\n" +
                    "        \"links\": {\n" +
                    "          \"self\": \"https://api.unsplash.com/photos/pb_lF8VWaPU\",\n" +
                    "          \"html\": \"http://unsplash.com/photos/pb_lF8VWaPU\",\n" +
                    "          \"download\": \"http://unsplash.com/photos/pb_lF8VWaPU/download\"\n" +
                    "        }\n" +
                    "      },\n" +
                    "      \"user\": {\n" +
                    "        \"id\": \"k_gSWNtOjS8\",\n" +
                    "        \"username\": \"cjmconnors\",\n" +
                    "        \"name\": \"Christine Connors\",\n" +
                    "        \"portfolio_url\": null,\n" +
                    "        \"bio\": \"\",\n" +
                    "        \"profile_image\": {\n" +
                    "          \"small\": \"https://images.unsplash.com/placeholder-avatars/extra-large.jpg?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=32&w=32&s=0ad68f44c4725d5a3fda019bab9d3edc\",\n" +
                    "          \"medium\": \"https://images.unsplash.com/placeholder-avatars/extra-large.jpg?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=64&w=64&s=356bd4b76a3d4eb97d63f45b818dd358\",\n" +
                    "          \"large\": \"https://images.unsplash.com/placeholder-avatars/extra-large.jpg?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=128&w=128&s=ee8bbf5fb8d6e43aaaa238feae2fe90d\"\n" +
                    "        },\n" +
                    "        \"links\": {\n" +
                    "          \"self\": \"https://api.unsplash.com/users/cjmconnors\",\n" +
                    "          \"html\": \"http://unsplash.com/@cjmconnors\",\n" +
                    "          \"photos\": \"https://api.unsplash.com/users/cjmconnors/photos\",\n" +
                    "          \"likes\": \"https://api.unsplash.com/users/cjmconnors/likes\"\n" +
                    "        }\n" +
                    "      },\n" +
                    "      \"links\": {\n" +
                    "        \"self\": \"https://api.unsplash.com/collections/193913\",\n" +
                    "        \"html\": \"http://unsplash.com/collections/193913/office\",\n" +
                    "        \"photos\": \"https://api.unsplash.com/collections/193913/photos\",\n" +
                    "        \"related\": \"https://api.unsplash.com/collections/193913/related\"\n" +
                    "      }\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": -3524,\n" +
                    "      \"title\": \"\",\n" +
                    "      \"description\": null,\n" +
                    "      \"published_at\": \"2016-04-15T21:05:44-04:00\",\n" +
                    "      \"curated\": false,\n" +
                    "      \"featured\": true,\n" +
                    "      \"total_photos\": 60,\n" +
                    "      \"private\": false,\n" +
                    "      \"share_key\": \"79ec77a237f014935eddc774f6aac1cd\",\n" +
                    "      \"cover_photo\": {\n" +
                    "        \"id\": \"pb_lF8VWaPU\",\n" +
                    "        \"created_at\": \"2015-02-12T18:39:43-05:00\",\n" +
                    "        \"width\": 5760,\n" +
                    "        \"height\": 3840,\n" +
                    "        \"color\": \"#1F1814\",\n" +
                    "        \"likes\": 786,\n" +
                    "        \"liked_by_user\": false,\n" +
                    "        \"user\": {\n" +
                    "          \"id\": \"tkoUSod3di4\",\n" +
                    "          \"username\": \"gilleslambert\",\n" +
                    "          \"name\": \"Gilles Lambert\",\n" +
                    "          \"portfolio_url\": \"http://www.gilleslambert.be/photography\",\n" +
                    "          \"profile_image\": {\n" +
                    "            \"small\": \"https://images.unsplash.com/profile-1445832407811-c04ed64d238b?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=32&w=32&s=4bb8fad0dcba43c46491c6fd0b92f537\",\n" +
                    "            \"medium\": \"https://images.unsplash.com/profile-1445832407811-c04ed64d238b?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=64&w=64&s=a6d8602c855914fe13650eedd5996cb5\",\n" +
                    "            \"large\": \"https://images.unsplash.com/profile-1445832407811-c04ed64d238b?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=128&w=128&s=26099ca5069692aac6973d08ae02dd71\"\n" +
                    "          },\n" +
                    "          \"links\": {\n" +
                    "            \"self\": \"https://api.unsplash.com/users/gilleslambert\",\n" +
                    "            \"html\": \"http://unsplash.com/@gilleslambert\",\n" +
                    "            \"photos\": \"https://api.unsplash.com/users/gilleslambert/photos\",\n" +
                    "            \"likes\": \"https://api.unsplash.com/users/gilleslambert/likes\"\n" +
                    "          }\n" +
                    "        },\n" +
                    "        \"urls\": {\n" +
                    "          \"raw\": \"https://images.unsplash.com/photo-1423784346385-c1d4dac9893a\",\n" +
                    "          \"full\": \"https://hd.unsplash.com/photo-1423784346385-c1d4dac9893a\",\n" +
                    "          \"regular\": \"https://images.unsplash.com/photo-1423784346385-c1d4dac9893a?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&s=d60d527cb347746ab3abf5fccecf0271\",\n" +
                    "          \"small\": \"https://images.unsplash.com/photo-1423784346385-c1d4dac9893a?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max&s=0bf0c97abca8b2741380f38d3debd45f\",\n" +
                    "          \"thumb\": \"https://images.unsplash.com/photo-1423784346385-c1d4dac9893a?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max&s=9bc3a6d42a16809b735c22720de3fb13\"\n" +
                    "        },\n" +
                    "        \"links\": {\n" +
                    "          \"self\": \"https://api.unsplash.com/photos/pb_lF8VWaPU\",\n" +
                    "          \"html\": \"http://unsplash.com/photos/pb_lF8VWaPU\",\n" +
                    "          \"download\": \"http://unsplash.com/photos/pb_lF8VWaPU/download\"\n" +
                    "        }\n" +
                    "      },\n" +
                    "      \"user\": {\n" +
                    "        \"id\": \"k_gSWNtOjS8\",\n" +
                    "        \"username\": \"cjmconnors\",\n" +
                    "        \"name\": \"Christine Connors\",\n" +
                    "        \"portfolio_url\": null,\n" +
                    "        \"bio\": \"\",\n" +
                    "        \"profile_image\": {\n" +
                    "          \"small\": \"https://images.unsplash.com/placeholder-avatars/extra-large.jpg?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=32&w=32&s=0ad68f44c4725d5a3fda019bab9d3edc\",\n" +
                    "          \"medium\": \"https://images.unsplash.com/placeholder-avatars/extra-large.jpg?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=64&w=64&s=356bd4b76a3d4eb97d63f45b818dd358\",\n" +
                    "          \"large\": \"https://images.unsplash.com/placeholder-avatars/extra-large.jpg?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=128&w=128&s=ee8bbf5fb8d6e43aaaa238feae2fe90d\"\n" +
                    "        },\n" +
                    "        \"links\": {\n" +
                    "          \"self\": \"https://api.unsplash.com/users/cjmconnors\",\n" +
                    "          \"html\": \"http://unsplash.com/@cjmconnors\",\n" +
                    "          \"photos\": \"https://api.unsplash.com/users/cjmconnors/photos\",\n" +
                    "          \"likes\": \"https://api.unsplash.com/users/cjmconnors/likes\"\n" +
                    "        }\n" +
                    "      },\n" +
                    "      \"links\": {\n" +
                    "        \"self\": \"https://api.unsplash.com/collections/193913\",\n" +
                    "        \"html\": \"http://unsplash.com/collections/193913/office\",\n" +
                    "        \"photos\": \"https://api.unsplash.com/collections/193913/photos\",\n" +
                    "        \"related\": \"https://api.unsplash.com/collections/193913/related\"\n" +
                    "      }\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";


}
