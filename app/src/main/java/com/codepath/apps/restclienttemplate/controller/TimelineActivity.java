package com.codepath.apps.restclienttemplate.controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.services.TwitterApp;
import com.codepath.apps.restclienttemplate.services.TwitterClient;
import com.codepath.apps.restclienttemplate.adapters.TweetsAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {
    TwitterClient client;
    RecyclerView rvTweets;
    List<Tweet> tweets;
    TweetsAdapter tweetAdapter;
    SwipeRefreshLayout swipeContainer;
    int REQUEST_CODE = 100;
    private String TAG = "TIME LINE ACTIVITY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            //GET data from the intent (tweet)
            Tweet tweet = Parcels.unwrap(data.getParcelableExtra("tweet"));
            //update the RV with tweet
            //modify data source of tweets
            tweets.add(0,tweet);
            // update the adapter
            tweetAdapter.notifyItemInserted(0);
            rvTweets.smoothScrollToPosition(0);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onComposeTweet(View view) {
//        Toast.makeText(this, "IM CLICKED", Toast.LENGTH_SHORT).show();
        Intent composeIntent = new Intent(this, ComposeActivity.class);
       // startActivity(intent);
        startActivityForResult(composeIntent,REQUEST_CODE );

    }
}
