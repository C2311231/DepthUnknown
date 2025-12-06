package io.Depth_Unknown.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.CollisionConstants;
import com.badlogic.gdx.physics.bullet.collision.btCapsuleShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import io.Depth_Unknown.engine.input.EngineInputProcessor;
import io.Depth_Unknown.engine.physics.PhysicsEngine;
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
    public Setting<Float> sensitivity;
    public PhysicsEngine physicsEngine;

    private boolean moveForward = false;
    private boolean moveBackward = false;
    private boolean moveLeft = false;
    private boolean moveRight = false;

    public float playerSpeed;
    public float jumpForce = 5;

    public Player(EngineInputProcessor inputProcessor, PhysicsEngine physics, Renderer renderer, SettingsManager settingsManager) {
        this.physicsEngine = physics;

        this.inputProcessor = inputProcessor;
        this.renderer = renderer;
        ModelBuilder modelBuilder = new ModelBuilder();
        // Temp model
        model = modelBuilder.createCapsule(0.2f, 1f, 15, new Material(ColorAttribute.createDiffuse(Color.GREEN)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        instance = new ModelInstance(model);
        btCapsuleShape shape = new btCapsuleShape(0.2f, 1f-2*0.2f + 0.35f);// Apparently for physics height is the distance between the capsule hemispheres, not including the radius. But rendering includes the radius...
        shape.setMargin(0.01f);
        physicsBody = physics.addRigidBody(shape, new Matrix4(), 1);
        physicsBody.setAngularFactor(Vector3.Zero);
        physicsBody.setActivationState(CollisionConstants.DISABLE_DEACTIVATION);
        physicsBody.setFriction(0.0f);
        physicsBody.setRollingFriction(0.0f);
        physicsBody.setSpinningFriction(0.0f);
        physicsBody.setDamping(0.2f, 0.0f);

        this.settingsManager = settingsManager;

        sensitivity = settingsManager.registerSetting("Mouse Sensitivity", 0.0025f * 100);

        playerSpeed = 5f;

        inputProcessor.registerControl("Forward", Input.Keys.W, () -> {
            moveForward = true;
        }, () -> {
            moveForward = false;
        });

        inputProcessor.registerControl("Backward", Input.Keys.S, () -> {
            moveBackward = true;
        }, () -> {
            moveBackward = false;
        });

        inputProcessor.registerControl("Left", Input.Keys.A, () -> {
            moveLeft = true;
        }, () -> {
            moveLeft = false;
        });

        inputProcessor.registerControl("Right", Input.Keys.D, () -> {
            moveRight = true;
        }, () -> {
            moveRight = false;
        });

        inputProcessor.registerControl("Jump", Input.Keys.SPACE, () -> {
            Vector3 from = physicsBody.getWorldTransform().getTranslation(new Vector3());
            Vector3 to = physicsBody.getWorldTransform().getTranslation(new Vector3()).add(0,-0.8f,0);

            // Check if on ground (or close enough)
            if (physics.rayCast(from, to, new ClosestRayResultCallback(from, to))) {
                physicsBody.applyCentralImpulse(new Vector3(0f, jumpForce, 0f));
            }
        }, () -> {
        });

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
        updateMovement(delta);
        super.update(delta);
        renderer.setCamera3dPosition(physicsBody.getWorldTransform().getTranslation(new Vector3()).add(0, 0.5f, 0));


        // Get mouse rotation change (easier than bundling it in input processor)
        float dx = Gdx.input.getDeltaX();
        float dy = Gdx.input.getDeltaY();

        float sensitivity = ((float) this.sensitivity.getValue()) / 100;

        Vector3 dir = renderer.getCamera3d().direction;

        float yaw = (float) Math.atan2(-dir.x, -dir.z);
        float pitch = (float) Math.asin(dir.y);

        yaw += -dx * sensitivity;  // mouse left/right
        pitch += -dy * sensitivity;  // mouse up/down

        float maxPitch = (float) Math.toRadians(89);
        pitch = Math.max(-maxPitch, Math.min(maxPitch, pitch));

        Vector3 position = physicsBody.getWorldTransform().getTranslation(new Vector3());
        Matrix4 mat = new Matrix4();
        mat.setFromEulerAnglesRad(yaw, 0, 0);
        mat.trn(position);
        physicsBody.setWorldTransform(mat);
        physicsBody.getMotionState().setWorldTransform(mat);

        float cosPitch = (float) Math.cos(pitch);

        renderer.getCamera3d().direction.set(-(float) Math.sin(yaw) * cosPitch,
            (float) Math.sin(pitch),
            -(float) Math.cos(yaw) * cosPitch).setLength(1).nor();

        instance.transform.set(physicsBody.getWorldTransform());
    }

    public void setPosition(float x, float y, float z) {
        physicsBody.setWorldTransform(physicsBody.getWorldTransform().setTranslation(x, y, z));
    }

    public void setPosition(Vector3 target) {
        physicsBody.setWorldTransform(physicsBody.getWorldTransform().setTranslation(target));
    }

    public void updateMovement(float delta) {

        float acceleration = 10f; // Tuned by feel

        Vector3 currentVel = physicsBody.getLinearVelocity();

        Matrix4 transform = physicsBody.getWorldTransform();

        Vector3 forward = (new Vector3(0, 0, -1)).rot(transform);
        Vector3 right = (new Vector3(1, 0, 0)).rot(transform);

        forward.y = 0; forward.nor();
        right.y   = 0; right.nor();

        Vector3 desiredVel = new Vector3();

        if (moveForward)  desiredVel.add(forward.cpy().scl(playerSpeed));
        if (moveBackward) desiredVel.add(forward.cpy().scl(-playerSpeed));
        if (moveLeft)     desiredVel.add(right.cpy().scl(-playerSpeed));
        if (moveRight)    desiredVel.add(right.cpy().scl(playerSpeed));

        // Preserve vertical velocity (gravity, jumps)
        desiredVel.y = currentVel.y;

        Vector3 newVel = currentVel.lerp(desiredVel, Math.min(1f, acceleration * delta));

        // Apply
        Vector3 from = physicsBody.getWorldTransform().getTranslation(new Vector3());
        Vector3 to = physicsBody.getWorldTransform().getTranslation(new Vector3()).add(0,-0.8f,0);

        // Check if on ground (or close enough)
        if (physicsEngine.rayCast(from, to, new ClosestRayResultCallback(from, to))) {
            physicsBody.setLinearVelocity(newVel);
        }

    }


}
