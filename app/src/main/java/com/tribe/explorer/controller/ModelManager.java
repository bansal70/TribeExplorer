package com.tribe.explorer.controller;

/*
 * Created by rishav on 9/12/2017.
 */

public class ModelManager {

    private static ModelManager modelManager;
    private SimpleLoginManager loginManager;
    private RegistrationManager registrationManager;
    private FacebookLoginManager facebookLoginManager;
    private TwitterLoginManager twitterLoginManager;
    private HomeManager homeManager;
    private LanguageManager languageManager;
    private ListingManager listingManager;
    private ListingDetailsManager listingDetailsManager;
    private FavouriteManager favouriteManager;
    private AboutManager aboutManager;
    private ForgotPasswordManager forgotPasswordManager;
    private ProfileManager profileManager;
    private LocationManager locationManager;
    private LabelsManager labelsManager;
    private AddReviewManager addReviewManager;
    private ReviewsManager reviewsManager;
    private ClaimListingManager claimListingManager;
    private AddListingManager addListingManager;
    private AddPhotoManager addPhotoManager;
    private DeleteListingManager deleteListingManager;

    public static ModelManager getInstance() {
        if (modelManager == null)
            return modelManager = new ModelManager();
        else
            return modelManager;
    }

    private ModelManager() {
        loginManager = new SimpleLoginManager();
        facebookLoginManager = new FacebookLoginManager();
        twitterLoginManager = new TwitterLoginManager();
        registrationManager = new RegistrationManager();
        homeManager = new HomeManager();
        languageManager = new LanguageManager();
        listingManager = new ListingManager();
        favouriteManager = new FavouriteManager();
        aboutManager = new AboutManager();
        forgotPasswordManager = new ForgotPasswordManager();
        profileManager = new ProfileManager();
        listingDetailsManager = new ListingDetailsManager();
        locationManager = new LocationManager();
        labelsManager = new LabelsManager();
        addReviewManager = new AddReviewManager();
        reviewsManager = new ReviewsManager();
        claimListingManager = new ClaimListingManager();
        addListingManager = new AddListingManager();
        addPhotoManager = new AddPhotoManager();
        deleteListingManager = new DeleteListingManager();
    }

    public SimpleLoginManager getLoginManager() {
        return loginManager;
    }

    public RegistrationManager getRegistrationManager() {
        return registrationManager;
    }

    public HomeManager getHomeManager() {
        return homeManager;
    }

    public LanguageManager getLanguageManager() {
        return languageManager;
    }

    public ListingManager getListingManager() {
        return listingManager;
    }

    public FavouriteManager getFavouriteManager() {
        return favouriteManager;
    }

    public AboutManager getAboutManager() {
        return aboutManager;
    }

    public ForgotPasswordManager getForgotPasswordManager() {
        return forgotPasswordManager;
    }

    public ProfileManager getProfileManager() {
        return profileManager;
    }

    public ListingDetailsManager getListingDetailsManager() {
        return listingDetailsManager;
    }

    public LocationManager getLocationManager() {
        return locationManager;
    }

    public FacebookLoginManager getFacebookLoginManager() {
        return facebookLoginManager;
    }

    public TwitterLoginManager getTwitterLoginManager() {
        return twitterLoginManager;
    }

    public LabelsManager getLabelsManager() {
        return labelsManager;
    }

    public AddReviewManager getAddReviewManager() {
        return addReviewManager;
    }

    public ReviewsManager getReviewsManager() {
        return reviewsManager;
    }

    public ClaimListingManager getClaimListingManager() {
        return claimListingManager;
    }

    public AddListingManager getAddListingManager() {
        return addListingManager;
    }

    public AddPhotoManager getAddPhotoManager() {
        return addPhotoManager;
    }

    public DeleteListingManager getDeleteListingManager() {
        return deleteListingManager;
    }
}
