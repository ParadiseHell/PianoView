package com.chengtao.pianoview.entity;

/**
 * Created by ChengTao on 2017-02-20.
 */

import com.google.gson.annotations.SerializedName;

/**
 * 自动播放实体
 */
public class AutoPlayEntity {
  /**
   * 按键类型(黑色和白色)
   */
  private Piano.PianoKeyType type;
  /**
   * 组数,从0开始
   */
  private int group;
  /**
   * 位置,从0开始
   */
  private int position;
  /**
   * 当前按键与之后按键的间隔时间
   */
  @SerializedName("break") private long currentBreakTime;

  public AutoPlayEntity() {
  }

  public AutoPlayEntity(Piano.PianoKeyType type, int group, int position, long currentBreakTime) {
    this.type = type;
    this.group = group;
    this.position = position;
    this.currentBreakTime = currentBreakTime;
  }

  public Piano.PianoKeyType getType() {
    return type;
  }

  public void setType(Piano.PianoKeyType type) {
    this.type = type;
  }

  public int getGroup() {
    return group;
  }

  public void setGroup(int group) {
    this.group = group;
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  public long getCurrentBreakTime() {
    return currentBreakTime;
  }

  public void setCurrentBreakTime(long currentBreakTime) {
    this.currentBreakTime = currentBreakTime;
  }

  @Override public String toString() {
    return "AutoPlayEntity{"
        + "type="
        + type
        + ", group="
        + group
        + ", position="
        + position
        + ", currentBreakTime="
        + currentBreakTime
        + '}';
  }
}
