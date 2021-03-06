package com.travel.enjoyindanang.ui.fragment.profile_menu;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.travel.enjoyindanang.MvpFragment;
import com.travel.enjoyindanang.R;
import com.travel.enjoyindanang.model.UserInfo;
import com.travel.enjoyindanang.utils.ImageUtils;
import com.travel.enjoyindanang.utils.Utils;
import com.travel.enjoyindanang.utils.helper.LanguageHelper;

/**
 * Created by chientruong on 1/5/17.
 */

public class ProfileMenuFragment extends MvpFragment<ProfileMenuPresenter> implements iProfileMenuView {

    @BindView(R.id.tv_username)
    TextView tvUserName;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_menthod)
    TextView tvMenthod;

    @BindView(R.id.lblUserName)
    TextView lblUserName;

    @BindView(R.id.lblPhone)
    TextView lblPhone;

    @BindView(R.id.lblEmail)
    TextView lblEmail;

    @BindView(R.id.lblMethodLogin)
    TextView lblMethodLogin;

    @BindView(R.id.tv_login_checking)
    TextView lblLogCheckin;

    @BindView(R.id.img_avatar)
    SimpleDraweeView imgAvatar;

    private UserInfo userInfo;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
//        MenuItem editItem = menu.findItem(R.id.menu_edit);
//        MenuItem scanItem = menu.findItem(R.id.menu_scan);
//        editItem.setVisible(false);
//        editItem.setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }
    @Override
    protected void init(View view) {
//        setHasOptionsMenu(true);
        userInfo = Utils.getUserInfo();
        if (userInfo!= null){initData();};
    }

    @Override
    protected void setEvent(View view) {

    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_profile_menu;
    }

    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected ProfileMenuPresenter createPresenter() {
        return new ProfileMenuPresenter(this);
    }

    @Override
    public void showToast(String desc) {

    }

    @Override
    public void unKnownError() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @OnClick(R.id.tv_login_checking)
    public void checking() {
        // TODO submit data to server...
    }

    private void initData(){
        tvUserName.setText(userInfo.getUserName());
        tvEmail.setText((StringUtils.isEmpty(userInfo.getEmail()) ? "" : userInfo.getEmail()));
        tvPhone.setText((StringUtils.isEmpty(userInfo.getPhone()) ? "" : userInfo.getPhone()));
        tvMenthod.setText((StringUtils.isEmpty(userInfo.getType()) ? "" : userInfo.getType()));
        ImageUtils.loadImageWithFreso( imgAvatar, userInfo.getImage());
    }

    @Override
    public void initViewLabel(View view) {
        super.initViewLabel(view);
        LanguageHelper.getValueByViewId(lblUserName, lblPhone, lblEmail, lblMethodLogin, lblLogCheckin);
    }

}
