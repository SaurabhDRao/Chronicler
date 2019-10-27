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

import com.example.myapplication.RoomDatabaseConfig.ImageLock;

import java.util.ArrayList;
import java.util.ListIterator;

public class EnterCurrentLockPointsActivity extends AppCompatActivity {

    ConstraintLayout layout;

    private ArrayList<Integer> pointsList;

    private final static int MAX_DISTANCE_FROM_LOCK_POINT = 75;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_current_lock_points);

        layout = findViewById(R.id.current_lock_points_layout);

        pointsList = new ArrayList<>();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(EnterCurrentLockPointsActivity.this);
        alertDialog.setTitle("Insert Password");
        alertDialog.setMessage("Insert the existing password before you can change it");
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
                    ImageLock imageLock = MainActivity.myDatabase.lockPointsDao().getPoints();
                    Log.wtf("IMAGE_LOCK_POINTS", imageLock.getPoint1x() + ", " + imageLock.getPoint4y());
                    if (
                            checkDistance(imageLock.getPoint1x(), imageLock.getPoint1y(), pointsList.get(0), pointsList.get(1)) &&
                            checkDistance(imageLock.getPoint2x(), imageLock.getPoint2y(), pointsList.get(2), pointsList.get(3)) &&
                            checkDistance(imageLock.getPoint3x(), imageLock.getPoint3y(), pointsList.get(4), pointsList.get(5)) &&
                            checkDistance(imageLock.getPoint4x(), imageLock.getPoint4y(), pointsList.get(6), pointsList.get(7))
                    ) {
                        startActivity(new Intent(getApplicationContext(), ChangeLockPointsActivity.class));
                        finish();
                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(EnterCurrentLockPointsActivity.this);
                        alertDialog.setTitle("Incorrect Lock Points");
                        alertDialog.setMessage("You have touched one or more incorrect lock points! Please try again...");
                        alertDialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                pointsList.clear();
                            }
                        });

                        alertDialog.show();
                    }
                }

                return false;
            }
        });
    }

    private boolean checkDistance(int x, int y, int a, int b) {
        int distance = (int) Math.sqrt(((x - a) * (x - a)) + ((y - b) * (y - b)));

        Log.wtf("DISTANCE", distance + " ");

        if(distance <= MAX_DISTANCE_FROM_LOCK_POINT)
            return true;
        else
            return false;
    }
}
