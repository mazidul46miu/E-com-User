package com.example.userapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.userapp.databinding.FragmentCheckoutBinding;
import com.example.userapp.utils.Constants;
import com.example.userapp.viewmodels.LoginViewModel;
import com.example.userapp.viewmodels.ProductViewModel;

public class CheckoutFragment extends Fragment {
    private FragmentCheckoutBinding binding;

    private ProductViewModel productViewModel;
    private LoginViewModel loginViewModel;

    public CheckoutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCheckoutBinding.inflate(inflater);
        loginViewModel = new ViewModelProvider(requireActivity())
                .get(LoginViewModel.class);
        productViewModel = new ViewModelProvider(requireActivity())
                .get(ProductViewModel.class);
        loginViewModel.getUserData().observe(getViewLifecycleOwner(),user ->{
            if (user.getDeliveryAddress() != null){
                loginViewModel.setEcomUser(user);
                binding.deliveryAddress.setText(user.getDeliveryAddress());
            }
        });
        binding.paymentRG.setOnCheckedChangeListener((radioGroup, i) -> {
            final RadioButton rb = container.findViewById(i);
            productViewModel.setPaymentMethod(rb.getText().toString());
        });

        binding.nextBtn.setOnClickListener(view -> {
            final String address = binding.deliveryAddress.getText().toString();
            if (address.isEmpty()){
                binding.deliveryAddress.setError("This Field must not be Empty");
                return;
            }
            loginViewModel.UpdateDeliveryAddress(address);
            Navigation.findNavController(view).navigate(R.id.action_checkoutFragment_to_confirmationFragment);
        });
        return binding.getRoot();
    }
}