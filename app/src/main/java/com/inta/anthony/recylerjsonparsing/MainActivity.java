package com.inta.anthony.recylerjsonparsing;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<AndroidVersion> data;
    private DataAdapter adapter;
    private Button viewPagerButton;
    private Button switchLayoutButton;
    private Button reverseLayoutButton;

    private ViewPager mViewPager;

    public static Intent newIntent(Context packageContext){
        Intent intent = new Intent(packageContext, MainActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        Timber.plant(new Timber.DebugTree() {
            // Add the line number to the tag
            @Override
            protected String createStackElementTag(StackTraceElement element) {
                return super.createStackElementTag(element) + ':' + element.getLineNumber();
            }
        });
    }

    private void initViews(){
        recyclerView = (RecyclerView) findViewById(R.id.card_recyler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        //Initialize switchLayoutbutton
        switchLayoutButton = (Button) findViewById(R.id.switch_layout_button);
        switchLayoutButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                // Switch layout orientations

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if(linearLayoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                } else {
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                }
            }
        });

        //Initialize reverseLayoutButton
        reverseLayoutButton = (Button) findViewById(R.id.reverse_layout_button);
        reverseLayoutButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                // Switch layout orientations
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if(linearLayoutManager.getReverseLayout()) {
                    linearLayoutManager.setReverseLayout(false);
                } else {
                    linearLayoutManager.setReverseLayout(true);
                }
            }
        });

        //Initialize viewPagerbutton
        viewPagerButton = (Button) findViewById(R.id.view_pager_button);
        viewPagerButton.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent viewPagerIntent = TabbedActivity.newIntent(getApplicationContext());
                    startActivity(viewPagerIntent);
                }
        });

        loadJSON();
    }

    private void loadJSON() {
        RequestInterface request = Api.getRestAdapter().create(RequestInterface.class);
        Call<JSONResponse> call = request.getJSON();
        call.enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                JSONResponse jsonResponse = response.body();
                data = new ArrayList<>(Arrays.asList(jsonResponse.getAndroid()));
                adapter = new DataAdapter(data);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }
}
