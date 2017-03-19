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
import android.support.v7.widget.Toolbar;
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
import com.aashreys.walls.persistence.favoriteimage.FavoriteImageRepository;
import com.aashreys.walls.ui.adapters.ImageStreamViewPagerAdapter;
import com.aashreys.walls.ui.helpers.StartupHelper;
import com.aashreys.walls.ui.helpers.UiHelper;
import com.aashreys.walls.ui.views.StreamImageView;

import javax.inject.Inject;

import dagger.Lazy;

public class StreamActivity extends BaseActivity implements StreamImageView.ImageSelectedCallback,
        ImageStreamFragment.CollectionProvider, NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = StreamActivity.class.getSimpleName();

    private static final String ARG_TAB_POSITION = "arg_tab_position";

    @Inject CollectionRepository collectionRepository;

    @Inject Lazy<CollectionFactory> collectionFactoryLazy;

    @Inject ImageCache imageCache;

    @Inject StartupHelper startupHelper;

    @Inject
    FavoriteImageRepository favoriteImageRepository;

    private DrawerLayout drawerLayout;

    private ImageStreamViewPagerAdapter viewPagerAdapter;

    private ViewPager viewPager;

    private Toolbar toolbar;

    public static Intent createLaunchIntent(Context context, int tabPosition) {
        Intent intent = new Intent(context, StreamActivity.class);
        intent.putExtra(ARG_TAB_POSITION, tabPosition);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);
        getActivityComponent().inject(this);

        if (startupHelper.isFirstStart()) {
            populateDatabases();
            startupHelper.onFirstStartCompleted();
        }

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
        handleIntent(getIntent());
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
        if (viewPager.getAdapter().getCount() > 0) {
            viewPager.setCurrentItem(position);
        }
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
}
