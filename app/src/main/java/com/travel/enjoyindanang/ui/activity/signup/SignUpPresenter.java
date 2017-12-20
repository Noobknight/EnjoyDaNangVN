package com.travel.enjoyindanang.ui.activity.signup;

import com.travel.enjoyindanang.BasePresenter;
import com.travel.enjoyindanang.api.ApiCallback;
import com.travel.enjoyindanang.api.model.Repository;
import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.model.UserInfo;
import com.travel.enjoyindanang.utils.Utils;

/**
 * Author: Tavv
 * Created on 20/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class SignUpPresenter extends BasePresenter<SignUpView>{
    public SignUpPresenter(SignUpView view) {
        super(view);
    }

    void normalRegister(UserInfo userInfo){
        addSubscription(apiStores.normalRegister(userInfo.getUserName(), userInfo.getPassword(),
                userInfo.getFullName(), userInfo.getEmail(), userInfo.getPhone()), new ApiCallback<Repository<UserInfo>>(){

            @Override
            public void onSuccess(Repository<UserInfo> userInfo) {
                mvpView.hideLoading();
                if(Utils.isResponseError(userInfo)){
                    mvpView.onRegisterFailure(new AppError(new Throwable(userInfo.getMessage())));
                    return;
                }
                mvpView.onRegisterSuccess(userInfo);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.hideLoading();
                mvpView.onRegisterFailure(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
