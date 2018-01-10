package com.travel.enjoyindanang.ui.fragment.detail;

import com.travel.enjoyindanang.BasePresenter;
import com.travel.enjoyindanang.api.ApiCallback;
import com.travel.enjoyindanang.api.model.Repository;
import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.model.DetailPartner;
import com.travel.enjoyindanang.model.DetailPartnerCombined;
import com.travel.enjoyindanang.model.Partner;
import com.travel.enjoyindanang.model.PartnerAlbum;
import com.travel.enjoyindanang.utils.Utils;
import rx.Observable;
import rx.functions.Func2;

/**
 * Author: Tavv
 * Created on 10/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class DetailPartnerPresenter extends BasePresenter<iDetailPartnerView> {

    public DetailPartnerPresenter(iDetailPartnerView view) {
        super(view);
    }

    void getAllDataHome(int partnerId) {
        addSubscription(Observable.zip(apiStores.getDetailPartnerById(partnerId), apiStores.getSlideByPartnerId(partnerId),
                new Func2<Repository<DetailPartner>, Repository<PartnerAlbum>, DetailPartnerCombined>() {
                    @Override
                    public DetailPartnerCombined call(Repository<DetailPartner> detailPartnerRepository,
                                                      Repository<PartnerAlbum> partnerAlbumRepository) {
                        return new DetailPartnerCombined(detailPartnerRepository, partnerAlbumRepository);
                    }
                }), new ApiCallback<DetailPartnerCombined>() {

            @Override
            public void onSuccess(DetailPartnerCombined data) {
                Repository<DetailPartner> detailPartnerRepository = data.getDetailPartnerRepository();
                Repository<PartnerAlbum> partnerAlbumRepository = data.getPartnerAlbumRepository();
                if (Utils.isResponseError(detailPartnerRepository) || Utils.isResponseError(partnerAlbumRepository)) {
                    mvpView.onFetchFailure(new AppError(new Throwable(AppError.DEFAULT_ERROR_MESSAGE)));
                }
                mvpView.onFetchAllData(detailPartnerRepository.getData(),
                        partnerAlbumRepository.getData());
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


    void getAllDataNearBy(int partnerId, String geoLat, String geoLng) {
        addSubscription(Observable.zip(apiStores.getDetailPartnerById(partnerId, geoLat, geoLng), apiStores.getSlideByPartnerId(partnerId),
                new Func2<Repository<DetailPartner>, Repository<PartnerAlbum>, DetailPartnerCombined>() {
                    @Override
                    public DetailPartnerCombined call(Repository<DetailPartner> detailPartnerRepository,
                                                      Repository<PartnerAlbum> partnerAlbumRepository) {
                        return new DetailPartnerCombined(detailPartnerRepository, partnerAlbumRepository);
                    }
                }), new ApiCallback<DetailPartnerCombined>() {

            @Override
            public void onSuccess(DetailPartnerCombined data) {
                Repository<DetailPartner> detailPartnerRepository = data.getDetailPartnerRepository();
                Repository<PartnerAlbum> partnerAlbumRepository = data.getPartnerAlbumRepository();
                if (Utils.isResponseError(detailPartnerRepository) || Utils.isResponseError(partnerAlbumRepository)) {
                    mvpView.onFetchFailure(new AppError(new Throwable(AppError.DEFAULT_ERROR_MESSAGE)));
                }
                mvpView.onFetchAllData(detailPartnerRepository.getData(),
                        partnerAlbumRepository.getData());
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

    void getListPartnerAround(long customerId, String geoLat, String geoLng) {
        addSubscription(apiStores.listPlaceAround(customerId, geoLat, geoLng), new ApiCallback<Repository<Partner>>() {

            @Override
            public void onSuccess(Repository<Partner> model) {
                if (Utils.isResponseError(model)) {
                    mvpView.onFetchFailure(new AppError(new Throwable(model.getMessage())));
                    return;
                }
                mvpView.onFetchListPartnerAround(model.getData());
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
