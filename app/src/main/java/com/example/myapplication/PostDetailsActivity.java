package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PostDetailsActivity extends AppCompatActivity {

    TextView titleTv, bodyTv, usernameTv, dateTimeTv, likeCountTv;
    ImageView userImg;

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

        String picUrl = getIntent().getExtras().getString("userImg");
        if(!picUrl.equals("")) {
            Picasso.get().load(picUrl).into(userImg);
        }

        titleTv.setText(getIntent().getExtras().getString("title"));
        bodyTv.setText(getIntent().getExtras().getString("body"));
        usernameTv.setText(getIntent().getExtras().getString("username"));
        dateTimeTv.setText(getIntent().getExtras().getString("dateTime"));
        likeCountTv.setText(getIntent().getExtras().getString("likeCount"));
    }
}
