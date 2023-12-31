package com.gnusl.actine.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.gnusl.actine.application.Atcine;
import com.gnusl.actine.enums.AppCategories;
import com.gnusl.actine.model.LatestPlayedPosition;
import com.gnusl.actine.model.Show;
import com.gnusl.actine.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class SharedPreferencesUtils {

    private final static String USER = "user";
    private final static String LatestPlayedPosition = "latestPlayedPosition";
    private final static String Token = "token";
    private final static String Token1 = "token1";
    private final static String Language = "Language";
    private final static String Category = "Category";
    private final static String ProfileId = "ProfileId";
    private final static String ProfileImageURL = "ProfileImageURL";
    private final static String SelectedPlan = "SelectedPlan";
    private final static String SelectedPlanPrice = "SelectedPlanPrice";
    private final static String LatestPlayedShow = "LatestPlayedShow";

    public static void saveUser(User user) {
        SharedPreferences.Editor sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Atcine.getAppContext()).edit();
        Gson gson = new Gson();
        sharedPreferences.putString(USER, gson.toJson(user));
        sharedPreferences.apply();

    }

    public static User getUser() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Atcine.getAppContext());
        Gson gson = new Gson();
        String json = sharedPreferences.getString(USER, "");
        if (json == null || json.isEmpty()) {
            return null;
        } else {
            Type type = (new TypeToken<User>() {
            }).getType();
            return (User) gson.fromJson(json, type);
        }
    }

    public static void saveLatestPlayedPosition(LatestPlayedPosition latestPlayedPosition) {
        SharedPreferences.Editor sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Atcine.getAppContext()).edit();
        Gson gson = new Gson();
        sharedPreferences.putString(LatestPlayedPosition, gson.toJson(latestPlayedPosition));
        sharedPreferences.apply();

    }

    public static LatestPlayedPosition getLatestPlayedPosition() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Atcine.getAppContext());
        Gson gson = new Gson();
        String json = sharedPreferences.getString(LatestPlayedPosition, "");
        if (json == null || json.isEmpty()) {
            return null;
        } else {
            Type type = (new TypeToken<LatestPlayedPosition>() {
            }).getType();
            return (LatestPlayedPosition) gson.fromJson(json, type);
        }
    }

    public static void saveLatestPlayedShow(Show latestPlayedShow) {
        SharedPreferences.Editor sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Atcine.getAppContext()).edit();
        Gson gson = new Gson();
        sharedPreferences.putString(LatestPlayedShow, gson.toJson(latestPlayedShow));
        sharedPreferences.apply();

    }

    public static Show getLatestPlayedShow() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Atcine.getAppContext());
        Gson gson = new Gson();
        String json = sharedPreferences.getString(LatestPlayedShow, "");
        if (json == null || json.isEmpty()) {
            return null;
        } else {
            Type type = (new TypeToken<Show>() {
            }).getType();
            return (Show) gson.fromJson(json, type);
        }
    }


    public static void saveToken(String token) {
        SharedPreferences.Editor sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Atcine.getAppContext()).edit();
        sharedPreferences.putString(Token1, token);
        token = "Bearer " + token;
        sharedPreferences.putString(Token, token);
        sharedPreferences.apply();

    }

    public static String getToken() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Atcine.getAppContext());
        return sharedPreferences.getString(Token, "");
    }
    public static String getToken1() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Atcine.getAppContext());
        return sharedPreferences.getString(Token1, "");
    }

    public static void saveCategory(String category) {
        SharedPreferences.Editor sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Atcine.getAppContext()).edit();
        sharedPreferences.putString(Category, category);
        sharedPreferences.apply();

    }

    public static AppCategories getCategory() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Atcine.getAppContext());
        String category = sharedPreferences.getString(Category, "movies");
        if (category != null) {
            switch (category) {
                case "movies": {
                    return AppCategories.Movies;
                }
                case "tvShows": {
                    return AppCategories.TvShows;
                }
            }
        }
        return null;
    }

    public static void saveCurrentProfile(int profileId) {
        SharedPreferences.Editor sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Atcine.getAppContext()).edit();
        sharedPreferences.putInt(ProfileId, profileId);
        sharedPreferences.apply();

    }

    public static int getCurrentProfile() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Atcine.getAppContext());

        return sharedPreferences.getInt(ProfileId, 0);
    }

    public static void saveCurrentSelectedPlan(String selectedPlan) {
        SharedPreferences.Editor sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Atcine.getAppContext()).edit();
        sharedPreferences.putString(SelectedPlan, selectedPlan);
        sharedPreferences.apply();

    }

    public static String getCurrentSelectedPlan() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Atcine.getAppContext());
        String selectedPlan = sharedPreferences.getString(SelectedPlan, "");
        return selectedPlan;
    }

    public static void saveCurrentSelectedPlanPrice(String selectedPlan) {
        SharedPreferences.Editor sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Atcine.getAppContext()).edit();
        sharedPreferences.putString(SelectedPlanPrice, selectedPlan + "$");
        sharedPreferences.apply();

    }

    public static String getCurrentSelectedPlanPrice() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Atcine.getAppContext());
        String selectedPlan = sharedPreferences.getString(SelectedPlanPrice, "");
        return selectedPlan;
    }


    public static void saveLanguage(Context c, String language) {
        SharedPreferences defaultSharedPreferences = c.getSharedPreferences("language", Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferences = defaultSharedPreferences.edit();
        sharedPreferences.clear();
        sharedPreferences.putString(Language, language);
        sharedPreferences.apply();

        Log.d("LANGUAGE in pref set", defaultSharedPreferences.getString(Language, "default"));

    }

    public static String getLanguage(Context c) {
        SharedPreferences sharedPreferences = c.getSharedPreferences("language", Context.MODE_PRIVATE);
        Log.d("LANGUAGE in pref get", sharedPreferences.getString(Language, "default"));
        return sharedPreferences.getString(Language, "en");
    }

    public static void clear() {
        PreferenceManager.getDefaultSharedPreferences(Atcine.getAppContext()).edit().clear().apply();
    }

    public static void saveCurrentProfileImageUrl(String imageUrl) {
        SharedPreferences.Editor sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Atcine.getAppContext()).edit();
        sharedPreferences.putString(ProfileImageURL, imageUrl);
        sharedPreferences.apply();
    }

    public static String getCurrentProfileImageUrl() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Atcine.getAppContext());
        return sharedPreferences.getString(ProfileImageURL, "");
    }
}

