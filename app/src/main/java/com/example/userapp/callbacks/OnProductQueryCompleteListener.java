package com.example.userapp.callbacks;


import com.example.userapp.models.ProductModel;

import java.util.List;

public interface OnProductQueryCompleteListener {
    void onProductQueryComplete(List<ProductModel> items);
}
