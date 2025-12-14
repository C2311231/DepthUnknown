package io.Depth_Unknown.game.world;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.bullet.collision.CollisionConstants;
import io.Depth_Unknown.engine.physics.PhysicsEngine;
import io.Depth_Unknown.engine.physics.PhysicsObject;
import io.Depth_Unknown.engine.rendering.Renderable3d;

public class LevelModel extends PhysicsObject implements Renderable3d {
    private final ModelInstance modelInstance;

    public LevelModel(Model model, PhysicsEngine physicsEngine) {
        this.modelInstance = new ModelInstance(model);
        physicsBody = physicsEngine.addStaticBody(physicsEngine.getMapShape(model), new Matrix4());
        physicsBody.setActivationState(CollisionConstants.DISABLE_DEACTIVATION);
        physicsBody.getCollisionShape().setMargin(0.01f);

    }

    public void update(float delta) {
        modelInstance.transform.set(physicsBody.getWorldTransform());
    }

    @Override
    public void render3d(ModelBatch modelBatch, Environment environment) {
        modelBatch.render(modelInstance, environment);
    }

    public void dispose() {
        physicsBody.dispose();
        modelInstance.model.dispose();
    }
}
