package com.example.myapplication.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.Models.Post;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    Context myContext;
    List<Post> myData;

    public PostAdapter(Context myContext, List<Post> myData) {
        this.myContext = myContext;
        this.myData = myData;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(myContext).inflate(R.layout.row_posts, parent, false);

        return new PostViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        String picUrl = myData.get(position).getUserPhoto();
        if(!picUrl.equals("")) {
            Picasso.get().load(picUrl).into(holder.userImg);
        }
        holder.titleTv.setText(myData.get(position).getTitle());
        holder.bodyTv.setText(myData.get(position).getBody());
        holder.usernameTv.setText(myData.get(position).getUsername());
        holder.dateTimeTv.setText(timeStampToString((long) myData.get(position).getPostedDateTime()));
        holder.likeCountTv.setText(myData.get(position).getLikeCount() + "");
    }

    @Override
    public int getItemCount() {
        return myData.size();
    }

    private String timeStampToString(long timeStamp) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(timeStamp);
        String date = DateFormat.format("MMM dd, yyyy | hh:mm:ss", calendar).toString();
        return date;
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {

        TextView titleTv, bodyTv, usernameTv, dateTimeTv, likeCountTv;
        ImageView userImg;

        public PostViewHolder(View itemView) {
            super(itemView);

            titleTv = itemView.findViewById(R.id.post_title);
            bodyTv = itemView.findViewById(R.id.post_body);
            usernameTv = itemView.findViewById(R.id.post_username);
            dateTimeTv = itemView.findViewById(R.id.post_date_time);
            likeCountTv = itemView.findViewById(R.id.post_like_count);
            userImg = itemView.findViewById(R.id.post_user_img);
        }
    }
}
