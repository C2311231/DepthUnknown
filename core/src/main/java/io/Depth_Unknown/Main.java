package io.Depth_Unknown;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import io.Depth_Unknown.engine.input.EngineInputProcessor;
import io.Depth_Unknown.engine.physics.Physics;
import io.Depth_Unknown.engine.rendering.Renderer;
import io.Depth_Unknown.game.EntityManager;
import io.Depth_Unknown.game.GameObject;
import io.Depth_Unknown.game.SettingsManager;
import io.Depth_Unknown.game.ui.UiManager;

import java.util.ArrayList;

public class Main implements ApplicationListener {
    public Renderer renderer;
    public Physics physics;
    public EngineInputProcessor input;
    public EntityManager entityManager;
    public UiManager uiManager;
    public SettingsManager settingsManager;
    public Preferences keybinds;
    public Preferences gameSettings;

    ArrayList<GameObject> gameObjects =  new ArrayList<>(20);

    /**
     * Called when the application is Started
     * */
    @Override
    public void create() {
        keybinds = Gdx.app.getPreferences("Depth_Unknown_KeyBinds");
        gameSettings = Gdx.app.getPreferences("Depth_Unknown_Game_Settings");
        physics = new Physics();
        renderer = new Renderer();
        input = new EngineInputProcessor(keybinds);
        entityManager = new EntityManager();
        settingsManager = new SettingsManager(gameSettings);
        uiManager = new UiManager(settingsManager);

        gameObjects.add(entityManager);
        gameObjects.add(settingsManager);
        gameObjects.add(uiManager);

        for (GameObject gameObject : gameObjects) {
            gameObject.create();
        }

        settingsManager.registerSetting("Test String", "Hello World!");
        settingsManager.registerSetting("Test Int", 15);
        settingsManager.registerSetting("Test Float", 15.2f);
        settingsManager.registerSetting("Test Bool", false);
    }

    /**
     * Called when the window is resized
     * */
    @Override
    public void resize(int width, int height) {
        uiManager.resize(width, height);
    }

    /**
     * Called on each frame
     * */
    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        for (GameObject gameObject : gameObjects) {
            gameObject.update(deltaTime);
        }


        for (GameObject gameObject : gameObjects) {
            gameObject.render(deltaTime);
        }
    }

    /**
     * Called when minimized and before closing
     * */
    @Override
    public void pause() {
        keybinds.flush();
        gameSettings.flush();
    }

    /**
     * Called when the window is unminimized
     * */
    @Override
    public void resume() {

    }

    /**
     * Called after pause when the window is closed
     * */
    @Override
    public void dispose() {
        for (GameObject gameObject : gameObjects) {
            gameObject.destroy();
        }
    }
}
