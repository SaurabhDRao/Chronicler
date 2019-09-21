package com.example.myapplication.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.RoomDatabaseConfig.JournalEntry;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.ViewEntryActivity;

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
    public void onBindViewHolder(@NonNull JournalViewHolder holder, final int position) {

        String str = myEntry.get(position).getDateTime();
        String dateTime[] = str.split(" ");
        String[] yearMonthDate = dateTime[0].split("/");

        String[] monthList = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

        holder.monthAndDateTv.setText(monthList[Integer.parseInt(yearMonthDate[1]) - 1] + " " + yearMonthDate[0]);
        holder.yearTv.setText(yearMonthDate[2]);
        holder.titleTv.setText(myEntry.get(position).getTitle());
        holder.bodyTv.setText(myEntry.get(position).getBody());

        final String title = myEntry.get(position).getTitle();
        final int entryId = myEntry.get(position).getId();

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
                builder.setTitle("Confirm delete");
                builder.setMessage("Are you sure you want to delete '" + title + "'?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        JournalEntry entry = new JournalEntry();
                        entry.setId(entryId);
                        Log.wtf("REMOVE", position + "");
                        Log.wtf("REMOVE", title);
                        myEntry.remove(position);
                        MainActivity.myDatabase.journalDao().deleteItem(entry);
                        Toast.makeText(myContext, title + " deleted!", Toast.LENGTH_SHORT).show();
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, myEntry.size());
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show();
            }
        });

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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(myContext, ViewEntryActivity.class);

                    int pos = getAdapterPosition();

                    String str = myEntry.get(pos).getDateTime();
                    String dateTime[] = str.split(" ");
                    String[] yearMonthDate = dateTime[0].split("/");
                    String[] monthList = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
                    String month = monthList[Integer.parseInt(yearMonthDate[1]) - 1];
                    String dateTimeStr = month + " " + yearMonthDate[0] + ", " + yearMonthDate[2] + " | " + dateTime[1];

                    i.putExtra("title", myEntry.get(pos).getTitle());
                    i.putExtra("body", myEntry.get(pos).getBody());
                    i.putExtra("dateTime", dateTimeStr);
                    i.putExtra("shared", myEntry.get(pos).getShared() + "");

                    myContext.startActivity(i);
                }
            });
        }
    }
}
