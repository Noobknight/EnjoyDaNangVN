package com.travel.enjoyindanang.ui.fragment.profile;

import com.travel.enjoyindanang.BasePresenter;
import com.travel.enjoyindanang.api.ApiCallback;
import com.travel.enjoyindanang.api.model.Repository;
import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.model.UserInfo;
import com.travel.enjoyindanang.utils.Utils;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Author: Tavv
 * Created on 27/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class ProfilePresenter extends BasePresenter<ProfileView> {
    public ProfilePresenter(ProfileView view) {
        super(view);
    }

    void updateProfile(RequestBody type, long userId, RequestBody fullName, RequestBody phone, RequestBody email, RequestBody code, MultipartBody.Part picture) {
        addSubscription(apiStores.updateProfile(type, fullName, email, phone, userId, code, picture), new ApiCallback<Repository<UserInfo>>() {
            @Override
            public void onSuccess(Repository<UserInfo> model) {
                if (Utils.isResponseError(model)) {
                    mvpView.onUpdateFailure(new AppError(new Throwable(model.getMessage())));
                    return;
                }
                mvpView.onUpdateSuccess(model.getData().get(0));
            }

            @Override
            public void onFailure(String msg) {
                mvpView.onUpdateFailure(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {
            }
        });
    }
}
