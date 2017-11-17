package com.travel.enjoyindanang.ui.activity.login;

import com.travel.enjoyindanang.BasePresenter;
import com.travel.enjoyindanang.api.ApiCallback;
import com.travel.enjoyindanang.api.model.Repository;
import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.model.User;
import com.travel.enjoyindanang.model.UserInfo;
import com.travel.enjoyindanang.utils.Utils;

/**
 * Author: Tavv
 * Created on 08/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class LoginPresenter extends BasePresenter<LoginView> {
    public LoginPresenter(LoginView view) {
        super(view);
    }

    void loginViaSocial(User user) {
        addSubscription(apiStores.doSignOrRegisterViaSocial(user.getId(), user.getAccessToken(),
                user.getType(), user.getPicture().getData().getImage(), user.getFullName(), user.getEmail()), new ApiCallback<Repository<UserInfo>>() {

            @Override
            public void onSuccess(Repository<UserInfo> userInfo) {
                if(Utils.isResponseError(userInfo)){
                    mvpView.onLoginFailure(new AppError(new Throwable(userInfo.getMessage())));
                    return;
                }
                mvpView.onLoginSuccess(userInfo);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.onLoginFailure(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {
            }
        });
    }

    void normalLogin(String userName, String password) {
        addSubscription(apiStores.normalLogin(userName, password), new ApiCallback<Repository<UserInfo>>() {

            @Override
            public void onSuccess(Repository<UserInfo> userInfo) {
                if (Utils.isResponseError(userInfo)) {
                    mvpView.onLoginFailure(new AppError(new Throwable(userInfo.getMessage())));
                    return;
                }
                mvpView.onLoginSuccess(userInfo);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.onLoginFailure(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {
            }
        });
    }


    void showLoading(){
        mvpView.showLoading();
    }

    void hideLoading(){
        mvpView.hideLoading();
    }

}
