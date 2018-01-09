package com.travel.enjoyindanang.api;


import com.travel.enjoyindanang.LogApp;
import com.travel.enjoyindanang.R;
import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.constant.Constant;
import com.travel.enjoyindanang.model.BaseReponse;
import com.travel.enjoyindanang.model.NetworkStatus;
import com.travel.enjoyindanang.utils.Utils;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

public abstract class ApiCallback<M> extends Subscriber<M> {

    public abstract void onSuccess(M model);

    public abstract void onFailure(String msg);

    public abstract void onFinish();


    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        String status;
        String message = e.getMessage();
        if (StringUtils.isNotEmpty(message)) {
            if (e instanceof HttpException) {
                HttpException httpException = (HttpException) e;
                int code = httpException.code();
                status = String.valueOf(code);
                if (StringUtils.equals(status, Constant.API_500)) {
                    String msg = Utils.getLanguageByResId(R.string.Message_Server_Error);
                    msg = StringUtils.isBlank(msg) ? AppError.DEFAULT_SERVER_ERROR_MSG : msg;
                    onFailure(msg);
                }
            } else if (e instanceof ConnectException) {
                String msg = Utils.getLanguageByResId(R.string.Message_Server_Error);
                msg = StringUtils.isBlank(msg) ? AppError.DEFAULT_SERVER_ERROR_MSG : msg;
                onFailure(msg);
            } else if (e instanceof SocketTimeoutException || e instanceof UnknownHostException) {
                if (AppError.ENABLE_CATCH_TIME_OUT) {
                    String msg = Utils.getLanguageByResId(R.string.Message_Internet_Poor);
                    msg = StringUtils.isBlank(msg) ? AppError.DEFAULT_NETWORK_ERROR_MSG : msg;
                    onFailure(msg);
                }
            }
        }
        onFinish();
    }

    @Override
    public void onNext(M model) {
        onSuccess(model);
    }

    @Override
    public void onCompleted() {
        onFinish();
    }
}
