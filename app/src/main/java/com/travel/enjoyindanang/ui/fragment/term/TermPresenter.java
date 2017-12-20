package com.travel.enjoyindanang.ui.fragment.term;

import com.travel.enjoyindanang.BasePresenter;
import com.travel.enjoyindanang.api.ApiCallback;
import com.travel.enjoyindanang.api.model.Repository;
import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.model.Content;
import com.travel.enjoyindanang.utils.Utils;

/**
 * Author: Tavv
 * Created on 18/12/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class TermPresenter extends BasePresenter<TermView>{
    public TermPresenter(TermView view) {
        super(view);
    }

    public void getTermSystem(){
        addSubscription(apiStores.getTermSystem("TERMSOFUSE"), new ApiCallback<Repository<Content>>(){

            @Override
            public void onSuccess(Repository<Content> model) {
                if(Utils.isResponseError(model)){
                    mvpView.onFailure(new AppError(new Throwable(model.getMessage())));
                    return;
                }
                mvpView.onLoadTermSuccess(model.getData().get(0));
            }

            @Override
            public void onFailure(String msg) {
                mvpView.onFailure(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {

            }
        });
    }
}
