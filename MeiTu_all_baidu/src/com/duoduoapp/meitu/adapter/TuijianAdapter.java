package com.duoduoapp.meitu.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.duoduoapp.meitu.itf.IData;
import com.duoduoapp.meitu.utils.ADBean;
import com.duoduoapp.meitu.utils.AppConfig;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hlxwdsj.bj.vz.R;


import java.util.List;

/**
 * 本地视频 适配器
 *
 * @author Administrator
 */
public class TuijianAdapter extends BaseAdapter{

    private Activity context;
    private List<ADBean> beans;
    private LayoutInflater inflater;
    private ViewHolder holder;
    Display display;
    public TuijianAdapter(Activity context, List<ADBean> infos) {
        this.context = context;
        this.beans = infos;
        inflater = LayoutInflater.from(context);
        display = ((Activity) context).getWindowManager().getDefaultDisplay();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return beans == null ? 0 : beans.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return beans.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.tuijian_list_item, null);
            holder.la_content = (LinearLayout) convertView.findViewById(R.id.la_content);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_dec = (TextView) convertView.findViewById(R.id.tv_dec);
            holder.btn_chaozuo = (Button) convertView.findViewById(R.id.btn_chaozuo);
            holder.iv_icon = (SimpleDraweeView) convertView.findViewById(R.id.iv_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final int p = position;
        ViewGroup.LayoutParams params = holder.iv_icon.getLayoutParams();
        params.width = display.getWidth() / 3;

        params.height = (int) (beans.get(p).getAd_iconscal() * params.width);
//        holder.iv_img.setLayoutParams(params);
        holder.iv_icon.setLayoutParams(params);
        holder.la_content.setVisibility(View.VISIBLE);
        holder.tv_name.setText(beans.get(p).getAd_name());
        holder.tv_dec.setText(beans.get(p).getAd_description());
        holder.iv_icon.setImageURI(beans.get(p).getAd_iconurl());
        holder.la_content.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AppConfig.openAD(context, beans.get(p),"wall_count");


            }
        });
        if (beans.get(p).isAd_have()) {

            holder.btn_chaozuo.setText("打开");

        } else {
            if (beans.get(p).getAd_type() == 1) {
                holder.btn_chaozuo.setText("下载");
            } else if (beans.get(p).getAd_type() == 2){
                holder.btn_chaozuo.setText("进入");
            }
            else  if (beans.get(p).getAd_type() == 3){
                holder.btn_chaozuo.setText("添加");
            }

        }
        holder.btn_chaozuo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AppConfig.openAD(context, beans.get(p),"wall_count");

            }
        });
        return convertView;
    }

    private class ViewHolder {
        LinearLayout la_content;
        SimpleDraweeView iv_icon;
        TextView tv_name;
        TextView tv_dec;
        Button btn_chaozuo;
    }

}
