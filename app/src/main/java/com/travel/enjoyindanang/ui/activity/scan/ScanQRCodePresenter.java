package com.travel.enjoyindanang.ui.activity.scan;

import com.travel.enjoyindanang.BasePresenter;
import com.travel.enjoyindanang.api.ApiCallback;
import com.travel.enjoyindanang.api.model.Repository;
import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.model.HistoryCheckin;
import com.travel.enjoyindanang.model.Partner;
import com.travel.enjoyindanang.utils.Utils;

/**
 * Author: Tavv
 * Created on 18/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class ScanQRCodePresenter extends BasePresenter<ScanQRCodeView> {
    public ScanQRCodePresenter(ScanQRCodeView view) {
        super(view);
    }

    void requestOrder(int partnerId, long customerId, int amount, String passCode) {
        addSubscription(apiStores.checkIn(partnerId, customerId, amount, passCode), new ApiCallback<Repository<HistoryCheckin>>() {

            @Override
            public void onSuccess(Repository<HistoryCheckin> response) {
                if (Utils.isResponseError(response)) {
                    mvpView.onRequestOrderSuccessError(new AppError(new Throwable(response.getMessage())));
                    return;
                }
                mvpView.onRequestOrderSuccess(response.getData().get(0));
            }

            @Override
            public void onFailure(String msg) {
                mvpView.onRequestOrderSuccessError(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    void getInfoScanQr(String qrCode) {
        addSubscription(apiStores.getInforByQRCode(qrCode), new ApiCallback<Repository<Partner>>() {

            @Override
            public void onSuccess(Repository<Partner> data) {
                if (Utils.isResponseError(data)) {
                    mvpView.onFetchError(new AppError(new Throwable(data.getMessage())));
                    return;
                }
                mvpView.onFetchInfoSuccess(data.getData().get(0));
            }

            @Override
            public void onFailure(String msg) {
                mvpView.onFetchError(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
