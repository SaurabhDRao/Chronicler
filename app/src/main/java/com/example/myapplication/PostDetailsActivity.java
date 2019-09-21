package com.example.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class PostDetailsActivity extends AppCompatActivity {

    TextView titleTv, bodyTv, usernameTv, dateTimeTv, likeCountTv;
    ImageView userImg;
    ImageButton likeBtn;

    boolean liked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        titleTv = findViewById(R.id.post_title_details);
        bodyTv = findViewById(R.id.post_body_details);
        usernameTv = findViewById(R.id.post_user_name_details);
        dateTimeTv = findViewById(R.id.post_date_time_details);
        likeCountTv = findViewById(R.id.post_like_count_details);
        userImg = findViewById(R.id.post_user_img_details);
        likeBtn = findViewById(R.id.post_details_like_img);

        liked = getIntent().getExtras().getBoolean("liked");
        Log.wtf("LIKED", liked + "");

        if(liked) {
            likeBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_thumb_up_blue_24dp));
        }

        final String picUrl = getIntent().getExtras().getString("userImg");
        if(!picUrl.equals("")) {
            Picasso.get().load(picUrl).into(userImg);
        }

        titleTv.setText(getIntent().getExtras().getString("title"));
        bodyTv.setText(getIntent().getExtras().getString("body"));
        usernameTv.setText(getIntent().getExtras().getString("username"));
        dateTimeTv.setText(getIntent().getExtras().getString("dateTime"));
        likeCountTv.setText(getIntent().getExtras().getString("likeCount"));

        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!liked) {
                    String userId = getIntent().getExtras().getString("currentUserId");
                    final int likeCount = Integer.parseInt(likeCountTv.getText().toString()) + 1;

                    String likedUsersStr = getIntent().getExtras().getString("likedUsers");
                    if(likedUsersStr.length() == 0)
                        likedUsersStr = userId;
                    else
                        likedUsersStr += ";" + userId;

                    String postKey = getIntent().getExtras().getString("postKey");

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = database.getReference("Posts").child(postKey);

                    HashMap<String, Object> postMap = new HashMap<>();
                    postMap.put("title", titleTv.getText().toString());
                    postMap.put("body", bodyTv.getText().toString());
                    postMap.put("likeCount", likeCount);
                    postMap.put("likedUsers", likedUsersStr);
                    postMap.put("postKey", postKey);
                    postMap.put("postedDateTime", getIntent().getExtras().getLong("dateTimeStamp"));
                    postMap.put("userId", getIntent().getExtras().getString("userId"));
                    postMap.put("username", usernameTv.getText().toString());
                    postMap.put("userPhoto", picUrl);

                    databaseReference.updateChildren(postMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                likeCountTv.setText(likeCount + "");

                                likeBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_thumb_up_blue_24dp));
                            }
                        }
                    });
                }
            }
        });
    }
}
