package com.tribe.explorer.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tribe.explorer.R;
import com.tribe.explorer.model.beans.CategoriesData;

import java.util.List;

/*0
 * Created by rishav on 9/14/2017.
 */

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {
    private Context context;
    private List<CategoriesData.Data> categoriesList;

    public CategoriesAdapter(Context context, List<CategoriesData.Data> categoriesList) {
        this.context = context;
        this.categoriesList = categoriesList;
    }

    @Override
    public CategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_spinner_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoriesAdapter.ViewHolder holder, int position) {
        CategoriesData.Data data = categoriesList.get(position);
        String category = data.name;
        holder.tvCategories.setText(category);
        holder.cbCategories.setChecked(categoriesList.get(position).isSelected());
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tvCategories;
        private CheckBox cbCategories;

        private ViewHolder(View itemView) {
            super(itemView);

            tvCategories = itemView.findViewById(R.id.tvCategories);
            cbCategories = itemView.findViewById(R.id.cbCategory);

            cbCategories.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            CategoriesData.Data data = categoriesList.get(getAdapterPosition());
            data.setSelected(cbCategories.isChecked());
            data.setSelected(cbCategories.isChecked());

        }
    }
}
