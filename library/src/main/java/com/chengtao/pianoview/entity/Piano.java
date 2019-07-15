package com.chengtao.pianoview.entity;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import java.util.ArrayList;
import java.util.List;


/*
 * 钢琴模型
 *
 * @author ChengTao <a href="mailto:tao@paradisehell.org">Contact me.</a>
 */

public final class Piano {
  //<editor-fold desc="常量">
  // 钢琴键数目
  public static final int PIANO_COUNT = 88;
  //</editor-fold>

  //<editor-fold desc="属性">
  // 钢琴是否初始化过
  private volatile boolean mHasInit;
  // 黑键集合, 黑键有 8 组
  private List<PianoKey[]> mBlackPianoKeys = new ArrayList<>(8);
  // 白键集合, 白键有 9 组
  private List<PianoKey[]> mWhitePianoKeys = new ArrayList<>(9);
  // 黑键宽度
  private int mBlackKeyWidth;
  // 黑键宽度的一半，用于计算黑键的区域
  private int mHalfBlackKeyWhite;
  // 黑键高度
  private int mBlackKeyHeight;
  // 白键宽度
  private int mWhiteKeyWidth;
  // 白键高度
  private int mWhiteKeyHeight;
  // 钢琴总宽度
  private int mPianoWith = 0;
  // 钢琴图标缩放比例
  private float mScale;
  // 上下文
  @NonNull
  private Context mContext;
  // 钢琴黑键图标
  @DrawableRes
  private int mPianoBlackKeyResourceId;
  // 钢琴白键图标
  @DrawableRes
  private int mPianoWhiteKeyResourceId;
  // 最大的钢琴白键高度
  private int mMaxPianoWhiteKeyHeight;
  //</editor-fold>

  //<editor-fold desc="构造函数">

  public Piano(
      @NonNull Context context,
      @DrawableRes int pianoBlackKeyResourceId,
      @DrawableRes int pianoWhiteKeyResourceId) {
    mContext = context;
    mPianoBlackKeyResourceId = pianoBlackKeyResourceId;
    mPianoWhiteKeyResourceId = pianoWhiteKeyResourceId;
  }
  //</editor-fold>

  //<editor-fold desc="Getter">
  public List<PianoKey[]> getWhitePianoKeys() {
    return mWhitePianoKeys;
  }

  public List<PianoKey[]> getBlackPianoKeys() {
    return mBlackPianoKeys;
  }

  public int getPianoWith() {
    return mPianoWith;
  }
  //</editor-fold>

  //<editor-fold desc="公开方法">

  /**
   * 初始化钢琴实体
   *
   * @param maxPianoWhiteKeyHeight 最大的钢琴白键高度
   */
  public void initPiano(int maxPianoWhiteKeyHeight) {
    if (validateInit(maxPianoWhiteKeyHeight)) {
      return;
    }
    initPiano();
    mHasInit = true;
  }

  /**
   * 是否已经初始化
   */
  public boolean hasInit() {
    return mHasInit;
  }
  //</editor-fold>

  //<editor-fold desc="私有方法">

  /**
   * 检查初始化
   *
   * @return true : 已经初始化
   */
  @SuppressWarnings("ConstantConditions")
  private boolean validateInit(int maxPianoWhiteKeyHeight) {
    if (maxPianoWhiteKeyHeight <= 0) {
      return false;
    }
    mHasInit = mMaxPianoWhiteKeyHeight == maxPianoWhiteKeyHeight;
    if (mHasInit) {
      return true;
    }
    mMaxPianoWhiteKeyHeight = maxPianoWhiteKeyHeight;
    mWhiteKeyHeight = maxPianoWhiteKeyHeight;
    Drawable whiteKey = ContextCompat.getDrawable(mContext, mPianoWhiteKeyResourceId);
    Drawable blackKey = ContextCompat.getDrawable(mContext, mPianoBlackKeyResourceId);
    mScale = (float) mWhiteKeyHeight / whiteKey.getIntrinsicHeight();
    mWhiteKeyWidth = (int) (mScale * whiteKey.getIntrinsicWidth());
    mBlackKeyHeight = (int) (mScale * blackKey.getIntrinsicHeight());
    mBlackKeyWidth = (int) (mScale * blackKey.getIntrinsicWidth());
    mHalfBlackKeyWhite = mBlackKeyWidth >> 1;
    return false;
  }

