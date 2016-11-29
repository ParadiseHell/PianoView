package com.chengtao.pianoview.impl;

/**
 * Created by ChengTao on 2016-11-27.
 */

public interface LoadAduioMessage {
    void sendStartMessage();
    void sendFinishMessage();
    void sendErrorMessage(Exception e);
    void sendProgressMessage(int progress);
}
