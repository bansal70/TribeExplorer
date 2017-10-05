package com.tribe.explorer.view.adapters;

/*
 * Created by rishav on 9/13/2017.
 */

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tribe.explorer.R;
import com.tribe.explorer.model.Utils;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Uri> uriList;

    public GalleryAdapter(Context context, ArrayList<Uri> uriList) {
        this.context = context;
        this.uriList = uriList;
    }

    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_gallery, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GalleryAdapter.ViewHolder holder, int position) {
        Uri imageUri = uriList.get(position);
        Glide.with(context)
                .load(imageUri)
                .crossFade()
                .centerCrop()
                .fitCenter()
                .into(holder.imgGallery);
    }

    @Override
    public int getItemCount() {
        return uriList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView imgGallery;
        private CardView cardCancel;

        private ViewHolder(View itemView) {
            super(itemView);

            imgGallery = itemView.findViewById(R.id.imgGallery);
            cardCancel = itemView.findViewById(R.id.cardCancel);

            cardCancel.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.cardCancel:
                    uriList.remove(getAdapterPosition());
                    Utils.uriGallery.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    notifyItemRangeChanged(getAdapterPosition(), uriList.size());
                    break;
            }
        }
    }
}
