package com.travel.enjoyindanang.ui.activity.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

import com.travel.enjoyindanang.constant.LoginType;
import com.travel.enjoyindanang.model.Data;
import com.travel.enjoyindanang.model.Picture;
import com.travel.enjoyindanang.model.User;
import com.travel.enjoyindanang.ui.activity.BaseActivity;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Author: Tavv
 * Created on 13/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class LoginViaGoogle implements ILogin<GoogleSignInAccount, User>, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = LoginViaGoogle.class.getSimpleName();

    private static final String SCOPES = "oauth2:profile email";

    static final int RC_SIGN_IN = 0x1;

    private GoogleApiClient mGoogleApiClient;

    private BaseActivity activity;

    private User user;

    private LoginPresenter mLoginPresenter;

    private LoginCallBack loginCallBack;

    public LoginViaGoogle(BaseActivity activity, LoginCallBack loginCallBack) {
        this.activity = activity;
        this.loginCallBack = loginCallBack;
    }

    @Override
    public void init() {
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .enableAutoManage(activity, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();
    }

    @Override
    public void login() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        loginCallBack.hideWaiting();
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void handleCallbackResult(GoogleSignInAccount signInAccount) {
        user = new User();
        user.setId(signInAccount.getEmail());
        user.setEmail(signInAccount.getEmail());
        user.setFullName(signInAccount.getDisplayName());
        user.setFirstName(signInAccount.getFamilyName());
        user.setLastName(signInAccount.getGivenName());
        Picture picture = new Picture();
        Data data = new Data();
        if (signInAccount.getPhotoUrl() != null) {
            data.setUrl(signInAccount.getPhotoUrl().toString());
        }else{
            data.setUrl(StringUtils.EMPTY);
        }
        picture.setData(data);
        user.setPicture(picture);
        user.setType(LoginType.GOOGLE);
    }

    @Override
    public BaseActivity getActivity() {
        return activity;
    }

    @Override
    public void pushToServer(User user) {
        if (user != null) {
            mLoginPresenter.showLoading();
            mLoginPresenter.loginViaSocial(user);
        }
    }

    @Override
    public void removeAccessToken() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            Toast.makeText(activity, "Remove token success", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    public void setLoginPresenter(LoginPresenter loginPresenter) {
        if (loginPresenter == null) {
            throw new NullPointerException("LoginPresenter not be null !");
        }
        this.mLoginPresenter = loginPresenter;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(activity, connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    public void getTokenGoogle(final GoogleSignInResult acct) {
        if (acct != null) {
            Observable.create(new Observable.OnSubscribe<String>() {
                @Override
                public void call(Subscriber<? super String> subscriber) {
                    try {
                        String token = GoogleAuthUtil.getToken(getApplicationContext(), acct.getSignInAccount().getAccount(), SCOPES);
                        subscriber.onNext(token);
                        subscriber.onCompleted();
                    } catch (IOException | GoogleAuthException e) {
                        e.printStackTrace();
                        subscriber.onError(e);
                    }
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(String token) {
                            if (user != null) {
                                user.setAccessToken(token);
                                pushToServer(user);
                            }
                            //removeAccessToken();
                        }
                    });
        }
    }
}
