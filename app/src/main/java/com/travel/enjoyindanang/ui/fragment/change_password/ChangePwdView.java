package com.travel.enjoyindanang.ui.fragment.change_password;

import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.iBaseView;
import com.travel.enjoyindanang.model.UserInfo;

/**
 * Author: Tavv
 * Created on 26/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public interface ChangePwdView extends iBaseView {

    void onChangeSuccess(UserInfo userInfo);

    void onChangeFailure(AppError error);
}
