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
import com.aashreys.walls.domain.display.images.Image;
import com.aashreys.walls.ui.adapters.StreamAdapter;
import com.aashreys.walls.ui.adapters.StreamViewPagerAdapter;
import com.aashreys.walls.ui.helpers.UiHelper;
import com.aashreys.walls.ui.views.StreamImageView;

public class StreamActivity extends BaseActivity<StreamActivityViewModel> implements
        NavigationView.OnNavigationItemSelectedListener, StreamActivityViewModel.EventListener {

    private static final String TAG = StreamActivity.class.getSimpleName();

    private static final String ARG_TAB_POSITION = "arg_tab_position";

    private DrawerLayout drawerLayout;

    private StreamViewPagerAdapter viewPagerAdapter;

    private ViewPager viewPager;

    public static Intent createLaunchIntent(Context context, int tabPosition) {
        Intent intent = new Intent(context, StreamActivity.class);
        intent.putExtra(ARG_TAB_POSITION, tabPosition);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUiComponent().inject(this);
        getViewModel().setEventListener(this);
        getViewModel().onActivityCreated();
        if (getViewModel().shouldOnboardingBeDisplayed()) {
            startOnboardingAndFinish();
        } else {
            setContentView(R.layout.activity_stream);
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
            NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
            navigationView.setNavigationItemSelectedListener(this);

            viewPager = (ViewPager) findViewById(R.id.viewpager);
            TabLayout tabs = (TabLayout) findViewById(R.id.tablayout);
            tabs.setupWithViewPager(viewPager);
            viewPagerAdapter = new StreamViewPagerAdapter(
                    getSupportFragmentManager(),
                    getViewModel()
            );
            viewPager.setAdapter(viewPagerAdapter);

            ImageButton menuButton = (ImageButton) findViewById(R.id.button_menu);
            menuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getViewModel().onMenuButtonClicked();
                }
            });

            ImageButton addCollectionsButton = (ImageButton) findViewById(R.id.button_add);
            addCollectionsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getViewModel().onAddCollectionButtonClicked();
                }
            });
            handleIntent(getIntent());
        }
    }

    private void startOnboardingAndFinish() {
        startActivity(new Intent(this, OnboardingActivity.class));
        finish();
    }

    @Override
    protected StreamActivityViewModel createViewModel() {
        StreamActivityViewModel vm = new StreamActivityViewModel();
        getUiComponent().inject(vm);
        return vm;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getViewModel().onActivityDestroyed();
    }

    @Override
    public void onImageClicked(Image image) {
        startActivity(ImageDetailActivity.createLaunchIntent(this, image));
    }

    @Override
    public void onMenuButtonClicked() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onAddCollectionsButtonClicked() {
        startActivity(AddCollectionsActivity.createLaunchIntent(StreamActivity.this, true));
    }

    @Override
    public void onCollectionsModified() {
        viewPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onImageFavoriteButtonClicked(final Image image, boolean isFavorite) {
        Snackbar snackbar = Snackbar.make(
                viewPager,
                R.string.title_snackbar_favorite_removed,
                Snackbar.LENGTH_LONG
        );
        snackbar.setActionTextColor(UiHelper.getColor(this, R.color.white));
        snackbar.setAction(R.string.action_undo, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getViewModel().onFavoriteRemovedUndoButtonClicked(image);
            }
        });

        snackbar.show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (getViewModel().shouldOnboardingBeDisplayed()) {
            startOnboardingAndFinish();
        } else {
            handleIntent(intent);
        }
    }

    private void handleIntent(Intent intent) {
        int tabPosition = intent.getIntExtra(ARG_TAB_POSITION, 0);
        int totalTabs = viewPager != null ? viewPager.getAdapter().getCount() : 0;
        getViewModel().onReceiveTabPositionFromIntent(tabPosition, totalTabs);
    }

    private void openCollectionsActivity() {
        startActivity(new Intent(this, CollectionsActivity.class));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return getViewModel().onNavigationItemSelected(item.getItemId());
    }

    private void openSettingsActivity() {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    @Override
    public void onSettingsNavigationItemSelected() {
        drawerLayout.closeDrawers();
        openSettingsActivity();
    }

    @Override
    public void onCollectionsNavigationItemSelected() {
        drawerLayout.closeDrawers();
        openCollectionsActivity();
    }

    @Override
    public void onNewTabSelected(int tabPosition) {
        viewPager.setCurrentItem(tabPosition);
    }

    public StreamImageView.InteractionCallback getImageInteractionCallback() {
        return getViewModel();
    }

    public StreamAdapter.CollectionProvider getCollectionProvider() {
        return getViewModel();
    }

}
