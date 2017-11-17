package com.travel.enjoyindanang.ui.fragment.album;

import com.travel.enjoyindanang.BasePresenter;
import com.travel.enjoyindanang.api.ApiCallback;
import com.travel.enjoyindanang.api.model.Repository;
import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.model.PartnerAlbum;
import com.travel.enjoyindanang.utils.Utils;

/**
 * Author: Tavv
 * Created on 10/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class AlbumDetailPresenter extends BasePresenter<iAlbumView>{

    public AlbumDetailPresenter(iAlbumView view) {
        super(view);
    }

    void getAlbum(int partnerId){
        addSubscription(apiStores.getAlbumPartnerById(partnerId), new ApiCallback<Repository<PartnerAlbum>>(){

            @Override
            public void onSuccess(Repository<PartnerAlbum> model) {
                if(Utils.isNotEmptyContent(model)){
                    mvpView.onFetchAlbumSuccess(model.getData());
                }else{
                    mvpView.onFetchFail(new AppError(new Throwable(model.getMessage())));
                }
            }

            @Override
            public void onFailure(String msg) {
                mvpView.onFetchFail(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {

            }
        });
    }

    void onFetchFail(AppError error){
        mvpView.onFetchFail(error);
    }
}

