package com.tribe.explorer.controller;

/*
 * Created by rishav on 9/12/2017.
 */

public class ModelManager {

    private static ModelManager modelManager;
    private SimpleLoginManager loginManager;
    private RegistrationManager registrationManager;
    private HomeManager homeManager;
    private LanguageManager languageManager;
    private ListingManager listingManager;
    private FavouriteManager favouriteManager;
    private AboutManager aboutManager;
    private ForgotPasswordManager forgotPasswordManager;
    private ProfileManager profileManager;

    public static ModelManager getInstance() {
        if (modelManager == null)
            return modelManager = new ModelManager();
        else
            return modelManager;
    }

    private ModelManager() {
        loginManager = new SimpleLoginManager();
        registrationManager = new RegistrationManager();
        homeManager = new HomeManager();
        languageManager = new LanguageManager();
        listingManager = new ListingManager();
        favouriteManager = new FavouriteManager();
        aboutManager = new AboutManager();
        forgotPasswordManager = new ForgotPasswordManager();
        profileManager = new ProfileManager();
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
}
