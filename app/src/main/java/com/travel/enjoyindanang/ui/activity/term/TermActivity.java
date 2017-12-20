package com.travel.enjoyindanang.ui.activity.term;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.travel.enjoyindanang.MvpActivity;
import com.travel.enjoyindanang.R;
import com.travel.enjoyindanang.annotation.DialogType;
import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.model.Content;
import com.travel.enjoyindanang.ui.fragment.term.TermPresenter;
import com.travel.enjoyindanang.ui.fragment.term.TermView;
import com.travel.enjoyindanang.utils.DialogUtils;
import com.travel.enjoyindanang.utils.Utils;
import com.travel.enjoyindanang.utils.helper.LanguageHelper;

import static com.travel.enjoyindanang.utils.Utils.getContext;

/**
 * Author: Tavv
 * Created on 18/12/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class TermActivity extends MvpActivity<TermPresenter> implements TermView {

    @BindView(R.id.txtTermSystemTitle)
    TextView txtTermSystemTitle;

    @BindView(R.id.txtContent)
    TextView txtContent;

    @BindView(R.id.progress_bar)
    ProgressBar prgLoading;

    @BindView(R.id.lrlTermContent)
    LinearLayout lrlTermContent;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public void showToast(String desc) {

    }

    @Override
    public void unKnownError() {

    }

    @Override
    protected TermPresenter createPresenter() {
        return new TermPresenter(this);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.fragment_term);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mvpPresenter.getTermSystem();
    }

    @Override
    public void init() {

    }

    @Override
    public void bindViews() {
        ButterKnife.bind(this);
    }

    @Override
    public void setValue(Bundle savedInstanceState) {
        initToolbar(toolbar);
        toolbar.setTitle(Utils.getLanguageByResId(R.string.Home_Term_System).toUpperCase());
        toolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void setEvent() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransitionExit();
            }
        });
    }

    @Override
    public void onLoadTermSuccess(Content content) {
        Spanned spanned = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            spanned = Html.fromHtml(content.getContent(), Html.FROM_HTML_MODE_LEGACY);
        }else{
            spanned = Html.fromHtml(content.getContent());
        }
        txtContent.setText(spanned);
        lrlTermContent.setVisibility(View.VISIBLE);
        prgLoading.setVisibility(View.GONE);
    }

    @Override
    public void onFailure(AppError error) {
        DialogUtils.showDialog(getContext(), DialogType.WRONG, DialogUtils.getTitleDialog(3), error.getMessage());
    }

    @Override
    public void initViewLabel() {
        LanguageHelper.getValueByViewId(txtTermSystemTitle);
    }
}
