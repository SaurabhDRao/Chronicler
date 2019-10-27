package com.example.myapplication;

import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.RoomDatabaseConfig.ImageLock;
import com.example.myapplication.RoomDatabaseConfig.JournalDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout layout;

    private boolean pointsAlreadySaved = false;
    private ArrayList<Integer> pointsList;

    private final static int MAX_DISTANCE_FROM_LOCK_POINT = 75;

    public static JournalDatabase myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = findViewById(R.id.layout);

        myDatabase = Room.databaseBuilder(getApplicationContext(), JournalDatabase.class, "chroniclerdb").allowMainThreadQueries().build();

        pointsList = new ArrayList<>();

        if(myDatabase.lockPointsDao().getPoints() == null) {
            Log.wtf("LOCK_POINTS", "No lock points");
            pointsAlreadySaved = false;

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle("Save Lock Points");
            alertDialog.setMessage("Touch on the image 4 times to record the lock points. Next time you open use the same points you touched to unlock the application.");
            alertDialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // do nothing
                }
            });

            alertDialog.show();
        } else {
            pointsAlreadySaved = true;
        }

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
                    if(pointsAlreadySaved) {
                        pointsList.add(touchX);
                        pointsList.add(touchY);
                        ImageLock imageLock = myDatabase.lockPointsDao().getPoints();
                        Log.wtf("IMAGE_LOCK_POINTS", imageLock.getPoint1x() + ", " + imageLock.getPoint4y());
                        if(
                                checkDistance(imageLock.getPoint1x(), imageLock.getPoint1y(), pointsList.get(0), pointsList.get(1)) &&
                                checkDistance(imageLock.getPoint2x(), imageLock.getPoint2y(), pointsList.get(2), pointsList.get(3)) &&
                                checkDistance(imageLock.getPoint3x(), imageLock.getPoint3y(), pointsList.get(4), pointsList.get(5)) &&
                                checkDistance(imageLock.getPoint4x(), imageLock.getPoint4y(), pointsList.get(6), pointsList.get(7))
                        ) {
                            startActivity(new Intent(getApplicationContext(), BaseActivity.class));
                            finish();
                        } else {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
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
                    } else {
                        pointsList.add(touchX);
                        pointsList.add(touchY);
                        ListIterator iterator = pointsList.listIterator();
        //                        String points = "";
        //                        while(iterator.hasNext())
        //                            points += ", " + iterator.next();

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

                        myDatabase.lockPointsDao().insertPoints(imageLock);

                        startActivity(new Intent(getApplicationContext(), BaseActivity.class));
                        finish();

        //                        Log.wtf("All points", points);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.forget_password_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.forget_password:
                myDatabase.lockPointsDao().deleteData();
                pointsList.clear();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                return true;
            default: return false;
        }
    }
}
