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

package com.aashreys.walls.persistence.shorturl;

import android.support.annotation.Nullable;

import com.aashreys.walls.domain.values.Url;

/**
 * Created by aashreys on 04/12/16.
 */

public interface ShortUrlRepository {

    @Nullable
    Url get(@Nullable Url longUrl);

    void save(Url longUrl, Url shortUrl);

}
