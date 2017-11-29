package com.travel.enjoyindanang.ui.activity.main;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.apache.commons.lang3.StringUtils;

import com.travel.enjoyindanang.BasePresenter;
import com.travel.enjoyindanang.GlobalApplication;
import com.travel.enjoyindanang.api.ApiCallback;
import com.travel.enjoyindanang.api.model.Repository;
import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.model.Popup;
import com.travel.enjoyindanang.model.UserInfo;
import com.travel.enjoyindanang.utils.ImageUtils;
import com.travel.enjoyindanang.utils.Utils;

/**
 * Created by chientruong on 3/27/17.
 */

public class MainPresenter extends BasePresenter<MainView> {

    public MainPresenter(MainView view) {
        super(view);
    }

    void loadInfoUserMenu(Context context, SimpleDraweeView imgAvatar, TextView fullName, TextView email) {
        UserInfo userInfo = GlobalApplication.getUserInfo();
        if (userInfo != null) {
            if (StringUtils.isEmpty(userInfo.getFullName())) {
                fullName.setVisibility(View.GONE);
            } else {
                fullName.setText(userInfo.getFullName());
            }
            if (StringUtils.isEmpty(userInfo.getEmail())) {
                email.setVisibility(View.GONE);
            } else {
                email.setText(userInfo.getEmail());
            }
            ImageUtils.loadImageWithFreso(imgAvatar, userInfo.getImage());
        } else {
            fullName.setVisibility(View.GONE);
            email.setVisibility(View.GONE);
            imgAvatar.setVisibility(View.GONE);
        }
    }

    void getPopup() {
        addSubscription(apiStores.getPopupInformation(), new ApiCallback<Repository<Popup>>() {

            @Override
            public void onSuccess(Repository<Popup> model) {
                if(!Utils.isResponseError(model)){
                    mvpView.onShowPopup(model);
                }
            }

            @Override
            public void onFailure(String msg) {
                mvpView.onError(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {

            }
        });
    }
}
