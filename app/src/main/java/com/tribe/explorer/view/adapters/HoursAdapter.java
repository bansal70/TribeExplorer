package com.tribe.explorer.view.adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.tribe.explorer.R;
import com.tribe.explorer.model.Utils;
import com.tribe.explorer.model.beans.HoursData;

import java.util.ArrayList;

/*
 * Created by win 10 on 9/13/2017.
 */

public class HoursAdapter extends RecyclerView.Adapter<HoursAdapter.ViewHolder> {

    private Context context;
    private ArrayList<HoursData> daysList;
    private Dialog hoursDialog;
    private int pos;

    public HoursAdapter(Context context) {
        this.context = context;
        daysList = new ArrayList<>();
        hoursDialog = Utils.createDialog(context, R.layout.dialog_hours);

        addDays();
        initDialog();
    }

    private void addDays() {
        daysList.add(new HoursData(context.getString(R.string.monday), "", ""));
        daysList.add(new HoursData(context.getString(R.string.tuesday), "", ""));
        daysList.add(new HoursData(context.getString(R.string.wednesday), "", ""));
        daysList.add(new HoursData(context.getString(R.string.thursday), "", ""));
        daysList.add(new HoursData(context.getString(R.string.friday), "", ""));
        daysList.add(new HoursData(context.getString(R.string.saturday), "", ""));
        daysList.add(new HoursData(context.getString(R.string.sunday), "", ""));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.hours_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HoursData data = daysList.get(position);
        holder.tvDays.setText(data.getDays());

        if (!data.getAm().isEmpty())
            holder.tvAM.setText(data.getAm());

        if (!data.getPm().isEmpty())
            holder.tvPM.setText(data.getPm());
    }

    @Override
    public int getItemCount() {
        return daysList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tvDays, tvAM, tvPM;

        private ViewHolder(View itemView) {
            super(itemView);

            tvDays = itemView.findViewById(R.id.tvDays);
            tvAM = itemView.findViewById(R.id.tvAM);
            tvPM = itemView.findViewById(R.id.tvPM);

            tvDays.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tvDays:
                    hoursDialog.show();
                    pos = getAdapterPosition();

                    break;
            }
        }
    }

    private void initDialog() {
        final EditText editOpenTime = hoursDialog.findViewById(R.id.editOpenTime);
        final EditText editCloseTime = hoursDialog.findViewById(R.id.editCloseTime);
        TextView tvAdd = hoursDialog.findViewById(R.id.tvAdd);
        TextView tvCancel = hoursDialog.findViewById(R.id.tvCancel);

        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hoursDialog.dismiss();
                String openTime = editOpenTime.getText().toString().trim();
                String closeTime = editCloseTime.getText().toString().trim();

                HoursData hoursData = daysList.get(pos);
                hoursData.setAm(openTime);
                hoursData.setPm(closeTime);

                daysList.set(pos, hoursData);
                notifyItemChanged(pos);
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hoursDialog.dismiss();
            }
        });

    }
}
