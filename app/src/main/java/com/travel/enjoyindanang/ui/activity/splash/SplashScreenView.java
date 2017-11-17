package com.travel.enjoyindanang.ui.activity.splash;

import org.json.JSONObject;

import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.iBaseView;

/**
 * Author: Tavv
 * Created on 21/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public interface SplashScreenView extends iBaseView {
    void onLoadLanguageSuccess(JSONObject json);
    void onLoadFailre(AppError appError);
}
