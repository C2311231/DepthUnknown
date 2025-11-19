package io.Depth_Unknown.game.ui;

import io.Depth_Unknown.game.GameObject;
import io.Depth_Unknown.game.settings.SettingsManager;
import io.Depth_Unknown.game.ui.hud.HudControler;
import io.Depth_Unknown.game.ui.menu.MenuControler;
import io.Depth_Unknown.game.ui.pause_menu.PauseMenuControler;

public class UiManager implements GameObject {
    int mode=0;
    MenuControler menuControler;
    PauseMenuControler pauseMenuControler;
    HudControler hudControler;
    SettingsManager settingsManager;

    public UiManager(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }

    @Override
    public void reset() {

    }

    @Override
    public void render(float delta) {
        switch (mode) {
            case 0:
                menuControler.render(delta);
                break;
            case 1:
                hudControler.render(delta);
                break;
            case 2:
                pauseMenuControler.render(delta);
                break;
        }

    }

    @Override
    public void update(float delta) {
        switch (mode) {
            case 0:
                menuControler.update(delta);
                break;
            case 1:
                hudControler.update(delta);
                break;
            case 2:
                pauseMenuControler.update(delta);
                break;
        }
    }

    public void resize(int width, int height) {
        switch (mode) {
            case 0:
                menuControler.resize(width, height);
                break;
            case 1:
                hudControler.resize(width, height);
                break;
            case 2:
                pauseMenuControler.resize(width, height);
                break;
        }
    }

    @Override
    public void create() {
        menuControler = new MenuControler(this, settingsManager);
        pauseMenuControler = new PauseMenuControler();
        hudControler = new HudControler();

        menuControler.create();
        pauseMenuControler.create();
        hudControler.create();
    }

    @Override
    public void destroy() {

    }

    public void setMode(int mode) {
        this.mode = mode;
        switch (mode) {
            case 0:
                menuControler.reset();
                break;
            case 1:
                hudControler.reset();
                break;
            case 2:
                pauseMenuControler.reset();
                break;
        }
    }
}
