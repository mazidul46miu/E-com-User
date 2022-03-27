package com.example.userapp.callbacks;

import com.example.userapp.models.OrderModel;

import java.util.List;

public interface OnOrderQueryCompleteListener {
    void onOrderQueryComplete(List<OrderModel> orderModels);
}
