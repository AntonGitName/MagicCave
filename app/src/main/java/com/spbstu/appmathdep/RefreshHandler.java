package com.spbstu.appmathdep;

import android.os.Handler;
import android.os.Message;

 //****************************************************************
//class RefreshHandler
//****************************************************************
public class RefreshHandler extends Handler
{
    AppView m_gameView;

    public RefreshHandler(AppView v)
    {
        m_gameView = v;
    }

    public void handleMessage(Message msg)
    {
        m_gameView.update();
        m_gameView.invalidate();
    }

    public void sleep(long delayMillis)
    {
        this.removeMessages(0);
        sendMessageDelayed(obtainMessage(0), delayMillis);
    }
}
