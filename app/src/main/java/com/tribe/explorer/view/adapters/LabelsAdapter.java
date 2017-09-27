package com.tribe.explorer.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tribe.explorer.R;
import com.tribe.explorer.model.Operations;

import java.util.ArrayList;
import java.util.List;

public class LabelsAdapter extends RecyclerView.Adapter<LabelsAdapter.ViewHolder>{
    private Context context;
    private List<String> labelsList;

    public LabelsAdapter(Context context, List<String> labelsList) {
        this.context = context;
        this.labelsList = labelsList;
        Operations.labelsList = new ArrayList<>();
    }

    @Override
    public LabelsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_labels, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LabelsAdapter.ViewHolder holder, int position) {
        String label = labelsList.get(position);
        holder.tvLabel.setText(label);
        Operations.labelsList.add(label);
    }

    @Override
    public int getItemCount() {
        return labelsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvLabel;

        private ViewHolder(View itemView) {
            super(itemView);

            tvLabel = itemView.findViewById(R.id.tvLabel);
        }
    }
}
