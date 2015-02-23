package com.spbstu.appmathdep;

import android.content.res.Configuration;
import android.graphics.Canvas;
import android.view.View;

// ****************************************************************
// class AppView
//****************************************************************
public class AppView extends View
{
    // CONST
    private static final int UPDATE_TIME_MS = 30;


    // DATA
    AboutActivity m_app;
    RefreshHandler	m_handler;
    long			m_startTime;
    int				m_lineLen;
    boolean			m_active;

    // METHODS
    public AppView(AboutActivity app)
    {
        super(app);
        m_app = app;

        m_handler 	= new RefreshHandler(this);
        m_startTime = 0;
        m_lineLen 	= 0;
        m_active 	= false;
        setOnTouchListener(app);

    }
    public void start()
    {
        m_active 	= true;
        m_handler.sleep(UPDATE_TIME_MS);
    }
    public void stop()
    {
        m_active 	= false;
        //m_handler.sleep(UPDATE_TIME_MS);
    }

    public void update()
    {
        // check switch to video
        //MainActivity app = m_app.getApp();

        // send next update to game
        if (m_active)
            m_handler.sleep(UPDATE_TIME_MS);
    }
    public boolean onTouch(int x, int y, int evtType)
    {
        AppIntro app = m_app.getApp();
        return app.onTouch(x,  y, evtType);
    }
    public void onConfigurationChanged(Configuration confNew)
    {
        AppIntro app = m_app.getApp();
        if (confNew.orientation == Configuration.ORIENTATION_LANDSCAPE)
            app.onOrientation(AppIntro.APP_ORI_LANDSCAPE);
        if (confNew.orientation == Configuration.ORIENTATION_PORTRAIT)
            app.onOrientation(AppIntro.APP_ORI_PORTRAIT);
    }
    public void onDraw(Canvas canvas)
    {
        AppIntro app = m_app.getApp();
        app.onDraw(canvas);
    }
}