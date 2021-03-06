package com.travel.enjoyindanang.ui.fragment.introduction;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.travel.enjoyindanang.MvpFragment;
import com.travel.enjoyindanang.R;
import com.travel.enjoyindanang.annotation.DialogType;
import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.model.Introduction;
import com.travel.enjoyindanang.utils.DialogUtils;
import com.travel.enjoyindanang.utils.helper.LanguageHelper;

/**
 * Author: Tavv
 * Created on 20/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class IntroductionFragment extends MvpFragment<IntroductionPresenter> implements IntroductionView {
    private static final String TAG = IntroductionFragment.class.getSimpleName();

    @BindView(R.id.txtIntroductName)
    TextView txtIntroductionName;

    @BindView(R.id.txtContent)
    TextView txtContent;

    @BindView(R.id.progress_bar)
    ProgressBar prgLoading;

    @BindView(R.id.lrlIntroContent)
    LinearLayout lrlIntroContent;

    @BindView(R.id.txtTopIntroduction)
    TextView txtTopIntroduction;


    @Override
    protected IntroductionPresenter createPresenter() {
        return new IntroductionPresenter(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter = createPresenter();
        mvpPresenter.getIntroduction();
    }

    @Override
    protected void init(View view) {
    }

    @Override
    protected void setEvent(View view) {

    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_introduction;
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

    @Override
    public void onGetIntroductionSuccess(Introduction introduction) {
        txtIntroductionName.setText(introduction.getTitle());
        Spanned spanned = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            spanned = Html.fromHtml(introduction.getContent(), Html.FROM_HTML_MODE_LEGACY);
        }else{
            spanned = Html.fromHtml(introduction.getContent());
        }
        txtContent.setText(spanned);
        lrlIntroContent.setVisibility(View.VISIBLE);
        prgLoading.setVisibility(View.GONE);
    }

    @Override
    public void onLoadFailure(AppError error) {
        DialogUtils.showDialog(getContext(), DialogType.WARNING, DialogUtils.getTitleDialog(2), error.getMessage());
    }

    @Override
    public void initViewLabel(View view) {
        super.initViewLabel(view);
        LanguageHelper.getValueByViewId(txtTopIntroduction);
    }
}
