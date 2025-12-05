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
import io.Depth_Unknown.game.settings.Setting;
import io.Depth_Unknown.game.settings.SettingsManager;

public class Player extends Entity implements Renderable3d {
    public EngineInputProcessor inputProcessor;
    public Model model;
    public ModelInstance instance;
    public Renderer renderer;
    public SettingsManager settingsManager;
    public Setting sensitivity;

    public float playerSpeed;

    public Player(EngineInputProcessor inputProcessor, Renderer renderer, SettingsManager settingsManager) {
        this.inputProcessor = inputProcessor;
        this.renderer = renderer;
        ModelBuilder modelBuilder = new ModelBuilder();
        // Temp model
        model = modelBuilder.createCapsule(0.2f, 1f, 15,
            new Material(ColorAttribute.createDiffuse(Color.GREEN)),
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        instance = new ModelInstance(model);

        this.settingsManager = settingsManager;

        sensitivity = settingsManager.registerSetting("Mouse Sensitivity", 0.0025f * 100);

        playerSpeed = 3f;

        inputProcessor.registerControl("Forward", Input.Keys.W, () -> velocity.add(0,0,-1*playerSpeed), () -> velocity.add(0,0,1*playerSpeed));
        inputProcessor.registerControl("Backward", Input.Keys.S, () -> velocity.add(0,0,1*playerSpeed), () -> velocity.add(0,0,-1*playerSpeed));
        inputProcessor.registerControl("Left", Input.Keys.A, () -> velocity.add(-1*playerSpeed,0,0), () -> velocity.add(1*playerSpeed,0,0));
        inputProcessor.registerControl("Right", Input.Keys.D, () -> velocity.add(1*playerSpeed,0,0), () -> velocity.add(-1*playerSpeed,0,0));
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
            new Vector3(position).add(0, 0.5f, 0)
        );

        // Get mouse rotation change (easier than bundling it in input processor)
        float dx = Gdx.input.getDeltaX();
        float dy = Gdx.input.getDeltaY();

        float sensitivity = ((float) this.sensitivity.getValue()) / 100;

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
