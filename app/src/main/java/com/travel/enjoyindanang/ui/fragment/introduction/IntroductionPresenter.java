package com.travel.enjoyindanang.ui.fragment.introduction;

import com.travel.enjoyindanang.BasePresenter;
import com.travel.enjoyindanang.api.ApiCallback;
import com.travel.enjoyindanang.api.model.Repository;
import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.model.Introduction;
import com.travel.enjoyindanang.utils.Utils;

/**
 * Author: Tavv
 * Created on 20/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class IntroductionPresenter extends BasePresenter<IntroductionView>{
    private static final String TAG = IntroductionPresenter.class.getSimpleName();

    public IntroductionPresenter(IntroductionView view) {
        super(view);
    }

    void getIntroduction(){
        addSubscription(apiStores.getIntroduction(), new ApiCallback<Repository<Introduction>>(){

            @Override
            public void onSuccess(Repository<Introduction> data) {
                if (Utils.isResponseError(data)) {
                    mvpView.onLoadFailure(new AppError(new Throwable(data.getMessage())));
                    return;
                }
                if(Utils.isNotEmptyContent(data)){
                    mvpView.onGetIntroductionSuccess(data.getData().get(0));
                }
            }

            @Override
            public void onFailure(String msg) {
                mvpView.onLoadFailure(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {

            }
        });
    }
}
