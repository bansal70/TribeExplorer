package com.tribe.explorer.view.adapters;

/*
 * Created by rishav on 9/15/2017.
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
import com.tribe.explorer.model.beans.AboutData;

import java.util.List;

public class HowItWorkAdapter extends RecyclerView.Adapter<HowItWorkAdapter.ViewHolder> {
    private Context context;
    private List<AboutData.Data> dataList;

    public HowItWorkAdapter(Context context, List<AboutData.Data> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public HowItWorkAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_how_it_works, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HowItWorkAdapter.ViewHolder holder, int position) {
        AboutData.Data data = dataList.get(position);
        holder.tvTitle.setText(data.title);
        holder.tvContent.setText(data.content);

        Glide.with(context)
                .load(data.featuredImage)
                .crossFade()
                .fitCenter()
                .centerCrop()
                .placeholder(R.mipmap.placeholder)
                .error(R.mipmap.placeholder)
                .into(holder.imgFeatured);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvContent;
        private ImageView imgFeatured;

        private ViewHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            imgFeatured = itemView.findViewById(R.id.imgFeatured);
        }
    }
}
