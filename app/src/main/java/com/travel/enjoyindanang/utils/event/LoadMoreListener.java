package com.travel.enjoyindanang.utils.event;

import android.support.v7.widget.RecyclerView;

/**
 * Author: Tavv
 * Created on 13/11/2017
 * Project Name: EnjoyInDaNang
 * Version 1.0
 */

public interface LoadMoreListener {
    void onLoadMore(int pager, int totalItemsCount, RecyclerView recyclerView);
}
