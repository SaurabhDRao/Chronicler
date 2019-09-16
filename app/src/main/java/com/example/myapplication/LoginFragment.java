package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {

    EditText emailInput, passwordInput;
    Button loginBtn;
    ImageButton backBtn;
    ProgressBar loginProgress;
    FirebaseAuth myAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_login, container, false);

        myAuth = FirebaseAuth.getInstance();

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

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginBtn.setVisibility(View.INVISIBLE);
                loginProgress.setVisibility(View.VISIBLE);

                final String email = emailInput.getText().toString();
                final String password = passwordInput.getText().toString();

                if(email.isEmpty()) {
                    showMessage("Email cannot be empty!");
                    loginBtn.setVisibility(View.VISIBLE);
                    loginProgress.setVisibility(View.INVISIBLE);
                } else if(password.isEmpty()) {
                    showMessage("Password cannot be empty!");
                    loginBtn.setVisibility(View.VISIBLE);
                    loginProgress.setVisibility(View.INVISIBLE);
                } else {
                    logIn(email, password);
                }

            }
        });

        return fragmentView;
    }

    private void logIn(String email, String password) {

        myAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    loginProgress.setVisibility(View.INVISIBLE);
                    loginBtn.setVisibility(View.VISIBLE);
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, new DashboardFragment()).commit();
                } else {
                    showMessage(task.getException().getMessage());
                    loginProgress.setVisibility(View.INVISIBLE);
                    loginBtn.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
