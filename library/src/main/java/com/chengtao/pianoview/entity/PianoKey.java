package com.chengtao.pianoview.entity;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

/*
 * 钢琴键模型
 *
 * @author ChengTao <a href="mailto:tao@paradisehell.org">Contact me.</a>
 */

public final class PianoKey {

  //<editor-fold desc="属性">
  // 是否为黑键
  private boolean mIsBlackKey;
  // 钢琴键的声音 [ DO, RE, MI, FA, SO, LA, SI ]
  private PianoKeyVoice mPianoKeyVoice;
  // 所属组
  @IntRange(from = 0)
  private int mGroup;
  // 所属组下的位置, 从 0 开始
  @IntRange(from = 0)
  private int mPositionOfGroup;
  // 图标
  private Drawable mKeyDrawable;
  // 音乐 id
  private int mVoiceId;
  // 标志，是否被点击，默认未点击
  private boolean mIsPressed;
  // 钢琴键的所占区域
  private Rect[] mAreaOfKey;
  // 音名（针对白键）
  private String mLetterName;
  // 被点击的手指的 id
  private int mFingerId = -1;
  //</editor-fold>

  //<editor-fold desc="Setter">

  public void setIsBlackKey(boolean isBlackKey) {
    mIsBlackKey = isBlackKey;
  }

  public void setPianoKeyVoice(@NonNull PianoKeyVoice pianoKeyVoice) {
    mPianoKeyVoice = pianoKeyVoice;
  }

  public void setGroup(int group) {
    mGroup = group;
  }

  public void setPositionOfGroup(int positionOfGroup) {
    mPositionOfGroup = positionOfGroup;
  }

  public void setKeyDrawable(@NonNull Drawable keyDrawable) {
    mKeyDrawable = keyDrawable;
  }

  public void setVoiceId(int voiceId) {
    mVoiceId = voiceId;
  }

  public void setIsPressed(boolean isPressed) {
    mIsPressed = isPressed;
  }

  public void setAreaOfKey(@NonNull Rect[] areaOfKey) {
    mAreaOfKey = areaOfKey;
  }

  public void setLetterName(@NonNull String letterName) {
    mLetterName = letterName;
  }

  public void setFingerId(int fingerId) {
    mFingerId = fingerId;
  }

  //</editor-fold>

  //<editor-fold desc="Getter">

  public boolean isBlackKey() {
    return mIsBlackKey;
  }

  public PianoKeyVoice getPianoKeyVoice() {
    return mPianoKeyVoice;
  }

  @IntRange(from = 0)
  public int getGroup() {
    return mGroup;
  }

  @IntRange(from = 0)
  public int getPositionOfGroup() {
    return mPositionOfGroup;
  }

  @NonNull
  public Drawable getKeyDrawable() {
    if (mKeyDrawable == null) {
      throw new NullPointerException("please call PianoKey#setKeyDrawable first !!!");
    }
    return mKeyDrawable;
  }

  public int getVoiceId() {
    return mVoiceId;
  }

  public boolean isPressed() {
    return mIsPressed;
  }

  @NonNull
  public Rect[] getAreaOfKey() {
    if (mAreaOfKey == null) {
      throw new NullPointerException("please call PianoKey#setAreaOfKey first !!!");
    }
    return mAreaOfKey;
  }

  @NonNull
  public String getLetterName() {
    if (mLetterName == null) {
      throw new NullPointerException("please call PianoKey#setLetterName first !!!");
    }
    return mLetterName;
  }

  public int getFingerId() {
    return mFingerId;
  }

  //</editor-fold>

  //<editor-fold desc="公开方法">

  /**
   * 重置手指 id
   */
  public void resetFingerId() {
    mFingerId = -1;
  }

  /**
   * 判断 x, y 坐标是否在钢琴键的点击区域内
   *
   * @param x x坐标
   * @param y y坐标
   * @return 是否在点击区域内
   */
  public boolean contain(int x, int y) {
    for (Rect rect : mAreaOfKey) {
      if (rect.contains(x, y)) {
        return true;
      }
    }
    return false;
  }
  //</editor-fold>
}