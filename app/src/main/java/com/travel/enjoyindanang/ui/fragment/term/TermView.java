package com.travel.enjoyindanang.ui.fragment.term;

import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.iBaseView;
import com.travel.enjoyindanang.model.Content;

/**
 * Author: Tavv
 * Created on 18/12/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public interface TermView extends iBaseView {

    void onLoadTermSuccess(Content content);

    void onFailure(AppError error);
}
