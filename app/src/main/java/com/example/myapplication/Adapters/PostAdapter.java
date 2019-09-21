package com.example.myapplication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.Models.Post;
import com.example.myapplication.PostDetailsActivity;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    Context myContext;
    List<Post> myData;
    FirebaseUser currentUser;

    public PostAdapter(Context myContext, List<Post> myData) {
        this.myContext = myContext;
        this.myData = myData;
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(myContext).inflate(R.layout.row_posts, parent, false);

        return new PostViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostViewHolder holder, final int position) {
        final String picUrl = myData.get(position).getUserPhoto();
        if(!picUrl.equals("")) {
            Picasso.get().load(picUrl).into(holder.userImg);
        }
        holder.titleTv.setText(myData.get(position).getTitle());
        holder.bodyTv.setText(myData.get(position).getBody());
        holder.usernameTv.setText(myData.get(position).getUsername());
        holder.dateTimeTv.setText(timeStampToString((long) myData.get(position).getPostedDateTime()));

        final int likeCount = myData.get(position).getLikeCount();
        holder.likeCountTv.setText(likeCount + "");

        final String likedUsers[] = myData.get(position).getLikedUsers().split(";");
        for(String likedUser : likedUsers) {
            if(likedUser.equals(currentUser.getUid())) {
                holder.likeImgBtn.setImageDrawable(myContext.getResources().getDrawable(R.drawable.ic_thumb_up_blue_24dp));
                break;
            }
        }

        holder.likeImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean flag = false;
                for(String likedUser : likedUsers) {
                    if(likedUser.equals(currentUser.getUid())) {
                        flag = true;
                        break;
                    }
                }

                if(!flag) {

//                    holder.likeCountTv.setText((likeCount + 1) + "");
                    final Post newPost = myData.get(position);

                    String likedUsersStr = newPost.getLikedUsers();
                    if(likedUsersStr.length() == 0)
                        likedUsersStr = currentUser.getUid();
                    else
                        likedUsersStr += ";" + currentUser.getUid();
                    newPost.setLikedUsers(likedUsersStr);
                    newPost.setLikeCount(likeCount + 1);

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = database.getReference("Posts").child(newPost.getPostKey());

                    HashMap<String, Object> postMap = new HashMap<>();
                    postMap.put("title", newPost.getTitle());
                    postMap.put("body", newPost.getBody());
                    postMap.put("likeCount", newPost.getLikeCount());
                    postMap.put("likedUsers", newPost.getLikedUsers());
                    postMap.put("postKey", newPost.getPostKey());
                    postMap.put("postedDateTime", newPost.getPostedDateTime());
                    postMap.put("userId", newPost.getUserId());
                    postMap.put("username", newPost.getUsername());
                    postMap.put("userPhoto", newPost.getUserPhoto());

                    databaseReference.updateChildren(postMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                myData.set(position, newPost);
                                notifyItemChanged(position);

                                holder.likeImgBtn.setImageDrawable(myContext.getResources().getDrawable(R.drawable.ic_thumb_up_blue_24dp));
                            }
                        }
                    });


                }

            }
        });

        Log.wtf("LIKED_USERS", myData.get(position).getLikedUsers() + " " + myData.get(position).getUserId());
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
        ImageButton likeImgBtn;

        public PostViewHolder(View itemView) {
            super(itemView);

            titleTv = itemView.findViewById(R.id.post_title);
            bodyTv = itemView.findViewById(R.id.post_body);
            usernameTv = itemView.findViewById(R.id.post_username);
            dateTimeTv = itemView.findViewById(R.id.post_date_time);
            likeCountTv = itemView.findViewById(R.id.post_like_count);
            userImg = itemView.findViewById(R.id.post_user_img);
            likeImgBtn = itemView.findViewById(R.id.post_like_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(myContext, PostDetailsActivity.class);

                    int pos = getAdapterPosition();

                    i.putExtra("title", myData.get(pos).getTitle());
                    i.putExtra("body", myData.get(pos).getBody());
                    i.putExtra("postKey", myData.get(pos).getPostKey());
                    i.putExtra("username", myData.get(pos).getUsername());
                    i.putExtra("userId", myData.get(pos).getUserId());
                    i.putExtra("currentUserId", currentUser.getUid());
                    i.putExtra("likeCount", myData.get(pos).getLikeCount() + "");
                    i.putExtra("userImg", myData.get(pos).getUserPhoto());
                    i.putExtra("dateTime", timeStampToString((long) myData.get(pos).getPostedDateTime()));
                    i.putExtra("dateTimeStamp", (long) myData.get(pos).getPostedDateTime());

                    boolean liked = false;
                    String likedUsers[] = myData.get(pos).getLikedUsers().split(";");
                    for(String likedUser : likedUsers) {
                        if(likedUser.equals(currentUser.getUid())) {
                            liked = true;
                            break;
                        }
                    }
                    i.putExtra("liked", liked);
                    i.putExtra("likedUsers", myData.get(pos).getLikedUsers());

                    myContext.startActivity(i);
                }
            });

        }
    }
}
