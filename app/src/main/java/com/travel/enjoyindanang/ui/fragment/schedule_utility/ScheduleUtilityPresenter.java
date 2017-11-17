package com.travel.enjoyindanang.ui.fragment.schedule_utility;

import java.util.Collections;
import java.util.List;

import com.travel.enjoyindanang.BasePresenter;
import com.travel.enjoyindanang.api.ApiCallback;
import com.travel.enjoyindanang.api.model.Repository;
import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.model.Schedule;
import com.travel.enjoyindanang.model.ScheduleComparator;
import com.travel.enjoyindanang.model.Utility;
import com.travel.enjoyindanang.utils.Utils;

/**
 * Author: Tavv
 * Created on 24/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class ScheduleUtilityPresenter extends BasePresenter<ScheduleUtilityView>{
    public ScheduleUtilityPresenter(ScheduleUtilityView view) {
        super(view);
    }

    void getSchedule(int partnerId){
        addSubscription(apiStores.getScheduleByPartnerId(partnerId), new ApiCallback<Repository<Schedule>>(){

            @Override
            public void onSuccess(Repository<Schedule> model) {
                if(Utils.isNotEmptyContent(model)){
                    List<Schedule> schedules = model.getData();
                    Collections.sort(schedules, new ScheduleComparator());
                    mvpView.onFetchScheduleSuccess(schedules);
                }else{
                    mvpView.onFetchFailure(new AppError(new Throwable(model.getMessage())));
                }
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

    void getUtility(int partnerId){
        addSubscription(apiStores.getUtilityByPartnerId(partnerId), new ApiCallback<Repository<Utility>>(){

            @Override
            public void onSuccess(Repository<Utility> model) {
                mvpView.hideLoading();
                if(Utils.isNotEmptyContent(model)){
                    mvpView.onFetchUtilitySuccess(model.getData());
                }else{
                    mvpView.onFetchFailure(new AppError(new Throwable(model.getMessage())));
                }
            }

            @Override
            public void onFailure(String msg) {
                mvpView.hideLoading();
                mvpView.onFetchFailure(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {

            }
        });
    }
}
