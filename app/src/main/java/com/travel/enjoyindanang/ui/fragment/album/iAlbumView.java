package com.travel.enjoyindanang.ui.fragment.album;

import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.iBaseView;
import com.travel.enjoyindanang.model.PartnerAlbum;

import java.util.List;

/**
 * Author: Tavv
 * Created on 10/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public interface iAlbumView extends iBaseView {
    void onFetchAlbumSuccess(List<PartnerAlbum> images);

    void onFetchFail(AppError error);

}
