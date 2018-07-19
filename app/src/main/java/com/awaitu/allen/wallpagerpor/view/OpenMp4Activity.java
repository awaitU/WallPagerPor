package com.awaitu.allen.wallpagerpor.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;


import com.awaitu.allen.wallpagerpor.R;
import com.awaitu.allen.wallpagerpor.adapter.VideoDetailListViewAdapter;
import com.awaitu.allen.wallpagerpor.service.VideoLiveWallpaper;
import com.awaitu.allen.wallpagerpor.util.BitmapEntity;

import java.util.ArrayList;
import java.util.List;


@SuppressLint("ShowToast")
public class OpenMp4Activity extends Activity implements View.OnClickListener {
    private ListView mListView;
    private VideoDetailListViewAdapter adapter;
    private List<BitmapEntity> bit = new ArrayList();
    private Button clearData;
    private CheckBox isVoice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.openmp4);
        mListView = (ListView) this.findViewById(R.id.listview);
        isVoice = (CheckBox)this.findViewById(R.id.isVoice);
        clearData = (Button)this.findViewById(R.id.clearData);
        clearData.setOnClickListener(this);
        isVoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    VideoLiveWallpaper.voiceSize = 0;
                }else{
                    VideoLiveWallpaper.voiceSize = 1;
                }
            }
        });
        new Search_photo().start();
    }

    private Handler mHandler = new Handler() {
        @SuppressLint("ShowToast")
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1 && bit != null) {
                adapter = new VideoDetailListViewAdapter(getApplication(), bit);
                mListView.setAdapter(adapter);
                mListView.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        if (bit.size() != 0) {
                            String uri = "file://" + bit.get(arg2).getUri();
                            SharedPreferences sp = getSharedPreferences("demo", MODE_PRIVATE);
                            sp.edit().putString("url", uri).apply();
                            Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                            intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                                    new ComponentName(getApplication(), VideoLiveWallpaper.class));
                            startActivity(intent);
                        }
                    }
                });
            }
        }
    };

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clearData:
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
                try {
                    wallpaperManager.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    /**
     * 遍历系统数据库找出相应的是视频的信息，每找出一条视频信息的同时利用与之关联的找出对应缩略图的uri
     * 再异步加载缩略图，
     * 由于查询速度非常快，全部查找完成在设置，等待时间不会太长
     *
     * @author Administrator
     */
    class Search_photo extends Thread {
        @Override
        public void run() {
            // 如果有sd卡（外部存储卡）
            if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
                Uri originalUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                ContentResolver cr = OpenMp4Activity.this.getApplicationContext().getContentResolver();
                Cursor cursor = cr.query(originalUri, null, null, null, null);
                if (cursor == null) {
                    return;
                }
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                    String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                    long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                    long duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                    //获取当前Video对应的Id，然后根据该ID获取其缩略图的uri
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                    String[] selectionArgs = new String[]{id + ""};
                    String[] thumbColumns = new String[]{MediaStore.Video.Thumbnails.DATA,
                            MediaStore.Video.Thumbnails.VIDEO_ID};
                    String selection = MediaStore.Video.Thumbnails.VIDEO_ID + "=?";

                    String uri_thumb = "";
                    Cursor thumbCursor = (OpenMp4Activity.this.getApplicationContext().getContentResolver()).query(
                            MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, thumbColumns, selection, selectionArgs,
                            null);

                    if (thumbCursor != null && thumbCursor.moveToFirst()) {
                        uri_thumb = thumbCursor
                                .getString(thumbCursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA));

                    }

                    BitmapEntity bitmapEntity = new BitmapEntity(title, path, size, uri_thumb, duration);

                    bit.add(bitmapEntity);

                }
                if (cursor != null) {
                    cursor.close();
                    mHandler.sendEmptyMessage(1);
                }
            }
        }
    }
}
