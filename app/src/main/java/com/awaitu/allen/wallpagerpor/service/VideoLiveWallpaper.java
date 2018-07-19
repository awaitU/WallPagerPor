package com.awaitu.allen.wallpagerpor.service;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import java.io.IOException;


public class VideoLiveWallpaper extends WallpaperService {
    public Engine onCreateEngine() {
        return new VideoEngine();
    }

    public static int voiceSize = 0;
    public String getVideoUrl() {
        SharedPreferences sp = getSharedPreferences("demo", MODE_PRIVATE);
        String url = sp.getString("url", null);
        return url;
    }

    class VideoEngine extends Engine {

        private MediaPlayer mMediaPlayer;

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);

        }

        @Override
        public void onDestroy() {

            super.onDestroy();

        }

        @Override
        public void onVisibilityChanged(boolean visible) {

            if (visible) {
                mMediaPlayer.start();
            } else {
                mMediaPlayer.pause();
            }
        }


        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            mMediaPlayer = new MediaPlayer();
            try {
                mMediaPlayer.setDataSource(getApplication(), Uri.parse(getVideoUrl()));
                mMediaPlayer.setSurface(holder.getSurface());
                if(voiceSize==0){
                    mMediaPlayer.setVolume(0,0);
                }else{
                    mMediaPlayer.setVolume(1.0f,1.0f);
                }
                mMediaPlayer.setLooping(true);
                mMediaPlayer.prepare();
                mMediaPlayer.start();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            mMediaPlayer.release();
            mMediaPlayer = null;

        }
    }
}  