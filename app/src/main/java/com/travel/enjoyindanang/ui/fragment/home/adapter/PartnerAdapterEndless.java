package com.travel.enjoyindanang.ui.fragment.home.adapter;

import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.travel.enjoyindanang.utils.event.LoadMoreListener;
import com.travel.enjoyindanang.utils.widget.EndlessAdapter;

/**
 * Author: Tavv
 * Created on 13/11/2017
 * Project Name: EnjoyInDaNang
 * Version 1.0
 */

public class PartnerAdapterEndless extends EndlessAdapter {


    public PartnerAdapterEndless(@NonNull NestedScrollView nestedScrollView, @NonNull RecyclerView recyclerView, @NonNull LoadMoreListener loadMoreListener) {
        super(nestedScrollView, recyclerView, loadMoreListener);
    }

    @Override
    public int getActualItemViewType(int position) {
        return 0;
    }

    @Override
    public int getActualItemCount() {
        return 0;
    }

    @Override
    public void onActualBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public RecyclerView.ViewHolder onActualCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }
}
