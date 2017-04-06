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

package com.aashreys.walls.sharedelegates.mocks;

import com.aashreys.walls.domain.values.Url;
import com.aashreys.walls.network.UrlShortener;

/**
 * Created by aashreys on 06/04/17.
 */

public class MockUrlShortener implements UrlShortener {

    private Url mockShortUrl;

    private UrlShortenerException mockException;

    boolean shouldFail;

    public void setMockException(UrlShortenerException mockException) {
        this.mockException = mockException;
    }

    public void setMockShortUrl(Url mockShortUrl) {
        this.mockShortUrl = mockShortUrl;
    }

    public void setShouldFail(boolean shouldFail) {
        this.shouldFail = shouldFail;
    }

    @Override
    public void shorten(
            Url longUrl, Listener listener
    ) {
        if (shouldFail) {
            listener.onError(mockException);
        } else {
            listener.onComplete(mockShortUrl);
        }
    }
}
