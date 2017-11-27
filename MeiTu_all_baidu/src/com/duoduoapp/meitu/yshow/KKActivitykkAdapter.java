package com.duoduoapp.meitu.yshow;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.duoduoapp.meitu.utils.ADBean;
import com.duoduoapp.meitu.utils.DownLoaderAPK;
import com.duoduoapp.meitu.utils.PackageUtil;
import com.duoduoapp.meitu.yshow.bean.kkroommodel;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hlxwdsj.bj.vz.R;


import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class KKActivitykkAdapter extends BaseAdapter {
    private final Context context;
    private final List<kkroommodel> tvmodels;

    ;

    Display display;
    Comparator comp_onlinecount = new Comparator() {

        @Override
        public int compare(Object obj1, Object obj2) {
            kkroommodel model1 = (kkroommodel) obj1;
            kkroommodel model2 = (kkroommodel) obj2;
            if (model1.onlineCount > model2.onlineCount) {
                return -1;
            } else if (model1.onlineCount == model2.onlineCount) {
                return 0;
            } else {
                return 1;
            }
        }

    };

    /*
     * type==1,按照在线人数排序
     */
    public KKActivitykkAdapter(List<kkroommodel> tvmodels, Context c, int type) {
        context = c;
        this.tvmodels = tvmodels;
        if (type == 1) {
            Collections.sort(this.tvmodels, comp_onlinecount);
        }

        display = ((Activity) c).getWindowManager().getDefaultDisplay();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return tvmodels.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_kkchangxiang, null);
            viewHolder.la_content = (LinearLayout) convertView.findViewById(R.id.la_content);
            viewHolder.txt_tvname = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.img_ico = (SimpleDraweeView) convertView.findViewById(R.id.ivIcon);
            viewHolder.tvOnlineCount = (TextView) convertView.findViewById(R.id.tvOnlineCount);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        LayoutParams params = viewHolder.img_ico.getLayoutParams();
        params.width = display.getWidth() / 3;
        params.height = params.width ;

        viewHolder.img_ico.setLayoutParams(params);
        final kkroommodel model = tvmodels.get(position);
        if (model.poster_path_300 == null || model.poster_path_300.equals("")) {

            viewHolder.img_ico.setImageResource(R.drawable.icon);
        } else {
            viewHolder.img_ico.setImageURI(model.poster_path_300);
        }
        viewHolder.la_content.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//				Intent intent = new Intent(mContext, KKLiveActivity.class);
//				// 房间ID(必选)
//				intent.putExtra("roomId", tvmodels.get(position).roomId);
//				mContext.startActivity(intent);
                final String packageNmae = "com.melot.meshow";
                if (PackageUtil.isInstallApp(context, packageNmae)) {

                    Intent myIntent = new Intent(Intent.ACTION_VIEW, null);
                    myIntent.setClassName("com.melot.meshow", "com.melot.kkcommon.activity.KKRoomActivity");
                    myIntent.putExtra("roomId",model.roomId);
                    myIntent.putExtra("roomType",model.roomType);
                    myIntent.putExtra("roomSource",model.roomSource);
                    try {
                        context.startActivity(myIntent);
                    } catch (Exception e) {


                        Intent myIntent3 = new Intent(Intent.ACTION_VIEW, null);
                        myIntent3.setClassName("com.melot.meshow", "com.melot.meshow.room.ChatRoom");
                        myIntent3.putExtra("roomId",model.roomId);
                        myIntent3.putExtra("roomType",model.roomType);
                        myIntent3.putExtra("roomSource",model.roomSource);
                        try {
                            context.startActivity(myIntent3);
                        } catch (Exception e3) {
                            PackageUtil.startApp(context,packageNmae);
                        }

                    }

                } else {
                    new AlertDialog.Builder(context).setTitle("初始化美女直播插件")
                            .setMessage("\t\t首次使用需要下载KK直播并安装插件，约40M，请您选择是否初始化!")
                            .setPositiveButton("下载", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ADBean adbean = new ADBean();
                                    adbean.setAd_name("KK直播插件");
                                    adbean.setAd_packagename(packageNmae);
                                    adbean.setAd_apkurl("http://www.kktv1.com/Share/download/70150/kktv.apk");
                                    adbean.setAd_versioncode(500);
                                    if (DownLoaderAPK.getInstance().addDownload(adbean)) {
                                        Toast.makeText(context, "开始下载:" + adbean.getAd_name(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, adbean.getAd_name() + " 已经在下载了:", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setCancelable(false).show();
                }
            }

        });
        viewHolder.tvOnlineCount.setText("在线人数" + model.onlineCount);
        viewHolder.txt_tvname.setText(model.nickname);
        return convertView;
    }

    public class ViewHolder {
        public LinearLayout la_content;
        public SimpleDraweeView img_ico;
        public TextView txt_tvname;
        public TextView tvOnlineCount;
    }


}