  /**
   * 初始化钢琴
   */
  @SuppressWarnings("ConstantConditions")
  private void initPiano() {
    // 清空数据
    mBlackPianoKeys.clear();
    mWhitePianoKeys.clear();
    mPianoWith = 0;
    // 初始化黑键
    for (int i = 0; i < 8; i++) {
      PianoKey[] keys = new PianoKey[i == 0 ? 1 : 5];
      for (int j = 0; j < keys.length; j++) {
        keys[j] = new PianoKey();
        Rect[] areaOfKey = new Rect[1];
        keys[j].setIsBlackKey(true);
        keys[j].setGroup(i);
        keys[j].setPositionOfGroup(j);
        keys[j].setVoiceId(getVoiceFromResources("b" + i + j));
        keys[j].setIsPressed(false);
        keys[j].setKeyDrawable(new ScaleDrawable(
            ContextCompat.getDrawable(mContext, mPianoBlackKeyResourceId),
            Gravity.NO_GRAVITY,
            mScale,
            mScale
        ).getDrawable());
        setBlackKeyDrawableBounds(i, j, keys[j].getKeyDrawable());
        areaOfKey[0] = keys[j].getKeyDrawable().getBounds();
        keys[j].setAreaOfKey(areaOfKey);
        if (i == 0) {
          keys[j].setPianoKeyVoice(PianoKeyVoice.LA);
          break;
        }
        switch (j) {
          case 0:
            keys[j].setPianoKeyVoice(PianoKeyVoice.DO);
            break;
          case 1:
            keys[j].setPianoKeyVoice(PianoKeyVoice.RE);
            break;
          case 2:
            keys[j].setPianoKeyVoice(PianoKeyVoice.FA);
            break;
          case 3:
            keys[j].setPianoKeyVoice(PianoKeyVoice.SO);
            break;
          case 4:
            keys[j].setPianoKeyVoice(PianoKeyVoice.LA);
            break;
        }
      }
      mBlackPianoKeys.add(keys);
    }
    // 初始化白键
    for (int i = 0; i < 9; i++) {
      PianoKey[] mKeys = new PianoKey[i == 0 ? 2 : i == 8 ? 1 : 7];
      for (int j = 0; j < mKeys.length; j++) {
        mKeys[j] = new PianoKey();
        mKeys[j].setIsBlackKey(false);
        mKeys[j].setGroup(i);
        mKeys[j].setPositionOfGroup(j);
        mKeys[j].setVoiceId(getVoiceFromResources("w" + i + j));
        mKeys[j].setIsPressed(false);
        mKeys[j].setKeyDrawable(new ScaleDrawable(
            ContextCompat.getDrawable(mContext, mPianoWhiteKeyResourceId),
            Gravity.NO_GRAVITY,
            mScale,
            mScale
        ).getDrawable());
        setWhiteKeyDrawableBounds(i, j, mKeys[j].getKeyDrawable());
        mPianoWith += mWhiteKeyWidth;
        if (i == 0) {
          switch (j) {
            case 0:
              mKeys[j].setAreaOfKey(getWhitePianoKeyArea(i, j, BlackKeyPosition.RIGHT));
              mKeys[j].setPianoKeyVoice(PianoKeyVoice.LA);
              mKeys[j].setLetterName("A0");
              break;
            case 1:
              mKeys[j].setAreaOfKey(getWhitePianoKeyArea(i, j, BlackKeyPosition.LEFT));
              mKeys[j].setPianoKeyVoice(PianoKeyVoice.SI);
              mKeys[j].setLetterName("B0");
              break;
          }
        } else if (i == 8) {
          Rect[] areaOfKey = new Rect[1];
          areaOfKey[0] = mKeys[j].getKeyDrawable().getBounds();
          mKeys[j].setAreaOfKey(areaOfKey);
          mKeys[j].setPianoKeyVoice(PianoKeyVoice.DO);
          mKeys[j].setLetterName("C8");
        } else {
          switch (j) {
            case 0:
              mKeys[j].setAreaOfKey(getWhitePianoKeyArea(i, j, BlackKeyPosition.RIGHT));
              mKeys[j].setPianoKeyVoice(PianoKeyVoice.DO);
              mKeys[j].setLetterName("C" + i);
              break;
            case 1:
              mKeys[j].setAreaOfKey(getWhitePianoKeyArea(i, j, BlackKeyPosition.BOTH));
              mKeys[j].setPianoKeyVoice(PianoKeyVoice.RE);
              mKeys[j].setLetterName("D" + i);
              break;
            case 2:
              mKeys[j].setAreaOfKey(getWhitePianoKeyArea(i, j, BlackKeyPosition.LEFT));
              mKeys[j].setPianoKeyVoice(PianoKeyVoice.MI);
              mKeys[j].setLetterName("E" + i);
              break;
            case 3:
              mKeys[j].setAreaOfKey(getWhitePianoKeyArea(i, j, BlackKeyPosition.RIGHT));
              mKeys[j].setPianoKeyVoice(PianoKeyVoice.FA);
              mKeys[j].setLetterName("F" + i);
              break;
            case 4:
              mKeys[j].setAreaOfKey(getWhitePianoKeyArea(i, j, BlackKeyPosition.BOTH));
              mKeys[j].setPianoKeyVoice(PianoKeyVoice.SO);
              mKeys[j].setLetterName("G" + i);
              break;
            case 5:
              mKeys[j].setAreaOfKey(getWhitePianoKeyArea(i, j, BlackKeyPosition.BOTH));
              mKeys[j].setPianoKeyVoice(PianoKeyVoice.LA);
              mKeys[j].setLetterName("A" + i);
              break;
            case 6:
              mKeys[j].setAreaOfKey(getWhitePianoKeyArea(i, j, BlackKeyPosition.LEFT));
              mKeys[j].setPianoKeyVoice(PianoKeyVoice.SI);
              mKeys[j].setLetterName("B" + i);
              break;
          }
        }
      }
      mWhitePianoKeys.add(mKeys);
    }
  }

