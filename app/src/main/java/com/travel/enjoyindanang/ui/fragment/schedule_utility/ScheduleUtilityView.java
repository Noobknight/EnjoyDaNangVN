package com.travel.enjoyindanang.ui.fragment.schedule_utility;

import java.util.List;

import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.iBaseView;
import com.travel.enjoyindanang.model.Schedule;
import com.travel.enjoyindanang.model.Utility;

/**
 * Author: Tavv
 * Created on 24/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public interface ScheduleUtilityView extends iBaseView {

    void onFetchUtilitySuccess(List<Utility> utilities);

    void onFetchScheduleSuccess(List<Schedule> schedules);

    void onFetchFailure(AppError error);

}
