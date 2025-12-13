package io.Depth_Unknown.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.CollisionConstants;
import com.badlogic.gdx.physics.bullet.collision.btCapsuleShape;
import io.Depth_Unknown.engine.input.EngineInputProcessor;
import io.Depth_Unknown.engine.physics.PhysicsEngine;
import io.Depth_Unknown.engine.rendering.Renderable3d;
import io.Depth_Unknown.engine.rendering.Renderer;
import io.Depth_Unknown.game.settings.Setting;
import io.Depth_Unknown.game.settings.SettingsManager;

public class Player extends Entity implements Renderable3d {
    private EngineInputProcessor inputProcessor;
    private final Model model;
    private final ModelInstance instance;
    private final Renderer renderer;
    private final SettingsManager settingsManager;
    private final Setting<Float> sensitivity;
    private final PhysicsEngine physicsEngine;

    private boolean moveForward = false;
    private boolean moveBackward = false;
    private boolean moveLeft = false;
    private boolean moveRight = false;

    // Variables for used for smooth camera switching
    private final float transitionTime = 0.5f;
    private boolean lockCamtoPlayer = true;
    private boolean cameraSwitching = false;
    private Vector3 cameraDestination;
    private Vector3 cameraStartPosition;
    private double cameraTimeStep;

    private boolean currentCamera2D;

    private final float ZOOMSTART = 0.005f;
    private final float ZOOMEND = 0.025f;


    private float playerSpeed;
    private float jumpForce = 5;
    private float movementAcceleration = 10f;

    private float yaw = 0f;
    private float pitch = 0f;

    // 2D camera following variables
    Vector3 camVel = new Vector3();
    float followStrength = 12f;       // higher = snappier


    public float getJumpForce() {
        return jumpForce;
    }

    public void setJumpForce(float jumpForce) {
        this.jumpForce = jumpForce;
    }

    public float getPlayerSpeed() {
        return playerSpeed;
    }

    public void setPlayerSpeed(float playerSpeed) {
        this.playerSpeed = playerSpeed;
    }

    public void raycastCameraPositionSet() {
        Vector3 from = renderer.getCamera3d().position.cpy();
        Vector3 direction = renderer.getCamera3d().direction.cpy().nor();
        Vector3 to = from.cpy().mulAdd(direction, 50);
        ClosestRayResultCallback cb = new ClosestRayResultCallback(from, to);
        physicsEngine.rayCast(from, to, cb);

        if (cb.hasHit()) {
            Vector3 hitPoint = new Vector3();
            cb.getHitPointWorld(hitPoint);
            Vector3 hitNormal = new Vector3();
            cb.getHitNormalWorld(hitNormal);
            hitNormal.nor();

            Vector3 camOffset = hitNormal.cpy().scl(0.05f); // small offset
            switchCamera2D(hitPoint.cpy().add(camOffset), hitNormal);


        }
    }

