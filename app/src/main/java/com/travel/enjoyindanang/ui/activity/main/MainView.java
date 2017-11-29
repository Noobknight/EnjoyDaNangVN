package com.travel.enjoyindanang.ui.activity.main;


import com.travel.enjoyindanang.api.model.Repository;
import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.iBaseView;
import com.travel.enjoyindanang.model.Popup;

/**
 * Created by chientruong on 3/27/17.
 */

public interface MainView extends iBaseView {

    void onShowPopup(Repository<Popup> response);

    void onError(AppError error);

}
