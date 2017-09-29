package com.tribe.explorer.view.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tribe.explorer.R;
import com.tribe.explorer.model.Utils;
import com.tribe.explorer.model.beans.ReviewsData;
import com.tribe.explorer.model.custom.CircleTransform;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private Context context;
    private List<ReviewsData.Data> reviewsList;

    public ReviewAdapter(Context context, List<ReviewsData.Data> reviewsList) {
        this.context = context;
        this.reviewsList = reviewsList;
    }

    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_reviews, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ViewHolder holder, int position) {
        ReviewsData.Data reviewsData = reviewsList.get(position);
        float speed = Float.parseFloat(reviewsData.speed);
        float quality = Float.parseFloat(reviewsData.quantity);
        float price = Float.parseFloat(reviewsData.price);

        holder.tvUser.setText(reviewsData.commentAuthorEmail);
        holder.tvTime.setText(reviewsData.commentDateGmt);

        if (reviewsData.image.isEmpty())
            holder.imgReview.setVisibility(View.GONE);
        else
            holder.imgReview.setVisibility(View.VISIBLE);

        Utils.setImage(context, reviewsData.image, holder.imgReview);
        holder.tvComment.setText(reviewsData.comment);

        holder.rbSpeed.setRating(speed);
        holder.rbQuality.setRating(quality);
        holder.rbPrice.setRating(price);

        if (reviewsData.commentAuthorUrl != null)
        Glide.with(context)
                .load(reviewsData.commentAuthorUrl)
                .transform(new CircleTransform(context))
                .placeholder(R.mipmap.ic_user)
                .into(holder.imgUser);
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgUser, imgReview;
        private RatingBar rbSpeed, rbQuality, rbPrice;
        private TextView tvUser, tvTime, tvComment;

        public ViewHolder(View itemView) {
            super(itemView);

            imgUser = itemView.findViewById(R.id.imgUser);
            imgReview = itemView.findViewById(R.id.imgReview);
            rbSpeed = itemView.findViewById(R.id.rbSpeed);
            rbQuality = itemView.findViewById(R.id.rbQuality);
            rbPrice = itemView.findViewById(R.id.rbPrice);
            tvUser = itemView.findViewById(R.id.tvUser);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvComment = itemView.findViewById(R.id.tvComment);
        }
    }
}
