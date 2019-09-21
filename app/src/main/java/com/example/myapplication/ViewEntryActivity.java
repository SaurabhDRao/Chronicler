package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Models.Post;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewEntryActivity extends AppCompatActivity {

    TextView titleInput, dateTimeInput, bodyInput;
    Button shareBtn;
    ProgressBar progressBar;
    FirebaseAuth myAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_entry);

        myAuth = FirebaseAuth.getInstance();
        currentUser = myAuth.getCurrentUser();

        titleInput = findViewById(R.id.view_entry_title);
        dateTimeInput = findViewById(R.id.view_entry_date_time);
        bodyInput = findViewById(R.id.view_entry_body);
        shareBtn = findViewById(R.id.share_to_dashboard);
        progressBar = findViewById(R.id.share_progressBar);

        String title = getIntent().getExtras().getString("title");
        String body = getIntent().getExtras().getString("body");
        String dateTime = getIntent().getExtras().getString("dateTime");

        titleInput.setText(title);
        dateTimeInput.setText(dateTime);
        bodyInput.setText(body);

        if(getIntent().getExtras().getString("shared").equals("1"))
            shareBtn.setVisibility(View.INVISIBLE);

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareBtn.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                if(currentUser != null) {
                    String picUrl = "";
                    if(currentUser.getPhotoUrl() != null)
                        picUrl = currentUser.getPhotoUrl().toString();

                    Post post = new Post(
                            currentUser.getUid(),
                            titleInput.getText().toString(),
                            bodyInput.getText().toString(),
                            picUrl,
                            currentUser.getDisplayName(),
                            0
                    );

                    addPost(post);
                } else {
                    shareBtn.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    showMessage("You need to login to share in Dashboard");
                }
            }
        });
    }

    private void addPost(Post post) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference("Posts").push();

        String key = dbRef.getKey();
        post.setPostKey(key);

        dbRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("Journal entry shared to Dashboard!");
                shareBtn.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                MainActivity.myDatabase.journalDao().updateShared(1);
            }
        });

    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
