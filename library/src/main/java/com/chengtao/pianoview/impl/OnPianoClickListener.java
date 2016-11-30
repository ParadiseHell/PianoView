package com.chengtao.pianoview.impl;

import com.chengtao.pianoview.entity.Piano;

/**
 * Created by ChengTao on 2016-11-25.
 */

/**
 * 钢琴点击接口
 */
public interface OnPianoClickListener {
    /**
     * 点击钢琴键
     * @param type 钢琴键类型（黑、白）
     * @param voice 钢琴音色（DO,RE,MI,FA,SO,LA,SI）
     * @param group 钢琴键所在组（白：9组；黑：7组）
     * @param positionOfGroup 钢琴在组内位置
     */
    void onPianoClick(Piano.PianoKeyType type, Piano.PianoVoice voice,int group,int positionOfGroup);
}
