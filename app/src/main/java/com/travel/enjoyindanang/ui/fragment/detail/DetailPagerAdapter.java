package com.travel.enjoyindanang.ui.fragment.detail;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.travel.enjoyindanang.model.Partner;
import com.travel.enjoyindanang.ui.fragment.album.AlbumDetailFragment;
import com.travel.enjoyindanang.ui.fragment.review.ReviewFragment;

/**
 * Author: Tavv
 * Created on 10/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class DetailPagerAdapter extends FragmentStatePagerAdapter {
    //integer to count number of tabs
    private int tabCount;
    private Partner partner;
    private boolean isOpenFromNearby;
    //Constructor to the class
    public DetailPagerAdapter(FragmentManager fm, int tabCount,Partner partner, boolean isOpenFromNearby) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
        this.partner = partner;
        this.isOpenFromNearby = isOpenFromNearby;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                return DetailPartnerFragment.newInstance(partner, isOpenFromNearby);
            case 1:
                return ReviewFragment.newInstance(partner);
//            case 2:
//                return ScheduleUtilityFragment.newInstance(partner);
            case 2:
                return AlbumDetailFragment.newInstance(partner);
            default:
                return null;
        }
    }



    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}
