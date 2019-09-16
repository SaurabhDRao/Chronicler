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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class LoginFragment extends Fragment {

    EditText emailInput, passwordInput;
    Button loginBtn;
    ImageButton backBtn;
    ProgressBar loginProgress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_login, container, false);

        emailInput = fragmentView.findViewById(R.id.login_email_input);
        passwordInput = fragmentView.findViewById(R.id.login_password_input);
        loginBtn = fragmentView.findViewById(R.id.login_btn);
        backBtn = fragmentView.findViewById(R.id.login_back_btn);
        loginProgress = fragmentView.findViewById(R.id.login_progress_bar);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new UnregisteredFragment()).commit();
            }
        });

        return fragmentView;
    }
}
