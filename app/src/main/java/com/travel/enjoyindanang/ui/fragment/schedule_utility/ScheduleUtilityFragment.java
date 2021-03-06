package com.travel.enjoyindanang.ui.fragment.schedule_utility;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.travel.enjoyindanang.MvpFragment;
import com.travel.enjoyindanang.R;
import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.model.Partner;
import com.travel.enjoyindanang.model.Schedule;
import com.travel.enjoyindanang.model.Utility;
import com.travel.enjoyindanang.ui.fragment.schedule_utility.adapter.ScheduleAdapter;
import com.travel.enjoyindanang.utils.helper.LanguageHelper;

/**
 * Author: Tavv
 * Created on 24/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class ScheduleUtilityFragment extends MvpFragment<ScheduleUtilityPresenter> implements ScheduleUtilityView {
    private static final String TAG = ScheduleUtilityFragment.class.getSimpleName();

    //    @BindView(R.id.lvUtility)
//    ListView lvUtility;
    @BindView(R.id.lvSchedule)
    ListView lvSchedule;

    @BindView(R.id.schedule_utility_content)
    LinearLayout lrlContent;

    @BindView(R.id.txtEmpty)
    TextView txtEmptyData;

    @BindView(R.id.progress_bar)
    ProgressBar prgLoading;

    public static ScheduleUtilityFragment newInstance(Partner partner) {
        ScheduleUtilityFragment fragment = new ScheduleUtilityFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TAG, partner);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void showToast(String desc) {

    }

    @Override
    public void unKnownError() {

    }

    @Override
    protected ScheduleUtilityPresenter createPresenter() {
        return new ScheduleUtilityPresenter(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter = createPresenter();
        Bundle bundle = getArguments();
        if (bundle != null) {
            Partner partner = (Partner) bundle.getParcelable(TAG);
            mvpPresenter.getSchedule(partner.getId());
//            mvpPresenter.getUtility(partnerId);
        }
    }

    @Override
    protected void init(View view) {

    }

    @Override
    protected void setEvent(View view) {

    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_schedule_utility;
    }

    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void onFetchUtilitySuccess(List<Utility> utilities) {
//        setVisibleContent(true);
//        UtilityAdapter adapter = new UtilityAdapter(getContext(), utilities);
//        lvUtility.setAdapter(adapter);
    }

    @Override
    public void onFetchScheduleSuccess(List<Schedule> schedules) {
        setVisibleContent(true);
        ScheduleAdapter adapter = new ScheduleAdapter(getContext(), schedules);
        lvSchedule.setAdapter(adapter);
        prgLoading.setVisibility(View.GONE);
        lvSchedule.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFetchFailure(AppError error) {
        if (StringUtils.isBlank(error.getMessage())) {
            setVisibleContent(false);
        }
    }

    private void setVisibleContent(boolean show) {
        if (show) {
            lrlContent.setVisibility(View.VISIBLE);
            txtEmptyData.setVisibility(View.GONE);
        } else {
            lrlContent.setVisibility(View.GONE);
            txtEmptyData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initViewLabel(View view) {
        super.initViewLabel(view);
        LanguageHelper.getValueByViewId(txtEmptyData);
    }
}
