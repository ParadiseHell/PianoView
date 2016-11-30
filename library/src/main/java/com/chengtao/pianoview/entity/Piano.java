package com.chengtao.pianoview.entity;

/**
 * Created by ChengTao on 2016-11-25.
 */

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;

import com.chengtao.pianoview.R;

import java.util.ArrayList;

/**
 * 钢琴实体
 */
public class Piano {
    //钢琴键数目
    public final static int PIANO_NUMS = 88;
    //黑白键的组数
    private final static int BLACK_PIANO_KEY_GROUPS = 8;
    private final static int WHITE_PIANO_KEY_GROUPS = 9;
    //黑白键集合
    private ArrayList<PianoKey[]> blackPianoKeys = new ArrayList<>(BLACK_PIANO_KEY_GROUPS);
    private ArrayList<PianoKey[]> whitePianoKeys = new ArrayList<>(WHITE_PIANO_KEY_GROUPS);
    //黑白键高度和宽度
    private int blackKeyWidth;
    private int blackKeyHeight;
    private int whiteKeyWidth;
    private int whiteKeyHeight;
    //钢琴总宽度
    private int pianoWith = 0;
    /**
     * 承载钢琴的布局的高度,用于初始化黑白键的高度和宽度
     */
    private float scale = 0;
    //上下文
    private Context context;
    //构造函数
    public Piano(Context context,float scale) {
        this.context = context;
        this.scale = scale;
        initPiano();
    }

