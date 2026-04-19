package com.apogames.logic.teavm;

import com.apogames.logic.Constants;
import com.apogames.logic.LogicGame;
import com.github.xpenatan.gdx.teavm.backends.web.WebApplication;
import com.github.xpenatan.gdx.teavm.backends.web.WebApplicationConfiguration;
import org.teavm.jso.JSBody;

import java.util.Locale;

public class TeaVMLauncher {

    @JSBody(script = "return navigator.language || navigator.userLanguage || '';")
    private static native String getBrowserLanguage();

    public static void main(String[] args) {
        Constants.IS_HTML = true;
        Constants.setLanguage(detectBrowserLocale());

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

    private static Locale detectBrowserLocale() {
        String lang = getBrowserLanguage();
        if (lang != null && lang.toLowerCase().startsWith("de")) {
            return Locale.GERMAN;
        }
        return Locale.ENGLISH;
    }
}
