package com.aashreys.walls.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ImageButton;

import com.aashreys.walls.R;
import com.aashreys.walls.WallsApplication;
import com.aashreys.walls.di.UiComponent;
import com.aashreys.walls.domain.display.collections.Collection;
import com.aashreys.walls.domain.display.collections.CollectionFactory;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.domain.display.images.utils.ImageCache;
import com.aashreys.walls.persistence.collections.CollectionRepository;
import com.aashreys.walls.ui.adapters.ImageStreamViewPagerAdapter;

import javax.inject.Inject;

import dagger.Lazy;

public class MainActivity extends BaseActivity implements ImageStreamFragment
        .ImageSelectedCallback, ImageStreamFragment.CollectionProvider {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String KEY_IS_FIRST_START = "key_is_first_start";

    @Inject CollectionRepository collectionRepository;

    @Inject Lazy<CollectionFactory> collectionFactoryLazy;

    @Inject ImageCache imageCache;

    private ImageStreamViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActivityComponent().inject(this);
        SharedPreferences preferences = getSharedPreferences();
        if (preferences.getBoolean(KEY_IS_FIRST_START, true)) {
            populateDatabases();
            preferences.edit().putBoolean(KEY_IS_FIRST_START, false).apply();
        }
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabs = (TabLayout) findViewById(R.id.tablayout);
        tabs.setupWithViewPager(viewPager);
        viewPagerAdapter = new ImageStreamViewPagerAdapter(
                getSupportFragmentManager(),
                collectionRepository
        );
        viewPager.setAdapter(viewPagerAdapter);
        if (viewPager.getAdapter().getCount() > 1) {
            tabs.setVisibility(View.VISIBLE);
        }

        ImageButton menuButton = (ImageButton) findViewById(R.id.button_menu);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        ImageButton imageSourcesButton = (ImageButton) findViewById(R.id.button_image_sources);
        imageSourcesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CollectionsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewPagerAdapter.release();
    }

    @Override
    public void onImageSelected(Image image) {
        imageCache.add(image);
        startActivity(ImageDetailActivity.createLaunchIntent(this, image));
    }

    private void populateDatabases() {
        CollectionFactory collectionFactory = collectionFactoryLazy.get();
        collectionRepository.insert(collectionFactory.create(Collection.Type.DISCOVER, null, null));
        collectionRepository.insert(collectionFactory.create(Collection.Type.FAVORITE, null, null));
    }

    private UiComponent getActivityComponent() {
        return ((WallsApplication) getApplication()).getApplicationComponent()
                .getUiComponent();
    }

    private SharedPreferences getSharedPreferences() {
        return ((WallsApplication) getApplication()).getSharedPreferences();
    }

    @Override
    public Collection getCollection(int position) {
        return viewPagerAdapter.getCollection(position);
    }
}
