package com.tribe.explorer.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tribe.explorer.R;
import com.tribe.explorer.controller.ModelManager;
import com.tribe.explorer.model.Config;
import com.tribe.explorer.model.Operations;
import com.tribe.explorer.model.TEPreferences;
import com.tribe.explorer.model.Utils;
import com.tribe.explorer.model.beans.ListingData;
import com.tribe.explorer.view.MainActivity;
import com.tribe.explorer.view.fragments.ListingDetailsFragment;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    private Context context;
    private List<ListingData.Data> listData;
    private String lang;
    private String user_id;
    private String isFav = "";

    public FavoriteAdapter(Context context, List<ListingData.Data> listData) {
        this.context = context;
        this.listData = listData;
        lang = TEPreferences.readString(context, "lang");
        user_id = TEPreferences.readString(context, "user_id");

    }

    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_listing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FavoriteAdapter.ViewHolder holder, int position) {
        ListingData.Data data = listData.get(position);
        holder.tvTitle.setText(data.title);
        holder.tvLocation.setText(data.loc);
        holder.tvPhone.setText(data.phone);
        float ratings = 0.0f;
        if (!data.rating.isEmpty())
            ratings = Float.parseFloat(data.rating);

        holder.rbRatings.setRating(ratings);

        if (data.claimed.equals("1"))
            holder.imgClaim.setVisibility(View.VISIBLE);
        else
            holder.imgClaim.setVisibility(View.GONE);

        if (data.fav.equals("yes"))
            holder.imgFavourite.setImageResource(R.mipmap.ic_favourite);
        else
            holder.imgFavourite.setImageResource(R.mipmap.ic_unfavourite);

        Utils.setImage(context, data.featured, holder.imgFeature);
        Utils.setImage(context, data.companyAvatar, holder.imgAvatar);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tvTitle, tvLocation, tvPhone;
        private ImageView imgFeature, imgFavourite, imgAvatar, imgClaim;
        private RatingBar rbRatings;

        private ViewHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            imgFeature = itemView.findViewById(R.id.imgCover);
            rbRatings = itemView.findViewById(R.id.rbRatings);
            imgFavourite = itemView.findViewById(R.id.imgFavourite);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            imgClaim = itemView.findViewById(R.id.imgClaim);

            imgFavourite.setOnClickListener(this);
            imgFeature.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imgCover:
                    replaceFragment(getAdapterPosition());
                    break;

                case R.id.imgFavourite:
                    if (user_id.isEmpty()) {
                        Toast.makeText(context, R.string.login_to_fav, Toast.LENGTH_SHORT).show();
                        return;
                    }

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
                        Toast.makeText(context, listing_name + " " +
                                context.getString(R.string.fav_removed), Toast.LENGTH_LONG).show();
                        listData.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(), listData.size());
                    }
                    else {
                        ModelManager.getInstance().getFavouriteManager().favouriteTask(Operations
                                .getFavouriteParams(Config.ADD_FAV_URL, user_id, data.iD, lang));
                        imgFavourite.setImageResource(R.mipmap.ic_favourite);
                        Toast.makeText(context, listing_name + " " +
                                context.getString(R.string.fav_added), Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    }

    private void replaceFragment(int position) {
        ListingData.Data data = listData.get(position);
        Fragment fragment = new ListingDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("cat_id", data.iD);
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}