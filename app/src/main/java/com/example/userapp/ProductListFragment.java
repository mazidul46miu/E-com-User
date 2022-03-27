package com.example.userapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.userapp.adapters.ProductAdapter;
import com.example.userapp.callbacks.OnAddRemoveCartItemListener;
import com.example.userapp.databinding.FragmentProductListBinding;
import com.example.userapp.models.CartModel;
import com.example.userapp.viewmodels.LoginViewModel;
import com.example.userapp.viewmodels.ProductViewModel;

public class ProductListFragment extends Fragment {
    private LoginViewModel loginViewModel;
    private ProductViewModel productViewModel;
    private FragmentProductListBinding binding;
    public ProductListFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductListBinding.inflate(inflater);
        loginViewModel = new ViewModelProvider(requireActivity())
                .get(LoginViewModel.class);

        productViewModel = new ViewModelProvider(requireActivity())
                .get(ProductViewModel.class);
        final ProductAdapter adapter = new ProductAdapter(productId -> {
            //

        }, new OnAddRemoveCartItemListener() {
            @Override
            public void onAddCartItem(CartModel cartModel, int position) {
                productViewModel.addToCart(cartModel,loginViewModel.getUser().getUid());
            }

            @Override
            public void onRemoveCartItem(String productId, int position) {
                productViewModel.removeFromCart(loginViewModel.getUser().getUid(),productId);
            }
        });
        binding.productRV.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        binding.productRV.setAdapter(adapter);
        productViewModel.productListLiveData.observe(getViewLifecycleOwner(),
                productList -> {
                    //adapter.submitList(productList);
                    productViewModel.getAllCartItems(loginViewModel.getUser().getUid());
                });
        productViewModel.cartListLiveData.observe(getViewLifecycleOwner(),cartModelList ->{
            productViewModel.prepareUserProductList();
        });

        productViewModel.userProductListLiveData.observe(getViewLifecycleOwner(),userProductList -> {
            adapter.submitList(userProductList);
        });

        return binding.getRoot();
    }
}