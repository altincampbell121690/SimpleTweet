package com.codepath.apps.restclienttemplate.controller

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import com.codepath.apps.restclienttemplate.R
import com.codepath.apps.restclienttemplate.adapters.TweetsAdapter
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.apps.restclienttemplate.services.TwitterApp
import com.codepath.apps.restclienttemplate.services.TwitterClient
import com.codepath.apps.restclienttemplate.utils.MAX_TWEET_LENGTH
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import kotlinx.android.synthetic.main.activity_compose.*
import okhttp3.Headers
import org.json.JSONException
import org.parceler.Parcels
import java.lang.Exception

class ComposeActivity : AppCompatActivity() {
    lateinit var client : TwitterClient
    val TAG = "Compose Activity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)
        etComposeContainer.counterMaxLength = MAX_TWEET_LENGTH
        client = TwitterApp.getRestClient(this)


        btnSentTweet.setOnClickListener(object : OnClickListener{
            override fun onClick(v: View?) {
               val tweetContent = etCompose.text.toString()
                Log.i(TAG, "tweet: $tweetContent")
                if(tweetContent.isEmpty()){
                    Toast.makeText(applicationContext, "Tweets shouldn't be empty",Toast.LENGTH_SHORT).show()
                    return
                }
                if(tweetContent.length >  etComposeContainer.counterMaxLength){
                    Toast.makeText(applicationContext, "Sorry your tweet is too big",Toast.LENGTH_SHORT).show()
                    return
                }
                client.publishTweet(tweetContent, object : JsonHttpResponseHandler() {
                    override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON?) {
                        Log.i(TAG, "onSuccess to Publish Tweet")
                        try {
                            val tweet = Tweet.fromJson(json?.jsonObject)
                            Log.i(TAG, "tweet published: ${tweet.body}")
                            etCompose.text.clear()
                            val returnIntent = Intent()
                            returnIntent.putExtra("tweet", Parcels.wrap(tweet))
                            //set resulst code and budle data to be returned to parent activity
                            setResult(RESULT_OK, returnIntent)
                            // closes the actvity, pass data to parent
                            finish()
                            Log.i(TAG,"COMPLETE")
                        }catch (execption:JSONException){
                            execption.printStackTrace()
                        }

                    }

                    override fun onFailure(statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?) {
                        Log.e(TAG, "onFailure to publish tweet",throwable)
                    }

                })
            }

        })
    }
}
