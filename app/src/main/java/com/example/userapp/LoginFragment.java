package com.example.userapp;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.userapp.databinding.FragmentLoginBinding;
import com.example.userapp.viewmodels.LoginViewModel;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class LoginFragment extends Fragment {
    private LoginViewModel loginViewModel;
    private FragmentLoginBinding binding;
    private boolean isLogin;
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );
    public LoginFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater);
        loginViewModel = new ViewModelProvider(requireActivity())
                .get(LoginViewModel.class);
        binding.loginBtn.setOnClickListener(v -> {
            isLogin = true;
            authenticate();
        });
        binding.registerBtn.setOnClickListener(v -> {
            isLogin = false;
            authenticate();
        });

        loginViewModel.getErrMsgLiveData()
                .observe(getViewLifecycleOwner(), errMsg -> {
                    binding.errMsgTV.setText(errMsg);
                });


        return binding.getRoot();
    }

    private void authenticate() {
        final String email = binding.emailInputET.getText().toString();
        final String password = binding.passwordInputET.getText().toString();

        if (isLogin) {
            loginViewModel.login(email, password);
        }else {
            //loginViewModel.register(email, password);
            if (loginViewModel.getPhone() == null){
                startPhoneAuthFlow();
            }else {
                loginViewModel.register(email, password);
            }
        }
    }

    private void startPhoneAuthFlow() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build());

        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        signInLauncher.launch(signInIntent);
    }
    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            loginViewModel.setPhone(user.getPhoneNumber());
            FirebaseAuth.getInstance().signOut();
            binding.errMsgTV.setText("Your Phone Number has been authorized. Please register your email and password now.");

        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }
}