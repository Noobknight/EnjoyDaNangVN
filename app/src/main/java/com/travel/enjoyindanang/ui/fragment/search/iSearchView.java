package com.travel.enjoyindanang.ui.fragment.search;

import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.iBaseView;
import com.travel.enjoyindanang.model.Partner;

import java.util.List;

/**
 * Author: Tavv
 * Created on 28/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public interface iSearchView extends iBaseView {

    void OnQuerySearchResult(List<Partner> lstPartner);

    void onResultPlaceByRange(List<Partner> lstPartner);

    void onGetLocationAddress(List<Partner> lstPartners);

    void onGetAddress(String strAddress);

    void onError(AppError error);
}
