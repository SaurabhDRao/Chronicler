package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterFragment extends Fragment {

    EditText nameInput, emailInput, passwordInput, confirmPasswordInput;
    Button registerBtn;
    ImageButton backBtn;
    ProgressBar progressBar;
    FirebaseAuth myAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_sign_up, container, false);

        myAuth = FirebaseAuth.getInstance();

        nameInput = fragmentView.findViewById(R.id.register_name_input);
        emailInput = fragmentView.findViewById(R.id.register_email_input);
        passwordInput = fragmentView.findViewById(R.id.register_password_input);
        confirmPasswordInput = fragmentView.findViewById(R.id.register_confirm_password_input);
        registerBtn = fragmentView.findViewById(R.id.register_btn);
        backBtn = fragmentView.findViewById(R.id.register_back_btn);
        progressBar = fragmentView.findViewById(R.id.sign_up_progress_bar);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);
                registerBtn.setVisibility(View.INVISIBLE);

                String name = nameInput.getText().toString();
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();
                String confirmPassword = confirmPasswordInput.getText().toString();

                if(email.isEmpty()) {
                    showMessage("Email cannot be empty!");
                    registerBtn.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                } else if(name.isEmpty()) {
                    showMessage("Name cannot be empty!");
                    registerBtn.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                } else if(password.isEmpty()) {
                    showMessage("Password cannot be empty!");
                    registerBtn.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                } else if(!password.equals(confirmPassword)) {
                    showMessage("Password and confirm password doesn't match!");
                    registerBtn.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    createUserAccount(email, name, password);
                }

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new UnregisteredFragment()).commit();
            }
        });

        return fragmentView;
    }

    private void createUserAccount(String email, final String name, String password) {

        myAuth.createUserWithEmailAndPassword(email ,password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {

                            showMessage("Account created successfully!");
                            FirebaseUser currentUser = myAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();

                            currentUser.updateProfile(profileUpdate)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                showMessage("Registration Complete!");
                                                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new DashboardFragment()).commit();
                                                setHasOptionsMenu(true);
                                                getActivity().invalidateOptionsMenu();
                                            }
                                        }
                                    });

                        } else {
                            showMessage("Account creation failed!\n" + task.getException().getMessage());
                            registerBtn.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });

    }


    private void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
