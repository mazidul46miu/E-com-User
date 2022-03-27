package com.example.userapp.models;

public class OrderConstants {
    private int delieryCharge;
    private double vat;
    private double discount;

    public OrderConstants() {
    }

    public OrderConstants(int delieryCharge, double vat, double discount) {
        this.delieryCharge = delieryCharge;
        this.vat = vat;
        this.discount = discount;
    }

    public int getDelieryCharge() {
        return delieryCharge;
    }

    public void setDelieryCharge(int delieryCharge) {
        this.delieryCharge = delieryCharge;
    }

    public double getVat() {
        return vat;
    }

    public void setVat(double vat) {
        this.vat = vat;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
