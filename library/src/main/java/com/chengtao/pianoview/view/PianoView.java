package com.chengtao.pianoview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.chengtao.pianoview.R;
import com.chengtao.pianoview.entity.Piano;
import com.chengtao.pianoview.entity.PianoKey;
import com.chengtao.pianoview.impl.OnLoadAudioListener;
import com.chengtao.pianoview.impl.OnPianoClickListener;
import com.chengtao.pianoview.utils.AudioUtils;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by ChengTao on 2016-11-25.
 */

public class PianoView extends View {
    private final static String TAG = "PianoView";
    //定义钢琴键
    private Piano piano = null;
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
    //播放器工具
    private AudioUtils utils = null;
    //上下文
    private Context context;
    //布局的宽度
    private int layoutWidth = 0;
    //缩放比例
    private float scale = 0;
    //音频加载接口
    private OnLoadAudioListener musicListener;

    //构造函数
    public PianoView(Context context) {
        this(context,null);
    }

    public PianoView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PianoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        paint = new Paint();
        paint.setAntiAlias(true);
        //初始化画笔
        paint.setStyle(Paint.Style.FILL);
        //初始化正方形
        square = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //初始化钢琴
        if (piano == null){
            piano = new Piano(context,scale);
            //获取白键
            whitePianoKeys = piano.getWhitePianoKeys();
            //获取黑键
            blackPianoKeys = piano.getBlackPianoKeys();
        }
        //初始化白键
        for (int i = 0; i < whitePianoKeys.size(); i++){
            for (PianoKey key : whitePianoKeys.get(i)){
                paint.setColor(Color.parseColor(pianoColors[i]));
                key.getKeyDrawable().draw(canvas);
                //初始化音名区域
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
        if (utils == null){
            //初始化播放器
            utils = AudioUtils.getInstance(getContext(),musicListener);
            try {
                utils.loadMusic(piano);
            } catch (Exception e) {
                Log.e(TAG,e.getMessage());
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable whiteKeyDrawable = ContextCompat.getDrawable(context, R.drawable.white_piano_key);
        //最小高度
        int whiteKeyHeight = whiteKeyDrawable.getMinimumHeight() / 2;
        //最小宽度
        int whiteKeyWidth = (whiteKeyDrawable.getMinimumHeight() / 2) * 7;
        //获取布局中的高度和宽度及其模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        //设置宽度
        switch (widthMode){
            case MeasureSpec.EXACTLY:
                break;
            case MeasureSpec.AT_MOST:
                width = Math.min(width,whiteKeyWidth);
                break;
            case MeasureSpec.UNSPECIFIED:
                width = whiteKeyWidth;
                break;
        }
        //设置高度
        switch (heightMode){
            case MeasureSpec.EXACTLY:
                break;
            case MeasureSpec.AT_MOST:
                height = Math.min(height,whiteKeyHeight);
                break;
            case MeasureSpec.UNSPECIFIED:
                height = whiteKeyHeight;
                break;
        }
        //设置缩放比例
        scale = (float) (height - getPaddingTop() - getPaddingBottom()) / (float) dpToPx(whiteKeyHeight);
        layoutWidth = width - getPaddingLeft() - getPaddingRight();
        //设置布局高度和宽度
        setMeasuredDimension(width,height);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (MotionEventCompat.getActionMasked(event)){
            //当第一个手指点击按键的时候
            case MotionEvent.ACTION_DOWN:
                handleDown(MotionEventCompat.getActionIndex(event),event);
                break;
            //当手指在键盘上滑动的时候
            case MotionEvent.ACTION_MOVE:
                for (int i = 0; i < event.getPointerCount(); i++){
                    handleMove(i,event);
                }
                for (int i = 0; i < event.getPointerCount(); i++){
                    handleDown(i,event);
                }
                break;
            //多点触控，当其他手指点击键盘的手
            case MotionEvent.ACTION_POINTER_DOWN:
                handleDown(MotionEventCompat.getActionIndex(event),event);
                break;
            //多点触控，当其他手指抬起的时候
            case MotionEvent.ACTION_POINTER_UP:
                handlePointerUp(event.getPointerId(MotionEventCompat.getActionIndex(event)));
                break;
            //但最后一个手指抬起的时候
            case MotionEvent.ACTION_UP:
                Log.e(TAG,"ACTION_UP:"+MotionEventCompat.getActionIndex(event));
                handleUp();
                break;
        }
        return true;
    }

    /**
     * 处理多点触控时，手指抬起事件
     * @param pointerId 触摸点ID
     */
    private void handlePointerUp(int pointerId) {
        for (PianoKey key : pressedKeys){
            if (key.getFingerID() == pointerId){
                key.setPressed(false);
                key.resetFingerID();
                key.getKeyDrawable().setState(new int[] {-android.R.attr.state_pressed});
                invalidate(key.getKeyDrawable().getBounds());
                pressedKeys.remove(key);
                break;
            }
        }
    }

    /**
     * 处理滑动
     * @param which 触摸点下标
     * @param event 事件对象
     */
    private void handleMove(int which, MotionEvent event) {
        int x = (int)event.getX(which) + this.getScrollX();
        int y = (int)event.getY(which);
        for (PianoKey key : pressedKeys){
            if (key.getFingerID() == event.getPointerId(which)){
                if (!key.contains(x,y)){
                    key.getKeyDrawable().setState(new int[] {-android.R.attr.state_pressed});
                    invalidate(key.getKeyDrawable().getBounds());
                    key.setPressed(false);
                    key.resetFingerID();
                    pressedKeys.remove(key);
                }
            }
        }
    }

    /**
     * 处理最后一个手指抬起事件
     */
    private void handleUp() {
        if (pressedKeys.size() > 0){
            for (PianoKey key : pressedKeys){
                key.getKeyDrawable().setState(new int[]{-android.R.attr.state_pressed});
                key.setPressed(false);
                invalidate(key.getKeyDrawable().getBounds());
            }
            pressedKeys.clear();
        }
    }

    /**
     * 处理按下事件
     * @param which 那个触摸点
     * @param event 事件对象
     */
    private void handleDown(int which, MotionEvent event) {
        int x = (int)event.getX(which) + this.getScrollX();
        int y = (int)event.getY(which);
        //检查白键
        for (int i = 0; i < whitePianoKeys.size(); i++){
            for (PianoKey key : whitePianoKeys.get(i)){
                if (!key.isPressed() && key.contains(x,y)){
                    key.getKeyDrawable().setState(new int[]{android.R.attr.state_pressed});
                    invalidate(key.getKeyDrawable().getBounds());
                    utils.playMusic(Piano.PianoKeyType.WHITE,key.getGroup(),key.getPositionOfGroup());
                    key.setPressed(true);
                    key.setFingerID(event.getPointerId(which));
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
                    key.setFingerID(event.getPointerId(which));
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
    public void setOnLoadMusicListener(OnLoadAudioListener musicListener) {
        this.musicListener = musicListener;
    }

    public int getPianoWidth(){
        if (piano != null){
            return piano.getPianoWith();
        }
        return 0;
    }

    /**
     * 移动
     * @param progress 移动百分比
     */
    public void scroll(int progress){
        switch (progress){
            case 0:
                this.scrollTo(0,0);
                break;
            case 100:
                this.scrollTo(getPianoWidth() - getLayoutWidth() ,0);
                break;
            default:
                this.scrollTo((int)(((float)progress / 100f) * (float)(getPianoWidth() - getLayoutWidth())),0);
                break;
        }
    }

    /**
     * 将dp装换成px
     * @param dp dp值
     * @return px值
     */
    private int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    /**
     * 获取钢琴布局的实际宽度
     * @return 钢琴布局的实际宽度
     */
    public int getLayoutWidth() {
        return layoutWidth;
    }

    /**
     * 设置显示音名的矩形的颜色<br>
     *     <b>注:一共9中颜色</b>
     * @param pianoColors 颜色数组，长度为9
     */
    public void setPianoColors(String[] pianoColors) {
        if (pianoColors.length == 9){
            this.pianoColors = pianoColors;
        }
    }
}
