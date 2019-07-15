package com.chengtao.pianoview.listener;

import com.chengtao.pianoview.entity.PianoKeyVoice;

/*
 * 钢琴绘制完成接口
 *
 * @author ChengTao <a href="mailto:tao@paradisehell.org">Contact me.</a>
 */

public interface OnPianoListener {
  /**
   * 钢琴初始化成功
   */
  void onPianoInitFinish();

  /**
   * 点击钢琴键
   *
   * @param isBlackKey 钢琴键类型（黑、白）
   * @param voice 钢琴音色（DO,RE,MI,FA,SO,LA,SI）
   * @param group 钢琴键所在组（白：9组；黑：7组）
   * @param positionOfGroup 钢琴在组内位置
   */
  void onPianoClick(boolean isBlackKey, PianoKeyVoice voice, int group,
      int positionOfGroup);
}
