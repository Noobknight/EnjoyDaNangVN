package com.travel.enjoyindanang.ui.fragment.introduction;

import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.iBaseView;
import com.travel.enjoyindanang.model.Introduction;

/**
 * Author: Tavv
 * Created on 20/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public interface IntroductionView extends iBaseView {

    void onGetIntroductionSuccess(Introduction introduction);

    void onLoadFailure(AppError error);
}
