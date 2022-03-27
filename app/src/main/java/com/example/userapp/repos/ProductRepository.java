package com.example.userapp.repos;

import androidx.annotation.Nullable;

import com.example.userapp.callbacks.OnCartItemQueryCompleteListener;
import com.example.userapp.callbacks.OnCategoryQueryCompleteListener;
import com.example.userapp.callbacks.OnProductQueryCompleteListener;
import com.example.userapp.callbacks.OnSingleProductQueryCompleteListener;
import com.example.userapp.models.CartModel;
import com.example.userapp.models.ProductModel;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductRepository {
    public final String TAG = ProductRepository.class.getSimpleName();
    public static final String COLLECTION_CATEGORY = "Categories";
    public static final String COLLECTION_PRODUCT = "Products";
    public static final String COLLECTION_USER = "Users";
    public static final String COLLECTION_CART = "Cart";
    public static final String COLLECTION_CONSTANTS = "Constants";
    public static final String COLLECTION_ORDER_CONSTANTS = "OrderConstants";
    public static final String COLLECTION_ORDER= "Order";
    public static final String COLLECTION_ORDER_DETAILS= "OrderDetails";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void addToCart(CartModel cartModel, String uid) {
        db.collection(COLLECTION_USER).document(uid)
                .collection(COLLECTION_CART).document(cartModel.getProductID())
                .set(cartModel).addOnSuccessListener(unused -> {

                 }).addOnFailureListener(unused -> {

        });
    }

    public void removeFromCart(String uid, String productId){
        db.collection(COLLECTION_USER).document(uid)
                .collection(COLLECTION_CART).document(productId)
                .delete().addOnSuccessListener(unused -> {

                }).addOnFailureListener(unused ->{

        });
    }

    public void getAllCategories(OnCategoryQueryCompleteListener listener) {
        db.collection(COLLECTION_CATEGORY)
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;
                    final List<String> temp = new ArrayList<>();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        temp.add(doc.get("name", String.class));
                    }
                    Collections.sort(temp);
                    listener.onCategoryQueryComplete(temp);
                });
    }

    public void getAllProducts(OnProductQueryCompleteListener listener) {
        db.collection(COLLECTION_PRODUCT)
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;
                    final List<ProductModel> temp = new ArrayList<>();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        temp.add(doc.toObject(ProductModel.class));
                    }
                    listener.onProductQueryComplete(temp);
                });
    }

    public void getAllProductsByCategory(String category, OnProductQueryCompleteListener listener) {
        db.collection(COLLECTION_PRODUCT)
                .whereEqualTo("category", category)
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;
                    final List<ProductModel> temp = new ArrayList<>();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        temp.add(doc.toObject(ProductModel.class));
                    }
                    listener.onProductQueryComplete(temp);
                });
    }


    public void getProductByProductId(String productId, OnSingleProductQueryCompleteListener listener) {
        db.collection(COLLECTION_PRODUCT).document(productId)
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;
                    final ProductModel model = value.toObject(ProductModel.class);
                    listener.onSingleProductQueryComplete(model);
                });
    }

    public void getAllCartItems(String uid, OnCartItemQueryCompleteListener cartItemQueryCompleteListener) {
        db.collection(COLLECTION_USER).document(uid).collection(COLLECTION_CART)
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;
                    List<CartModel>items = new ArrayList<>();
                    for (DocumentSnapshot doc : value.getDocuments()){
                        items.add(doc.toObject(CartModel.class));
                    }
                    cartItemQueryCompleteListener.onCartItemQueryCompleted(items);
                });
    }

    public void updateCartItemQuantity(String uid, List<CartModel> cartModels) {
        final WriteBatch writeBatch = db.batch();

        for (CartModel c : cartModels) {
            final DocumentReference doc =
                    db.collection(COLLECTION_USER)
                            .document(uid)
                            .collection(COLLECTION_CART)
                            .document(c.getProductID());
            writeBatch.update(doc, "quantity", c.getQuantity());
        }

        writeBatch.commit()
                .addOnSuccessListener(unused -> {

                })
                .addOnFailureListener(unused -> {

                });
    }
}