    public Player(EngineInputProcessor inputProcessor, PhysicsEngine physics, Renderer renderer, SettingsManager settingsManager) {
        this.physicsEngine = physics;

        this.inputProcessor = inputProcessor;
        this.renderer = renderer;
        ModelBuilder modelBuilder = new ModelBuilder();
        // Temp model
        model = modelBuilder.createCapsule(0.2f, 1f, 15, new Material(ColorAttribute.createDiffuse(Color.GREEN)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        instance = new ModelInstance(model);
        btCapsuleShape shape = new btCapsuleShape(0.2f, 1f-2*0.2f); // + 0.35f);// Apparently for physics height is the distance between the capsule hemispheres, not including the radius. But rendering includes the radius... Extra margin was added to prevent clipping...
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

        inputProcessor.registerControl("TestKey", Input.Keys.N, () -> {
        }, () -> {
            if (currentCamera2D) {
                switchCamera3D();
            }
            else {
                raycastCameraPositionSet();
            }
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

    // TODO Separate this out into more submethods
    @Override
    public void update(float delta) {
        System.out.println(renderer.getCurrentCamera().position);
        System.out.println(renderer.getCurrentCamera().direction);
        super.update(delta);

        if (!currentCamera2D)
            updateMovement3d(delta);
        else
            updateMovement2d(delta);

        // Get mouse rotation change (easier than bundling it in input processor)
        float dx = Gdx.input.getDeltaX();
        float dy = Gdx.input.getDeltaY();

        float sensitivity = ((float) this.sensitivity.getValue()) / 100;

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

        if (lockCamtoPlayer) {
            renderer.setCamera3dPosition(physicsBody.getWorldTransform().getTranslation(new Vector3()).add(0, 0.5f, 0));
            renderer.getCamera3d().direction.set(-(float) Math.sin(yaw) * cosPitch,
                (float) Math.sin(pitch),
                -(float) Math.cos(yaw) * cosPitch).setLength(1).nor();
        }

        // TODO Come up with a new way to move the 2d camera smoothly along the same plane to follow the player

        instance.transform.set(physicsBody.getWorldTransform());

        if (cameraSwitching){
            if (currentCamera2D) {
                handleCameraSwitch3D(delta);
            }
            else {
                handleCameraSwitch2D(delta);
            }
        }
    }

    public void setPosition(float x, float y, float z) {
        physicsBody.setWorldTransform(physicsBody.getWorldTransform().setTranslation(x, y, z));
    }

    public void setPosition(Vector3 target) {
        physicsBody.setWorldTransform(physicsBody.getWorldTransform().setTranslation(target));
    }

    private void updateMovement3d(float delta) {
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

        // Preserve vertical velocity
        desiredVel.y = currentVel.y;

        Vector3 newVel = currentVel.lerp(desiredVel, Math.min(1f, movementAcceleration * delta));

        // Apply
        Vector3 from = physicsBody.getWorldTransform().getTranslation(new Vector3());
        Vector3 to = physicsBody.getWorldTransform().getTranslation(new Vector3()).add(0,-0.8f,0);

        // Check if on ground (or close enough)
        if (physicsEngine.rayCast(from, to, new ClosestRayResultCallback(from, to))) {
            physicsBody.setLinearVelocity(newVel);
        }

    }

    //TODO Fix this function it does not accurately work based upon camera rotation
    private void updateMovement2d(float delta) {
        float acceleration = 10f; // Tuned by feel

        Vector3 currentVel = physicsBody.getLinearVelocity();

        Vector3 direction = renderer.getCamera2d().direction;

        Vector3 right = new Vector3(direction).crs(Vector3.Y).nor();

        right.y   = 0;
        right.nor();

        Vector3 desiredVel = new Vector3();
        if (moveLeft)     desiredVel.add(right.cpy().scl(-playerSpeed));
        if (moveRight)    desiredVel.add(right.cpy().scl(playerSpeed));

        // Preserve vertical velocity
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

    public void switchCamera2D(Vector3 position, Vector3 direction) {
        lockCamtoPlayer = false;
        cameraSwitching = true;
        cameraDestination = position;
        cameraTimeStep = 0.0;
        cameraStartPosition = renderer.getCamera3d().position;
        renderer.getCamera2d().position.set(position);
        renderer.getCamera2d().update();
        renderer.getCamera2d().direction.set(direction);
        renderer.getCamera2d().up.set(Vector3.Y);  // world up (0, 1, 0)
        renderer.getCamera2d().update();
    }

    // Created with AI assistance
    // TODO Fix this so the transition is smoother
    private void handleCameraSwitch2D(float delta) {
        cameraTimeStep += delta;
        float totalTime = transitionTime; // total duration of the transition

        Camera camera3d = renderer.getCamera3d();
        OrthographicCamera camera2d = renderer.getCamera2d();

        // Clamp t to [0,1]
        float t = Math.min((float) cameraTimeStep / totalTime, 1f);
        float tInterp = Interpolation.exp5Out.apply(t);

        // Fly 3D camera toward surface (linear/ eased)
        Vector3 flyPos = new Vector3(
            MathUtils.lerp(cameraStartPosition.x, cameraDestination.x, tInterp),
            MathUtils.lerp(cameraStartPosition.y, cameraDestination.y, tInterp),
            MathUtils.lerp(cameraStartPosition.z, cameraDestination.z, tInterp)
        );
        camera3d.position.set(flyPos);
        camera3d.update();

        // Position 2D camera at surface (already in place)
        camera2d.position.set(cameraDestination);

        // Zoom 2D camera from zoomStart → ZOOMEND
        camera2d.zoom = MathUtils.lerp(ZOOMSTART, ZOOMEND, tInterp);
        camera2d.update();

        // Decide which camera to render with
        if (t < 0.5f) {
            // first half: show 3D fly
            renderer.setCurrentCamera(camera3d);
        } else {
            // second half: switch to 2D smoothly
            renderer.setCurrentCamera(camera2d);
        }

        // End of transition
        if (t >= 1f) {
            currentCamera2D = true;
            cameraSwitching = false;
            lockCamtoPlayer = true;
            camera2d.position.set(cameraDestination);
            camera2d.zoom = ZOOMEND;
            camera2d.update();
            renderer.setCurrentCamera(camera2d);
        }
    }

    public void switchCamera3D() {
        lockCamtoPlayer = false;
        cameraSwitching = true;
        cameraTimeStep = 0.0;
        cameraDestination = renderer.getCamera3d().position;
        cameraStartPosition = renderer.getCamera2d().position;
        renderer.getCamera3d().position.set(renderer.getCamera3d().position);
        renderer.getCamera3d().update();
        renderer.getCamera3d().rotate(renderer.getCamera2d().view);
        renderer.getCamera3d().update();
        renderer.setCurrentCamera(renderer.getCamera3d());
    }
    // TODO Fix this so the transition is smoother
    private void handleCameraSwitch3D(float delta) {
        cameraTimeStep += delta;
        float totalTime = transitionTime; // total duration of the transition

        Camera camera3d = renderer.getCamera3d();

        // Clamp t to [0,1]
        float t = Math.min((float) cameraTimeStep / totalTime, 1f);
        float tInterp = Interpolation.exp5Out.apply(t);

        // Fly 3D camera toward surface (linear/ eased)
        Vector3 flyPos = new Vector3(
            MathUtils.lerp(cameraStartPosition.x, cameraDestination.x, tInterp),
            MathUtils.lerp(cameraStartPosition.y, cameraDestination.y, tInterp),
            MathUtils.lerp(cameraStartPosition.z, cameraDestination.z, tInterp)
        );
        camera3d.position.set(flyPos);
        camera3d.update();

        // End of transition
        if (t >= 1f) {
            currentCamera2D = false;
            cameraSwitching = false;
            lockCamtoPlayer = true;
            renderer.setCurrentCamera(camera3d);
            camera3d.up.set(Vector3.Y);  // world up (0, 1, 0)
            camera3d.update();
            Vector3 dir = camera3d.direction.cpy().nor();
            yaw = MathUtils.atan2(-dir.x, -dir.z);
            pitch = MathUtils.asin(dir.y);
        }
    }

}
