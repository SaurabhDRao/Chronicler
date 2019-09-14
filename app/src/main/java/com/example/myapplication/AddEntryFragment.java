package com.example.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myapplication.Database.JournalEntry;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddEntryFragment extends Fragment {

    EditText titleInput, bodyInput;
    Button addEntryBtn;
    ImageButton backButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_add_entry, container, false);

        titleInput = fragmentView.findViewById(R.id.add_entry_title);
        bodyInput = fragmentView.findViewById(R.id.add_entry_body);
        addEntryBtn = fragmentView.findViewById(R.id.add_entry_btn);
        backButton = fragmentView.findViewById(R.id.add_entry_back_btn);

        addEntryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JournalEntry jEntry = new JournalEntry();

                jEntry.setId(MainActivity.myDatabase.journalDao().getMaxId() + 1);
                jEntry.setTitle(titleInput.getText().toString());
                jEntry.setBody(bodyInput.getText().toString());
                jEntry.setDateTime(new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(new Date()));

                MainActivity.myDatabase.journalDao().addEntry(jEntry);

                Toast.makeText(getContext(), "Journal entry added!", Toast.LENGTH_SHORT).show();

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
}
