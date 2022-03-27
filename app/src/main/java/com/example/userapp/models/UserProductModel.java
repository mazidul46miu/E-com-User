package com.example.userapp.models;

public class UserProductModel extends ProductModel{
    private boolean isInCart;
    private boolean isFavourite;

    public boolean isInCart() {
        return isInCart;
    }

    public void setInCart(boolean inCart) {
        isInCart = inCart;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }
}
