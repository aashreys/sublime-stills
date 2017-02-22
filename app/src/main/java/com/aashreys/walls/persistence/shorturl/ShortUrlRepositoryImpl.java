package com.aashreys.walls.persistence.shorturl;


import com.aashreys.walls.domain.values.Url;
import com.raizlabs.android.dbflow.sql.language.SQLite;

/**
 * Created by aashreys on 04/12/16.
 */

public class ShortUrlRepositoryImpl implements ShortUrlRepository {

    public ShortUrlRepositoryImpl() {}

    @Override
    public Url get(Url longUrl) {
        ShortUrlModel model = SQLite.select()
                .from(ShortUrlModel.class)
                .where(ShortUrlModel_Table.longUrl.eq(longUrl.value()))
                .querySingle();
        if (model != null) {
            Url url = new Url(model.shortUrl);
            if (url.isValid()) {
                return url;
            }
        }
        return null;
    }

    @Override
    public void save(Url longUrl, Url shortUrl) {
        new ShortUrlModel(longUrl, shortUrl).save();
    }
}
