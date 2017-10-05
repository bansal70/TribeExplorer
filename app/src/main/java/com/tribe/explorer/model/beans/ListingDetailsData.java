package com.tribe.explorer.model.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListingDetailsData {
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("data")
    @Expose
    public List<Data> data = null;

    public class Data {
        @SerializedName("ID")
        public Integer ID;
        @SerializedName("auther_id")
        public Integer autherId;
        @SerializedName("title")
        public String title;
        @SerializedName("content")
        public String content;
        @SerializedName("loc")
        public String loc;
        @SerializedName("email")
        public String email;
        @SerializedName("contact_email")
        public String contactEmail;
        @SerializedName("facebook_url")
        public String facebookUrl;
        @SerializedName("twitter_url")
        public String twitterUrl;
        @SerializedName("instagram_url")
        public String instagramUrl;
        @SerializedName("website")
        public String website;
        @SerializedName("vedio")
        public String vedio;
        @SerializedName("featured")
        public String featured;
        @SerializedName("job_expires")
        public String jobExpires;
        @SerializedName("phone")
        public String phone;
        @SerializedName("company_avatar")
        public String companyAvatar;
        @SerializedName("job_author")
        public String jobAuthor;
        @SerializedName("gallery_images")
        public List<String> galleryImages = null;
        @SerializedName("products")
        public List<Object> products = null;
        @SerializedName("job_hours")
        public JobHours jobHours;
        @SerializedName("job_hours_timezone")
        public String jobHoursTimezone;
        @SerializedName("category")
        public List<String> category = null;
        @SerializedName("category_id")
        public List<Integer> categoryId = null;
        @SerializedName("label")
        public List<String> label = null;
        @SerializedName("region")
        public List<String> region = null;
        @SerializedName("social")
        public List<Social> social = null;
        @SerializedName("review")
        public List<Review> review = null;
        @SerializedName("review_count")
        @Expose
        public int reviewCount;
        @SerializedName("rating")
        public List<Rating> rating = null;
        @SerializedName("fav")
        public String fav;
        @SerializedName("fav_count")
        public String favCount;
        @SerializedName("cover_image")
        public String coverImage;
        @SerializedName("latitude")
        public String latitude;
        @SerializedName("longitude")
        public String longitude;
        @SerializedName("language")
        public List<Language> language = null;
        @SerializedName("claimed")
        public String claimed;
    }

    public class Language {
        @SerializedName("id")
        @Expose
        public String name;
        @SerializedName("language")
        @Expose
        public String language;
    }

    public class JobHours {
        @SerializedName("mon")
        public List<Mon> mon = null;
        @SerializedName("tue")
        public List<Tue> tue = null;
        @SerializedName("wed")
        public List<Wed> wed = null;
        @SerializedName("thu")
        public List<Thu> thu = null;
        @SerializedName("fri")
        public List<Fri> fri = null;
        @SerializedName("sat")
        public List<Sat> sat = null;
        @SerializedName("sun")
        public List<Sun> sun = null;
    }

    public class Mon {
        @SerializedName("open")
        public String open;
        @SerializedName("close")
        public String close;
    }

    public class Tue {
        @SerializedName("open")
        public String open;
        @SerializedName("close")
        public String close;
    }

    public class Wed {
        @SerializedName("open")
        public String open;
        @SerializedName("close")
        public String close;
    }

    public class Thu {
        @SerializedName("open")
        public String open;
        @SerializedName("close")
        public String close;
    }

    public class Fri {
        @SerializedName("open")
        public String open;
        @SerializedName("close")
        public String close;
    }

    public class Sat {
        @SerializedName("open")
        public String open;
        @SerializedName("close")
        public String close;
    }

    public class Sun {
        @SerializedName("open")
        public String open;
        @SerializedName("close")
        public String close;
    }

    public class Rating {
        @SerializedName("speed")
        public String speed;
        @SerializedName("quality")
        public String quality;
        @SerializedName("price")
        public String price;
    }

    public class Review {
        @SerializedName("comment_ID")
        public String commentID;
        @SerializedName("comment_author")
        public String commentAuthor;
        @SerializedName("comment_author_email")
        public String commentAuthorEmail;
        @SerializedName("comment_content")
        public String commentContent;
        @SerializedName("speed")
        public String speed;
        @SerializedName("quantity")
        public String quantity;
        @SerializedName("price")
        public String price;
    }

    public class Social {
        @SerializedName("company_instagram")
        public String companyInstagram;
        @SerializedName("company_linkedin")
        public String companyLinkedin;
        @SerializedName("company_googleplus")
        public String companyGoogleplus;
        @SerializedName("company_twitter")
        public String companyTwitter;
        @SerializedName("company_facebook")
        public String companyFacebook;
        @SerializedName("company_pinterest")
        public String companyPinterest;
    }

}
