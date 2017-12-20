package com.travel.enjoyindanang.ui.activity.login;

import com.travel.enjoyindanang.api.model.Repository;
import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.iBaseView;
import com.travel.enjoyindanang.model.UserInfo;

/**
 * Author: Tavv
 * Created on 09/10/2017.
 * Project Name: EnJoyDanang
 * Version : 1.0
 */

public interface LoginView extends iBaseView {

    void onLoginSuccess(Repository<UserInfo> resultCallBack);

    void onLoginFailure(AppError error);

}
