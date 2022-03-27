package com.example.userapp.repos;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.userapp.models.EcomUser;
import com.example.userapp.utils.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserRepository {
    public final String TAG = UserRepository.class.getSimpleName();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void UpdateDeliveryAddress (String uid, String address){
        final DocumentReference doc = db.collection(ProductRepository.COLLECTION_USER).document(uid);
        doc.update("DeliveryAddress", address)
                .addOnSuccessListener(unused -> {

                })
                .addOnFailureListener(unused -> {

                });
    }
    public LiveData<EcomUser>getCurrentUserInfo(String uid){
        final MutableLiveData<EcomUser> userLivedata = new MutableLiveData<>();
        db.collection(ProductRepository.COLLECTION_USER)
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    userLivedata.postValue(documentSnapshot.toObject(EcomUser.class));

                }).addOnFailureListener(e -> {

                });
        return userLivedata;
    }
    public void updateUserName(String uid, String name){

    }
}
