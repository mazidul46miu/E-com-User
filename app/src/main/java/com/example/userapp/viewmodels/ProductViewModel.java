package com.example.userapp.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.userapp.callbacks.OnCartItemQueryCompleteListener;
import com.example.userapp.models.CartModel;
import com.example.userapp.models.ProductModel;
import com.example.userapp.models.UserProductModel;
import com.example.userapp.repos.ProductRepository;
import com.example.userapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class ProductViewModel extends ViewModel {
    private final String TAG = ProductViewModel.class.getSimpleName();
    private final ProductRepository repository = new ProductRepository();
    public MutableLiveData<List<String>> categoryListLiveData = new MutableLiveData<>();
    public MutableLiveData<List<ProductModel>> productListLiveData = new MutableLiveData<>();
    public MutableLiveData<List<CartModel>> cartListLiveData = new MutableLiveData<>();
    public MutableLiveData<List<UserProductModel>> userProductListLiveData = new MutableLiveData<>();
    public MutableLiveData<ProductModel> productLiveData = new MutableLiveData<>();
    public List<CartModel> cartModelList = new ArrayList<>();
    private String paymentMethod = Constants.PaymentMethod.COD;

    public ProductViewModel() {
        getCategories();
        getProducts();
    }

    public void addToCart(CartModel cartModel, String uid){
        repository.addToCart(cartModel,uid);
    }

    public void removeFromCart(String uid, String productId){
        repository.removeFromCart(uid, productId);
    }


    private void getCategories() {
        repository.getAllCategories(items ->
                categoryListLiveData.postValue(items));
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void getProducts() {
        repository.getAllProducts(items ->
                productListLiveData.postValue(items));
    }

    public void getProductsByCategory(String category) {
        repository.getAllProductsByCategory(category, items ->
                productListLiveData.postValue(items));
    }

    public void getProductById(String productId) {
        repository.getProductByProductId(productId, productModel -> {
            productLiveData.postValue(productModel);
        });
    }

    public void getAllCartItems(String uid) {
        repository.getAllCartItems(uid, cartModels -> {
          cartListLiveData.postValue(cartModels);
        });
    }

    public void prepareUserProductList() {
        final List<UserProductModel> upmList = new ArrayList<>();
        for (ProductModel p : productListLiveData.getValue()){
            final UserProductModel upm = new UserProductModel();
            upm.setProductId(p.getProductId());
            upm.setProductName(p.getProductName());
            upm.setCategory(p.getCategory());
            upm.setDescription(p.getDescription());
            upm.setPrice(p.getPrice());
            upm.setProductImageUrl(p.getProductImageUrl());
            upm.setInCart(false);
            upm.setFavourite(false);
            upmList.add(upm);
        }
        for (CartModel c : cartListLiveData.getValue()){
            for (UserProductModel u : upmList){
                if (c.getProductID().equals(u.getProductId())){
                    u.setInCart(true);
                }
            }
        }
        userProductListLiveData.postValue(upmList);

    }

    public void updateCartItemQty(String uid, List<CartModel> cartModels) {
        repository.updateCartItemQuantity(uid, cartModels);
    }

    public double calculateTotalPrice(List<CartModel> cartModels) {
        double total = 0.0;
        for (CartModel c : cartModels) {
            total += c.getProductPrice() * c.getQuantity();
        }
        return total;
    }
}
