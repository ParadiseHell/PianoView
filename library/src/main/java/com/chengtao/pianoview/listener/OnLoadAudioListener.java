package com.chengtao.pianoview.listener;


/*
 * 加载音频接口
 *
 * @author ChengTao <a href="mailto:tao@paradisehell.org">Contact me.</a>
 */

public interface OnLoadAudioListener {
  /**
   * 开始
   */
  void loadPianoAudioStart();

  /**
   * 完成
   */
  void loadPianoAudioFinish();

  /**
   * 错误
   *
   * @param e 异常
   */
  void loadPianoAudioError(Exception e);

  /**
   * 进度
   *
   * @param progress 进度值
   */
  void loadPianoAudioProgress(int progress);
}
