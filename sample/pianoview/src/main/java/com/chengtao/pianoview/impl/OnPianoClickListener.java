package com.chengtao.pianoview.impl;

import com.chengtao.pianoview.entity.Piano;

/**
 * Created by ChengTao on 2016-11-25.
 */

public interface OnPianoClickListener {
    void onPianoClick(Piano.PianoKeyType type, Piano.PianoVoice voice,int group,int positionOfGroup);
}
