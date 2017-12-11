package com.travel.enjoyindanang.ui.fragment.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.travel.enjoyindanang.MvpFragment;
import com.travel.enjoyindanang.R;
import com.travel.enjoyindanang.annotation.DialogType;
import com.travel.enjoyindanang.api.model.Repository;
import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.constant.Constant;
import com.travel.enjoyindanang.constant.Extras;
import com.travel.enjoyindanang.framework.FragmentTransitionInfo;
import com.travel.enjoyindanang.model.Banner;
import com.travel.enjoyindanang.model.Category;
import com.travel.enjoyindanang.model.ExchangeRate;
import com.travel.enjoyindanang.model.Partner;
import com.travel.enjoyindanang.model.UserInfo;
import com.travel.enjoyindanang.model.Weather;
import com.travel.enjoyindanang.ui.activity.main.MainActivity;
import com.travel.enjoyindanang.ui.fragment.detail.dialog.DetailHomeDialogFragment;
import com.travel.enjoyindanang.ui.fragment.home.adapter.CategoryAdapter;
import com.travel.enjoyindanang.ui.fragment.home.adapter.PartnerAdapter;
import com.travel.enjoyindanang.utils.DialogUtils;
import com.travel.enjoyindanang.utils.Utils;
import com.travel.enjoyindanang.utils.event.OnItemClickListener;
import com.travel.enjoyindanang.utils.helper.EndlessParentScrollListener;
import com.travel.enjoyindanang.utils.helper.SeparatorDecoration;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.travel.enjoyindanang.R.id.fabFavorite;

/**
 * Created by chien on 10/8/17.
 */

public class HomeFragment extends MvpFragment<HomePresenter> implements iHomeView, AdapterView.OnItemClickListener, OnItemClickListener {
    private static final String TAG = HomeFragment.class.getSimpleName();
    private static final int VERTICAL_ITEM_SPACE = 8;
    private static final int DURATION_SLIDE = 3000;

    @BindView(R.id.rcv_partner)
    RecyclerView rcvPartner;
    @BindView(R.id.grv_menu)
    GridView gridView;

    @BindView(R.id.lrlContentHome)
    LinearLayout lrlContentHome;

    @BindView(R.id.progress_bar)
    ProgressBar prgLoading;

    @BindView(R.id.nestedScroll)
    NestedScrollView nestedScrollView;

    @BindView(R.id.carouselView)
    SliderLayout bannerSlider;

    private CategoryAdapter mCategoryAdapter;

    private final int startPage = 0;

    private int currentPage;

    private List<Partner> lstPartner;

    private List<Category> lstCategories;

    private PartnerAdapter mPartnerAdapter;

    private boolean hasLoadmore;

    private int countCategoryClick = 0;

    private LinearLayoutManager mLayoutManager;

    private LoadmorePartner loadmorePartner;

    private UserInfo user;

    private Location mLastLocation;

    @Override
    public void showToast(String desc) {

    }

    @Override
    public void unKnownError() {

    }

    @Override
    protected void init(View view) {
        user = Utils.getUserInfo();
        mMainActivity.setNameToolbar(Utils.getLanguageByResId(R.string.Home).toUpperCase());
        lstPartner = new ArrayList<>();
        mPartnerAdapter = new PartnerAdapter(getContext(), lstPartner, this);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rcvPartner.addItemDecoration(
                new SeparatorDecoration(getContext(), Utils.getColorRes(R.color.material_grey_300), VERTICAL_ITEM_SPACE));
        rcvPartner.setLayoutManager(mLayoutManager);
        rcvPartner.setAdapter(mPartnerAdapter);
        rcvPartner.setHasFixedSize(false);
        rcvPartner.setNestedScrollingEnabled(false);

        lstCategories = new ArrayList<>();
        mCategoryAdapter = new CategoryAdapter(getContext(), lstCategories);
        gridView.setAdapter(mCategoryAdapter);
    }

