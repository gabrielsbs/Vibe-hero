package com.vha.game;

import android.app.Application;


import com.myscript.iink.Engine;
import com.vha.game.certificate.MyCertificate;

public class VibeHeroApplication extends Application
{
    private static Engine engine;

    public static synchronized Engine getEngine()
    {
        if (engine == null)
        {
            engine = Engine.create(MyCertificate.getBytes());
        }

        return engine;
    }

}