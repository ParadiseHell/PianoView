package com.chengtao.pianoview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.chengtao.pianoview.entity.Piano;
import com.chengtao.pianoview.entity.PianoKey;
import com.chengtao.pianoview.impl.OnLoadMusicListener;
import com.chengtao.pianoview.impl.OnPianoClickListener;
import com.chengtao.pianoview.utils.MusicUtils;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by ChengTao on 2016-11-25.
 */

public class PianoView extends View {
    private final static String TAG = "PianoView";
    //定义钢琴键
    private final Piano piano;
    private ArrayList<PianoKey[]> whitePianoKeys;
    private ArrayList<PianoKey[]> blackPianoKeys;
    //被点击过的钢琴键
    private CopyOnWriteArrayList<PianoKey> pressedKeys = new CopyOnWriteArrayList<>();
    //接口
    private OnPianoClickListener listener;
    //画笔
    private Paint paint;
    //定义标识音名的正方形
    private RectF square;
    //正方形北京颜色
    private String pianoColors[] = {"#C0C0C0", "#A52A2A","#FF8C00", "#FFFF00","#00FA9A", "#00CED1", "#4169E1", "#FFB6C1", "#FFEBCD"};
    //播放器
    private static MusicUtils utils;


    public PianoView(Context context) {
        this(context,null);
    }

    public PianoView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PianoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setAntiAlias(true);
        piano = new Piano(context);
        //获取白键
        whitePianoKeys = piano.getWhitePiaoKeys();
        //获取黑键
        blackPianoKeys = piano.getBlackPianoKyes();
        //初始化画笔
        paint.setStyle(Paint.Style.FILL);
        //初始化正方形
        square = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //初始化白键
        for (int i = 0; i < whitePianoKeys.size(); i++){
            for (PianoKey key : whitePianoKeys.get(i)){
                paint.setColor(Color.parseColor(pianoColors[i]));
                key.getKeyDrawable().draw(canvas);
                Rect r = key.getKeyDrawable().getBounds();
                int sideLength = (r.right - r.left) / 2;
                int left = r.left + sideLength / 2;
                int top = r.bottom - sideLength - sideLength / 3;
                int right = r.right - sideLength / 2;
                int bottom = r.bottom - sideLength / 3;
                square.set(left,top,right,bottom);
                canvas.drawRoundRect(square,6f,6f,paint);
                paint.setColor(Color.BLACK);
                paint.setTextSize(sideLength/1.8f);
                Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
                int baseline = (int)((square.bottom + square.top - fontMetrics.bottom - fontMetrics.top) / 2);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(key.getLetterName(),square.centerX(),baseline,paint);
            }
        }
        //初始化黑键
        for (int i = 0; i < blackPianoKeys.size(); i++){
            for (PianoKey key : blackPianoKeys.get(i)){
                key.getKeyDrawable().draw(canvas);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                handleDown(0,event);
                break;
            case MotionEvent.ACTION_MOVE:
                for (int i = 0; i < event.getPointerCount(); i++){
                    handleMove(i,event);
                }
                break;
            case MotionEvent.ACTION_UP:
                handleUp();
                break;
        }
        return true;
    }

    /**
     * 处理滑动
     * @param which 那个触摸点
     * @param event 事件对象
     */
    private void handleMove(int which, MotionEvent event) {
        int x = (int)event.getX(which);
        int y = (int)event.getY(which);
        for (PianoKey key : pressedKeys){
            if (!key.contains(x,y)){
                key.setPressed(false);
                key.getKeyDrawable().setState(new int[] {-android.R.attr.state_pressed});
                invalidate(key.getKeyDrawable().getBounds());
                pressedKeys.remove(key);
            }
        }
        for (int i = 0; i < event.getPointerCount(); i++){
            handleDown(i,event);
        }
    }

    /**
     * 处理抬起事件
     */
    private void handleUp() {
        for (PianoKey key : pressedKeys){
            key.getKeyDrawable().setState(new int[]{-android.R.attr.state_pressed});
            key.setPressed(false);
            invalidate(key.getKeyDrawable().getBounds());
        }
        pressedKeys.clear();
    }

    /**
     * 处理按下事件
     * @param which 那个触摸点
     * @param event 事件对象
     */
    private void handleDown(int which, MotionEvent event) {
        int x = (int)event.getX(which);
        int y = (int)event.getY(which);
        //检查白键
        for (int i = 0; i < whitePianoKeys.size(); i++){
            for (PianoKey key : whitePianoKeys.get(i)){
                if (!key.isPressed() && key.contains(x,y)){
                    key.getKeyDrawable().setState(new int[]{android.R.attr.state_pressed});
                    key.setPressed(true);
                    invalidate(key.getKeyDrawable().getBounds());
                    utils.playMusic(Piano.PianoKeyType.WHITE,key.getGroup(),key.getPositionOfGroup());
                    if (listener != null){
                        listener.onPianoClick(key.getType(),key.getVoice(),key.getGroup(),key.getPositionOfGroup());
                    }
                    pressedKeys.add(key);
                }
            }
        }
        //检查黑键
        for (int i = 0; i < blackPianoKeys.size(); i++){
            for (PianoKey key : blackPianoKeys.get(i)){
                if (!key.isPressed() && key.contains(x,y)){
                    key.getKeyDrawable().setState(new int[]{android.R.attr.state_pressed});
                    invalidate(key.getKeyDrawable().getBounds());
                    utils.playMusic(Piano.PianoKeyType.BLACK,key.getGroup(),key.getPositionOfGroup());
                    key.setPressed(true);
                    if (listener != null){
                        listener.onPianoClick(key.getType(),key.getVoice(),key.getGroup(),key.getPositionOfGroup());
                    }
                    pressedKeys.add(key);
                }
            }
        }
    }

    /**
     * 设置钢琴点击接口
     * @param listener 点击接口
     */
    public void setOnPianoClickListener(OnPianoClickListener listener) {
        this.listener = listener;
    }

    /**
     * 初始化OnLoadMusicListener接口
     * @param musicListener OnLoadMusicListener接口
     */
    public void setOnLoadMusicListener(OnLoadMusicListener musicListener) {
        //初始化播放器
        if (utils == null){
            utils = new MusicUtils(getContext(),musicListener);
        }
        if (!utils.isLoadFinish()){
            try {
                Log.e(TAG,"loadMusic");
                utils.loadMusic(piano);
            } catch (Exception e) {
                Log.e(TAG,e.getMessage());
            }
        }
    }
}
