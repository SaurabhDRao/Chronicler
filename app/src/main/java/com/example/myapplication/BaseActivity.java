package com.example.myapplication;

import android.arch.persistence.room.Room;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.myapplication.RoomDatabaseConfig.JournalDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BaseActivity extends AppCompatActivity {

    BottomNavigationView navView;
    private FirebaseAuth myAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        myAuth = FirebaseAuth.getInstance();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(navListener);

        Log.d("YO", "das");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if(myAuth.getCurrentUser() != null)
            getMenuInflater().inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        myAuth.signOut();
        navView.getMenu().findItem(R.id.navigation_dashboard).setChecked(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UnregisteredFragment()).commit();
        item.setVisible(false);
        return true;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch(item.getItemId()) {
                        case R.id.navigation_home:
                            selectedFragment = new HomeFragment();
//                            getSupportActionBar().hide();
                            break;

                        case R.id.navigation_settings:
                            selectedFragment = new SettingsFragment();
//                            getSupportActionBar().show();
                            break;

                        case R.id.navigation_dashboard:
                            if(myAuth.getCurrentUser() != null) {
                                selectedFragment = new DashboardFragment();
                            } else {
                                selectedFragment = new UnregisteredFragment();
                            }
////                            getSupportActionBar().show();
                            break;

                        default: return false;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

                    return true;
                }
            };

    @Override
    protected void onStart() {
        super.onStart();

        user = myAuth.getCurrentUser();
    }
}
