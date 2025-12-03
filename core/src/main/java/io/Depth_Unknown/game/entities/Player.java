package io.Depth_Unknown.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import io.Depth_Unknown.engine.input.EngineInputProcessor;
import io.Depth_Unknown.engine.rendering.Renderable3d;
import io.Depth_Unknown.engine.rendering.Renderer;

public class Player extends Entity implements Renderable3d {
    EngineInputProcessor inputProcessor;
    Model model;
    ModelInstance instance;
    Renderer renderer;

    public Player(EngineInputProcessor inputProcessor, Renderer renderer) {
        this.inputProcessor = inputProcessor;
        this.renderer = renderer;
        ModelBuilder modelBuilder = new ModelBuilder();
        // Temp model
        model = modelBuilder.createCapsule(2f, 5f, 15,
            new Material(ColorAttribute.createDiffuse(Color.GREEN)),
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        instance = new ModelInstance(model);

        inputProcessor.registerControl("Forward", Input.Keys.W, () -> velocity.add(1,0,0), () -> velocity.add(-1,0,0));
        inputProcessor.registerControl("Backward", Input.Keys.S, () -> velocity.add(-1,0,0), () -> velocity.add(1,0,0));
        inputProcessor.registerControl("Left", Input.Keys.A, () -> velocity.add(0,1,0), () -> velocity.add(0,-1,0));
        inputProcessor.registerControl("Forward", Input.Keys.D, () -> velocity.add(0,-1,0), () -> velocity.add(0,1,0));
    }

    /**
     * @param modelBatch
     * @param environment
     */
    @Override
    public void render3d(ModelBatch modelBatch, Environment environment) {
        modelBatch.render(instance, environment);
    }

    @Override
    public void destroy() {
        super.destroy();
        model.dispose();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        renderer.setCamera3dPosition(
            new Vector3(position).add(0, 8f, 0)
        );

        // Get mouse rotation change (easier than bundling it in input processor)
        float dx = Gdx.input.getDeltaX();
        float dy = Gdx.input.getDeltaY();
        float sensitivity = 0.0025f; // TODO add this to settings

        Vector3 dir = renderer.getCamera3d().direction;


        float yaw   = (float)Math.atan2(-dir.x, -dir.z);
        float pitch = (float)Math.asin(dir.y);

        yaw += -dx * sensitivity;  // mouse left/right
        pitch += -dy * sensitivity;  // mouse up/down

        float maxPitch = (float)Math.toRadians(89);

        pitch = Math.max(-maxPitch, Math.min(maxPitch, pitch));
        rotation = rotation.setEulerAnglesRad(yaw, 0, 0);
        float cosPitch = (float)Math.cos(pitch);

        renderer.getCamera3d().direction.set(-(float)Math.sin(yaw) * cosPitch,
            (float)Math.sin(pitch),
            -(float)Math.cos(yaw) * cosPitch).setLength(1).nor();

        instance.transform.set(position, rotation);
    }
}
