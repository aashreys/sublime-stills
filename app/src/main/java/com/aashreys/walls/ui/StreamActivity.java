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

package com.aashreys.walls.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
import com.aashreys.walls.domain.display.collections.DiscoverCollection;
import com.aashreys.walls.domain.display.collections.FavoriteCollection;
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.persistence.KeyValueStore;
import com.aashreys.walls.persistence.collections.CollectionRepository;
import com.aashreys.walls.persistence.favoriteimage.FavoriteImageRepository;
import com.aashreys.walls.ui.adapters.ImageStreamViewPagerAdapter;
import com.aashreys.walls.ui.helpers.StartupHelper;
import com.aashreys.walls.ui.helpers.UiHelper;
import com.aashreys.walls.ui.views.StreamImageView;

import javax.inject.Inject;

public class StreamActivity extends BaseActivity implements StreamImageView.ImageSelectedCallback,
        ImageStreamFragment.CollectionProvider, NavigationView.OnNavigationItemSelectedListener,
        ImageStreamViewPagerAdapter.CollectionsModifiedListener {

    public static final String KEY_IS_ONBOARDING_COMPLETED = "key_is_onboarding_completed";

    private static final String TAG = StreamActivity.class.getSimpleName();

    private static final String ARG_TAB_POSITION = "arg_tab_position";

    private static final String FRAGMENT_TAG_ADD_COLLECTION = "fragment_tag_add_collection";

    @Inject CollectionRepository collectionRepository;

    @Inject CollectionFactory collectionFactory;

    @Inject StartupHelper startupHelper;

    @Inject KeyValueStore keyValueStore;

    @Inject
    FavoriteImageRepository favoriteImageRepository;

    private DrawerLayout drawerLayout;

    private ImageStreamViewPagerAdapter viewPagerAdapter;

    private ViewPager viewPager;

    public static Intent createLaunchIntent(Context context, int tabPosition) {
        Intent intent = new Intent(context, StreamActivity.class);
        intent.putExtra(ARG_TAB_POSITION, tabPosition);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        addDefaultCollectionsIfMissing();
        startOnboardingIfNeeded();
        setContentView(R.layout.activity_stream);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabs = (TabLayout) findViewById(R.id.tablayout);
        tabs.setupWithViewPager(viewPager);
        viewPagerAdapter = new ImageStreamViewPagerAdapter(
                getSupportFragmentManager(),
                collectionRepository
        );
        viewPager.setAdapter(viewPagerAdapter);
        viewPagerAdapter.setCollectionsModifiedListener(this);

        ImageButton menuButton = (ImageButton) findViewById(R.id.button_menu);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        ImageButton collectionsButton = (ImageButton) findViewById(R.id.button_add);
        collectionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(AddCollectionsActivity.createLaunchIntent(StreamActivity.this, true));
            }
        });
        handleIntent(getIntent());
    }

    private void startOnboardingIfNeeded() {
        if (!keyValueStore.getBoolean(KEY_IS_ONBOARDING_COMPLETED, false)) {
            startActivity(new Intent(this, OnboardingActivity.class));
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewPagerAdapter.release();
    }

    @Override
    public void onImageSelected(Image image) {
        startActivity(ImageDetailActivity.createLaunchIntent(this, image));
    }

    @Override
    public void onImageFavorited(Image image) {

    }

    @Override
    public void onImageUnfavorited(final Image image) {
        Snackbar snackbar = Snackbar
                .make(viewPager, R.string.title_snackbar_favorite_removed, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(UiHelper.getColor(this, R.color.white));
        snackbar.setAction(R.string.action_undo, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favoriteImageRepository.favorite(image);
            }
        });

        snackbar.show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        int position = intent.getIntExtra(ARG_TAB_POSITION, 0);
        int itemCount = viewPager.getAdapter().getCount();
        if (itemCount > 0 && position > 0 && position < itemCount) {
            viewPager.setCurrentItem(position);
        }
    }

    private void addDefaultCollectionsIfMissing() {
        DiscoverCollection discoverCollection = (DiscoverCollection) collectionFactory.create(
                Collection.Type.DISCOVER,
                null,
                null
        );
        FavoriteCollection favoriteCollection = (FavoriteCollection) collectionFactory.create(
                Collection.Type.FAVORITE,
                null,
                null
        );
        if (!collectionRepository.exists(discoverCollection)) {
            collectionRepository.insert(discoverCollection);
        }
        if (!collectionRepository.exists(favoriteCollection)) {
            collectionRepository.insert(favoriteCollection);
        }
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
        Intent intent = new Intent(this, CollectionsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawers();
        switch (item.getItemId()) {

            case R.id.menu_item_settings:
                openSettingsActivity();
                return true;

            case R.id.menu_item_collections:
                openCollectionsActivity();
                return true;

        }
        return false;
    }

    private void openSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCollectionAdded(Collection collection, int position) {

    }

    @Override
    public void onCollectionRemoved(Collection collection, int position) {

    }
}