    private void initPiano() {
        if (scale > 0) {
            //获取黑键和白键的高度和宽度
            Drawable blackDrawable = ContextCompat.getDrawable(context, R.drawable.black_piano_key);
            Drawable whiteDrawable = ContextCompat.getDrawable(context, R.drawable.white_piano_key);
            blackKeyWidth = blackDrawable.getIntrinsicWidth();
            blackKeyHeight = (int)((float)blackDrawable.getIntrinsicHeight() * scale);
            whiteKeyWidth = whiteDrawable.getIntrinsicWidth();
            whiteKeyHeight = (int)((float)whiteDrawable.getIntrinsicHeight() * scale);

            //初始化黑键
            for (int i = 0; i < BLACK_PIANO_KEY_GROUPS; i++) {
                switch (i) {
                    case 0:
                        PianoKey[] keys0 = new PianoKey[1];
                        for (int j = 0; j < keys0.length; j++) {
                            keys0[j] = new PianoKey();
                            keys0[j].setType(PianoKeyType.BLACK);
                            Rect areaOfKey[] = new Rect[1];
                            keys0[j].setGroup(i);
                            keys0[j].setPositionOfGroup(j);
                            keys0[j].setVoiceId(getVoiceFromResources("b" + i + j));
                            keys0[j].setPressed(false);
                            keys0[j].setKeyDrawable(
                                    new ScaleDrawable(ContextCompat.getDrawable(context, R.drawable.black_piano_key),
                                            Gravity.NO_GRAVITY,1,scale).getDrawable()
                            );
                            setBlackKeyDrawableBounds(i, j, keys0[j].getKeyDrawable());
                            areaOfKey[0] = keys0[j].getKeyDrawable().getBounds();
                            keys0[j].setAreaOfKey(areaOfKey);
                            keys0[j].setVoice(PianoVoice.LA);
                        }
                        blackPianoKeys.add(keys0);
                        break;
                    default:
                        PianoKey[] keys1 = new PianoKey[5];
                        for (int j = 0; j < keys1.length; j++) {
                            keys1[j] = new PianoKey();
                            Rect areaOfKey[] = new Rect[1];
                            keys1[j].setType(PianoKeyType.BLACK);
                            keys1[j].setGroup(i);
                            keys1[j].setPositionOfGroup(j);
                            keys1[j].setVoiceId(getVoiceFromResources("b" + i + j));
                            keys1[j].setPressed(false);
                            keys1[j].setKeyDrawable(
                                    new ScaleDrawable(ContextCompat.getDrawable(context, R.drawable.black_piano_key),
                                            Gravity.NO_GRAVITY,1,scale).getDrawable()
                            );
                            setBlackKeyDrawableBounds(i, j, keys1[j].getKeyDrawable());
                            areaOfKey[0] = keys1[j].getKeyDrawable().getBounds();
                            keys1[j].setAreaOfKey(areaOfKey);
                            switch (j) {
                                case 0:
                                    keys1[j].setVoice(PianoVoice.DO);
                                    break;
                                case 1:
                                    keys1[j].setVoice(PianoVoice.RE);
                                    break;
                                case 2:
                                    keys1[j].setVoice(PianoVoice.FA);
                                    break;
                                case 3:
                                    keys1[j].setVoice(PianoVoice.SO);
                                    break;
                                case 4:
                                    keys1[j].setVoice(PianoVoice.LA);
                                    break;
                            }
                        }
                        blackPianoKeys.add(keys1);
                        break;
                }
            }
            //初始化白键
            for (int i = 0; i < WHITE_PIANO_KEY_GROUPS; i++) {
                switch (i) {
                    case 0:
                        PianoKey keys0[] = new PianoKey[2];
                        for (int j = 0; j < keys0.length; j++) {
                            keys0[j] = new PianoKey();
                            keys0[j].setType(PianoKeyType.WHITE);
                            keys0[j].setGroup(i);
                            keys0[j].setPositionOfGroup(j);
                            keys0[j].setVoiceId(getVoiceFromResources("w" + i + j));
                            keys0[j].setPressed(false);
                            keys0[j].setKeyDrawable(
                                    new ScaleDrawable(ContextCompat.getDrawable(context, R.drawable.white_piano_key),
                                            Gravity.NO_GRAVITY,1,scale).getDrawable()
                            );
                            setWhiteKeyDrawableBounds(i, j, keys0[j].getKeyDrawable());
                            switch (j) {
                                case 0:
                                    keys0[j].setAreaOfKey(getWhitePianoKeyArea(i, j, BlackKeyPosition.RIGHT));
                                    keys0[j].setVoice(PianoVoice.LA);
                                    keys0[j].setLetterName("A0");
                                    break;
                                case 1:
                                    keys0[j].setAreaOfKey(getWhitePianoKeyArea(i, j, BlackKeyPosition.LEFT));
                                    keys0[j].setVoice(PianoVoice.SI);
                                    keys0[j].setLetterName("B0");
                                    break;
                            }
                            pianoWith += whiteKeyWidth;
                        }
                        whitePianoKeys.add(keys0);
                        break;
                    case 8:
                        PianoKey keys1[] = new PianoKey[1];
                        for (int j = 0; j < keys1.length; j++) {
                            keys1[j] = new PianoKey();
                            Rect areaOfKey[] = new Rect[1];
                            keys1[j].setType(PianoKeyType.WHITE);
                            keys1[j].setGroup(i);
                            keys1[j].setPositionOfGroup(j);
                            keys1[j].setVoiceId(getVoiceFromResources("w" + i + j));
                            keys1[j].setPressed(false);
                            keys1[j].setKeyDrawable(
                                    new ScaleDrawable(ContextCompat.getDrawable(context, R.drawable.white_piano_key),
                                            Gravity.NO_GRAVITY,1,scale).getDrawable()
                            );
                            setWhiteKeyDrawableBounds(i, j, keys1[j].getKeyDrawable());
                            areaOfKey[0] = keys1[j].getKeyDrawable().getBounds();
                            keys1[j].setAreaOfKey(areaOfKey);
                            keys1[j].setVoice(PianoVoice.DO);
                            keys1[j].setLetterName("C8");
                            pianoWith += whiteKeyWidth;
                        }
                        whitePianoKeys.add(keys1);
                        break;
                    default:
                        PianoKey keys2[] = new PianoKey[7];
                        for (int j = 0; j < keys2.length; j++) {
                            keys2[j] = new PianoKey();
                            //固定属性
                            keys2[j].setType(PianoKeyType.WHITE);
                            keys2[j].setGroup(i);
                            keys2[j].setPositionOfGroup(j);
                            keys2[j].setVoiceId(getVoiceFromResources("w" + i + j));
                            keys2[j].setPressed(false);
                            keys2[j].setKeyDrawable(
                                    new ScaleDrawable(ContextCompat.getDrawable(context, R.drawable.white_piano_key),
                                            Gravity.NO_GRAVITY,1,scale).getDrawable()
                            );
                            setWhiteKeyDrawableBounds(i, j, keys2[j].getKeyDrawable());
                            //非固定属性
                            switch (j) {
                                case 0:
                                    keys2[j].setAreaOfKey(getWhitePianoKeyArea(i, j, BlackKeyPosition.RIGHT));
                                    keys2[j].setVoice(PianoVoice.DO);
                                    keys2[j].setLetterName("C" + i);
                                    break;
                                case 1:
                                    keys2[j].setAreaOfKey(getWhitePianoKeyArea(i, j, BlackKeyPosition.LEFT_RIGHT));
                                    keys2[j].setVoice(PianoVoice.RE);
                                    keys2[j].setLetterName("D" + i);
                                    break;
                                case 2:
                                    keys2[j].setAreaOfKey(getWhitePianoKeyArea(i, j, BlackKeyPosition.LEFT));
                                    keys2[j].setVoice(PianoVoice.MI);
                                    keys2[j].setLetterName("E" + i);
                                    break;
                                case 3:
                                    keys2[j].setAreaOfKey(getWhitePianoKeyArea(i, j, BlackKeyPosition.RIGHT));
                                    keys2[j].setVoice(PianoVoice.FA);
                                    keys2[j].setLetterName("F" + i);
                                    break;
                                case 4:
                                    keys2[j].setAreaOfKey(getWhitePianoKeyArea(i, j, BlackKeyPosition.LEFT_RIGHT));
                                    keys2[j].setVoice(PianoVoice.SO);
                                    keys2[j].setLetterName("G" + i);
                                    break;
                                case 5:
                                    keys2[j].setAreaOfKey(getWhitePianoKeyArea(i, j, BlackKeyPosition.LEFT_RIGHT));
                                    keys2[j].setVoice(PianoVoice.LA);
                                    keys2[j].setLetterName("A" + i);
                                    break;
                                case 6:
                                    keys2[j].setAreaOfKey(getWhitePianoKeyArea(i, j, BlackKeyPosition.LEFT));
                                    keys2[j].setVoice(PianoVoice.SI);
                                    keys2[j].setLetterName("B" + i);
                                    break;
                            }
                            pianoWith += whiteKeyWidth;
                        }
                        whitePianoKeys.add(keys2);
                        break;
                }
            }
        }
    }

