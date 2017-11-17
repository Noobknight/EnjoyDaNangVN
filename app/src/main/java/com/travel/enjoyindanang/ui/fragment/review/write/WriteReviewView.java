package com.travel.enjoyindanang.ui.fragment.review.write;

import com.travel.enjoyindanang.api.model.Repository;
import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.iBaseView;

/**
 * Author: Tavv
 * Created on 29/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public interface WriteReviewView extends iBaseView {

    void onSubmitSuccess(Repository data);

    void onSubmitFailure(AppError error);
}
