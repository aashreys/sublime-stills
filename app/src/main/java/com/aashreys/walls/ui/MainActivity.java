package com.aashreys.walls.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
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
import com.aashreys.walls.ui.helpers.StartupHelper;

import javax.inject.Inject;

import dagger.Lazy;

public class MainActivity extends BaseActivity implements ImageStreamFragment
        .ImageSelectedCallback, ImageStreamFragment.CollectionProvider {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Inject CollectionRepository collectionRepository;

    @Inject Lazy<CollectionFactory> collectionFactoryLazy;

    @Inject ImageCache imageCache;

    private ImageStreamViewPagerAdapter viewPagerAdapter;

    @Inject StartupHelper startupHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActivityComponent().inject(this);

        if (startupHelper.isFirstStart()) {
            populateDatabases();
            startupHelper.onFirstStartCompleted();
        }

        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView
                .OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(
                    @NonNull MenuItem item
            ) {
                drawerLayout.closeDrawers();
                if (item.getItemId() == R.id.menu_item_collections) {
                    openCollectionsActivity();
                    return true;
                }
                return false;
            }
        });


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

        ImageButton collectionsButton = (ImageButton) findViewById(R.id.button_collections);
        collectionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCollectionsActivity();
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

    @Override
    public Collection getCollection(int position) {
        return viewPagerAdapter.getCollection(position);
    }

    private void openCollectionsActivity() {
        Intent intent = new Intent(MainActivity.this, CollectionsActivity.class);
        startActivity(intent);
    }
}
