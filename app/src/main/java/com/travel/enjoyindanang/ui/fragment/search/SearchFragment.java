package com.travel.enjoyindanang.ui.fragment.search;

import android.location.Address;
import android.location.Location;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.travel.enjoyindanang.MvpFragment;
import com.travel.enjoyindanang.R;
import com.travel.enjoyindanang.annotation.DialogType;
import com.travel.enjoyindanang.model.Partner;
import com.travel.enjoyindanang.ui.fragment.detail.dialog.DetailHomeDialogFragment;
import com.travel.enjoyindanang.utils.DialogUtils;
import com.travel.enjoyindanang.utils.LocationUtils;
import com.travel.enjoyindanang.utils.Utils;
import com.travel.enjoyindanang.utils.event.OnItemClickListener;
import com.travel.enjoyindanang.utils.helper.LanguageHelper;
import com.travel.enjoyindanang.utils.helper.LocationHelper;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Author: Tavv
 * Created on 28/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class SearchFragment extends MvpFragment<SearchPresenter> implements iSearchView, OnMapReadyCallback,
        SearchView.OnQueryTextListener, OnItemClickListener {
    private static final String TAG = SearchFragment.class.getSimpleName();
    private static final float INIT_ZOOM_LEVEL = 17f;

    @BindView(R.id.search)
    SearchView searchView;

    @BindView(R.id.mapView)
    MapView mMapView;

    private GoogleMap mGoogleMap;

    @BindView(R.id.rcvSearchResult)
    RecyclerView rcvSearchResult;

    @BindView(R.id.lnlSearch)
    LinearLayout lnlSearch;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;


    private LocationHelper mLocationHelper;

    private Location mLastLocation;

    private SearchResultAdapter mAdapter;

    private List<Partner> lstPartner;

    @Override
    public void showToast(String desc) {

    }

    @Override
    public void unKnownError() {

    }

    @Override
    protected SearchPresenter createPresenter() {
        return new SearchPresenter(this);
    }

    @Override
    protected void init(View view) {
        progressBar.setVisibility(View.VISIBLE);
        if (mMainActivity != null) {
            if (mMainActivity.getLocationService() != null) {
                mLastLocation = mMainActivity.getLocationService().getLastLocation();
            }
            mLocationHelper = mMainActivity.mLocationHelper;
        }
        try {
            if (mMapView != null) {
                mMapView.onCreate(null);
                mMapView.getMapAsync(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        initRecyclerView();
    }


    @Override
    public void onResume() {
        super.onResume();
        if(mMapView != null){
            mMapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mMapView != null){
            mMapView.onPause();
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcvSearchResult.setLayoutManager(layoutManager);
        rcvSearchResult.setHasFixedSize(false);
        lstPartner = new ArrayList<>();
        mAdapter = new SearchResultAdapter(getContext(), lstPartner, this);
        rcvSearchResult.setAdapter(mAdapter);
    }


    @Override
    public void onStop() {
        super.onStop();
        if (mMapView != null) {
            mMapView.onStop();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mMapView != null && mGoogleMap != null) {
            mMapView.onLowMemory();
            mGoogleMap.clear();
            System.gc();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mGoogleMap != null) {
            mGoogleMap.clear();
        }
        if (mMapView != null) {
            mMapView.onDestroy();
            mMapView = null;
        }
        System.gc();
    }

    @Override
    protected void setEvent(View view) {
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
        customSearchView();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mLocationHelper.setGoogleMap(mGoogleMap);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        mGoogleMap.setMyLocationEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (LocationUtils.isGpsEnabled() && LocationUtils.isLocationEnabled()) {
            loadMapView(mLastLocation);
        } else {
            DialogUtils.showDialog(getContext(), DialogType.INFO, Utils.getLanguageByResId(R.string.Permisstion_Title),
                    Utils.getLanguageByResId(R.string.Map_Location_NotFound));
        }
    }

    private void customSearchView() {
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(Utils.getColorRes(R.color.color_title_category));
        searchEditText.setHintTextColor(Utils.getColorRes(R.color.material_grey_200));
    }

    private void loadMapView(Location currentLocation) {
        if (currentLocation != null) {
            LatLng point = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            MarkerOptions marker = new MarkerOptions();
            Address address = mLocationHelper.getAddress(currentLocation.getLatitude(), currentLocation.getLongitude());
            String titleMarker = mLocationHelper.getFullInfoAddress(address);
            if (mGoogleMap != null) {
                marker.position(point).title(titleMarker).draggable(false)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                CameraPosition cameraPosition = CameraPosition.builder().target(point).zoom(INIT_ZOOM_LEVEL).bearing(0).tilt(45).build();
                mGoogleMap.addMarker(marker);
                mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        }
    }

    @Override
    public void OnQuerySearchResult(List<Partner> lstPartner) {
        mAdapter.updateResult(lstPartner);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (StringUtils.isNotEmpty(query)) {
            showResultContainer(true);
            mvpPresenter.searchWithTitle(query);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (StringUtils.isNotEmpty(newText)) {
            mvpPresenter.searchWithTitle(newText);
            showResultContainer(true);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            mAdapter.clearAllItem();
            Utils.hideSoftKeyboard(getActivity());
            showResultContainer(false);
            progressBar.setVisibility(View.GONE);
        }
        return false;
    }


    @Override
    public void onClick(View view, int position) {
        DetailHomeDialogFragment dialog = DetailHomeDialogFragment.newInstance(lstPartner.get(position));
        DialogUtils.openDialogFragment(mFragmentManager, dialog);
    }

    private void showResultContainer(final boolean isShow) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rcvSearchResult.setVisibility(isShow ? View.VISIBLE : View.GONE);
                mMapView.setVisibility(isShow ? View.GONE : View.VISIBLE);
            }
        }, 500);
    }

    @Override
    public void initViewLabel(View view) {
        super.initViewLabel(view);
        LanguageHelper.getValueByViewId(searchView);
    }
}
