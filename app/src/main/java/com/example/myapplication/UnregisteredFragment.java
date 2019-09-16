package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class UnregisteredFragment extends Fragment {

    Button loginBtn, signUpBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_unregistered, container, false);

        loginBtn = fragmentView.findViewById(R.id.dashboard_login_btn);
        signUpBtn = fragmentView.findViewById(R.id.dashboard_sign_up_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).commit();
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new RegisterFragment()).commit();
            }
        });
//        ((AppCompatActivity)getActivity()).getSupportActionBar().show();

        return fragmentView;
    }
}
