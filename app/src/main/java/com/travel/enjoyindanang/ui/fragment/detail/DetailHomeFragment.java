package com.travel.enjoyindanang.ui.fragment.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.travel.enjoyindanang.MvpFragment;
import com.travel.enjoyindanang.R;
import com.travel.enjoyindanang.model.Partner;

/**
 * Author: Tavv
 * Created on 11/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */
@Deprecated
public class DetailHomeFragment extends MvpFragment<DetailHomePresenter> implements iDetailHomeView, TabLayout.OnTabSelectedListener{
    private static final String TAG = DetailHomeFragment.class.getSimpleName();

    //This is our tablayout
    @BindView(R.id.tabs)
    TabLayout tabLayout;

    //This is our viewPager
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    public static DetailHomeFragment newInstance(Partner partner) {
        DetailHomeFragment fragment = new DetailHomeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TAG, partner);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    protected DetailHomePresenter createPresenter() {
        return new DetailHomePresenter(this);
    }

    @Override
    protected void init(View view) {
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.detail_tab_name)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.review_tab_name)));
        //tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.schedule_tab_name)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.album_tab_name)));
        //Creating our pager adapter
        Bundle bundle = getArguments();
        if (bundle != null) {
            Partner partner = (Partner) bundle.getParcelable(TAG);
            if (partner != null) {
                DetailPagerAdapter adapter = new DetailPagerAdapter(mFragmentManager, tabLayout.getTabCount(), partner, false);
                viewPager.setAdapter(adapter);
            }
        }

        //Adding adapter to pager
    }

    @Override
    protected void setEvent(View view) {
        tabLayout.addOnTabSelectedListener(this);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_detail_home;
    }

    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void showToast(String desc) {

    }

    @Override
    public void unKnownError() {

    }

}

