package com.travel.enjoyindanang.ui.fragment.favorite;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.travel.enjoyindanang.R;
import com.travel.enjoyindanang.constant.Constant;
import com.travel.enjoyindanang.model.Partner;
import com.travel.enjoyindanang.utils.ImageUtils;
import com.travel.enjoyindanang.utils.Utils;
import com.travel.enjoyindanang.utils.event.OnItemClickListener;

/**
 * Author: Tavv
 * Created on 25/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private List<Partner> lstFavorites;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public FavoriteAdapter(List<Partner> lstFavorites, Context context, OnItemClickListener onItemClickListener) {
        this.lstFavorites = lstFavorites;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, final int position) {
        Partner favorite = lstFavorites.get(position);
        if (favorite != null) {
            holder.txtPartnerName.setText(favorite.getName());
            holder.txtDate.setText(Utils.formatDate(Constant.DATE_SERVER_FORMAT, Constant.DATE_FORMAT_DMY, favorite.getDate()));
            ImageUtils.loadImageNoRadius(context, holder.imgPartner, favorite.getPicture());
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(v, position);
                }
            });
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(v, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return CollectionUtils.isEmpty(lstFavorites) ? 0 : lstFavorites.size();
    }

    static class FavoriteViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imgPartner)
        ImageView imgPartner;

        @BindView(R.id.txtPartnerName)
        TextView txtPartnerName;

        @BindView(R.id.btnDelete)
        ImageView btnDelete;

        @BindView(R.id.txtDate)
        TextView txtDate;

        public View view;

        public FavoriteViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    public void removeItemAtPosition(ArrayList<Integer> pos) {
        for (int j = pos.size()-1; j >= 0; j--) {
            int position = pos.get(j);
            lstFavorites.remove(position);
            notifyDataSetChanged();
        }
    }

    public void removeAt(int position) {
        lstFavorites.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, lstFavorites.size());
        notifyDataSetChanged();
    }
}
