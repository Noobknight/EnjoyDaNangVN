package com.travel.enjoyindanang.ui.activity.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.travel.enjoyindanang.GlobalApplication;
import com.travel.enjoyindanang.MvpActivity;
import com.travel.enjoyindanang.R;
import com.travel.enjoyindanang.annotation.DialogType;
import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.constant.Constant;
import com.travel.enjoyindanang.model.UserInfo;
import com.travel.enjoyindanang.ui.activity.login.LoginActivity;
import com.travel.enjoyindanang.ui.activity.main.MainActivity;
import com.travel.enjoyindanang.utils.DialogUtils;
import com.travel.enjoyindanang.utils.FileUtils;
import com.travel.enjoyindanang.utils.JsonUtils;
import com.travel.enjoyindanang.utils.SharedPrefsUtils;
import com.travel.enjoyindanang.utils.Utils;
import com.travel.enjoyindanang.utils.helper.LanguageHelper;
import com.travel.enjoyindanang.utils.network.NetworkUtils;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.travel.enjoyindanang.constant.Constant.SPLASH_TIME_OUT;


public class ScreenSplashActivity extends MvpActivity<SplashScreenPresenter> implements SplashScreenView {
    private static final String TAG = ScreenSplashActivity.class.getSimpleName();

    @BindView(R.id.txtLoadingContent)
    TextView txtLoadingContent;

    private boolean hasTextContent;

    private UserInfo localUser;

    @Override
    protected SplashScreenPresenter createPresenter() {
        return new SplashScreenPresenter(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (NetworkUtils.isNetworkContented(ScreenSplashActivity.this)) {
            mvpPresenter = createPresenter();
            mvpPresenter.loadLanguage();

        } else {
            DialogUtils.showDialog(ScreenSplashActivity.this, DialogType.WARNING, DialogUtils.getTitleDialog(2), Utils.getLanguageByResId(R.string.Message_No_Internet));
        }
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_splash);
    }

    @Override
    public void init() {
        if (Utils.hasSessionLogin()) {
            localUser = JsonUtils.convertJsonToObject(SharedPrefsUtils.getStringFromPrefs(Constant.SHARED_PREFS_NAME,
                    Constant.KEY_EXTRAS_USER_INFO), UserInfo.class);
            mvpPresenter.getUserInfoById(localUser.getUserId());
        }
    }

    @Override
    public void bindViews() {
        ButterKnife.bind(this);
    }

    @Override
    public void setValue(Bundle savedInstanceState) {

    }

    @Override
    public void setEvent() {

    }


    /**
     * Delay 1s to start Home Activity
     */
    private void start() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openNextActivity();
            }
        }, SPLASH_TIME_OUT);
    }


    private void openNextActivity() {
        if (Utils.hasSessionLogin()) {
            UserInfo userInfo = JsonUtils.convertJsonToObject(SharedPrefsUtils.getStringFromPrefs(Constant.SHARED_PREFS_NAME,
                    Constant.KEY_EXTRAS_USER_INFO), UserInfo.class);
            if (userInfo != null) {
                GlobalApplication.setUserInfo(userInfo);
            }
        }
        Class<?> nextClass = Utils.hasSessionLogin() ? MainActivity.class : LoginActivity.class;
        Intent i = new Intent(ScreenSplashActivity.this, nextClass);
        startActivity(i);
        finish();
    }

    @Override
    public void showToast(String desc) {

    }

    @Override
    public void unKnownError() {

    }

    @Override
    public void onLoadLanguageSuccess(JSONObject json) {
        if (json != null) {
            FileUtils.saveFilePrivateMode(Constant.FILE_NAME_LANGUAGE, json.toString());
            GlobalApplication.getGlobalApplicationContext().setJsLanguage(json);
            if (!hasTextContent) {
                LanguageHelper.getValueByViewId(txtLoadingContent);
            }
        }
        if(!Utils.hasSessionLogin()){
            start();
        }
    }

    @Override
    public void onLoadFailure(AppError appError) {
        DialogUtils.showDialog(ScreenSplashActivity.this, DialogType.WRONG, DialogUtils.getTitleDialog(3), appError.getMessage());
    }

    @Override
    public void onGetUserInfoSuccess(UserInfo userInfo) {
        if(!localUser.equals(userInfo)){
            Utils.saveUserInfo(userInfo);
        }
        start();
    }

    @Override
    public void onFailure(AppError appError) {
        Log.e(TAG, "onFailure: " + appError.getMessage());
    }

    @Override
    public void initViewLabel() {
        String value = LanguageHelper.getValueByKey(txtLoadingContent.getText().toString().trim());
        if (StringUtils.isNotEmpty(value)) {
            txtLoadingContent.setText(value);
            hasTextContent = true;
        }
    }


}
