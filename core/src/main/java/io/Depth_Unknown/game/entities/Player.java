package io.Depth_Unknown.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import io.Depth_Unknown.engine.input.EngineInputProcessor;
import io.Depth_Unknown.engine.rendering.Renderable3d;
import io.Depth_Unknown.engine.rendering.Renderer;

public class Player extends Entity implements Renderable3d {
    PerspectiveCamera camera;
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
        renderer.setCamera3dPosition(position.add(new Vector3(0,2.5f,0)));
        renderer.setCamera3dRotation(rotation);
        instance.transform.setTranslation(position);
        instance.transform.rotate(rotation);
    }
}
