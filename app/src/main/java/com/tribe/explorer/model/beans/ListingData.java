package com.tribe.explorer.model.beans;

/*
 * Created by rishav on 9/14/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListingData {
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("data")
    @Expose
    public List<Data> data = null;

    public class Data {
        @SerializedName("ID")
        @Expose
        public Integer iD;
        @SerializedName("title")
        @Expose
        public String title;
        @SerializedName("content")
        @Expose
        public String content;
        @SerializedName("loc")
        @Expose
        public String loc;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("facebook_url")
        @Expose
        public String facebookUrl;
        @SerializedName("twitter_url")
        @Expose
        public String twitterUrl;
        @SerializedName("instagram_url")
        @Expose
        public String instagramUrl;
        @SerializedName("contact_email")
        @Expose
        public String contactEmail;
        @SerializedName("website")
        @Expose
        public String website;
        @SerializedName("vedio")
        @Expose
        public String vedio;
        @SerializedName("featured")
        @Expose
        public String featured;
        @SerializedName("job_expires")
        @Expose
        public String jobExpires;
        @SerializedName("phone")
        @Expose
        public String phone;
        @SerializedName("company_avatar")
        @Expose
        public String companyAvatar;
        @SerializedName("job_author")
        @Expose
        public String jobAuthor;
        @SerializedName("gallery_images")
        @Expose
        public List<String> galleryImages = null;
        @SerializedName("products")
        @Expose
        public List<Object> products = null;
        @SerializedName("category")
        @Expose
        public List<String> category = null;
        @SerializedName("category_id")
        @Expose
        public List<Integer> category_id = null;
        @SerializedName("job_hours")
        @Expose
        public JobHours jobHours;
        @SerializedName("region")
        @Expose
        public List<String> region = null;
        @SerializedName("job_hours_timezone")
        @Expose
        public String jobHoursTimezone;
        @SerializedName("label")
        @Expose
        public List<String> label = null;
        @SerializedName("social")
        @Expose
        public List<Social> social = null;
        /* @SerializedName("review")
         @Expose
         public List<Review> review = null;*/
        @SerializedName("review_count")
        @Expose
        public int reviewCount;
        @SerializedName("rating")
        @Expose
        public String rating;
        @SerializedName("fav")
        @Expose
        public String fav;
        @SerializedName("fav_count")
        @Expose
        public String favCount;
        @SerializedName("cover_image")
        @Expose
        public String coverImage;
        @SerializedName("latitude")
        @Expose
        public String latitude;
        @SerializedName("longitude")
        @Expose
        public String longitude;
        @SerializedName("language")
        @Expose
        public List<Language> language = null;
        @SerializedName("claimed")
        @Expose
        public String claimed;
    }

    public class Social {
        @SerializedName("company_instagram")
        @Expose
        public String companyInstagram;
        @SerializedName("company_linkedin")
        @Expose
        public String companyLinkedin;
        @SerializedName("company_googleplus")
        @Expose
        public String companyGoogleplus;
        @SerializedName("company_twitter")
        @Expose
        public String companyTwitter;
        @SerializedName("company_facebook")
        @Expose
        public String companyFacebook;
        @SerializedName("company_pinterest")
        @Expose
        public String companyPinterest;
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
        @Expose
        public List<Mon> mon = null;
        @SerializedName("tue")
        @Expose
        public List<Tue> tue = null;
        @SerializedName("wed")
        @Expose
        public List<Wed> wed = null;
        @SerializedName("thu")
        @Expose
        public List<Thu> thu = null;
        @SerializedName("fri")
        @Expose
        public List<Fri> fri = null;
        @SerializedName("sat")
        @Expose
        public List<Sat> sat = null;
        @SerializedName("sun")
        @Expose
        public List<Sun> sun = null;
    }

    public class Mon {
        @SerializedName("open")
        @Expose
        public String open;
        @SerializedName("close")
        @Expose
        public String close;
    }

    public class Tue {
        @SerializedName("open")
        @Expose
        public String open;
        @SerializedName("close")
        @Expose
        public String close;
    }

    public class Wed {
        @SerializedName("open")
        @Expose
        public String open;
        @SerializedName("close")
        @Expose
        public String close;
    }

    public class Thu {
        @SerializedName("open")
        @Expose
        public String open;
        @SerializedName("close")
        @Expose
        public String close;
    }

    public class Fri {
        @SerializedName("open")
        @Expose
        public String open;
        @SerializedName("close")
        @Expose
        public String close;
    }

    public class Sat {
        @SerializedName("open")
        @Expose
        public String open;
        @SerializedName("close")
        @Expose
        public String close;
    }

    public class Sun {
        @SerializedName("open")
        @Expose
        public String open;
        @SerializedName("close")
        @Expose
        public String close;
    }
}
