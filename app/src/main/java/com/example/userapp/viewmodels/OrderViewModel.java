package com.example.userapp.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.userapp.callbacks.ActionCallbackListener;
import com.example.userapp.callbacks.OnOrderConstantsQueryCompleteListener;
import com.example.userapp.models.CartModel;
import com.example.userapp.models.OrderConstants;
import com.example.userapp.models.OrderModel;
import com.example.userapp.repos.OrderRepository;

import java.util.List;

public class OrderViewModel extends ViewModel {
    private final OrderRepository orderRepository = new OrderRepository();
    private final MutableLiveData<OrderConstants> orderConstantsMutableLiveData = new MutableLiveData<>();

    private OrderConstants orderConstants;
    public double totalVatOnPrice = 0.0;
    public double totalDiscountOnPrice = 0.0;
    public double grandTotal = 0.0;


    public void getOrderConstantsFromDB(){
        orderRepository.getOrderConstantData(orderConstants -> {
            orderConstantsMutableLiveData.postValue(orderConstants);
            this.orderConstants = orderConstants;
        });
    }

    public OrderConstants getOrderConstants() {
        return orderConstants;
    }

    public void setOrderConstants(OrderConstants orderConstants) {
        this.orderConstants = orderConstants;
    }

    public MutableLiveData<OrderConstants> getOrderConstantsMutableLiveData() {
        return orderConstantsMutableLiveData;
    }

    public void calculatePayableAmount(double totalPrice){
        totalVatOnPrice = (totalPrice * orderConstants.getVat())/100;
        totalDiscountOnPrice = (totalPrice * orderConstants.getDiscount())/100;
        grandTotal = (totalPrice - totalDiscountOnPrice) + totalVatOnPrice + orderConstants.getDelieryCharge();
    }

    public void addNewOrder(OrderModel orderModel, List<CartModel> cartModelList, ActionCallbackListener actionCallbackListener) {
        orderRepository.addNewOrder(orderModel,cartModelList,actionCallbackListener);

    }
}
