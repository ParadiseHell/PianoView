package com.chengtao.pianoview.listener;

/*
 * 钢琴自动播放接口
 *
 * @author ChengTao <a href="mailto:tao@paradisehell.org">Contact me.</a>
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