    @Override
    protected void setEvent(View view) {
        loadmorePartner = new LoadmorePartner(mLayoutManager);
        gridView.setOnItemClickListener(this);
        nestedScrollView.setOnScrollChangeListener(loadmorePartner);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter = createPresenter();
        loadmorePartner.setCategoryId(-1);
        gridView.setVisibility(View.VISIBLE);
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
    }


    @Override
    public void onGetBannerSuccess(List<Banner> images) {
        HashMap<String, String> sources = new HashMap<>();
        int length = images.size();
        for (int i = 0; i < length; i++) {
            sources.put(images.get(i).getTitle() + " [Slide " + i + " ]", images.get(i).getPicture());
        }
        for (String name : sources.keySet()) {
            DefaultSliderView textSliderView = new DefaultSliderView(getContext());
            textSliderView
                    .image(sources.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            bannerSlider.addSlider(textSliderView);
        }
        bannerSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        bannerSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        bannerSlider.setCustomAnimation(new DescriptionAnimation());
        bannerSlider.setDuration(DURATION_SLIDE);
    }

    @Override
    public void onGetBannerFailure(AppError error) {
        Log.e(TAG, "onGetBannerFailure: " + error.getMessage());
    }

    @Override
    public void onGetPartnerSuccess(List<Partner> partners) {
        if (CollectionUtils.isEmpty(partners)) {
            rcvPartner.setVisibility(View.GONE);
            return;
        }
        updateItemNoLoadmore(partners);
    }

    @Override
    public void onGetPartnerFailure(AppError error) {
        Log.e(TAG, "onGetPartnerFailure: " + error.getMessage());
    }

    @Override
    public void onGetCategorySuccess(List<Category> data) {
        lstCategories.addAll(data);
        mCategoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetCategoryFailure(AppError error) {
        Log.e(TAG, "onGetCategoryFailure: " + error.getMessage());
    }

    @Override
    public void onGetPartnerByCategorySuccess(Repository<Partner> data) {
        hideLoading();
        if (!Utils.isNotEmptyContent(data) && !hasLoadmore) {
            mPartnerAdapter.clearAndUpdateData(Collections.EMPTY_LIST);
            rcvPartner.setVisibility(View.GONE);
        } else {
            updateItemAdapter(data.getData());
            rcvPartner.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onGetPartnerByCategoryFailure(AppError error) {
        hideLoading();
        Log.e(TAG, "onGetPartnerByCategoryFailure: " + error.getMessage());
    }

    @Override
    public void onFetchWeatherSuccess(List<Weather> lstWeathers) {
//        WeatherAdapter mWeatherAdapter = new WeatherAdapter(lstWeathers, getContext());
//        rcvWeather.setAdapter(mWeatherAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(onChangedLocationReceiver, new IntentFilter(Extras.KEY_RECEIVER_LOCATION_ON_FOUND_FILTER));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(onChangedLocationReceiver);
    }

    @Override
    public void onFetchWeatherFailure(AppError error) {
        Log.e(TAG, "onGetPartnerByCategoryFailure: " + error.getMessage());
    }

    @Override
    public void onFetchExchangeRateSuccess(List<ExchangeRate> lstExchanges) {
        //Only one item return
//        ExchangeRate exchangeRate = lstExchanges.get(0);
//        String strExchange = String.format(Constant.EXCHANGE_RATE_FORMAT, exchangeRate.getRate());
//        txtNameExchange.setText(exchangeRate.getName());
//        ImageUtils.loadImageNoRadius(getContext(), imgUSRate, exchangeRate.getFlagEN());
//        ImageUtils.loadImageNoRadius(getContext(), imgVNRate, exchangeRate.getFlagVN());
//        txtExchangeRate.setText(strExchange);
    }

    @Override
    public void onFetchExchangeRateFailure(AppError error) {
        Log.e(TAG, "onGetPartnerByCategoryFailure: " + error.getMessage());
    }


    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(this);
    }

    private void updateItemAdapter(@NonNull List<Partner> partners) {
        if (countCategoryClick == 1 && !hasLoadmore) {
            clearFirstTimeData();
            lstPartner.addAll(partners);
            mPartnerAdapter.notifyItemRangeChanged(0, partners.size());
        } else {
            int newSize = partners.size();
            int oldSize = lstPartner.size();
            lstPartner.addAll(partners);
            mPartnerAdapter.notifyItemRangeChanged(0, newSize + oldSize);
        }
        mPartnerAdapter.notifyDataSetChanged();

    }

    private void updateItemNoLoadmore(@NonNull List<Partner> partners) {
        int newSize = partners.size();
        lstPartner.addAll(partners);
        mPartnerAdapter.notifyItemRangeChanged(0, newSize);
        mPartnerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        loadmorePartner = new LoadmorePartner(mLayoutManager);
//        nestedScrollView.setOnScrollChangeListener(loadmorePartner);
//        countCategoryClick++;
//        lstPartner.clear();
//        mMainActivity.enableBackButton(true);
        Category category = (Category) parent.getItemAtPosition(position);
        FragmentTransitionInfo transitionInfo = new FragmentTransitionInfo(R.anim.slide_up_in, R.anim.slide_to_left, R.anim.slide_up_in, R.anim.slide_to_left);
        mMainActivity.addFragment(PartnerCategoryFragment.newInstance(category.getId(), category.getName(), mLastLocation),
                R.id.container_fragment, PartnerCategoryFragment.class.getName(),
                transitionInfo);
//        if (category != null) {
//            for (int i = 0; i < gridView.getCount(); i++) {
//                View childView = gridView.getChildAt(i);
//                TextView txtName = (TextView) childView.findViewById(R.id.tv_name);
//                if (i == position) {
//                    txtName.setTextColor(Utils.getColorRes(R.color.color_category_selected));
//                } else {
//                    txtName.setTextColor(Utils.getColorRes(R.color.color_title_category));
//                }
//            }
//            showLoading();
//            loadmorePartner.setCategoryId(category.getId());
//            hasLoadmore = false;
//            mvpPresenter.getPartnerByCategory(category.getId(), startPage, user.getUserId());
//        }
    }


    private class LoadmorePartner extends EndlessParentScrollListener {
        private int categoryId = -1;

        public LoadmorePartner(RecyclerView.LayoutManager layoutManager) {
            super(layoutManager);
        }

        @Override
        public void onLoadMore(int page, int totalItemsCount) {
            currentPage = page;
            hasLoadmore = true;
            onRetryGetPartner(page, categoryId);
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }
    }


    private void onRetryGetPartner(int page, int categoryId) {
        if (categoryId != -1) {
            mvpPresenter.getPartnerByCategory(categoryId, page, user.getUserId());
        }
    }

    @Override
    public void onClick(View view, final int position) {
        if (view.getId() == fabFavorite) {
            if (user.getUserId() != 0) {
                FloatingActionButton fabFavorite = (FloatingActionButton) view;
                mvpPresenter.addFavorite(user.getUserId(), lstPartner.get(position).getId());
                boolean isFavorite = lstPartner.get(position).getFavorite() > 0;
                fabFavorite.setImageResource(isFavorite ? R.drawable.unfollow : R.drawable.follow);
                lstPartner.get(position).setFavorite(isFavorite ? 0 : 1);
            } else {
                DialogUtils.showDialog(getContext(), DialogType.WARNING, DialogUtils.getTitleDialog(2), Utils.getLanguageByResId(R.string.Message_You_Need_Login));
            }

        } else {
            MainActivity activity = (MainActivity) getActivity();
            activity.currentTab = HomeTab.None;
            activity.setShowMenuItem(Constant.SHOW_MENU_BACK);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (CollectionUtils.isNotEmpty(lstPartner)) {
                        Partner partner;
                        try {
                            partner = lstPartner.get(position);
                        } catch (IndexOutOfBoundsException ex) {
                            partner = null;
                        }
                        DetailHomeDialogFragment dialog = DetailHomeDialogFragment.newInstance(partner);
                        DialogUtils.openDialogFragment(mFragmentManager, dialog);
                    }
                }
            }, 50);
        }
    }

