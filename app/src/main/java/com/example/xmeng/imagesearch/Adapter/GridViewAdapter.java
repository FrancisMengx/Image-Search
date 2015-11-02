package com.example.xmeng.imagesearch.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xmeng.imagesearch.Activities.ImageDisplayActivity;
import com.example.xmeng.imagesearch.Models.QueryImage;
import com.example.xmeng.imagesearch.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by xmeng on 11/1/15.
 */
public class GridViewAdapter extends ArrayAdapter<QueryImage>{
    public GridViewAdapter(Context context, List<QueryImage> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final QueryImage qi = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_layout_item, parent, false);
        }
        ImageView ivIamge = (ImageView) convertView.findViewById(R.id.ivImage);
        TextView tvContent = (TextView) convertView.findViewById(R.id.tvContent);
        ivIamge.setImageResource(0);
        tvContent.setText(Html.fromHtml(qi.content));
        Picasso.with(getContext()).load(qi.tbURL).into(ivIamge);
        return convertView;
    }
}
