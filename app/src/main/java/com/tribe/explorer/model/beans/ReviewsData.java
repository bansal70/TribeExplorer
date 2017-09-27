package com.tribe.explorer.model.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewsData {
    @SerializedName("status")
    public String status;
    @SerializedName("data")
    public List<Data> data = null;

    public class Data {
        @SerializedName("comment_ID")
        @Expose
        public String commentID;
        @SerializedName("comment")
        @Expose
        public String comment;
        @SerializedName("comment_author")
        @Expose
        public String commentAuthor;
        @SerializedName("comment_author_email")
        @Expose
        public String commentAuthorEmail;
        @SerializedName("comment_author_url")
        @Expose
        public String commentAuthorUrl;
        @SerializedName("comment_author_IP")
        @Expose
        public String commentAuthorIP;
        @SerializedName("comment_date")
        @Expose
        public String commentDate;
        @SerializedName("comment_date_gmt")
        @Expose
        public String commentDateGmt;
        @SerializedName("comment_karma")
        @Expose
        public String commentKarma;
        @SerializedName("comment_approved")
        @Expose
        public String commentApproved;
        @SerializedName("comment_agent")
        @Expose
        public String commentAgent;
        @SerializedName("comment_type")
        @Expose
        public String commentType;
        @SerializedName("comment_parent")
        @Expose
        public String commentParent;
        @SerializedName("user_id")
        @Expose
        public String userId;
        @SerializedName("speed")
        @Expose
        public String speed;
        @SerializedName("quantity")
        @Expose
        public String quantity;
        @SerializedName("price")
        @Expose
        public String price;
        @SerializedName("image")
        @Expose
        public String image;
    }
}
