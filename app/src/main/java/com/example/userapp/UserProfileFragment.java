package com.example.userapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.userapp.databinding.FragmentUserProfileBinding;
import com.example.userapp.viewmodels.LoginViewModel;

public class UserProfileFragment extends Fragment {
    private FragmentUserProfileBinding binding;
    private LoginViewModel loginViewModel;

    public UserProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserProfileBinding.inflate(inflater);
        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);

        loginViewModel.getUserData().observe(getViewLifecycleOwner(),ecomUser -> {
            if (ecomUser != null){
                binding.setUser(ecomUser);
            }
        });
        return binding.getRoot();
    }
}