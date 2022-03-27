package com.example.userapp;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.userapp.callbacks.ActionCallbackListener;
import com.example.userapp.databinding.FragmentConfirmationBinding;
import com.example.userapp.models.CartModel;
import com.example.userapp.models.OrderConstants;
import com.example.userapp.models.OrderModel;
import com.example.userapp.utils.Constants;
import com.example.userapp.viewmodels.LoginViewModel;
import com.example.userapp.viewmodels.OrderViewModel;
import com.example.userapp.viewmodels.ProductViewModel;

import java.util.Calendar;
import java.util.Locale;

public class ConfirmationFragment extends Fragment {
    private FragmentConfirmationBinding binding;
    private ProductViewModel productViewModel;
    private OrderViewModel orderViewModel;
    private LoginViewModel loginViewModel;

    public ConfirmationFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentConfirmationBinding.inflate(inflater);
        loginViewModel = new ViewModelProvider(requireActivity())
                .get(LoginViewModel.class);
        orderViewModel = new ViewModelProvider(requireActivity())
                .get(OrderViewModel.class);
        productViewModel = new ViewModelProvider(requireActivity())
                .get(ProductViewModel.class);
        orderViewModel.getOrderConstantsFromDB();
        orderViewModel.getOrderConstantsMutableLiveData().observe(getViewLifecycleOwner(),orderConstants -> {
            if (orderConstants != null){
                orderViewModel.calculatePayableAmount(productViewModel.calculateTotalPrice(productViewModel.cartModelList));
                updateUI(orderConstants);
            }

        });

        for (CartModel c : productViewModel.cartModelList){
            final CardView cv = (CardView) inflater.inflate(R.layout.cart_item_simple_row, null, true);
            final TextView nameTV = cv.findViewById(R.id.cartRowConfirmProductNameTV);
            final TextView priceQtyTV = cv.findViewById(R.id.cartRowConfirmProductPriceTV);

            nameTV.setText(c.getProductName());
            priceQtyTV.setText(c.getQuantity()+"x"+c.getProductPrice());

            binding.cartItemsLinearLayout.addView(cv);

        }
        binding.orderBtn.setOnClickListener(view -> {
            placeOrder();
        });

        return binding.getRoot();
    }

    private void placeOrder() {
        final Calendar calendar = Calendar.getInstance(Locale.getDefault());
        final OrderModel orderModel = new OrderModel();
        orderModel.setOrderID(loginViewModel.getUser().getUid());
        orderModel.setDeliveryAddress(loginViewModel.getEcomUser().getDeliveryAddress());
        orderModel.setOrderTimestamp(calendar.getTimeInMillis());
        orderModel.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        orderModel.setMonth(calendar.get(Calendar.MONTH));
        orderModel.setYear(calendar.get(Calendar.YEAR));
        orderModel.setOrderStatus(Constants.OderStatus.PENDING);
        orderModel.setDiscount(orderViewModel.getOrderConstants().getDiscount());
        orderModel.setVat(orderViewModel.getOrderConstants().getVat());
        orderModel.setDeliveryCharge(orderViewModel.getOrderConstants().getDelieryCharge());
        orderModel.setGrandTotal(orderViewModel.grandTotal);

        orderViewModel.addNewOrder(orderModel, productViewModel.cartModelList, new ActionCallbackListener() {
            @Override
            public void onSuccess() {
                Navigation.findNavController(getActivity(),R.id.fragmentContainerView)
                        .navigate(R.id.action_confirmationFragment_to_orderSuccessfulFragment);

            }

            @Override
            public void onFailure() {

            }
        });
    }

    private void updateUI(OrderConstants orderConstants) {
        binding.totalTV.setText((String.valueOf(
                productViewModel.calculateTotalPrice(productViewModel.cartModelList)
        )));
        binding.discountLevelTV.setText("Discount("+orderConstants.getDiscount()+"%");
        binding.vatAmountTV.setText("Discount("+ orderConstants.getVat()+"%");
        binding.deliveryTV.setText(String.valueOf(orderConstants.getDelieryCharge()));
        binding.vatAmountTV.setText(String.valueOf(orderViewModel.totalVatOnPrice));
        binding.discountLevelTV.setText(String.valueOf(orderViewModel.totalDiscountOnPrice));
        binding.grandTotalTV.setText(String.valueOf(orderViewModel.grandTotal));
        binding.paymentOption.setText(productViewModel.getPaymentMethod());
    }
}