package com.mcho.recipient.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {

    private static final String PREFERENCE_NAME = "MCHO_SF";
    private final SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;

    public SharedPrefs(Context context) {
        sharedpreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public String getUserId() {
        String userId = sharedpreferences.getString("USER_ID", "");
        return userId;
    }

    public void setUserId(String userId) {
        editor = sharedpreferences.edit();
        editor.putString("USER_ID", userId );
        editor.apply();
    }

    public String getRecipientId() {
        String recipientId = sharedpreferences.getString("RECIPIENT_ID", "");
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        editor = sharedpreferences.edit();
        editor.putString("RECIPIENT_ID", recipientId);
        editor.apply();
    }

    public String getToken() {
        String token = sharedpreferences.getString("TOKEN", "");
        return token;
    }

    public void setToken(String token) {
        editor = sharedpreferences.edit();
        editor.putString("TOKEN", token);
        editor.apply();
    }

    public void clearToken(){
        editor = sharedpreferences.edit();
        editor.putString("TOKEN", "");
        editor.apply();
    }
/*
    public String getFirebaseToken() {
        String fbToken = sharedpreferences.getString("FIREBASE_TOKEN", "NO_TOKEN");
        return fbToken;
    }

    public void setFirebaseToken(String token) {
        editor = sharedpreferences.edit();
        editor.putString("FIREBASE_TOKEN", token);
        editor.apply();
    }

    public String getDeviceId() {
        String deviceId = sharedpreferences.getString("DEVICE_ID", "NO_DEVICE_ID");
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        editor = sharedpreferences.edit();
        editor.putString("DEVICE_ID", deviceId);
        editor.apply();
    }

    public boolean isTutViewed() {
        boolean isviewed = sharedpreferences.getBoolean("TUT_VIEWED", false);
        return isviewed;
    }

    public void setTutViewed(boolean isviewed) {
        editor = sharedpreferences.edit();
        editor.putBoolean("TUT_VIEWED", isviewed);
        editor.apply();
    }

    public int getLastSection() {
        int lastSection = sharedpreferences.getInt("LAST_SECTION", 0);
        return lastSection;
    }

    public void setLastSection(int lastSection) {
        editor = sharedpreferences.edit();
        editor.putInt("LAST_SECTION", lastSection);
        editor.apply();
    }

    public int getLastSectionCustom() {
        int lastSectionCustom = sharedpreferences.getInt("LAST_SECTION_CUSTOM", 0);
        return lastSectionCustom;
    }

    public void setLastSectionCustom(int lastSectionCustom) {
        editor = sharedpreferences.edit();
        editor.putInt("LAST_SECTION_CUSTOM", lastSectionCustom);
        editor.apply();
    }

    public int getOptionId() {
        int optionId = sharedpreferences.getInt("OPTION_ID", 0);
        return optionId;
    }

    public void setOptionId(int optionId) {
        editor = sharedpreferences.edit();
        editor.putInt("OPTION_ID", optionId);
        editor.apply();
    }

    public String getName() {
        String name = sharedpreferences.getString("REG_NAME", "NAME");
        return name;
    }

    public void setName(String name) {
        editor = sharedpreferences.edit();
        editor.putString("REG_NAME", name);
        editor.apply();
    }

    public String getGender() {
        String gender = sharedpreferences.getString("REG_GENDER", "");
        return gender;
    }

    public void setGender(String gender) {
        editor = sharedpreferences.edit();
        editor.putString("REG_GENDER", gender);
        editor.apply();
    }

    public String getBdate() {
        String gender = sharedpreferences.getString("REG_BDATE", "");
        return gender;
    }

    public void setBdate(String bdate) {
        editor = sharedpreferences.edit();
        editor.putString("REG_BDATE", bdate);
        editor.apply();
    }
    public boolean isRegistered() {
        boolean isviewed = sharedpreferences.getBoolean("REGISTERED", false);
        return isviewed;
    }

    public void setAsRegistered(boolean isviewed) {
        editor = sharedpreferences.edit();
        editor.putBoolean("REGISTERED", isviewed);
        editor.apply();
    }

    public int getNotifCount() {
        int count = sharedpreferences.getInt("NOTIF_COUNT", 0);
        return count;
    }

    public void setNotifCount(int count) {
        editor = sharedpreferences.edit();
        editor.putInt("NOTIF_COUNT", count);
        editor.apply();
    }

    public void clearNotifCount() {
        editor = sharedpreferences.edit();
        editor.putInt("NOTIF_COUNT", 0);
        editor.apply();
    }
//    public void clearCount() {
//        SharedPreferences.Editor editor = sharedpreferences.edit();
//        editor.remove("count");
//        editor.commit();
//    }

    public boolean isQRTutViewed() {
        boolean isviewed = sharedpreferences.getBoolean("QRTUT_VIEWED", false);
        return isviewed;
    }

    public void setQRTutViewed(boolean isviewed) {
        editor = sharedpreferences.edit();
        editor.putBoolean("QRTUT_VIEWED", isviewed);
        editor.apply();
    }

    public boolean isMyInqutViewed() {
        boolean isviewed = sharedpreferences.getBoolean("MYINQTUT_VIEWED", false);
        return isviewed;
    }

    public void setMyInqTutViewed(boolean isviewed) {
        editor = sharedpreferences.edit();
        editor.putBoolean("MYINQTUT_VIEWED", isviewed);
        editor.apply();
    }

    public boolean hasStoragePermission() {
        boolean isviewed = sharedpreferences.getBoolean("STORAGE_PERMISSION", false);
        return isviewed;
    }

    public void setStoragePermission(boolean setValue) {
        editor = sharedpreferences.edit();
        editor.putBoolean("STORAGE_PERMISSION", setValue);
        editor.apply();
    }

    public int getLastSectionPosition() {
        int lastSectionPos = sharedpreferences.getInt("LAST_SECTION_POS", -1);
        return lastSectionPos;
    }

    public void setLastSectionPosition(int lastSectionPos) {
        editor = sharedpreferences.edit();
        editor.putInt("LAST_SECTION_POS", lastSectionPos);
        editor.apply();
    }

    public int getLastCustomSectionPosition() {
        int lastSectionPos = sharedpreferences.getInt("LAST_CUSTOM_SECTION_POS", -1);
        return lastSectionPos;
    }

    public void setLastCustomSectionPosition(int lastSectionPos) {
        editor = sharedpreferences.edit();
        editor.putInt("LAST_CUSTOM_SECTION_POS", lastSectionPos);
        editor.apply();
    }

    public String getPreferenceSections() {
        String sections = sharedpreferences.getString("PREF_SECTIONS", null);
        return sections;
    }

    public void setPreferenceSections(String sections) {
        editor = sharedpreferences.edit();
        editor.putString("PREF_SECTIONS", sections);
        editor.apply();
    }

    public boolean isSFCleared() {
        boolean iscleared = sharedpreferences.getBoolean("SF_CLEARED", false);
        return iscleared;
    }

    public void setSFCleared(boolean iscleared) {
        editor = sharedpreferences.edit();
        editor.putBoolean("SF_CLEARED", iscleared);
        editor.apply();
    }

    public void clearSharedPrefs(){
        editor = sharedpreferences.edit();
        editor.clear().apply();
    }*/

}
