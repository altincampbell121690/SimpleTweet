package com.codepath.apps.restclienttemplate.adapters

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.codepath.apps.restclienttemplate.R
import com.codepath.apps.restclienttemplate.models.Tweet

// pass in the context and list of tweets
class TweetsAdapter(val context: Context,var tweets:MutableList<Tweet>): RecyclerView.Adapter<TweetsAdapter.TweetViewHolder>() {
    //Define a view Holder
    inner class TweetViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {//Define a view Holder
        val ivProfileImage = itemView.findViewById<ImageView>(R.id.ivProfileImage)
        val tvBody = itemView.findViewById<TextView>(R.id.tvBody)
        val tvScreenName = itemView.findViewById<TextView>(R.id.tvScreenName)


        fun bindTweetView(context: Context, tweet: Tweet){
            tvBody.text = tweet.body
            tvScreenName.text = tweet.user.screenName
           // for image we use GLIDE!!!
            Glide.with(context).load(tweet.user.profileImageUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(ivProfileImage) // this is nice!
        }
    }
    // for each row, inflate the layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetViewHolder {  // for each row, inflate the layout
       val view  = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false)
        return TweetViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tweets.count()
    }
    // bind values based on the postion of the element
    override fun onBindViewHolder(holder: TweetViewHolder, position: Int) {  // bind values based on the postion of the element
        holder.bindTweetView(context,tweets[position])
    }
//clear all items out of the recycler view
    fun clear(){
        tweets.clear()
        notifyDataSetChanged()
    }
// add new items
    fun addAll(tweetList : List<Tweet>){
        tweets.addAll(tweetList)
        notifyDataSetChanged()
    }



}