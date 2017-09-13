package com.tribe.explorer.controller;

/*
 * Created by rishav on 9/12/2017.
 */

public class ModelManager {

    private static ModelManager modelManager;
    private SimpleLoginManager loginManager;
    private RegistrationManager registrationManager;
    private HomeManager homeManager;

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
}
