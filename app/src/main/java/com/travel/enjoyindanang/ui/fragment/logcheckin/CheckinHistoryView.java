package com.travel.enjoyindanang.ui.fragment.logcheckin;

import java.util.List;

import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.iBaseView;
import com.travel.enjoyindanang.model.HistoryCheckin;

/**
 * Author: Tavv
 * Created on 30/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public interface CheckinHistoryView extends iBaseView {

    void onFetchHistorySuccess(List<HistoryCheckin> lstHistoryCheckins);

    void onFetchFailure(AppError error);
}
