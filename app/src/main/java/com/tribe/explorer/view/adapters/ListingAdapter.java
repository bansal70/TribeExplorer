package com.tribe.explorer.view.adapters;

/*
 * Created by rishav on 9/14/2017.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tribe.explorer.R;
import com.tribe.explorer.controller.ModelManager;
import com.tribe.explorer.model.Config;
import com.tribe.explorer.model.Operations;
import com.tribe.explorer.model.TEPreferences;
import com.tribe.explorer.model.beans.ListingData;

import java.util.List;

public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.ViewHolder> {
    private Context context;
    private List<ListingData.Data> listData;
    private String lang;
    private String user_id;
    private String isFav = "";

    public ListingAdapter(Context context, List<ListingData.Data> listData) {
        this.context = context;
        this.listData = listData;
        lang = TEPreferences.readString(context, "lang");
        user_id = TEPreferences.readString(context, "user_id");

    }

    @Override
    public ListingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_listing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListingAdapter.ViewHolder holder, int position) {
        ListingData.Data data = listData.get(position);
        holder.tvTitle.setText(data.title);
        holder.tvLocation.setText(data.loc);
        holder.tvPhone.setText(data.phone);
        float ratings = Float.parseFloat(data.rating);
        holder.rbRatings.setRating(ratings);

        if (data.fav.equals("yes"))
            holder.imgFavourite.setImageResource(R.mipmap.ic_favourite);
        else
            holder.imgFavourite.setImageResource(R.mipmap.ic_unfavourite);

        Glide.with(context)
                .load(data.featured)
                .centerCrop()
                .fitCenter()
                .crossFade()
                .into(holder.imgFeature);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tvTitle, tvLocation, tvPhone;
        private ImageView imgFeature, imgFavourite;
        private RatingBar rbRatings;

        private ViewHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            imgFeature = itemView.findViewById(R.id.imgCover);
            rbRatings = itemView.findViewById(R.id.rbRatings);
            imgFavourite = itemView.findViewById(R.id.imgFavourite);

            imgFavourite.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imgFavourite:
                    ListingData.Data data = listData.get(getAdapterPosition());
                    String listing_name = data.title;

                    Drawable.ConstantState constantState;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        constantState = context.getResources()
                                .getDrawable(R.mipmap.ic_favourite, context.getTheme())
                                .getConstantState();
                    } else {
                        constantState = context.getResources().getDrawable(R.mipmap.ic_favourite)
                                .getConstantState();
                    }

                    if (imgFavourite.getDrawable().getConstantState() == constantState) {
                        ModelManager.getInstance().getFavouriteManager().favouriteTask(Operations
                                .getFavouriteParams(Config.REMOVE_FAV_URL, user_id, data.iD, lang));
                        imgFavourite.setImageResource(R.mipmap.ic_unfavourite);
                        Toast.makeText(context, listing_name + context.getString(R.string.fav_removed),
                                Toast.LENGTH_SHORT).show();
                    }
                    else {
                        ModelManager.getInstance().getFavouriteManager().favouriteTask(Operations
                                .getFavouriteParams(Config.ADD_FAV_URL, user_id, data.iD, lang));
                        imgFavourite.setImageResource(R.mipmap.ic_favourite);
                        Toast.makeText(context, listing_name + context.getString(R.string.fav_added),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

}
