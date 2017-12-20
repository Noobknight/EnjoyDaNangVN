package com.travel.enjoyindanang.ui.fragment.home;

import java.util.List;

import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.iBaseView;
import com.travel.enjoyindanang.model.Banner;
import com.travel.enjoyindanang.model.Category;
import com.travel.enjoyindanang.model.Partner;

/**
 * Created by chien on 10/8/17.
 */

public interface iHomeView  extends iBaseView {

    void onGetPartnerSuccess(List<Partner> data);

    void onGetPartnerFailure(AppError error);

    void addFavoriteSuccess();

    void addFavoriteFailure(AppError error);

    void onFetchAllDataSuccess(List<Partner> partners, List<Banner> banners, List<Category> categories);

    void onFetchFailure(AppError error);

}
