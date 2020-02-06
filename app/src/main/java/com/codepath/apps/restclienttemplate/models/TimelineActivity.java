package com.codepath.apps.restclienttemplate.models;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.adapters.TweetsAdapter;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {
    TwitterClient client;
    RecyclerView rvTweets;
    List<Tweet> tweets;
    TweetsAdapter tweetAdapter;
    SwipeRefreshLayout swipeContainer;
    private String TAG = "TIME LINE ACTIVITY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timelin);
        ActionBar actionBar = getSupportActionBar(); // or getActionBar();
        assert actionBar != null;
        //actionBar.setIcon(R.drawable.ic_launcher_twitter);
       // getSupportActionBar().setLogo(R.drawable.ic_launcher_twitter);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.ic_launcher_twitter_round);
        actionBar.setDisplayUseLogoEnabled(true);

        client = TwitterApp.getRestClient(this);
        /* STEPS
        * // find the recycler view
        * //init th elist of tweets and adapter
        * recylcer view setup: layout manager and the adapter
        * */

        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        // find the recycler view
        rvTweets = findViewById(R.id.rvTweets);
        tweets = new ArrayList<>();
        tweetAdapter = new TweetsAdapter(this,tweets);
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        rvTweets.setAdapter(tweetAdapter);
        populateHomeTimeLine();


        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "fetching New Data");
                populateHomeTimeLine();
            }
        });

    }

    private void populateHomeTimeLine(){
       client.getHomeTimeline(new JsonHttpResponseHandler() { // JsonHTTP RESPONSEHANDLER IS IMPORTANT
           @Override
           public void onSuccess(int statusCode, Headers headers, JSON json) {
               Log.i(TAG,"onSuccess\n" + json.toString());
               JSONArray jsonArray = json.jsonArray;
               try{
                   tweetAdapter.clear();
                   tweetAdapter.addAll(Tweet.fromJsonArray(jsonArray));
                   // Now we call setRefreshing(false) to signal refresh has finished
                   swipeContainer.setRefreshing(false);
//                   tweets.addAll(Tweet.fromJsonArray(jsonArray));
//                   tweetAdapter.notifyDataSetChanged();
               } catch (JSONException e) {
                   Log.e(TAG, "Json Exception", e);
                   e.printStackTrace();
               }

           }

           @Override
           public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG,"onFailure", throwable);
           }
       });
    }

}
