package com.example.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Models.Post;
import com.example.myapplication.RoomDatabaseConfig.JournalEntry;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddEntryFragment extends Fragment {

    EditText titleInput, bodyInput;
    Button addEntryBtn;
    ImageButton backButton;
    TextView dateTv;
    CheckBox shareCkb;
    ProgressBar progressBar;

    FirebaseAuth myAuth;
    FirebaseUser currentUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_add_entry, container, false);

        myAuth = FirebaseAuth.getInstance();
        currentUser = myAuth.getCurrentUser();

        titleInput = fragmentView.findViewById(R.id.add_entry_title);
        bodyInput = fragmentView.findViewById(R.id.add_entry_body);
        addEntryBtn = fragmentView.findViewById(R.id.add_entry_btn);
        backButton = fragmentView.findViewById(R.id.add_entry_back_btn);
        dateTv = fragmentView.findViewById(R.id.add_entry_date);
        shareCkb = fragmentView.findViewById(R.id.add_entry_share_checkbox);
        progressBar = fragmentView.findViewById(R.id.add_entry_progress_bar);

        final Date jeDate = new Date();

        SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy");
        dateTv.setText(format.format(jeDate));

        addEntryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);
                addEntryBtn.setVisibility(View.INVISIBLE);

                JournalEntry jEntry = new JournalEntry();

                jEntry.setId(MainActivity.myDatabase.journalDao().getMaxId() + 1);
                jEntry.setTitle(titleInput.getText().toString());
                jEntry.setBody(bodyInput.getText().toString());

                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                jEntry.setDateTime(formatter.format(jeDate));

                MainActivity.myDatabase.journalDao().addEntry(jEntry);

                if(shareCkb.isChecked()) {
                    if(currentUser != null) {
                        Post post = new Post(
                                currentUser.getUid(),
                                titleInput.getText().toString(),
                                bodyInput.getText().toString(),
                                currentUser.getPhotoUrl().toString(),
                                currentUser.getDisplayName(),
                                0
                        );

                        addPost(post);
                    } else {
                        showMessage("You need to login to share in Dashboard\nBut journal entry added to local storage");
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                    }
                } else {
                    showMessage("Journal entry added!");
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                }

                titleInput.setText("");
                bodyInput.setText("");

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            }
        });

        return fragmentView;
    }

    private void addPost(Post post) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference("Posts").push();

        String key = dbRef.getKey();
        post.setPostKey(key);

        dbRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("Post added successfully!");
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            }
        });

    }

    private void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
