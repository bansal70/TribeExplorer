package com.tribe.explorer.view.adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tribe.explorer.R;
import com.tribe.explorer.model.Utils;

import java.util.List;

public class PhotoListAdapter extends RecyclerView.Adapter<PhotoListAdapter.ViewHolder>{
    private Context context;
    private List<String> galleryImages;
    private Dialog dialog;
    private ImageView imgPhoto;

    public PhotoListAdapter(Context context, List<String> galleryImages) {
        this.context = context;
        this.galleryImages = galleryImages;
        dialog = Utils.createDialog(context, R.layout.dialog_image);
        initDialog();
    }

    @Override
    public PhotoListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_photo_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoListAdapter.ViewHolder holder, int position) {
        String image = galleryImages.get(position);
        Utils.setImage(context, image, holder.imgGallery);
    }

    @Override
    public int getItemCount() {
        return galleryImages.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView imgGallery;

        private ViewHolder(View itemView) {
            super(itemView);

            imgGallery = itemView.findViewById(R.id.imgGallery);
            imgGallery.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imgGallery:
                    String image = galleryImages.get(getAdapterPosition());
                    Utils.setImage(context, image, imgPhoto);
                    dialog.show();
                    break;
            }
        }
    }

    private void initDialog() {
        imgPhoto = dialog.findViewById(R.id.imgPhoto);
        dialog.setCancelable(true);
        ImageView imgCancel = dialog.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}
