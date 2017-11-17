package com.travel.enjoyindanang.ui.fragment.favorite;

import com.travel.enjoyindanang.BasePresenter;
import com.travel.enjoyindanang.api.ApiCallback;
import com.travel.enjoyindanang.api.model.Repository;
import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.model.Partner;
import com.travel.enjoyindanang.utils.Utils;

/**
 * Author: Tavv
 * Created on 25/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class FavoritePresenter extends BasePresenter<FavoriteView>{

    public FavoritePresenter(FavoriteView view) {
        super(view);
    }

    void getFavorite(long userId){
        addSubscription(apiStores.getFavoriteByUserId(userId), new ApiCallback<Repository<Partner>>(){

            @Override
            public void onSuccess(Repository<Partner> model) {
                if (Utils.isResponseError(model)){
                    mvpView.onFetchFailure(new AppError(new Throwable(model.getMessage())));
                }
                mvpView.onFetchFavorite(model.getData());
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
