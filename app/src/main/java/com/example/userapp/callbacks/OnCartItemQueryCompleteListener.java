package com.example.userapp.callbacks;

import com.example.userapp.models.CartModel;

import java.util.List;

public interface OnCartItemQueryCompleteListener {
    void onCartItemQueryCompleted(List<CartModel>cartModels);
}
