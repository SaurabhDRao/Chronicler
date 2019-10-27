package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;


public class SettingsFragment extends Fragment {

    TextView profileTv, changePasswordTv;
    FirebaseAuth myAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_settings, container, false);

        myAuth = FirebaseAuth.getInstance();

        profileTv = fragmentView.findViewById(R.id.settings_profile);
        changePasswordTv = fragmentView.findViewById(R.id.settings_change_password);

        if(myAuth.getCurrentUser() == null)
            profileTv.setVisibility(View.INVISIBLE);

        profileTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ProfileActivity.class));
            }
        });

        changePasswordTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Change lock screen background");
                alertDialog.setMessage("Do you want to change the lock screen background along with points?");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getContext(), EnterCurrentLockPointsActivity.class);
                        startActivity(intent);
                    }
                });

                alertDialog.show();
            }
        });

        return fragmentView;
    }
}
