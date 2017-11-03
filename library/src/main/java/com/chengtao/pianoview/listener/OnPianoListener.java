package com.chengtao.pianoview.listener;

/**
 * Created by ChengTao on 2017-02-20.
 */

import com.chengtao.pianoview.entity.Piano;

/**
 * 钢琴绘制完成接口
 */
public interface OnPianoListener {
  /**
   * 钢琴初始化成功
   */
  void onPianoInitFinish();

  /**
   * 点击钢琴键
   *
   * @param type 钢琴键类型（黑、白）
   * @param voice 钢琴音色（DO,RE,MI,FA,SO,LA,SI）
   * @param group 钢琴键所在组（白：9组；黑：7组）
   * @param positionOfGroup 钢琴在组内位置
   */
  void onPianoClick(Piano.PianoKeyType type, Piano.PianoVoice voice, int group,
      int positionOfGroup);
}
