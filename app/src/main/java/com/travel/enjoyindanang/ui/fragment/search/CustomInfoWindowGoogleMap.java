package com.travel.enjoyindanang.ui.fragment.search;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.Map;

import com.travel.enjoyindanang.R;
import com.travel.enjoyindanang.model.InfoWindow;

/**
 * Author: Tavv
 * Created on 14/12/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {
    private final Map<Marker, Bitmap> images = new HashMap<>();
    private final Map<Marker, Target<Bitmap>> targets = new HashMap<>();
    private static final int ICON_MARKER_SIZE = 70;

    private Context context;

    public CustomInfoWindowGoogleMap(Context context) {
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    /**
     * initiates loading the info window and makes sure the new image is used in case it changed
     */
    public void showInfoWindow(Marker marker) {
        Glide.clear(targets.get(marker)); // will do images.remove(marker) too
        marker.showInfoWindow(); // indirectly calls getInfoContents which will return null and start Glide load
    }

    /**
     * use this to discard a marker to make sure all resources are freed and not leaked
     */
    public void remove(Marker marker) {
        images.remove(marker);
        Glide.clear(targets.remove(marker));
        marker.remove();
    }

    @Override
    public View getInfoContents(Marker marker) {
        Bitmap image = images.get(marker);
        View view = ((Activity) context).getLayoutInflater().inflate(R.layout.item_marker_info, null);

        ImageView imgPartner = (ImageView) view.findViewById(R.id.imgPartner);
        TextView txtPartnerName = (TextView) view.findViewById(R.id.txtPartnerName);
        TextView txtAddress = (TextView) view.findViewById(R.id.txtAddress);

        InfoWindow infoWindow = (InfoWindow) marker.getTag();
        if (infoWindow != null) {
            txtPartnerName.setText(infoWindow.getPartnerName());
            txtAddress.setText(infoWindow.getAddress());

            if (image == null) {
                Glide.with(context).load(infoWindow.getImage()).asBitmap().dontAnimate().into(getTarget(marker));
                return null; // or something indicating loading
            } else {
                imgPartner.setImageBitmap(image);
            }
        }
        return view;
    }


    private Target<Bitmap> getTarget(Marker marker) {
        Target<Bitmap> target = targets.get(marker);
        if (target == null) {
            target = new InfoTarget(marker);
        }
        return target;
    }

    private class InfoTarget extends SimpleTarget<Bitmap> {
        Marker marker;

        InfoTarget(Marker marker) {
            super(ICON_MARKER_SIZE, ICON_MARKER_SIZE); // otherwise Glide will load original sized bitmap which is huge
            this.marker = marker;
        }

        @Override
        public void onLoadCleared(Drawable placeholder) {
            images.remove(marker); // clean up previous image, it became invalid
            // don't call marker.showInfoWindow() to update because this is most likely called from Glide.into()
        }

        @Override
        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
            // this prevents recursion, because Glide load only starts if image == null in getInfoContents
            images.put(marker, resource);
            // tell the maps API it can try to call getInfoContents again, this time finding the loaded image
            marker.showInfoWindow();
        }
    }
}
