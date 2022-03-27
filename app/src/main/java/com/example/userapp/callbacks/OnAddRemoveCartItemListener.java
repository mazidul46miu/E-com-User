package com.example.userapp.callbacks;

import com.example.userapp.models.CartModel;

public interface OnAddRemoveCartItemListener {
    void onAddCartItem(CartModel cartModel, int position);
    void onRemoveCartItem(String productId, int position);
}
