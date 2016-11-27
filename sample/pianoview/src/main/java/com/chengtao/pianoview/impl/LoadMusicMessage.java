package com.chengtao.pianoview.impl;

/**
 * Created by ChengTao on 2016-11-27.
 */

public interface LoadMusicMessage {
    void sendStartMessage();
    void sendFinishMessage();
    void sendErrorMessage(Exception e);
}
