package io.Depth_Unknown.game.ui;

import io.Depth_Unknown.engine.rendering.Renderer;
import io.Depth_Unknown.game.GameObject;
import io.Depth_Unknown.game.settings.SettingsManager;
import io.Depth_Unknown.game.ui.hud.HudControler;
import io.Depth_Unknown.game.ui.menu.MenuController;
import io.Depth_Unknown.game.ui.pause_menu.PauseMenuControler;
import io.Depth_Unknown.game.world.LevelManager;

public class UiManager implements GameObject {
    int mode=0;
    MenuController menuController;
    PauseMenuControler pauseMenuControler;
    HudControler hudControler;
    SettingsManager settingsManager;
    LevelManager levelManager;
    Renderer renderer;

    public UiManager(SettingsManager settingsManager, LevelManager levelManager, Renderer renderer) {
        this.settingsManager = settingsManager;
        this.levelManager = levelManager;
        this.renderer = renderer;
    }

    @Override
    public void reset() {

    }

    @Override
    public void render(float delta) {
        switch (mode) {
            case 0:
                menuController.render(delta);
                break;
            case 1:
                hudControler.render(delta);
                renderer.render();
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
                menuController.update(delta);
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
                menuController.resize(width, height);
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
        menuController = new MenuController(this, settingsManager, levelManager);
        pauseMenuControler = new PauseMenuControler();
        hudControler = new HudControler();

        menuController.create();
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
                menuController.reset();
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
