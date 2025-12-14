package io.Depth_Unknown.game.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import io.Depth_Unknown.game.GameObject;

import java.util.ArrayList;

public class HudControler implements GameObject {
    private ArrayList<Actor> actors = new ArrayList<>();

    public HudControler() {
        registerActors();
    }

    private void registerActors() {

    }

    @Override
    public void destroy() {

    }

    /**
     * @param deltaTime
     */
    @Override
    public void render(float deltaTime) {

    }

    @Override
    public void update(float delta) {


    }

    public void resize(int width, int height) {
    }
}
