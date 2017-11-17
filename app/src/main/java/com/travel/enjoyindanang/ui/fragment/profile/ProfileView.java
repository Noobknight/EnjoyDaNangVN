package com.travel.enjoyindanang.ui.fragment.profile;

import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.iBaseView;
import com.travel.enjoyindanang.model.UserInfo;

/**
 * Author: Tavv
 * Created on 27/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public interface ProfileView extends iBaseView {

    void onUpdateSuccess(UserInfo userInfo);

    void onUpdateFailure(AppError error);
}
