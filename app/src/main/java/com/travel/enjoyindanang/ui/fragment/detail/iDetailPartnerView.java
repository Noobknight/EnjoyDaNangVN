package com.travel.enjoyindanang.ui.fragment.detail;

import java.util.List;

import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.iBaseView;
import com.travel.enjoyindanang.model.DetailPartner;
import com.travel.enjoyindanang.model.Partner;
import com.travel.enjoyindanang.model.PartnerAlbum;

/**
 * Author: Tavv
 * Created on 10/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public interface iDetailPartnerView extends iBaseView {

    void onFetchFailure(AppError appError);
    void onFetchAllData(List<DetailPartner> lstDetailPartner, List<PartnerAlbum> lstAlbum);
    void onFetchListPartnerAround(List<Partner> lstPartnerAround);
}