    @Override
    public void addFavoriteSuccess() {
    }

    @Override
    public void addFavoriteFailure(AppError error) {
        Log.e(TAG, "onError: " + error.getMessage());
        DialogUtils.showDialog(getContext(), DialogType.WRONG, DialogUtils.getTitleDialog(3), error.getMessage());
    }

    @Override
    public void onFetchAllDataSuccess(List<Partner> partners, List<Banner> banners, List<Category> categories) {
        setDataBanner(banners);
        setDataPartner(partners);
        setDataCategory(categories);
        prgLoading.setVisibility(View.GONE);
        lrlContentHome.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFetchFailure(AppError error) {
        Log.e(TAG, "onFetchFailure " + error.getMessage());
        DialogUtils.showDialog(getContext(), DialogType.WRONG, DialogUtils.getTitleDialog(3), error.getMessage());
    }

    private void clearFirstTimeData() {
        if (CollectionUtils.isNotEmpty(lstPartner)) {
            int size = this.lstPartner.size();
            lstPartner.clear();
            mPartnerAdapter.notifyItemRangeChanged(0, size);
        }
    }


    private void setDataBanner(List<Banner> images) {
        HashMap<String, String> sources = new HashMap<>();
        int length = images.size();
        for (int i = 0; i < length; i++) {
            sources.put(images.get(i).getTitle() + " [Slide " + i + " ]", images.get(i).getPicture());
        }
        for (String name : sources.keySet()) {
            DefaultSliderView textSliderView = new DefaultSliderView(getContext());
            textSliderView
                    .image(sources.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            bannerSlider.addSlider(textSliderView);
        }
        bannerSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        bannerSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        bannerSlider.setCustomAnimation(new DescriptionAnimation());
        bannerSlider.setDuration(DURATION_SLIDE);
    }

    private void setDataCategory(List<Category> categories) {
        lstCategories.addAll(categories);
        mCategoryAdapter.notifyDataSetChanged();
    }

    private void setDataPartner(List<Partner> partners) {
        if (CollectionUtils.isEmpty(partners)) {
            rcvPartner.setVisibility(View.GONE);
            return;
        }
        updateItemNoLoadmore(partners);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageReceive(Object obj) {
        if (obj instanceof String) {
            String msg = (String) obj;
            if (msg.equalsIgnoreCase(Constant.LOCATION_NOT_FOUND)) {
                mvpPresenter.getAllDataHome(user.getUserId(), StringUtils.EMPTY, StringUtils.EMPTY);
            } else {
                nestedScrollView.scrollTo(0, 0);
            }

        } else if (obj instanceof Location) {
            mLastLocation = (Location) obj;
            prgLoading.post(new Runnable() {
                public void run() {
                    if (mLastLocation == null) {
                        mvpPresenter.getAllDataHome(user.getUserId(), StringUtils.EMPTY, StringUtils.EMPTY);
                    } else {
                        String strGeoLat = String.valueOf(mLastLocation.getLatitude());
                        String strGeoLng = String.valueOf(mLastLocation.getLongitude());
                        mvpPresenter.getAllDataHome(user.getUserId(), strGeoLat, strGeoLng);
                    }
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    private BroadcastReceiver onChangedLocationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                Bundle bundle = intent.getBundleExtra(Extras.KEY_RECEIVER_LOCATION);
                if (bundle != null) {
                    Location currentLocation = bundle.getParcelable(Extras.EXTRAS_RECEIVER_LOCATION);
                    if (action.equalsIgnoreCase(Extras.KEY_RECEIVER_LOCATION_ON_FOUND_FILTER)) {
                        mLastLocation = currentLocation;
                    }
                }
            }
        }
    };
}
