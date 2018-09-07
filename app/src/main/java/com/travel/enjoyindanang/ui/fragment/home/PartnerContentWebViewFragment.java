package com.travel.enjoyindanang.ui.fragment.home;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebBackForwardList;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.travel.enjoyindanang.R;
import com.travel.enjoyindanang.annotation.DialogType;
import com.travel.enjoyindanang.common.Common;
import com.travel.enjoyindanang.constant.Constant;
import com.travel.enjoyindanang.model.Category;
import com.travel.enjoyindanang.model.Share;
import com.travel.enjoyindanang.model.UserInfo;
import com.travel.enjoyindanang.ui.fragment.share.ShareDialogFragment;
import com.travel.enjoyindanang.utils.DialogUtils;
import com.travel.enjoyindanang.utils.Utils;
import com.travel.enjoyindanang.utils.ZaloUtils;
import com.travel.enjoyindanang.utils.event.OnShareZaloListener;
import com.travel.enjoyindanang.utils.network.NetworkUtils;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author: Tavv
 * Created on 23/08/2018
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class PartnerContentWebViewFragment extends DialogFragment implements View.OnTouchListener, OnShareZaloListener {
    private static final String TAG = PartnerContentWebViewFragment.class.getSimpleName();
    private static final String KEY_EXTRAS_TITLE = "title_category";
    private static final String KEY_EXTRAS_LOCATION = "current_location";

    private int categoryId;
    private Location mLocation;
    private UserInfo userInfo;
    private Category category;

    @BindView(R.id.webViewPartner)
    WebView webViewPartner;

    @BindView(R.id.progress_bar)
    ProgressBar prgLoading;

    @BindView(R.id.rlrEventContent)
    LinearLayout rlrEventContent;

    @BindView(R.id.frToolBar)
    FrameLayout frToolBar;

    @BindView(R.id.imgLogo)
    ImageView imgLogo;

    @BindView(R.id.name)
    TextView toolbarName;

    @BindView(R.id.img_share)
    ImageView imgShare;

    private ZaloUtils zaloUtils;

    private ShareDialogFragment bottomShareDialog;

    public static PartnerContentWebViewFragment newInstance(Category category, Location location) {
        PartnerContentWebViewFragment fragment = new PartnerContentWebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, category);
        bundle.putParcelable(KEY_EXTRAS_LOCATION, location);
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        final Dialog dialog = new Dialog(getActivity(), getTheme()) {
            @Override
            public void onBackPressed() {
                if (webViewPartner.canGoBack()) {
                    WebBackForwardList mWebBackForwardList = webViewPartner.copyBackForwardList();
                    String urlOriginal = webViewPartner.getOriginalUrl();
                    boolean isUrlShare = urlOriginal.contains("mobile/post");
                    if (mWebBackForwardList.getCurrentIndex() > 1 && isUrlShare) {
                        imgShare.setVisibility(View.VISIBLE);
                    } else {
                        imgShare.setVisibility(View.INVISIBLE);
                    }
                    webViewPartner.goBack();
                } else {
                    dismiss();
                }
            }
        };
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() == null) return;
        getDialog().getWindow().getAttributes().windowAnimations = R.style.dialog_animation_fade;
    }

    @Override
    public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
        // Call super onResume after sizing
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_partner_category_webview, container, false);
        ButterKnife.bind(this, rootView);
        initToolbar();
        initWebView();
        zaloUtils = new ZaloUtils();
        zaloUtils.setLoginZaLoListener(this);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userInfo = Utils.getUserInfo();
        getDataSent();
        if (category != null) {
            if (NetworkUtils.isNetworkAvailable(getContext())) {
                double latitude = 0;
                double longitude = 0;
                if (mLocation != null) {
                    latitude = mLocation.getLatitude();
                    longitude = mLocation.getLongitude();
                }
                String url = String.format(Constant.URL_PARTNER_WEB, category.getOriginalUrl(), userInfo.getUserId(),
                        String.valueOf(latitude), String.valueOf(longitude));
                webViewPartner.loadUrl(url);
            } else {
                webViewPartner.loadUrl("file:///android_asset/no_internet_template.html");
            }
        }
    }


    private void initWebView() {
        imgShare.setVisibility(View.INVISIBLE);
        WebSettings webSettings = webViewPartner.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setAppCacheEnabled(false);
        webSettings.setLoadWithOverviewMode(true);
        webViewPartner.setWebViewClient(new WebClient(getActivity()));
        webViewPartner.setOnTouchListener(this);
    }

    private void getDataSent() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            category = (Category) bundle.getSerializable(TAG);
            mLocation = bundle.getParcelable(KEY_EXTRAS_LOCATION);
        }
    }

    private void initToolbar() {
        setHeightToolbar();
        imgShare.setVisibility(View.VISIBLE);
        toolbarName.setVisibility(View.GONE);
    }

    private void setHeightToolbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            frToolBar.setPadding(0, Utils.getStatusBarHeight(), 0, 0);
        }
    }

    @OnClick({R.id.img_back, R.id.img_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                if (webViewPartner.canGoBack()) {
                    WebBackForwardList mWebBackForwardList = webViewPartner.copyBackForwardList();
                    if (mWebBackForwardList.getCurrentIndex() > 1) {
                        imgShare.setVisibility(View.VISIBLE);
                    } else {
                        imgShare.setVisibility(View.INVISIBLE);
                    }
                    webViewPartner.goBack();
                } else {
                    dismiss();
                }
                break;
            case R.id.img_share:
                String urlToShare = getUrlShare(webViewPartner.getOriginalUrl());
                Share share = new Share(webViewPartner.getTitle(), urlToShare, "");
                bottomShareDialog = DialogUtils.showSheetShareDialog(getActivity(), share);
//                DialogUtils.showPopupShare(getContext(), shareDialog, zaloUtils, getActivity(), urlToShare, webViewPartner.getTitle());
                break;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        WebView.HitTestResult hr = ((WebView) view).getHitTestResult();
        String urlOriginal = webViewPartner.getOriginalUrl();
        if (urlOriginal.contains("mobile/post")) {
            imgShare.setVisibility(View.VISIBLE);
        } else {
            imgShare.setVisibility(View.INVISIBLE);
        }
        return false;
    }

    @Override
    public void onShareSuccess() {
        if (bottomShareDialog != null) {
            bottomShareDialog.dismiss();
            String title = Utils.getString(R.string.share_title_success);
            title = String.format(title, "Zalo");
            DialogUtils.showDialog(getContext(), DialogType.SUCCESS, title, Utils.getString(R.string.share_content));
        }
    }

    @Override
    public void onShareFailure(String message) {

    }

    private class WebClient extends WebViewClient {
        private Activity activity;

        public WebClient(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            Common.onSslError(activity, view, handler, error);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            prgLoading.setVisibility(View.GONE);
            rlrEventContent.setVisibility(View.VISIBLE);
        }
    }

    private String getUrlShare(String originUrl) {
        if (StringUtils.isNotEmpty(originUrl)) {
            try {
                URL url = new URL(originUrl);
                Map<String, String> query_pairs = Utils.splitQuery(url);
                String userId = query_pairs.get("id");
                String friendly = query_pairs.get("friendlyUrl");
                if (StringUtils.isBlank(friendly)) {
                    friendly = "enjoy";
                }
                return Constant.URL_HOST_IMAGE + "/lands/topic/" + userId + "/" + friendly;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return StringUtils.EMPTY;
    }
}
