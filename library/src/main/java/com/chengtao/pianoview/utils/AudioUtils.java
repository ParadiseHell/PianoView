package com.chengtao.pianoview.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.util.SparseIntArray;

import com.chengtao.pianoview.entity.Piano;
import com.chengtao.pianoview.entity.PianoKey;
import com.chengtao.pianoview.impl.LoadAudioMessage;
import com.chengtao.pianoview.impl.OnLoadAudioListener;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ChengTao on 2016-11-26.
 */

/**
 * 音频工具类
 */
public class AudioUtils implements LoadAudioMessage {
    private final static String TAG = "AudioUtils";
    //线程池,用于加载和播放音频
    private ExecutorService service = Executors.newCachedThreadPool();
    //最大音频数目
    private final static int MAX_STREAM = 20;
    private static AudioUtils instance = null;
    //消息ID
    private final static int LOAD_START = 1;
    private final static int LOAD_FINISH = 2;
    private final static int LOAD_ERROR = 3;
    private final static int LOAD_PROGRESS = 4;
    //发送进度的间隙时间
    private final static int SEND_PROGRESS_MESSAGE_BREAK_TIME = 500;
    //音频池，用于播放音频
    private SoundPool pool;
    //上下文
    private Context context;
    //加载音频接口
    private OnLoadAudioListener listener;
    //存放黑键和白键的音频加载后的ID的集合
    private SparseIntArray whiteKeyMusics = new SparseIntArray();
    private SparseIntArray blackKeyMusics = new SparseIntArray();
    //是否加载成功
    private boolean isLoadFinish = false;
    //是否正在加载
    private boolean isLoading = false;
    //用于处理进度消息
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case LOAD_START:
                    listener.loadPianoAudioStart();
                    break;
                case LOAD_FINISH:
                    listener.loadPianoAudioFinish();
                    break;
                case LOAD_ERROR:
                    listener.loadPianoAudioError((Exception) msg.obj);
                    break;
                case LOAD_PROGRESS:
                    listener.loadPianoAudioProgress((int) msg.obj);
                    break;
            }
        }
    };

    @SuppressWarnings("deprecation")
    private AudioUtils(Context context, OnLoadAudioListener listener){
        this.context = context;
        this.listener = listener;
        pool = new SoundPool(MAX_STREAM, AudioManager.STREAM_MUSIC,0);
    }

    //单例模式，只返回一个工具实例
    public static AudioUtils getInstance(Context context, OnLoadAudioListener listener){
        if (instance == null){
            synchronized (AudioUtils.class){
                if (instance == null){
                    instance = new AudioUtils(context,listener);
                }
            }
        }
        return instance;
    }

    /**
     * 记载音乐
     * @param piano 钢琴实体
     * @throws Exception
     */
    public void loadMusic(final Piano piano) throws Exception {
        if (pool == null){
            throw new Exception("请初始化SoundPool");
        }
        if (piano != null) {
            if (listener != null) {
                if (!isLoading && !isLoadFinish) {
                    isLoading = true;
                    service.execute(new Runnable() {
                        @Override
                        public void run() {
                            long currentTime = System.currentTimeMillis();
                            int currentNum = 0;
                            sendStartMessage();
                            ArrayList<PianoKey[]> whiteKeys = piano.getWhitePianoKeys();
                            int whiteKeyPos = 0;
                            for (int i = 0; i < whiteKeys.size(); i++) {
                                for (PianoKey key : whiteKeys.get(i)) {
                                    if (System.currentTimeMillis() - currentTime >= SEND_PROGRESS_MESSAGE_BREAK_TIME) {
                                        sendProgressMessage((int) (((float) currentNum / (float) Piano.PIANO_NUMS) * 100f));
                                        currentTime = System.currentTimeMillis();
                                    }
                                    try {
                                        whiteKeyMusics.put(whiteKeyPos, pool.load(context, key.getVoiceId(), 1));
                                        whiteKeyPos++;
                                    } catch (Exception e) {
                                        isLoading = false;
                                        sendErrorMessage(e);
                                        return;
                                    }
                                    currentNum++;
                                }
                            }
                            ArrayList<PianoKey[]> blackKeys = piano.getBlackPianoKeys();
                            int blackKeyPos = 0;
                            for (int i = 0; i < blackKeys.size(); i++) {
                                for (PianoKey key : blackKeys.get(i)) {
                                    if (System.currentTimeMillis() - currentTime >= SEND_PROGRESS_MESSAGE_BREAK_TIME) {
                                        sendProgressMessage((int) (((float) currentNum / (float) Piano.PIANO_NUMS) * 100f));
                                        currentTime = System.currentTimeMillis();
                                    }
                                    try {
                                        blackKeyMusics.put(blackKeyPos, pool.load(context, key.getVoiceId(), 1));
                                        blackKeyPos++;
                                    } catch (Exception e) {
                                        isLoading = false;
                                        sendErrorMessage(e);
                                        return;
                                    }
                                    currentNum++;
                                }
                            }
                            isLoadFinish = true;
                            sendProgressMessage(100);
                            sendFinishMessage();
                        }
                    });
                }
            } else {
                throw new Exception("请实现OnLoadMusicListener接口");
            }
        }
    }

    /**
     * 播放音乐
     * @param type 钢琴键类型
     * @param group 组数，从0开始
     * @param positionOfGroup 组内位置
     */
    public void playMusic(final Piano.PianoKeyType type, final int group, final int positionOfGroup){
        if (isLoadFinish) {
            service.execute(new Runnable() {
                @Override
                public void run() {
                    switch (type) {
                        case BLACK:
                            playBlackKeyMusic(group, positionOfGroup);
                            break;
                        case WHITE:
                            playWhiteKeyMusic(group, positionOfGroup);
                            break;
                    }
                }
            });
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
        pool.play(whiteKeyMusics.get(position),1f,1f,1,0,1f);
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
        pool.play(blackKeyMusics.get(position),1f,1f,1,0,1f);
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

    @Override
    public void sendProgressMessage(int progress) {
        handler.sendMessage(Message.obtain(handler,LOAD_PROGRESS,progress));
    }
}
