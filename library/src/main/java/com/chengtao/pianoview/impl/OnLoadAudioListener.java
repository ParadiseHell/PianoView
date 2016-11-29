package com.chengtao.pianoview.impl;

/**
 * Created by ChengTao on 2016-11-26.
 */

public interface OnLoadAudioListener {
    void loadPianoAudioStart();
    void loadPianoAudioFinish();
    void loadPianoAudioError(Exception e);
    void loadPianoAudioProgress(int progress);
}
