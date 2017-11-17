package com.travel.enjoyindanang.ui.activity.signup;

import com.travel.enjoyindanang.api.model.Repository;
import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.iBaseView;
import com.travel.enjoyindanang.model.UserInfo;

/**
 * Author: Tavv
 * Created on 20/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public interface SignUpView extends iBaseView {
    void onRegisterSuccess(Repository<UserInfo> resultCallBack);

    void onRegisterFailure(AppError error);
}
