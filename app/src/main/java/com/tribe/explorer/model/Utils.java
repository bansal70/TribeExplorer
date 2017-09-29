package com.tribe.explorer.model;

/*
 * Created by rishav on 9/12/2017.
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tribe.explorer.R;
import com.tribe.explorer.model.beans.Language;
import com.tribe.explorer.view.LoginActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class Utils {

    public static String location = "";
    public static List<Language> langList;

    @SuppressWarnings("ConstantConditions")
    public static void dialogError(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_error);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView tvRetry = dialog.findViewById(R.id.tvRetry);
        tvRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().postSticky(new Event(Constants.RETRY, ""));
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @SuppressWarnings("ConstantConditions")
    public static Dialog showDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        return dialog;
    }

    @SuppressWarnings("ConstantConditions")
    public static Dialog createDialog(Context context, int layout) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layout);
        dialog.setCancelable(false);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        return dialog;
    }

    public static boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void setImage(Context context, String imageURL, ImageView view) {
        Glide.with(context)
                .load(imageURL)
                .fitCenter().centerCrop()
                .dontAnimate()
                .placeholder(R.mipmap.placeholder)
                .error(R.mipmap.placeholder)
                .into(view);
    }

    public static String getCompleteAddressString(Context context, double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

              /*  for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i));
                }*/
                strReturnedAddress.append(returnedAddress.getAddressLine(0));

                if (returnedAddress.getSubLocality() != null)
                    strReturnedAddress.append(", ").append(returnedAddress.getSubLocality());

                strAdd = strReturnedAddress.toString();
                // Log.e(TAG, "address-- "+strReturnedAddress.toString());
            } else {
                Log.e(TAG, "No address returned");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }

    public static void setThumbnail(final ImageView imageView, final String videoPath,
                                    final ImageView imgPlay) {
        new AsyncTask<Void, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap bitmap = null;
                MediaMetadataRetriever mediaMetadataRetriever = null;
                try {
                    mediaMetadataRetriever = new MediaMetadataRetriever();
                    mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
                    //   mediaMetadataRetriever.setDataSource(videoPath);
                    bitmap = mediaMetadataRetriever.getFrameAtTime(10);
                } catch (Exception e) {
                    e.printStackTrace();

                } finally {
                    if (mediaMetadataRetriever != null)
                        mediaMetadataRetriever.release();
                }
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                    imgPlay.setVisibility(View.VISIBLE);
                }
                else {
                    imageView.setImageResource(R.mipmap.ic_no_thumb);
                    imgPlay.setVisibility(View.GONE);
                }
            }
        }.execute();
    }

    public static void logoutAlert(final Activity context) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setMessage("Would you like to go out of the app?");

        alertBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                TEPreferences.clearPref(context);
                context.startActivity(new Intent(context, LoginActivity.class));
                context.finish();
                Toast.makeText(context, R.string.logged_out, Toast.LENGTH_SHORT).show();
            }
        });
        alertBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }
}
