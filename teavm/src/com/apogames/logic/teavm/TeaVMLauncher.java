package com.apogames.logic.teavm;

import com.apogames.logic.Constants;
import com.apogames.logic.LogicGame;
import com.github.xpenatan.gdx.teavm.backends.web.WebApplication;
import com.github.xpenatan.gdx.teavm.backends.web.WebApplicationConfiguration;

public class TeaVMLauncher {
    public static void main(String[] args) {
        Constants.IS_HTML = true;
        WebApplicationConfiguration config = new WebApplicationConfiguration();
        config.width = 0;
        config.height = 0;
        config.antialiasing = true;
        config.showDownloadLogs = true;
        config.preloadListener = assetLoader -> {
            assetLoader.loadScript("freetype.js");
        };
        new WebApplication(new LogicGame(), config);
    }
}
