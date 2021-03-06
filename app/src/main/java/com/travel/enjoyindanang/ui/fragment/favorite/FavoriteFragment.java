package com.travel.enjoyindanang.ui.fragment.favorite;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.collections.CollectionUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.refactor.lib.colordialog.ColorDialog;
import com.travel.enjoyindanang.MvpFragment;
import com.travel.enjoyindanang.R;
import com.travel.enjoyindanang.annotation.DialogType;
import com.travel.enjoyindanang.api.ApiCallback;
import com.travel.enjoyindanang.api.ApiStores;
import com.travel.enjoyindanang.api.model.Repository;
import com.travel.enjoyindanang.api.module.AppClient;
import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.model.Partner;
import com.travel.enjoyindanang.model.UserInfo;
import com.travel.enjoyindanang.ui.fragment.detail.dialog.DetailHomeDialogFragment;
import com.travel.enjoyindanang.utils.DialogUtils;
import com.travel.enjoyindanang.utils.Utils;
import com.travel.enjoyindanang.utils.event.OnItemClickListener;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Author: Tavv
 * Created on 25/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class FavoriteFragment extends MvpFragment<FavoritePresenter> implements FavoriteView, OnItemClickListener {
    private static final String TAG = FavoriteFragment.class.getSimpleName();

    private static final int VERTICAL_ITEM_SPACE = 5;


    @BindView(R.id.rcvFavorite)
    RecyclerView rcvFavorite;

    @BindView(R.id.txtEmpty)
    TextView txtEmpty;

    @BindView(R.id.progress_bar)
    ProgressBar prgLoading;

    private UserInfo userInfo;

    private FavoriteAdapter mAdapter;

    private List<Partner> lstFavorites;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter = createPresenter();
        prgLoading.post(new Runnable() {
            @Override
            public void run() {
                mvpPresenter.getFavorite(userInfo.getUserId());
            }
        });
    }

    @Override
    public void showToast(String desc) {

    }

    @Override
    public void unKnownError() {

    }

    @Override
    public void onFetchFavorite(List<Partner> lstFavorites) {
        prgLoading.setVisibility(View.GONE);
        if (CollectionUtils.isEmpty(lstFavorites)) {
            txtEmpty.setVisibility(View.VISIBLE);
            rcvFavorite.setVisibility(View.GONE);
            return;
        }
        this.lstFavorites = lstFavorites;
        txtEmpty.setVisibility(View.GONE);
        rcvFavorite.setVisibility(View.VISIBLE);
        mAdapter = new FavoriteAdapter(lstFavorites, getContext(), this);
        rcvFavorite.setAdapter(mAdapter);
    }

    @Override
    public void onFetchFailure(AppError error) {
        DialogUtils.showDialog(getContext(), DialogType.WRONG, DialogUtils.getTitleDialog(3), error.getMessage());
    }

    @Override
    protected FavoritePresenter createPresenter() {
        return new FavoritePresenter(this);
    }

    @Override
    protected void init(View view) {
        userInfo = Utils.getUserInfo();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), layoutManager.getOrientation());
        rcvFavorite.addItemDecoration(decoration);
        rcvFavorite.setLayoutManager(layoutManager);
        rcvFavorite.setHasFixedSize(false);
    }

    @Override
    protected void setEvent(View view) {

    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_favorite;
    }

    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void onClick(View view, final int position) {
        final ApiStores apiStores = AppClient.getClient().create(ApiStores.class);
        if (view.getId() == R.id.btnDelete) {
            DialogUtils.showDialogConfirm(getContext(), DialogUtils.getTitleDialog(2),
                    Utils.getLanguageByResId(R.string.Message_Confirm_Remove_Favorite),
                    Utils.getLanguageByResId(R.string.Message_Confirm_Ok),
                    Utils.getLanguageByResId(R.string.Message_Confirm_Cancel),
                    new ColorDialog.OnPositiveListener() {
                        @Override
                        public void onClick(ColorDialog colorDialog) {
                            colorDialog.dismiss();
                            new CompositeSubscription().add(apiStores.addFavorite(userInfo.getUserId(), lstFavorites.get(position).getId())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new ApiCallback<Repository>() {
                                        @Override
                                        public void onSuccess(Repository model) {
                                            if (!Utils.isResponseError(model)) {
                                                mAdapter.removeAt(position);
                                            }
                                        }

                                        @Override
                                        public void onFailure(String msg) {
                                            Toast.makeText(mMainActivity, msg, Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFinish() {

                                        }
                                    }));
                        }
                    }, new ColorDialog.OnNegativeListener() {
                        @Override
                        public void onClick(ColorDialog colorDialog) {
                            colorDialog.dismiss();
                        }
                    }
            );
        } else {
            DetailHomeDialogFragment dialog = DetailHomeDialogFragment.newInstance(lstFavorites.get(position), false);
            DialogUtils.openDialogFragment(mFragmentManager, dialog);
        }

    }

}
