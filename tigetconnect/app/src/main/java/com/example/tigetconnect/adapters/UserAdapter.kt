package com.example.tigetconnect.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tigetconnect.R
import com.example.tigetconnect.model.User
import kotlinx.android.synthetic.main.user_item.view.*
import java.util.zip.Inflater

class UserAdapter(val context: Context, val userList:MutableList<User>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindUserView(user: User){
            itemView.tvName.text = user.name
            itemView.tvTitle.text = user.title
            Glide.with(context)
                .load(user.avatar)
                .into(itemView.ivAvatar)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
      val view = LayoutInflater.from(context).inflate(
           viewType, parent, false
       )
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.count()
    }

    override fun getItemViewType(position: Int): Int {

        return R.layout.user_item
    }
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
           holder.bindUserView(userList[position])
    }
}