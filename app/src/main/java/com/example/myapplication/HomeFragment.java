package com.example.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.Database.JournalEntry;

import java.util.List;


public class HomeFragment extends Fragment {

    TextView allEntriesTv;
    FloatingActionButton addEntryBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_home, container, false);

        allEntriesTv = fragmentView.findViewById(R.id.allEntries);
        addEntryBtn = fragmentView.findViewById(R.id.home_add_entry_btn);

        showAllEntries();

        addEntryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddEntryFragment()).commit();
            }
        });

        return fragmentView;
    }

    private void showAllEntries() {

        List<JournalEntry> allEntries = MainActivity.myDatabase.journalDao().getAllJournalEntries();

        String entryStr = "";

        for(JournalEntry entry : allEntries) {
            entryStr += (entry.getId() + " " + entry.getTitle() + " " + entry.getBody() + " " + entry.getDateTime() + "\n");
        }

        allEntriesTv.setText(entryStr);

    }
}