    public enum  PianoVoice{
        DO,RE,MI,FA,SO,LA,SI
    }
    public enum PianoKeyType{
        BLACK,WHITE
    }
    private enum BlackKeyPosition{
        LEFT,
        LEFT_RIGHT,
        RIGHT
    }

    /**
     * 从资源文件中获取声音ID
     * @param voiceName 声音的文件名
     * @return 声音ID
     */
    private int getVoiceFromResources(String voiceName){
        return context.getResources().getIdentifier(voiceName,"raw",context.getPackageName());
    }

    /**
     * 设置白色键的点击区域
     * @param group 组数，从0开始
     * @param positionOfGroup 本组数内的位置
     * @param blackKeyPosition 黑键占白键的位置
     * @return 矩形数组
     */
    private Rect[] getWhitePianoKeyArea(int group,int positionOfGroup,BlackKeyPosition blackKeyPosition){
        int offset = 0;
        if (group == 0){
            offset = 5;
        }
        switch (blackKeyPosition){
            case LEFT:
                Rect left[] = new Rect[2];
                left[0] = new Rect(
                        (7 * group - 5 + offset + positionOfGroup) * whiteKeyWidth,
                        blackKeyHeight,
                        (7 * group - 5 + offset + positionOfGroup) * whiteKeyWidth + blackKeyWidth / 2,
                        whiteKeyHeight
                );
                left[1] = new Rect(
                        (7 * group - 5 + offset + positionOfGroup) * whiteKeyWidth + blackKeyWidth / 2,
                        0,
                        (7 * group - 4 + offset + positionOfGroup) * whiteKeyWidth,
                        whiteKeyHeight
                );
                return left;
            case LEFT_RIGHT:
                Rect leftRight[] = new Rect[3];
                leftRight[0] = new Rect(
                        (7 * group - 5 + offset + positionOfGroup) * whiteKeyWidth,
                        blackKeyHeight,
                        (7 * group - 5 + offset + positionOfGroup) * whiteKeyWidth + blackKeyWidth / 2,
                        whiteKeyHeight
                );
                leftRight[1] = new Rect(
                        (7 * group - 5 + offset + positionOfGroup) * whiteKeyWidth + blackKeyWidth / 2,
                        0,
                        (7 * group - 4 + offset + positionOfGroup) * whiteKeyWidth - blackKeyWidth / 2,
                        whiteKeyHeight
                );
                leftRight[2] = new Rect(
                        (7 * group - 4 + offset + positionOfGroup) * whiteKeyWidth - blackKeyWidth / 2,
                        blackKeyHeight,
                        (7 * group - 4 + offset + positionOfGroup) * whiteKeyWidth,
                        whiteKeyHeight
                );
                return leftRight;
            case RIGHT:
                Rect right[] = new Rect[2];
                right[0] = new Rect(
                        (7 * group - 5 + offset + positionOfGroup) * whiteKeyWidth,
                        0,
                        (7 * group - 4 + offset + positionOfGroup) * whiteKeyWidth - blackKeyWidth / 2,
                        whiteKeyHeight
                );
                right[1] = new Rect(
                        (7 * group - 4 + offset + positionOfGroup) * whiteKeyWidth - blackKeyWidth / 2,
                        blackKeyHeight,
                        (7 * group - 4 + offset + positionOfGroup) * whiteKeyWidth,
                        whiteKeyHeight
                );
                return right;
        }
        return null;
    }

