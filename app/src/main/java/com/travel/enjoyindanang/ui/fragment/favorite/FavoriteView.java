package com.travel.enjoyindanang.ui.fragment.favorite;

import java.util.List;

import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.iBaseView;
import com.travel.enjoyindanang.model.Partner;

/**
 * Author: Tavv
 * Created on 25/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public interface FavoriteView extends iBaseView {

    void onFetchFavorite(List<Partner> lstFavorites);

    void onFetchFailure(AppError error);
}
