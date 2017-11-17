package com.travel.enjoyindanang.ui.activity.scan;

import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.iBaseView;
import com.travel.enjoyindanang.model.HistoryCheckin;
import com.travel.enjoyindanang.model.Partner;

/**
 * Author: Tavv
 * Created on 18/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public interface ScanQRCodeView extends iBaseView {

    void onFetchInfoSuccess(Partner partner);

    void onRequestOrderSuccess(HistoryCheckin response);

    void onFetchError(AppError appError);

    void onRequestOrderSuccessError(AppError appError);

}