    /**
     * 设置白色键图案的位置
     * @param group 组数，从0开始
     * @param positionOfGroup 在本组中的位置
     * @param drawable 要设置的Drawale对象
     */
    private void setWhiteKeyDrawableBounds(int group,int positionOfGroup,Drawable drawable){
        int offset = 0;
        if (group == 0){
            offset = 5;
        }
        drawable.setBounds(
                (7 * group - 5 + offset + positionOfGroup) * whiteKeyWidth,
                0,
                (7 * group - 4 + offset + positionOfGroup) * whiteKeyWidth,
                whiteKeyHeight
        );
    }

    /**
     * 设置黑色键图案的位置
     * @param group 组数，从0开始
     * @param positionOfGroup 组内的位置
     * @param drawable 要设置的Drawale对象
     */
    private void setBlackKeyDrawableBounds(int group,int positionOfGroup,Drawable drawable){
        int whiteOffset = 0;
        int blackOffset = 0;
        if (group == 0){
            whiteOffset = 5;
        }
        if (positionOfGroup == 2 || positionOfGroup ==3 || positionOfGroup == 4){
            blackOffset = 1;
        }
        drawable.setBounds(
                (7 * group - 4 + whiteOffset + blackOffset + positionOfGroup) * whiteKeyWidth - blackKeyWidth / 2,
                0,
                (7 * group - 4 + whiteOffset + blackOffset + positionOfGroup) * whiteKeyWidth + blackKeyWidth / 2,
                blackKeyHeight
        );
    }

    public ArrayList<PianoKey[]> getWhitePianoKeys() {
        return whitePianoKeys;
    }

    public ArrayList<PianoKey[]> getBlackPianoKeys() {
        return blackPianoKeys;
    }

    public int getPianoWith() {
        return pianoWith;
    }
}
