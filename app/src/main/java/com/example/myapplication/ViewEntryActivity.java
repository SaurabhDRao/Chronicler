package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ViewEntryActivity extends AppCompatActivity {

    TextView titleInput, dateTimeInput, bodyInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_entry);

        titleInput = findViewById(R.id.view_entry_title);
        dateTimeInput = findViewById(R.id.view_entry_date_time);
        bodyInput = findViewById(R.id.view_entry_body);

        String title = getIntent().getExtras().getString("title");
        String body = getIntent().getExtras().getString("body");
        String dateTime = getIntent().getExtras().getString("dateTime");

        titleInput.setText(title);
        dateTimeInput.setText(dateTime);
        bodyInput.setText(body);
    }
}
