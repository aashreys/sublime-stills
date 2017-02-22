package com.aashreys.walls.persistence.shorturl;

import com.aashreys.walls.domain.values.Url;
import com.aashreys.walls.persistence.WallsDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by aashreys on 03/12/16.
 */

@Table(database = WallsDatabase.class)
public class ShortUrlModel extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    String longUrl;

    @Column
    String shortUrl;

    ShortUrlModel() {}

    ShortUrlModel(Url longUrl, Url shortUrl) {
        this.shortUrl = shortUrl.value();
        this.longUrl = longUrl.value();
    }

}
