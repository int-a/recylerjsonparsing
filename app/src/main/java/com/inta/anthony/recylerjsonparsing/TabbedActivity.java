package com.inta.anthony.recylerjsonparsing;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import timber.log.Timber;

public class TabbedActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter sectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager viewPager;

    public static Intent newIntent(Context packageContext){
        Intent intent = new Intent(packageContext, TabbedActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tabbed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public static class RecyclerFragment extends Fragment {

        private static final String ARG_LAYOUT_TYPE = "layout-type";

        private int layoutType = 0;

        RecyclerView recyclerView;
        public ArrayList<AndroidVersion> data;

        public static final int FRAGMENT_LINEAR = 0;
        public static final int FRAGMENT_GRID = 1;
        public static final int FRAGMENT_STAG_GRID = 2;

        public static RecyclerFragment newInstance(int layoutType) {
            Timber.d("newInstance() called with int: " + layoutType);
            RecyclerFragment fragment = new RecyclerFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_LAYOUT_TYPE, layoutType);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);

            if(getArguments() != null){
                layoutType = getArguments().getInt(ARG_LAYOUT_TYPE);
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
            View rootView = inflater.inflate(R.layout.fragment_recycler, container, false);

            Button button = (Button) rootView.findViewById(R.id.view_pager_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mainActivityIntent = MainActivity.newIntent(getActivity().getApplicationContext());
                    startActivity(mainActivityIntent);
                }
            });

            recyclerView = (RecyclerView) rootView.findViewById(R.id.card_recyler_view_pager);
            recyclerView.setHasFixedSize(true);

            Timber.d("LayoutType: " + layoutType);

            // Check to see what the layout type should be and create the necessary LayoutManager
            switch(layoutType){
                case FRAGMENT_LINEAR:
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    break;
                case FRAGMENT_GRID:
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    break;
                case FRAGMENT_STAG_GRID:
                    StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(staggeredGridLayoutManager);
                    break;
            }

            loadJSON();

            return rootView;
        }

        private void loadJSON() {
            Retrofit retrofit = Api.getRestAdapter();
            RequestInterface request = retrofit.create(RequestInterface.class);
            Call<JSONResponse> call = request.getJSON();
            call.enqueue(new Callback<JSONResponse>() {
                @Override
                public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                    JSONResponse jsonResponse = response.body();
                    data = new ArrayList<>(Arrays.asList(jsonResponse.getAndroid()));
                    DataAdapter adapter = new DataAdapter(data);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onFailure(Call<JSONResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                }
            });
        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            return RecyclerFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Linear";
                case 1:
                    return "Grid";
                case 2:
                    return "Staggered";
            }
            return null;
        }
    }
}
