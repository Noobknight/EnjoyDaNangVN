package com.travel.enjoyindanang.ui.fragment.home;

import java.util.Collections;

import com.travel.enjoyindanang.BasePresenter;
import com.travel.enjoyindanang.api.ApiCallback;
import com.travel.enjoyindanang.api.model.Repository;
import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.model.Banner;
import com.travel.enjoyindanang.model.Category;
import com.travel.enjoyindanang.model.ExchangeRate;
import com.travel.enjoyindanang.model.HomeCombined;
import com.travel.enjoyindanang.model.Partner;
import com.travel.enjoyindanang.model.Weather;
import com.travel.enjoyindanang.utils.Utils;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func3;
import rx.schedulers.Schedulers;

/**
 * Created by chien on 10/8/17.
 */

public class HomePresenter extends BasePresenter<iHomeView> {

    public HomePresenter(iHomeView view) {
        super(view);
    }

    void getBanner() {
        addSubscription(apiStores.getBanner(), new ApiCallback<Repository<Banner>>() {

            @Override
            public void onSuccess(Repository<Banner> data) {
                mvpView.hideLoading();
                if (Utils.isNotEmptyContent(data)) {
                    mvpView.onGetBannerSuccess(data.getData());
                }
            }

            @Override
            public void onFailure(String msg) {
                mvpView.hideLoading();
                mvpView.onGetBannerFailure(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {

            }
        });
    }

    void getPartner(int page) {
        addSubscription(apiStores.getPartner(page), new ApiCallback<Repository<Partner>>() {

            @Override
            public void onSuccess(Repository<Partner> data) {
                mvpView.hideLoading();
                if (Utils.isNotEmptyContent(data)) {
                    mvpView.onGetPartnerSuccess(data.getData());
                } else {
                    mvpView.onGetPartnerSuccess(Collections.EMPTY_LIST);
                }

            }

            @Override
            public void onFailure(String msg) {
                mvpView.hideLoading();
                mvpView.onGetPartnerFailure(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {

            }
        });
    }

    void getAllCategories() {
        addSubscription(apiStores.getAllCategories(), new ApiCallback<Repository<Category>>() {

            @Override
            public void onSuccess(Repository<Category> data) {
//                mvpView.hideLoading();
                if (Utils.isNotEmptyContent(data)) {
                    mvpView.onGetCategorySuccess(data.getData());
                }

            }

            @Override
            public void onFailure(String msg) {
//                mvpView.hideLoading();
                mvpView.onGetCategoryFailure(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {

            }
        });
    }

    void getPartnerByCategory(int categoryId, int page, long userId) {
        addSubscription(apiStores.getPartnerByCategoryId(categoryId, page, userId), new ApiCallback<Repository<Partner>>() {

            @Override
            public void onSuccess(Repository<Partner> data) {
                mvpView.onGetPartnerByCategorySuccess(data);

            }

            @Override
            public void onFailure(String msg) {
                mvpView.onGetPartnerByCategoryFailure(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {

            }
        });
    }


    void getWeather() {
        addSubscription(apiStores.getWidgetWeather(), new ApiCallback<Repository<Weather>>() {

            @Override
            public void onSuccess(Repository<Weather> model) {
                if (Utils.isNotEmptyContent(model)) {
                    mvpView.onFetchWeatherSuccess(model.getData());
                }
            }

            @Override
            public void onFailure(String msg) {
                mvpView.hideLoading();
                mvpView.onFetchWeatherFailure(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {

            }
        });
    }

    void getExchangeRate() {
        addSubscription(apiStores.getWidgetExchangeRate(), new ApiCallback<Repository<ExchangeRate>>() {

            @Override
            public void onSuccess(Repository<ExchangeRate> model) {
                if (Utils.isNotEmptyContent(model)) {
                    mvpView.onFetchExchangeRateSuccess(model.getData());
                }
            }

            @Override
            public void onFailure(String msg) {
                mvpView.hideLoading();
                mvpView.onFetchExchangeRateFailure(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {

            }
        });
    }

    void getListHome(long customerId) {
        addSubscription(apiStores.getListPartnerHome(customerId), new ApiCallback<Repository<Partner>>() {

            @Override
            public void onSuccess(Repository<Partner> model) {
//                mvpView.hideLoading();
                if (Utils.isNotEmptyContent(model)) {
                    mvpView.onGetPartnerSuccess(model.getData());
                } else {
                    mvpView.onGetPartnerFailure(new AppError(new Throwable(model.getMessage())));
                }
            }

            @Override
            public void onFailure(String msg) {
                mvpView.onGetPartnerFailure(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {

            }
        });
    }

    void addFavorite(long userId, int partnerId) {
        addSubscription(apiStores.addFavorite(userId, partnerId), new ApiCallback<Repository>() {

            @Override
            public void onSuccess(Repository model) {
                if (Utils.isResponseError(model)) {
                    mvpView.addFavoriteFailure(new AppError(new Throwable(model.getMessage())));
                    return;
                }
                mvpView.addFavoriteSuccess();
            }

            @Override
            public void onFailure(String msg) {
                mvpView.addFavoriteFailure(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {

            }
        });
    }

    void getAllDataHome(long userId, String latitude, String longtitude) {
        Observable.zip(apiStores.getListPartnerHome(userId, latitude, longtitude), apiStores.getBanner(), apiStores.getAllCategories(), new Func3<Repository<Partner>, Repository<Banner>, Repository<Category>, HomeCombined>() {
            @Override
            public HomeCombined call(Repository<Partner> partnerRepository, Repository<Banner> bannerRepository, Repository<Category> categoryRepository) {
                return new HomeCombined(partnerRepository, bannerRepository, categoryRepository);
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiCallback<HomeCombined>() {
                    @Override
                    public void onSuccess(HomeCombined data) {
                        Repository<Partner> partnerRepository = data.getPartnerRepository();
                        Repository<Banner> bannerRepository = data.getBannerRepository();
                        Repository<Category> categoryRepository = data.getCategoryRepository();
                        if(Utils.isResponseError(partnerRepository)){
                           mvpView.onGetPartnerFailure(new AppError(new Throwable(partnerRepository.getMessage())));
                        }
                        if(Utils.isResponseError(bannerRepository)){
                            mvpView.onGetPartnerFailure(new AppError(new Throwable(bannerRepository.getMessage())));
                        }
                        if(Utils.isResponseError(categoryRepository)){
                            mvpView.onGetPartnerFailure(new AppError(new Throwable(categoryRepository.getMessage())));
                        }
                        mvpView.onFetchAllDataSuccess(partnerRepository.getData(), bannerRepository.getData(), categoryRepository.getData());
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.onFetchFailure(new AppError(new Throwable(msg)));
                    }

                    @Override
                    public void onFinish() {

                    }

                });
    }
}