  /**
   * 设置黑色键图案的位置
   *
   * @param group 组数, 从 0 开始
   * @param positionOfGroup 组内的位置
   * @param drawable 要设置的 Drawable 对象
   */
  private void setBlackKeyDrawableBounds(
      @IntRange(from = 0) int group,
      @IntRange(from = 0) int positionOfGroup,
      @NonNull Drawable drawable) {
    int whiteOffset = 0;
    if (group == 0) {
      whiteOffset = 5;
    }
    int blackOffset = 0;
    if (positionOfGroup == 2 || positionOfGroup == 3 || positionOfGroup == 4) {
      blackOffset = 1;
    }
    int whiteKeyOffsetCount = 7 * group - 4 + whiteOffset + blackOffset + positionOfGroup;
    int whiteKeyOffsetWidth = whiteKeyOffsetCount * mWhiteKeyWidth;
    drawable.setBounds(
        whiteKeyOffsetWidth - mHalfBlackKeyWhite,
        0,
        whiteKeyOffsetWidth + mHalfBlackKeyWhite,
        mBlackKeyHeight
    );
    Log.w("TAG", "white key bounds : " + drawable.getBounds());
  }

  /**
   * 设置白色键的点击区域
   *
   * @param group 组数，从0开始
   * @param positionOfGroup 本组数内的位置
   * @param blackKeyPosition 黑键占白键的位置
   * @return 矩形数组
   */
  private Rect[] getWhitePianoKeyArea(
      @IntRange(from = 0) int group,
      @IntRange(from = 0) int positionOfGroup,
      @NonNull BlackKeyPosition blackKeyPosition) {
    int offset = 0;
    if (group == 0) {
      offset = 5;
    }
    switch (blackKeyPosition) {
      case LEFT: {
        Rect[] left = new Rect[2];
        int whiteKeyStart = (7 * group - 5 + offset + positionOfGroup) * mWhiteKeyWidth;
        left[0] = new Rect(
            whiteKeyStart,
            mBlackKeyHeight,
            whiteKeyStart += mHalfBlackKeyWhite,
            mWhiteKeyHeight
        );
        left[1] = new Rect(
            whiteKeyStart,
            0,
            (7 * group - 4 + offset + positionOfGroup) * mWhiteKeyWidth,
            mWhiteKeyHeight
        );
        return left;
      }
      case BOTH: {
        Rect[] both = new Rect[3];
        int whiteStart = (7 * group - 5 + offset + positionOfGroup) * mWhiteKeyWidth;
        both[0] = new Rect(
            whiteStart,
            mBlackKeyHeight,
            whiteStart += mHalfBlackKeyWhite,
            mWhiteKeyHeight
        );
        both[1] = new Rect(
            whiteStart,
            0,
            whiteStart =
                (7 * group - 4 + offset + positionOfGroup) * mWhiteKeyWidth - mHalfBlackKeyWhite,
            mWhiteKeyHeight
        );
        both[2] = new Rect(
            whiteStart,
            mBlackKeyHeight,
            whiteStart + mHalfBlackKeyWhite,
            mWhiteKeyHeight
        );
        return both;
      }
      case RIGHT:
      default: {
        Rect[] right = new Rect[2];
        int whiteStart = (7 * group - 5 + offset + positionOfGroup) * mWhiteKeyWidth;
        right[0] = new Rect(
            whiteStart,
            0,
            whiteStart =
                (7 * group - 4 + offset + positionOfGroup) * mWhiteKeyWidth - mHalfBlackKeyWhite,
            mWhiteKeyHeight
        );
        right[1] = new Rect(
            whiteStart,
            mBlackKeyHeight,
            whiteStart + mHalfBlackKeyWhite,
            mWhiteKeyHeight
        );
        return right;
      }
    }
  }

  /**
   * 设置白色键图案的位置
   *
   * @param group 组数，从0开始
   * @param positionOfGroup 在本组中的位置
   * @param drawable 要设置的 Drawable 对象
   */
  private void setWhiteKeyDrawableBounds(int group, int positionOfGroup, Drawable drawable) {
    int offset = 0;
    if (group == 0) {
      offset = 5;
    }
    drawable.setBounds((7 * group - 5 + offset + positionOfGroup) * mWhiteKeyWidth, 0,
        (7 * group - 4 + offset + positionOfGroup) * mWhiteKeyWidth, mWhiteKeyHeight);
  }

  /**
   * 从资源文件中获取声音 id
   *
   * @param voiceName 声音的文件名
   * @return 声音 id
   */
  private int getVoiceFromResources(String voiceName) {
    return mContext.getResources().getIdentifier(voiceName, "raw", mContext.getPackageName());
  }

  //</editor-fold>
}
