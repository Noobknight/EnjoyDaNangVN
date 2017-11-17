package com.travel.enjoyindanang.ui.fragment.profile;

import com.travel.enjoyindanang.BasePresenter;
import com.travel.enjoyindanang.api.ApiCallback;
import com.travel.enjoyindanang.api.model.Repository;
import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.model.UserInfo;
import com.travel.enjoyindanang.utils.Utils;

/**
 * Author: Tavv
 * Created on 27/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class ProfilePresenter extends BasePresenter<ProfileView>{
    public ProfilePresenter(ProfileView view) {
        super(view);
    }

    void updateProfile(long userId, String fullName, String phone, String email, String picBase64){
        addSubscription(apiStores.updateProfile(userId, fullName, phone, email, picBase64), new ApiCallback<Repository<UserInfo>>(){
            @Override
            public void onSuccess(Repository<UserInfo> model) {
                if(Utils.isResponseError(model)){
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
