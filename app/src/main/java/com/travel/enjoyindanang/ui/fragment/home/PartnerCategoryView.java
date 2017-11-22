package com.travel.enjoyindanang.ui.fragment.home;

import com.travel.enjoyindanang.api.model.Repository;
import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.iBaseView;
import com.travel.enjoyindanang.model.Partner;

/**
 * Author: Tavv
 * Created on 22/11/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public interface PartnerCategoryView extends iBaseView {

    void onGetPartnerByCategorySuccess(Repository<Partner> data);

    void onGetPartnerByCategoryFailure(AppError error);

    void addFavoriteSuccess();

    void addFavoriteFailure(AppError error);
}
