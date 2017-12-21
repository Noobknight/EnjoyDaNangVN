package com.travel.enjoyindanang.ui.fragment.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.travel.enjoyindanang.R;
import com.travel.enjoyindanang.model.Category;
import com.travel.enjoyindanang.utils.ImageUtils;

import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * Created by chien on 10/8/17.
 */

public class CategoryAdapter extends BaseAdapter {
    private Context context;
    private List<Category> categories;

    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int i) {
        return categories.get(i);
    }

    @Override
    public long getItemId(int position) {
        return CollectionUtils.isNotEmpty(categories) ? categories.get(position).getId() : 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder = null;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_home_menu, viewGroup, false);
            holder = new Holder();
            holder.tvName = (TextView) view.findViewById(R.id.tv_name);
            holder.imgCategory = (SimpleDraweeView) view.findViewById(R.id.img_icon);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        Category category = categories.get(i);
        holder.tvName.setText(category.getName());
        ImageUtils.loadImageWithFreso(holder.imgCategory, category.getIcon());
        return view;
    }

    static final class Holder {
        TextView tvName;
        SimpleDraweeView imgCategory;
    }

}