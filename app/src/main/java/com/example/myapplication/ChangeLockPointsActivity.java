package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.RoomDatabaseConfig.ImageLock;

import java.util.ArrayList;
import java.util.ListIterator;

public class ChangeLockPointsActivity extends AppCompatActivity {

    ConstraintLayout layout;

    private ArrayList<Integer> pointsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_lock_points);

        layout = findViewById(R.id.change_lock_point_layout);

        pointsList = new ArrayList<>();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ChangeLockPointsActivity.this);
        alertDialog.setTitle("Insert new lock points");
        alertDialog.setMessage("Now insert the new lock points and remember it :)");
        alertDialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // do nothing
            }
        });

        alertDialog.show();

        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int touchX = (int) motionEvent.getX();
                int touchY = (int) motionEvent.getY();

                Log.wtf("POINTS", touchX + ", " + touchY);

                if(pointsList.size() < 6) {
                    pointsList.add(touchX);
                    pointsList.add(touchY);
                } else {
                    pointsList.add(touchX);
                    pointsList.add(touchY);
                    ListIterator iterator = pointsList.listIterator();

                    ImageLock imageLock = new ImageLock();
                    imageLock.setId(1);
                    imageLock.setPoint1x((int) iterator.next());
                    imageLock.setPoint1y((int) iterator.next());
                    imageLock.setPoint2x((int) iterator.next());
                    imageLock.setPoint2y((int) iterator.next());
                    imageLock.setPoint3x((int) iterator.next());
                    imageLock.setPoint3y((int) iterator.next());
                    imageLock.setPoint4x((int) iterator.next());
                    imageLock.setPoint4y((int) iterator.next());
                    imageLock.setImage("");

                    MainActivity.myDatabase.lockPointsDao().deleteData();

                    MainActivity.myDatabase.lockPointsDao().insertPoints(imageLock);

                    Toast.makeText(getApplicationContext(), "Lock points changed!", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getApplicationContext(), BaseActivity.class));
                    finish();
                }

                return false;
            }
        });
    }
}
