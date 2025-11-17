package io.Depth_Unknown;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import io.Depth_Unknown.engine.input.EngineInputProcessor;
import io.Depth_Unknown.engine.physics.Physics;
import io.Depth_Unknown.engine.rendering.Renderer;
import io.Depth_Unknown.game.EntityManager;
import io.Depth_Unknown.game.GameObject;
import io.Depth_Unknown.game.ui.UiManager;

import java.util.ArrayList;

public class Main implements ApplicationListener {
    public Renderer renderer;
    public Physics physics;
    public EngineInputProcessor input;
    public EntityManager entityManager;
    public UiManager uiManager;
    public Preferences keybinds;
    public Preferences gameState;

    ArrayList<GameObject> gameObjects =  new ArrayList<>(20);

    @Override
    public void create() {
        keybinds = Gdx.app.getPreferences("Depth_Unknown_KeyBinds");
        gameState = Gdx.app.getPreferences("Depth_Unknown_Game_State");
        physics = new Physics();
        renderer = new Renderer();
        input = new EngineInputProcessor(keybinds);
        entityManager = new EntityManager();
        uiManager = new UiManager();



        gameObjects.add(entityManager);
        gameObjects.add(uiManager);
    }
    @Override
    public void resize(int width, int height) {

    }
    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();

        for (GameObject gameObject : gameObjects) {
            gameObject.update(deltaTime);
        }


        for (GameObject gameObject : gameObjects) {
            gameObject.render(deltaTime);
        }
    }
    @Override
    public void pause() {
        keybinds.flush();
        gameState.flush();
    }
    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
