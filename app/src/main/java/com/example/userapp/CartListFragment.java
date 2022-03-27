package com.example.userapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.userapp.adapters.CartAdapter;
import com.example.userapp.databinding.FragmentCartlistBinding;
import com.example.userapp.models.CartModel;
import com.example.userapp.viewmodels.LoginViewModel;
import com.example.userapp.viewmodels.ProductViewModel;

import java.util.List;

public class CartListFragment extends Fragment {
    private FragmentCartlistBinding binding;
    private ProductViewModel productViewModel;
    private LoginViewModel loginViewModel;
    private CartAdapter adapter;
    public CartListFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCartlistBinding.inflate(inflater);
        loginViewModel = new ViewModelProvider(requireActivity())
                .get(LoginViewModel.class);
        productViewModel = new ViewModelProvider(requireActivity())
                .get(ProductViewModel.class);
        productViewModel.getAllCartItems(loginViewModel.getUser().getUid());
        adapter = new CartAdapter(() -> {
            updateUI(adapter.getCurrentList());
        });
        binding.cartRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.cartRV.setAdapter(adapter);
        productViewModel.cartListLiveData.observe(getViewLifecycleOwner(),
                cartModels -> {
                    Log.e("CartModel", ""+cartModels.size());
                    for (CartModel c : cartModels) {
                        Log.e("CartModel", c.toString());
                    }
                    adapter.submitList(cartModels);
                    productViewModel.cartModelList = cartModels;
                    updateUI(cartModels);
                });
        binding.checkoutBtn.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_cartlistFragment_to_checkoutFragment);
        });
        return binding.getRoot();
    }

    private void updateUI(List<CartModel> cartModels) {
        final double price = productViewModel.calculateTotalPrice(cartModels);
        binding.cartTotalTV.setText("Total: BDT"+price);
    }

    @Override
    public void onStop() {
        super.onStop();
        productViewModel.updateCartItemQty(
                loginViewModel.getUser().getUid(),
                adapter.getCurrentList()
        );
        productViewModel.cartModelList = adapter.getCurrentList();
    }
}