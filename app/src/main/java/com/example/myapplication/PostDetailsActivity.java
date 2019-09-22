package com.example.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Adapters.CommentAdapter;
import com.example.myapplication.Models.Comment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class PostDetailsActivity extends AppCompatActivity {

    TextView titleTv, bodyTv, usernameTv, dateTimeTv, likeCountTv;
    ImageView userImg, currentUserImg;
    ImageButton likeBtn;
    EditText commentInput;
    Button addComentBtn;
    RecyclerView rvComment;

    FirebaseDatabase firebaseDatabase;

    CommentAdapter commentAdapter;
    List<Comment> commentList;

    boolean liked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        firebaseDatabase = FirebaseDatabase.getInstance();

        titleTv = findViewById(R.id.post_title_details);
        bodyTv = findViewById(R.id.post_body_details);
        usernameTv = findViewById(R.id.post_user_name_details);
        dateTimeTv = findViewById(R.id.post_date_time_details);
        likeCountTv = findViewById(R.id.post_like_count_details);
        userImg = findViewById(R.id.post_user_img_details);
        likeBtn = findViewById(R.id.post_details_like_img);
        currentUserImg = findViewById(R.id.post_details_current_user_img);
        commentInput = findViewById(R.id.post_details_write_comment);
        addComentBtn = findViewById(R.id.post_details_add_comment_btn);
        rvComment = findViewById(R.id.rv_comment);

        commentInput.clearFocus();

        liked = getIntent().getExtras().getBoolean("liked");
        Log.wtf("LIKED", liked + "");

        if(liked) {
            likeBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_thumb_up_blue_24dp));
        }

        final String picUrl = getIntent().getExtras().getString("userImg");
        if(!picUrl.equals("")) {
            Picasso.get().load(picUrl).into(userImg);
        }

        final String currentUserPicUrl = getIntent().getExtras().getString("currentUserImg");
        if(!currentUserPicUrl.equals("")) {
            Picasso.get().load(currentUserPicUrl).into(currentUserImg);
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

        addComentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String postKey = getIntent().getExtras().getString("postKey");
                DatabaseReference commentRef = firebaseDatabase.getReference("Comments").child(postKey).push();
                String commentContent = commentInput.getText().toString();
                String uid = getIntent().getExtras().getString("currentUserId");
                String uname = getIntent().getExtras().getString("currentUserName");
                String uimg = getIntent().getExtras().getString("currentUserImg");

                Comment comment = new Comment(uid, uname, uimg, commentContent);

                commentRef.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showMessage("Comment posted successfully!");
                        commentInput.setText("");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage("Failed to add comment!\n" + e.getMessage());
                    }
                });
            }
        });

        initRVComment();
    }

    private void initRVComment() {

        rvComment.setLayoutManager(new LinearLayoutManager(this));
        rvComment.setNestedScrollingEnabled(false);

        String postKey = getIntent().getExtras().getString("postKey");

        DatabaseReference commentRef = firebaseDatabase.getReference("Comments").child(postKey);
        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentList = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Comment comment = snapshot.getValue(Comment.class);
                    commentList.add(0, comment);
                }

                commentAdapter = new CommentAdapter(getApplicationContext(), commentList);
                rvComment.setAdapter(commentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void showMessage(String message) {
        Toast.makeText(PostDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
