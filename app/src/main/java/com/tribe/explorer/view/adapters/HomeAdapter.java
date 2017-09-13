package com.tribe.explorer.view.adapters;

/*
 * Created by rishav on 9/12/2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tribe.explorer.R;
import com.tribe.explorer.model.beans.CategoriesData;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private Context context;
    private List<CategoriesData.Data> categoriesList;

    public HomeAdapter(Context context, List<CategoriesData.Data> categoriesList) {
        this.context = context;
        this.categoriesList = categoriesList;
    }

    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_categories, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeAdapter.ViewHolder holder, int position) {
        CategoriesData.Data data = categoriesList.get(position);

        String image = data.img;
        String name = data.name;

        holder.tvCategories.setText(name);

        Glide.with(context)
                .load(image)
                .crossFade()
                .fitCenter()
                .centerCrop()
                .into(holder.imgCategory);
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgCategory;
        private TextView tvCategories;

        private ViewHolder(View itemView) {
            super(itemView);

            imgCategory = itemView.findViewById(R.id.imgCategories);
            tvCategories = itemView.findViewById(R.id.tvCategories);
        }
    }
}
