package com.chengtao.pianoview.listener;

/**
 * Created by ChengTao on 2017-02-20.
 */

/**
 * 钢琴自动播放接口
 */
public interface OnPianoAutoPlayListener {
  /**
   * 自动播放开始
   */
  void onPianoAutoPlayStart();

  /**
   * 自动播放结束
   */
  void onPianoAutoPlayEnd();
}
