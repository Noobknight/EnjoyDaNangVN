package com.travel.enjoyindanang;


import android.os.Bundle;
import android.view.View;

import com.travel.enjoyindanang.ui.fragment.BaseFragment;
import com.travel.enjoyindanang.utils.Utils;


/**
 * Created by chientruong on 12/14/16.
 */

public abstract class MvpFragment<P extends BasePresenter> extends BaseFragment {
    protected P mvpPresenter;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter = createPresenter();
    }

    protected abstract P createPresenter();


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mvpPresenter != null) {
            mvpPresenter.detachView();
        }
    }
    public void showLoading() {
        showProgressDialog(Utils.getLanguageByResId(R.string.Message_Updating));
    }

    public void hideLoading() {
        dismissProgressDialog();
    }
}
