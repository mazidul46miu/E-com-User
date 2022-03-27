package com.example.userapp.repos;

import androidx.annotation.NonNull;

import com.example.userapp.callbacks.ActionCallbackListener;
import com.example.userapp.callbacks.OnOrderConstantsQueryCompleteListener;
import com.example.userapp.callbacks.OnOrderQueryCompleteListener;
import com.example.userapp.models.CartModel;
import com.example.userapp.models.OrderConstants;
import com.example.userapp.models.OrderModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;

public class OrderRepository {
    public static final String COLLECTION_ORDER = "Orders";
    public static final String COLLECTION_ORDER_DETAILS = "OrderDetails";
    public static final String COLLECTION_CONSTANTS = "Constants";
    public static final String DOCUMENT_ORDER_CONSTANTS = "OrderContants";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void getOrderConstantData(OnOrderConstantsQueryCompleteListener listener) {
        db.collection(COLLECTION_CONSTANTS)
                .document(DOCUMENT_ORDER_CONSTANTS)
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;
                    listener.onOrderConstantsQueryComplete(
                            value.toObject(OrderConstants.class));
                });
    }

    public void addNewOrder(OrderModel orderModel, List<CartModel> cartModelList, ActionCallbackListener actionCallbackListener) {
        final WriteBatch writeBatch = db.batch();
        final DocumentReference orderDoc =
                db.collection(COLLECTION_ORDER).document();
        orderModel.setOrderID(orderDoc.getId());
        writeBatch.set(orderDoc, orderModel);

        for (CartModel c : cartModelList) {
            final DocumentReference docRef =
                    db.collection(COLLECTION_ORDER)
                            .document(orderDoc.getId())
                            .collection(COLLECTION_ORDER_DETAILS)
                            .document();
            writeBatch.set(docRef, c);
        }

        writeBatch.commit().addOnSuccessListener(unused -> {
            clearCart(orderModel.getOrderID(), cartModelList, actionCallbackListener);
        })
                .addOnFailureListener(unused -> {
                    actionCallbackListener.onFailure();
                });

    }

    public void getAllOrders(String userId, OnOrderQueryCompleteListener listener) {
        db.collection(COLLECTION_ORDER)
                .whereEqualTo("userId", userId)
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;

                    final List<OrderModel> items = new ArrayList<>();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        items.add(doc.toObject(OrderModel.class));
                    }

                    listener.onOrderQueryComplete(items);
                });
    }

    public void clearCart(String userId, List<CartModel> cartModelList, ActionCallbackListener actionCallbackListener) {
        final WriteBatch writeBatch = db.batch();

        for (CartModel c : cartModelList) {
            final DocumentReference doc = db.collection(ProductRepository.COLLECTION_USER)
                    .document(userId)
                    .collection(ProductRepository.COLLECTION_CART)
                    .document(c.getProductID());
            writeBatch.delete(doc);
        }

        writeBatch.commit().
                addOnSuccessListener(unused -> {
                    actionCallbackListener.onSuccess();
                }).addOnFailureListener(unused -> {
            actionCallbackListener.onFailure();
        });
    }


}
