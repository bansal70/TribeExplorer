package com.tribe.explorer.view.adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tribe.explorer.R;
import com.tribe.explorer.model.Operations;
import com.tribe.explorer.model.Utils;
import com.tribe.explorer.model.beans.HoursData;
import com.tribe.explorer.model.beans.ListingData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EditHoursAdapter extends RecyclerView.Adapter<EditHoursAdapter.ViewHolder> {

    private Context context;
    private ArrayList<HoursData> daysList;
    private Dialog hoursDialog;
    private int pos;
    private String unit1, unit2;
    private String day="";
    private ListingData.Data listData;
    String hoursOperation = "";

    public EditHoursAdapter(Context context, ListingData.Data listData) {
        this.context = context;
        this.listData = listData;
        daysList = new ArrayList<>();
        hoursDialog = Utils.createDialog(context, R.layout.dialog_hours);
        unit1 = context.getString(R.string.AM);
        unit2 = context.getString(R.string.PM);
        addDays();
        initDialog();
        Operations.hours = new ArrayList<>();
        Operations.hoursOfOperation = "";
        Operations.jsonObject = new JSONObject();
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
        ListingData.JobHours jobHours = listData.jobHours;
        Gson gson = new Gson();
        String json = gson.toJson(jobHours);

        try {
            JSONObject issueObj = new JSONObject(json);
            for (int j = 0; j < issueObj.length(); j++) {
                String key = issueObj.names().getString(j);
                String day = daysList.get(position).getDays().toLowerCase().substring(0,
                        Math.min(daysList.get(position).getDays().length(), 3));
                Log.e("days", key);
                if (key.equals(day)) {
                    JSONArray jsonArray = issueObj.getJSONArray(key);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String open = jsonObject.getString("open");
                    String close = jsonObject.getString("close");
                    data.setAm(open);
                    data.setPm(close);
                    holder.tvAM.setText(open);
                    holder.tvPM.setText(close);
                    hoursOperation = "&" + daysList.get(position).getDays() + "=" + jsonArray;
                    Operations.hoursOfOperation += hoursOperation;
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!data.getAm().isEmpty())
            holder.tvAM.setText(data.getAm());

        if (!data.getAm().isEmpty())
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
        final EditText editOpen1 = hoursDialog.findViewById(R.id.editOpen1);
        final EditText editOpen2 = hoursDialog.findViewById(R.id.editOpen2);
        final EditText editClose1 = hoursDialog.findViewById(R.id.editClose1);
        final EditText editClose2 = hoursDialog.findViewById(R.id.editClose2);
        final Button btnOpen = hoursDialog.findViewById(R.id.btnOpen);
        final Button btnClose = hoursDialog.findViewById(R.id.btnClose);

        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (unit1.equals(context.getString(R.string.AM))) {
                    unit1 = context.getString(R.string.PM);
                } else {
                    unit1 = context.getString(R.string.AM);
                }
                btnOpen.setText(unit1);
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (unit2.equals(context.getString(R.string.AM))) {
                    unit2 = context.getString(R.string.PM);
                } else {
                    unit2 = context.getString(R.string.AM);
                }
                btnClose.setText(unit2);
            }
        });

        TextView tvAdd = hoursDialog.findViewById(R.id.tvAdd);
        TextView tvCancel = hoursDialog.findViewById(R.id.tvCancel);

        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String open1 = editOpen1.getText().toString().trim();
                String open2 = editOpen2.getText().toString().trim();
                String close1 = editClose1.getText().toString().trim();
                String close2 = editClose2.getText().toString().trim();

                if (open1.isEmpty() || open2.isEmpty() || close1.isEmpty() || close2.isEmpty()) {
                    Toast.makeText(context, R.string.fill_required_fields, Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(open1) > 12 || Integer.parseInt(close1) > 12) {
                    Toast.makeText(context, R.string.hour_format, Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(open2) > 59 || Integer.parseInt(close2) > 59) {
                    Toast.makeText(context, R.string.minutes_range, Toast.LENGTH_SHORT).show();
                }
                else {
                    hoursDialog.dismiss();
                    String openTime = open1 + ":" + open2 + " " + unit1;
                    String closeTime = close1 + ":" + close2 + " " + unit2;

                    HoursData hoursData = daysList.get(pos);
                    hoursData.setAm(openTime);
                    hoursData.setPm(closeTime);

                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("open", openTime);
                        jsonObject.put("close", closeTime);
                        JSONArray jsonArray = new JSONArray();
                        jsonArray.put(jsonObject);
                        if (pos==0) {
                            day = "&Monday="+jsonArray.toString();
                        } else if (pos == 1) {
                            day = "&Tuesday="+jsonArray.toString();
                        } else if (pos == 2) {
                            day = "&Wednesday="+jsonArray.toString();
                        } else if (pos == 3) {
                            day = "&Thursday="+jsonArray.toString();
                        } else if (pos == 4) {
                            day = "&Friday="+jsonArray.toString();
                        } else if (pos == 5) {
                            day = "&Saturday="+jsonArray.toString();
                        } else if (pos == 6) {
                            day = "&Sunday="+jsonArray.toString();
                        }

                        Operations.hours.add(day);
                        Operations.hoursOfOperation += day;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    daysList.set(pos, hoursData);
                    notifyItemChanged(pos);
                }
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
