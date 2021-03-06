package com.travel.enjoyindanang.ui.fragment.logcheckin;

import com.travel.enjoyindanang.BasePresenter;
import com.travel.enjoyindanang.api.ApiCallback;
import com.travel.enjoyindanang.api.model.Repository;
import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.model.HistoryCheckin;
import com.travel.enjoyindanang.utils.Utils;

/**
 * Author: Tavv
 * Created on 30/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class CheckinHistoryPresenter extends BasePresenter<CheckinHistoryView>{

    public CheckinHistoryPresenter(CheckinHistoryView view) {
        super(view);
    }

    void getListHistory(long customerId, String fromDate, String toDate){
        addSubscription(apiStores.historyCheckIn(customerId, fromDate, toDate), new ApiCallback<Repository<HistoryCheckin>>(){

            @Override
            public void onSuccess(Repository<HistoryCheckin> model) {
                if(Utils.isResponseError(model)){
                    mvpView.onFetchFailure(new AppError(new Throwable(model.getMessage())));
                    return;
                }
                mvpView.onFetchHistorySuccess(model.getData());
            }

            @Override
            public void onFailure(String msg) {
                mvpView.onFetchFailure(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {

            }
        });
    }
}
