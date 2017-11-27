package com.duoduoapp.meitu.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.duoduoapp.meitu.bean.MainListBean;
import com.duoduoapp.meitu.utils.RecyclerViewOnItemClickListener;
import com.duoduoapp.meitu.utils.SelectUrlUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.hlxwdsj.bj.vz.R;

import java.util.List;

/**
 * Created by dkli on 2017/11/24.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private Context context;
    private List<MainListBean> beans;
    private double scale;
    private RecyclerViewOnItemClickListener listener;
    public MainAdapter(Context context, List<MainListBean> beans) {
        this.context = context;
        this.beans = beans;
        scale=context.getResources().getDisplayMetrics().density;
    }
    public void setOnItemClickListener(RecyclerViewOnItemClickListener listener){
        this.listener=listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.show2_grid_item,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int p = position;
        DraweeController controller;
        ImageRequest request;
        MainListBean listBean=beans.get(p);
        if ("ad".equals(beans.get(p).getAd_platform())) {
            request= ImageRequestBuilder.newBuilderWithSource(Uri.parse(beans.get(p).getAd_thumbnail())).setResizeOptions(new ResizeOptions((int)scale*100,(int)scale*178)).build();
            controller= Fresco.newDraweeControllerBuilder().setImageRequest(request).setOldController(holder.iv_image.getController()).setAutoPlayAnimations(true).build();
            holder.ad_log.setVisibility(View.VISIBLE);
            holder.tv_text.setText(beans.get(p).getAd_name());
        }else {
            request= ImageRequestBuilder.newBuilderWithSource(Uri.parse(SelectUrlUtils.selectUrl(beans.get(p)))).setResizeOptions(new ResizeOptions((int)scale*100,(int)scale*178)).build();
            controller= Fresco.newDraweeControllerBuilder().setImageRequest(request).setOldController(holder.iv_image.getController()).setAutoPlayAnimations(true).build();
            holder.ad_log.setVisibility(View.GONE);
            holder.tv_text.setText("1");
        }
        holder.iv_image.setController(controller);
    }

    @Override
    public int getItemCount() {
        return beans!=null?beans.size():0;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private RelativeLayout rl_content;
        private SimpleDraweeView iv_image;
        private View ad_log;
        private TextView tv_text;
        public ViewHolder(View itemView) {
            super(itemView);
            rl_content= (RelativeLayout) itemView.findViewById(R.id.rl_content);
            iv_image= (SimpleDraweeView) itemView.findViewById(R.id.iv_image);
            ad_log=itemView.findViewById(R.id.ad_log);
            tv_text= (TextView) itemView.findViewById(R.id.tv_text);
            rl_content.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(v,getLayoutPosition());
        }
    }
}
