package io.Depth_Unknown;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import io.Depth_Unknown.engine.input.EngineInputProcessor;
import io.Depth_Unknown.engine.physics.PhysicsEngine;
import io.Depth_Unknown.engine.rendering.Renderer;
import io.Depth_Unknown.game.EntityManager;
import io.Depth_Unknown.game.GameObject;
import io.Depth_Unknown.game.settings.SettingsManager;
import io.Depth_Unknown.game.ui.UiManager;
import io.Depth_Unknown.game.world.LevelManager;

import java.util.ArrayList;

public class Main implements ApplicationListener {
    private Renderer renderer;
    private PhysicsEngine physics;
    private EngineInputProcessor input;
    private EntityManager entityManager;
    private UiManager uiManager;
    private SettingsManager settingsManager;
    private Preferences keybinds;
    private Preferences gameSettings;

    ArrayList<GameObject> gameObjects =  new ArrayList<>(20);

    /**
     * Called when the application is Started
     * */
    @Override
    public void create() {
        keybinds = Gdx.app.getPreferences("Depth_Unknown_KeyBinds");
        gameSettings = Gdx.app.getPreferences("Depth_Unknown_Game_Settings");
        physics = new PhysicsEngine();
        renderer = new Renderer(gameObjects, physics);
        input = new EngineInputProcessor(keybinds);
        entityManager = new EntityManager();
        settingsManager = new SettingsManager(gameSettings);
        LevelManager levelManager = new LevelManager(renderer, settingsManager, input, entityManager, physics);
        uiManager = new UiManager(settingsManager, levelManager, renderer);
        gameObjects.add(levelManager);
        gameObjects.add(physics);
        gameObjects.add(entityManager);
        gameObjects.add(settingsManager);
        gameObjects.add(uiManager); //Must be created after level manager


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
        renderer.resize(width, height);
    }

    /**
     * Called on each frame
     * */
    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

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
