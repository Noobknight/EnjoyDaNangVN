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
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            //httpException.response().errorBody().string()
            int code = httpException.code();
            status = String.valueOf(code);
            if(StringUtils.equals(status, Constant.API_500)){
                String msg = Utils.getLanguageByResId(R.string.Message_Server_Error);
                msg = StringUtils.isBlank(msg) ? AppError.DEFAULT_SERVER_ERROR_MSG : msg;
                onFailure(msg);
                return;
            }else if(status.matches(Constant.REGEX_NUMBER)){
                onFailure(AppError.DEFAULT_ERROR_MESSAGE);
                return;
            }
            onFailure(status);
        }
        else if(e instanceof ConnectException){
            String msg = Utils.getLanguageByResId(R.string.Message_Server_Error);
            msg = StringUtils.isBlank(msg) ? AppError.DEFAULT_SERVER_ERROR_MSG : msg;
            onFailure(msg);
        }
        else if(e instanceof SocketTimeoutException || e instanceof UnknownHostException){
            if(AppError.ENABLE_CATCH_TIME_OUT){
                String msg = Utils.getLanguageByResId(R.string.Message_Internet_Poor);
                msg = StringUtils.isBlank(msg) ? AppError.DEFAULT_NETWORK_ERROR_MSG : msg;
                onFailure(msg);
            }
        }
        else {
            String msgServer = e.getMessage();
            if(StringUtils.equals(msgServer, Constant.API_500)){
                String msg = Utils.getLanguageByResId(R.string.Message_Server_Error);
                msg = StringUtils.isBlank(msg) ? AppError.DEFAULT_SERVER_ERROR_MSG : msg;
                onFailure(msg);
                return;
            }else if(msgServer.matches(Constant.REGEX_NUMBER)){
                onFailure(AppError.DEFAULT_ERROR_MESSAGE);
                return;
            }
            onFailure(msgServer);
        }
        onFinish();
    }

    @Override
    public void onNext(M model) {
        if(model instanceof BaseReponse){
            if(!Constant.API_OK.equals(((BaseReponse)model).getStatus())){
                if(Constant.API_405.equals(((BaseReponse) model).getStatus())) {
                    LogApp.log("Device token expired");
                    EventBus.getDefault().post(new NetworkStatus("405"));
                    return;
                }else{
                    switch (((BaseReponse) model).getStatus()){
                        case Constant.API_402:
                            ((BaseReponse) model).setMessage(AppError.DEFAULT_ERROR_MESSAGE);
                            break;
                        case Constant.API_403:
                            ((BaseReponse) model).setMessage(AppError.DEFAULT_ERROR_MESSAGE);
                            break;
                        case Constant.API_404:
                            ((BaseReponse) model).setMessage(AppError.DEFAULT_ERROR_MESSAGE);
                            break;
                        case Constant.API_501:
                            ((BaseReponse) model).setMessage(AppError.DEFAULT_ERROR_MESSAGE);
                            break;
                        case Constant.API_502:
                            ((BaseReponse) model).setMessage(AppError.DEFAULT_ERROR_MESSAGE);
                            break;
                    }
                }
            }
        }
        onSuccess(model);
    }

    @Override
    public void onCompleted() {
        onFinish();
    }
}
