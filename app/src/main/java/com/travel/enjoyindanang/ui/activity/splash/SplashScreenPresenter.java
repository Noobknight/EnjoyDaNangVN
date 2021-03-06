package com.travel.enjoyindanang.ui.activity.splash;

import com.travel.enjoyindanang.BasePresenter;
import com.travel.enjoyindanang.api.ApiCallback;
import com.travel.enjoyindanang.api.model.Repository;
import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.model.Language;
import com.travel.enjoyindanang.model.UserInfo;
import com.travel.enjoyindanang.utils.Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Tavv
 * Created on 21/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class SplashScreenPresenter extends BasePresenter<SplashScreenView> {


    public SplashScreenPresenter(SplashScreenView view) {
        super(view);
    }

    void loadLanguage() {
        addSubscription(apiStores.getLanguage(), new ApiCallback<Repository<Language>>() {

            @Override
            public void onSuccess(Repository<Language> data) {
                if (Utils.isNotEmptyContent(data)) {
                    List<Language> lstLanguages = data.getData();
                    Map<String, String> maps = new HashMap<String, String>();
                    int length = lstLanguages.size();
                    for (int i = 0; i < length; i++) {
                        Language language = lstLanguages.get(i);
                        maps.put(language.getName(), language.getValue());
                    }
                    JSONObject json = new JSONObject(maps);
                    mvpView.onLoadLanguageSuccess(json);
                }
            }

            @Override
            public void onFailure(String msg) {
                mvpView.onLoadFailure(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {

            }
        });
    }


    void getUserInfoById(long userId) {
        addSubscription(apiStores.getUserInfoById(userId, "CUSTOMERINFO"), new ApiCallback<Repository<UserInfo>>() {

            @Override
            public void onSuccess(Repository<UserInfo> data) {
                if (Utils.isResponseError(data)) {
                    mvpView.onFailure(new AppError(new Throwable(data.getMessage())));
                    return;
                }
                mvpView.onGetUserInfoSuccess(data.getData().get(0));
            }

            @Override
            public void onFailure(String msg) {
                mvpView.onLoadFailure(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {

            }
        });
    }

}
