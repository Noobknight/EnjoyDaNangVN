package com.travel.enjoyindanang.ui.fragment.review;

import java.util.List;

import com.travel.enjoyindanang.api.model.Repository;
import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.iBaseView;
import com.travel.enjoyindanang.model.Reply;
import com.travel.enjoyindanang.model.Review;

/**
 * Author: Tavv
 * Created on 10/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public interface iReviewView extends iBaseView {

    void onFetchReviews(List<Review> models);

    void onFetchFailure(AppError error);

    void onFetchReplyByReview(Repository<Reply> data);

    void onRefreshReviews(List<Review> models);

}
