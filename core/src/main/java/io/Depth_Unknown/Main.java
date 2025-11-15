package io.Depth_Unknown;

import com.badlogic.gdx.ApplicationListener;
import io.Depth_Unknown.engine.input.Input;
import io.Depth_Unknown.engine.physics.Physics;
import io.Depth_Unknown.engine.rendering.Renderer;

public class Main implements ApplicationListener {
    public static Renderer renderer;
    public static Physics physics;
    public static Input input;

    @Override
    public void create() {
        physics = new Physics();
        renderer = new Renderer();
        input = new Input();
    }
    @Override
    public void resize(int width, int height) {

    }
    @Override
    public void render() {

    }
    @Override
    public void pause() {

    }
    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
