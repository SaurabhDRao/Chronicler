package com.example.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    TextView emailTv;
    CircleImageView profileImg;
    ImageButton backBtn;
    FirebaseAuth myAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_profile, container, false);

        emailTv = fragmentView.findViewById(R.id.profile_email);
        profileImg = fragmentView.findViewById(R.id.profile_image);

        myAuth = FirebaseAuth.getInstance();
        emailTv.setText(myAuth.getCurrentUser().getEmail());

        backBtn = fragmentView.findViewById(R.id.profile_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
            }
        });

        return fragmentView;
    }
}
