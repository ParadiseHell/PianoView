package com.chengtao.pianoview.entity;

/**
 * Created by ChengTao on 2016-11-25.
 */

import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * 钢琴键实体
 */
public class PianoKey {
  //类型[黑键、白键]
  private Piano.PianoKeyType type;
  //音乐类型[DO,RE,MI,FA,SO,LA,SI]
  private Piano.PianoVoice voice;
  //所属组
  private int group;
  //所属组下的位置
  private int positionOfGroup;
  //图案
  private Drawable keyDrawable;
  //音乐ID
  private int voiceId;
  //标志，是否被点击，默认未点击
  private boolean isPressed;
  //钢琴键的所占区域
  private Rect[] areaOfKey;
  //音名（针对白键）
  private String letterName;
  //被点击的手指的下标
  private int fingerID = -1;

  public Piano.PianoKeyType getType() {
    return type;
  }

  public void setType(Piano.PianoKeyType type) {
    this.type = type;
  }

  public Piano.PianoVoice getVoice() {
    return voice;
  }

  public void setVoice(Piano.PianoVoice voice) {
    this.voice = voice;
  }

  public int getGroup() {
    return group;
  }

  public void setGroup(int group) {
    this.group = group;
  }

  public int getPositionOfGroup() {
    return positionOfGroup;
  }

  public void setPositionOfGroup(int positionOfGroup) {
    this.positionOfGroup = positionOfGroup;
  }

  public Drawable getKeyDrawable() {
    return keyDrawable;
  }

  public void setKeyDrawable(Drawable keyDrawable) {
    this.keyDrawable = keyDrawable;
  }

  public int getVoiceId() {
    return voiceId;
  }

  public void setVoiceId(int voiceId) {
    this.voiceId = voiceId;
  }

  public boolean isPressed() {
    return isPressed;
  }

  public void setPressed(boolean pressed) {
    isPressed = pressed;
  }

  public Rect[] getAreaOfKey() {
    return areaOfKey;
  }

  public void setAreaOfKey(Rect[] areaOfKey) {
    this.areaOfKey = areaOfKey;
  }

  public String getLetterName() {
    return letterName;
  }

  public void setLetterName(String letterName) {
    this.letterName = letterName;
  }

  /**
   * 判断x,y坐标是否在钢琴键的点击区域内
   *
   * @param x x坐标
   * @param y y坐标
   * @return 是否在点击区域内
   */
  public boolean contains(int x, int y) {
    boolean isContain = false;
    Rect[] areas = getAreaOfKey();
    int length = getAreaOfKey().length;
    for (int i = 0; i < length; i++) {
      if (areas[i] != null && areas[i].contains(x, y)) {
        isContain = true;
        break;
      }
    }
    return isContain;
  }

  public void resetFingerID() {
    fingerID = -1;
  }

  public void setFingerID(int fingerIndex) {
    this.fingerID = fingerIndex;
  }

  public int getFingerID() {
    return fingerID;
  }
}