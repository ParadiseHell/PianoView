package com.chengtao.pianoview.impl;

/**
 * Created by ChengTao on 2016-11-26.
 */

public interface OnLoadAudioListener {
    void loadPianoMusicStart();
    void loadPianoMusicFinish();
    void loadPianoMusicError(Exception e);
}
