package com.example.myapplication.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myapplication.Database.JournalEntry;
import com.example.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class JournalEntryAdapter extends RecyclerView.Adapter<JournalEntryAdapter.JournalViewHolder> {

    private Context myContext;
    private List<JournalEntry> myEntry;

    public JournalEntryAdapter(Context myContext, List<JournalEntry> myEntry) {
        this.myContext = myContext;
        this.myEntry = myEntry;
    }

    @NonNull
    @Override
    public JournalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(myContext).inflate(R.layout.row_journal_entry, parent, false);

        return new JournalViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalViewHolder holder, int position) {

        String str = myEntry.get(position).getDateTime();
        String dateTime[] = str.split(" ");
        String[] yearMonthDate = dateTime[0].split("/");

        String[] monthList = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

        System.out.println(yearMonthDate[0] + yearMonthDate[1] + yearMonthDate[2]);
        holder.monthAndDateTv.setText(monthList[Integer.parseInt(yearMonthDate[1]) - 1] + " " + yearMonthDate[0]);
        holder.yearTv.setText(yearMonthDate[2]);
        holder.titleTv.setText(myEntry.get(position).getTitle());
        String bodyStr = myEntry.get(position).getBody();
        int l = bodyStr.length();
        String res = "";
        if(l > 50)
            res = bodyStr.substring(0, 51) + "...";
        else
            res = bodyStr.substring(0, l);
        holder.bodyTv.setText(res);

    }

    @Override
    public int getItemCount() {
        return myEntry.size();
    }

    public class JournalViewHolder extends RecyclerView.ViewHolder {

        TextView monthAndDateTv, yearTv, titleTv, bodyTv;
        ImageButton deleteBtn;

        public JournalViewHolder(View itemView) {
            super(itemView);

            monthAndDateTv = itemView.findViewById(R.id.je_month_and_date);
            yearTv = itemView.findViewById(R.id.je_year);
            titleTv = itemView.findViewById(R.id.je_title);
            bodyTv = itemView.findViewById(R.id.je_body);
            deleteBtn = itemView.findViewById(R.id.je_delete_btn);
        }
    }
}
