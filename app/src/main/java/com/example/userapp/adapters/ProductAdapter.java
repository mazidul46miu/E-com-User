package com.example.userapp.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userapp.callbacks.OnAddRemoveCartItemListener;
import com.example.userapp.callbacks.OnProductItemClickListener;
import com.example.userapp.databinding.ProductRowItemBinding;
import com.example.userapp.models.CartModel;
import com.example.userapp.models.ProductModel;
import com.example.userapp.models.UserProductModel;
import com.google.protobuf.Internal;

public class ProductAdapter extends ListAdapter<UserProductModel, ProductAdapter.ProductViewHolder> {
    private OnProductItemClickListener listener;
    private OnAddRemoveCartItemListener cartItemListener;
    public ProductAdapter(OnProductItemClickListener listener, OnAddRemoveCartItemListener cartItemListener) {
        super(new ProductDiff());
        this.listener = listener;
        this.cartItemListener = cartItemListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ProductRowItemBinding binding = ProductRowItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        final UserProductModel productModel = getItem(position);
        holder.bind(productModel);

        if (productModel.isInCart()){
            holder.binding.addToCartBtn.setVisibility(View.GONE);
            holder.binding.removeToCartBtn.setVisibility(View.VISIBLE);
        }else {
            holder.binding.addToCartBtn.setVisibility(View.VISIBLE);
            holder.binding.removeToCartBtn.setVisibility(View.GONE);
        }

        holder.binding.addToCartBtn.setOnClickListener(view -> {
            holder.binding.addToCartBtn.setVisibility(View.GONE);
            holder.binding.removeToCartBtn.setVisibility(View.VISIBLE);
            final CartModel cartModel = new CartModel(productModel.getProductId(), productModel.getProductName(), productModel.getPrice(), 1);
            cartItemListener.onAddCartItem(cartModel, position);
        });
        holder.binding.removeToCartBtn.setOnClickListener(view -> {
            holder.binding.addToCartBtn.setVisibility(View.VISIBLE);
            holder.binding.removeToCartBtn.setVisibility(View.GONE);
            cartItemListener.onRemoveCartItem(productModel.getProductId(),position);
        });
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        private ProductRowItemBinding binding;
        public ProductViewHolder(ProductRowItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.productRowCardView.setOnClickListener(v -> {
                listener.onProductItemClicked(
                        getItem(getAdapterPosition()).getProductId());
            });
        }
        public void bind(UserProductModel productModel) {
            binding.setProduct(productModel);
        }
    }

    static class ProductDiff extends DiffUtil.ItemCallback<UserProductModel>{
        @Override
        public boolean areItemsTheSame(@NonNull UserProductModel oldItem, @NonNull UserProductModel newItem) {
            return oldItem.getProductId().equals(newItem.getProductId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull UserProductModel oldItem, @NonNull UserProductModel newItem) {
            return oldItem.getProductId().equals(newItem.getProductId());
        }
    }
}
