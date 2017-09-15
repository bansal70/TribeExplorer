package com.tribe.explorer.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tribe.explorer.R;
import com.tribe.explorer.model.beans.Language;

import java.util.List;

/*
 * Created by rishav on 9/14/2017.
 */

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {
    private Context context;
    private List<Language> languagesList;

    public LanguageAdapter(Context context, List<Language> languagesList) {
        this.context = context;
        this.languagesList = languagesList;
    }

    @Override
    public LanguageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_language, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LanguageAdapter.ViewHolder holder, int position) {
        Language data = languagesList.get(position);
        holder.tvLanguage.setText(data.getLanguage());
        holder.tvOwner.setText(data.getOwner());
    }

    @Override
    public int getItemCount() {
        return languagesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tvOwner, tvLanguage;
        private ImageView imgDelete;

        private ViewHolder(View itemView) {
            super(itemView);

            tvOwner = itemView.findViewById(R.id.tvOwner);
            tvLanguage = itemView.findViewById(R.id.tvLanguage);
            imgDelete = itemView.findViewById(R.id.imgDelete);

            imgDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            languagesList.remove(getAdapterPosition());
            notifyItemRemoved(getAdapterPosition());
            notifyItemRangeChanged(getAdapterPosition(), languagesList.size());
        }
    }
}
