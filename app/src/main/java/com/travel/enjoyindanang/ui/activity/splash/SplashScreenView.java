package com.travel.enjoyindanang.ui.activity.splash;

import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.iBaseView;
import com.travel.enjoyindanang.model.UserInfo;

import org.json.JSONObject;

/**
 * Author: Tavv
 * Created on 21/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public interface SplashScreenView extends iBaseView {
    void onLoadLanguageSuccess(JSONObject json);

    void onLoadFailure(AppError appError);

    void onGetUserInfoSuccess(UserInfo userInfo);

    void onFailure(AppError appError);
}
