package com.travel.enjoyindanang;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Base64;
import android.util.Log;

import com.facebook.appevents.AppEventsLogger;
import com.facebook.drawee.backends.pipeline.DraweeConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.kakao.auth.KakaoSDK;
import com.travel.enjoyindanang.model.UserInfo;
import com.travel.enjoyindanang.ui.activity.login.KakaoSDKAdapter;
import com.travel.enjoyindanang.utils.SharedPrefsUtils;
import com.travel.enjoyindanang.utils.Utils;
import com.travel.enjoyindanang.utils.config.AppUpdateConfiguration;
import com.travel.enjoyindanang.utils.helper.LanguageHelper;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.kakao.util.helper.Utility.getPackageInfo;

/**
 * Created by chien on 10/8/17.
 */

public class GlobalApplication extends MultiDexApplication{
    private static final String TAG = GlobalApplication.class.getSimpleName();
    private static volatile GlobalApplication sInstance = null;
    private static volatile Activity currentActivity = null;
    public JSONObject jsLanguage;
    private static UserInfo userInfo;
    private boolean hasSessionLogin;

    @Override
    public void onCreate() {
        super.onCreate();
        ImagePipelineConfig frescoConfig = ImagePipelineConfig.newBuilder(getApplicationContext()) .setDownsampleEnabled(true).build();
        DraweeConfig draweeConfig = DraweeConfig.newBuilder()
                .build();
        Fresco.initialize(this, frescoConfig, draweeConfig);
        sInstance = this;
        AppEventsLogger.activateApp(this);
        KakaoSDK.init(new KakaoSDKAdapter());
        new AppUpdateConfiguration().configFirebaseUpdate();
        SharedPrefsUtils.setContext(this);
        hasSessionLogin = Utils.hasSessionLogin();
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    public static void setCurrentActivity(Activity currentActivity) {
        GlobalApplication.currentActivity = currentActivity;
    }

    /**
     * singleton 애플리케이션 객체를 얻는다.
     * @return singleton 애플리케이션 객체
     */
    public static GlobalApplication getGlobalApplicationContext() {
        if(sInstance == null)
            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
        return sInstance;
    }

    /**
     * 애플리케이션 종료시 singleton 어플리케이션 객체 초기화한다.
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        sInstance = null;
    }

    public JSONObject getJsLanguage() {
        return jsLanguage;
    }

    public void setJsLanguage(JSONObject jsLanguage) {
        this.jsLanguage = jsLanguage;
    }

    public static UserInfo getUserInfo() {
        return userInfo;
    }

    public static void setUserInfo(UserInfo userInfo) {
        GlobalApplication.userInfo = userInfo;
    }

    public boolean isHasSessionLogin() {
        return hasSessionLogin;
    }

    public void setHasSessionLogin(boolean hasSessionLogin) {
        this.hasSessionLogin = hasSessionLogin;
    }
}