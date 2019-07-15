package com.chengtao.pianoview.entity;

import android.support.annotation.IntRange;
import com.google.gson.annotations.SerializedName;

/*
 * 自动播放模型
 *
 * @author ChengTao <a href="mailto:tao@paradisehell.org">Contact me.</a>
 */

public final class AutoPlayEntity {

  //<editor-fold desc="属性">

  // 是否为黑键
  private boolean mIsBlackKey;
  // 组数, 从0开始
  private int mGroup;
  // 位置, 从0开始
  private int mPosition;
  // 当前按键与之后按键的间隔时间
  @SerializedName("break")
  private long mCurrentBreakTime;
  //</editor-fold>

  //<editor-fold desc="构造函数">

  public AutoPlayEntity() {
  }

  public AutoPlayEntity(
      boolean isBlackKey,
      @IntRange(from = 0) int group,
      @IntRange(from = 0) int position,
      long currentBreakTime) {
    mIsBlackKey = isBlackKey;
    mGroup = group;
    mPosition = position;
    mCurrentBreakTime = currentBreakTime;
  }
  //</editor-fold>

  //<editor-fold desc="Setter">

  public void setIsBlackKey(boolean isBlackKey) {
    mIsBlackKey = isBlackKey;
  }

  public void setGroup(int group) {
    mGroup = group;
  }

  public void setPosition(int position) {
    mPosition = position;
  }

  public void setCurrentBreakTime(long currentBreakTime) {
    mCurrentBreakTime = currentBreakTime;
  }
  //</editor-fold>

  //<editor-fold desc="Getter">

  public boolean isBlackKey() {
    return mIsBlackKey;
  }

  public int getGroup() {
    return mGroup;
  }

  public int getPosition() {
    return mPosition;
  }

  public long getCurrentBreakTime() {
    return mCurrentBreakTime;
  }
  //</editor-fold>
}
