package com.travel.enjoyindanang.ui.fragment.search;

import com.travel.enjoyindanang.BasePresenter;
import com.travel.enjoyindanang.api.ApiCallback;
import com.travel.enjoyindanang.api.model.Repository;
import com.travel.enjoyindanang.model.Partner;

/**
 * Author: Tavv
 * Created on 28/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class SearchPresenter extends BasePresenter<iSearchView> {
    public SearchPresenter(iSearchView view) {
        super(view);
    }

    void searchWithTitle(String query) {
        addSubscription(apiStores.searchWithQuery(query), new ApiCallback<Repository<Partner>>() {

            @Override
            public void onSuccess(Repository<Partner> model) {
                mvpView.OnQuerySearchResult(model.getData());
            }

            @Override
            public void onFailure(String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
    }
}
