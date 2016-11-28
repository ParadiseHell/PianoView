package com.chengtao.pianoview.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseIntArray;

import com.chengtao.pianoview.entity.Piano;
import com.chengtao.pianoview.entity.PianoKey;
import com.chengtao.pianoview.impl.LoadAduioMessage;
import com.chengtao.pianoview.impl.OnLoadAudioListener;

import java.util.ArrayList;

/**
 * Created by ChengTao on 2016-11-26.
 */

public class AduioUtils implements LoadAduioMessage {
    private final static String TAG = "AduioUtils";
    private final static int MAX_STREAM = 20;
    private static AduioUtils instance = null;
    private final static int LOAD_START = 1;
    private final static int LOAD_FINISH = 2;
    private final static int LOAD_ERROR = 3;
    private SoundPool pool;
    private Context context;
    private OnLoadAudioListener listener;
    private SparseIntArray whiteKeyMusics = new SparseIntArray();
    private SparseIntArray blackKeyMusics = new SparseIntArray();
    private boolean isLoadFinish = false;
    private boolean isLoading = false;
    //
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case LOAD_START:
                    listener.loadPianoMusicStart();
                    break;
                case LOAD_FINISH:
                    listener.loadPianoMusicFinish();
                    break;
                case LOAD_ERROR:
                    listener.loadPianoMusicError((Exception) msg.obj);
                    break;
            }
        }
    };

    private AduioUtils(Context context, OnLoadAudioListener listener){
        this.context = context;
        this.listener = listener;
        pool = new SoundPool(MAX_STREAM, AudioManager.STREAM_MUSIC,0);
    }

    public static AduioUtils getInstance(Context context, OnLoadAudioListener listener){
        if (instance == null){
            synchronized (AduioUtils.class){
                if (instance == null){
                    instance = new AduioUtils(context,listener);
                }
            }
        }
        return instance;
    }

    public void loadMusic(final Piano piano) throws Exception {
        if (pool == null){
            throw new Exception("请初始化SoundPool");
        }
        if (listener != null){
            if (!isLoading && !isLoadFinish) {
                isLoading = true;
                new Thread() {
                    @Override
                    public void run() {
                        sendStartMessage();
                        ArrayList<PianoKey[]> whiteKeys = piano.getWhitePianoKeys();
                        int whiteKeyPos = 0;
                        for (int i = 0; i < whiteKeys.size(); i++) {
                            for (PianoKey key : whiteKeys.get(i)) {
                                try {
                                    whiteKeyMusics.put(whiteKeyPos, pool.load(context, key.getVoiceId(), 1));
                                    whiteKeyPos++;
                                } catch (Exception e) {
                                    sendErrorMessage(e);
                                    return;
                                }
                            }
                        }
                        ArrayList<PianoKey[]> blackKeys = piano.getBlackPianoKeys();
                        int blackKeyPos = 0;
                        for (int i = 0; i < blackKeys.size(); i++) {
                            for (PianoKey key : blackKeys.get(i)) {
                                try {
                                    blackKeyMusics.put(blackKeyPos, pool.load(context, key.getVoiceId(), 1));
                                    blackKeyPos++;
                                } catch (Exception e) {
                                    sendErrorMessage(e);
                                    return;
                                }
                            }
                        }
                        isLoadFinish = true;
                        sendFinishMessage();
                    }
                }.start();
            }
        }else {
            throw new Exception("请实现OnLoadMusicListener接口");
        }
    }

    /**
     * 播放音乐
     * @param type 钢琴键类型
     * @param group 组数，从0开始
     * @param positionOfGroup 组内位置
     */
    public void playMusic(Piano.PianoKeyType type,int group,int positionOfGroup){
        if (isLoadFinish) {
            switch (type) {
                case BLACK:
                    playBlackKeyMusic(group, positionOfGroup);
                    break;
                case WHITE:
                    playWhiteKeyMusic(group, positionOfGroup);
                    break;
            }
        }
    }

    /**
     * 播放白键音乐
     * @param group 组数，从0开始
     * @param positionOfGroup 组内位置
     */
    private void playWhiteKeyMusic(int group, int positionOfGroup) {
        int offset = 0;
        if (group == 0){
            offset = 5;
        }
        int position = 7 * group - 5 + offset + positionOfGroup;
        int result = pool.play(whiteKeyMusics.get(position),1f,1f,1,0,1f);
    }

    /**
     * 播放黑键音乐
     * @param group 组数，从0开始
     * @param positionOfGroup 组内位置
     */
    private void playBlackKeyMusic(int group, int positionOfGroup) {
        int offset = 0;
        if (group == 0){
            offset = 3;
        }
        int position = 4 * group - 3 + offset + positionOfGroup;
        int result = pool.play(blackKeyMusics.get(position),1f,1f,1,0,1f);
    }

    /**
     *
     */
    public void stop(){
        pool.release();
        pool = null;
        whiteKeyMusics.clear();
        blackKeyMusics.clear();
    }

    @Override
    public void sendStartMessage() {
        handler.sendEmptyMessage(LOAD_START);
    }

    @Override
    public void sendFinishMessage() {
        handler.sendEmptyMessage(LOAD_FINISH);
    }

    @Override
    public void sendErrorMessage(Exception e) {
        handler.sendMessage(Message.obtain(handler,LOAD_ERROR,e));
    }
}
