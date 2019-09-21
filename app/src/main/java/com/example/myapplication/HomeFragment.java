package com.example.myapplication;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.example.myapplication.Adapters.JournalEntryAdapter;
import com.example.myapplication.RoomDatabaseConfig.JournalEntry;

import java.util.Calendar;
import java.util.List;


public class HomeFragment extends Fragment {

    RecyclerView allEntriesView;
    FloatingActionButton addEntryBtn, selectDateBtn;
    List<JournalEntry> allEntries;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_home, container, false);

        allEntriesView = fragmentView.findViewById(R.id.all_entries_recycler_view);
        addEntryBtn = fragmentView.findViewById(R.id.home_add_entry_btn);
        selectDateBtn = fragmentView.findViewById(R.id.select_date);

        allEntriesView.setLayoutManager(new LinearLayoutManager(getContext()));

        allEntries = MainActivity.myDatabase.journalDao().getAllJournalEntries();

        if(allEntries.size() == 0) {
            fragmentView.findViewById(R.id.no_entries).setVisibility(View.VISIBLE);
        }

        showEntries(fragmentView, allEntries);

        addEntryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddEntryFragment()).commit();
            }
        });

        selectDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                final int yearToday = c.get(Calendar.YEAR);
                final int monthToday = c.get(Calendar.MONTH);
                final int day = c.get(Calendar.DAY_OF_MONTH);
                final String todayStr = day + "/" + monthToday + "/" + yearToday;

                DatePickerDialog d = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String m = (month + 1) + "";
                        if(m.length() == 1)
                            m = "0" + m;
                        String dateStr = "%" + dayOfMonth + "/" + m + "/" + year + "%";
                        Log.wtf("DATE_STR", dateStr + " " + todayStr);
                        List<JournalEntry> searchEntries = MainActivity.myDatabase.journalDao().getEntriesBasedOnDate(dateStr);
                        if(searchEntries.size() == 0) {
                            fragmentView.findViewById(R.id.no_entries).setVisibility(View.VISIBLE);
                        } else {
                            fragmentView.findViewById(R.id.no_entries).setVisibility(View.INVISIBLE);
                        }
                        showEntries(fragmentView, searchEntries);
                    }
                }, yearToday, monthToday, day);
                d.show();
            }
        });

        return fragmentView;
    }

    private void showEntries(View fragmentView, List<JournalEntry> entries) {

        JournalEntryAdapter entryAdapter = new JournalEntryAdapter(getContext(), entries);
        allEntriesView.setAdapter(entryAdapter);

    }
}
