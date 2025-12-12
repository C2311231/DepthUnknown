package io.Depth_Unknown.engine.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import io.Depth_Unknown.engine.physics.PhysicsEngine;
import io.Depth_Unknown.game.GameObject;

import java.util.ArrayList;

public class Renderer {
    private final PerspectiveCamera camera3d;
    private final OrthographicCamera camera2d;
    private Camera currentCamera;
    private final ModelBatch modelBatch;
    private final SpriteBatch spriteBatch;
    private final Environment environment;
    private final ArrayList<GameObject> gameObjects;
    private final ModelInstance boxInstance;
    private final DebugDrawer debugDrawer;
    private final PhysicsEngine physicsEngine;

    public Renderer(ArrayList<GameObject> gameObjects, PhysicsEngine physicsEngine) {
        camera3d = new PerspectiveCamera(67f,
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight());
        camera2d = new OrthographicCamera(
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight());
        currentCamera = camera3d;
        environment = new Environment();
        modelBatch = new ModelBatch();
        spriteBatch = new SpriteBatch();
        this.gameObjects = gameObjects;
        this.physicsEngine = physicsEngine;

        // Configure soft global light
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.7f, 0.7f, 0.7f, 1f));
        environment.add(new DirectionalLight().set(0.5f, 0.5f, 0.5f, -1f, -0.8f, -0.2f));
        ModelBuilder mb = new ModelBuilder();
        Model box = mb.createBox(2, 2, 2, new Material(ColorAttribute.createDiffuse(Color.RED)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        boxInstance = new ModelInstance(box); boxInstance.transform.setToTranslation(0, 0, -5); // IN FRONT OF CAMERA

        camera2d.zoom = 0.025f;
        camera2d.near = 0.01f;
        debugDrawer = new DebugDrawer();
        physicsEngine.getWorld().setDebugDrawer(debugDrawer);
        debugDrawer.setDebugMode(
            DebugDrawer.DebugDrawModes.DBG_DrawWireframe |
                DebugDrawer.DebugDrawModes.DBG_DrawConstraints);
    }

    public void setCamera3dPosition(Vector3 position) {
        camera3d.position.set(position);
    }

    public void setCurrentCamera(Camera camera) {
        currentCamera = camera;
    }

    public Camera getCurrentCamera() {
        return currentCamera;
    }

    public Camera getCamera3d() {
        return camera3d;
    }

    public OrthographicCamera getCamera2d() {
        return camera2d;
    }

    public void setCamera2dPosition(float x, float y,  float z, boolean direction_x, boolean negative) {
        camera2d.position.set(x, y, z);
        if  (direction_x) {
            camera2d.lookAt(x+ ((negative)? -1 : 1), y, z);
        }
        else  {
            camera2d.lookAt(x, y+ ((negative)? -1 : 1), z);
        }
    }

    public void render() {
        /*
        // Used for debugging
        System.out.println(camera3d.position.x + " " + camera3d.position.y + " " + camera3d.position.z);
        System.out.println(camera3d.direction.x + " " + camera3d.direction.y + " " + camera3d.direction.z);

        camera3d.near = 0.1f;
        camera3d.far = 1000f;
        currentCamera.lookAt(0, 0, 0);
        */
        currentCamera.update();
        modelBatch.begin(currentCamera);
        for (GameObject gameObject : gameObjects) {
            if (gameObject instanceof Renderable3d) {
                ((Renderable3d) gameObject).render3d(modelBatch, environment);
            }
        }
        modelBatch.render(boxInstance, environment); // Used for debugging
        modelBatch.end();
        spriteBatch.setProjectionMatrix(currentCamera.combined);
        spriteBatch.begin();
        for (GameObject gameObject : gameObjects) {
            if (gameObject instanceof Renderable2d) {
                ((Renderable2d) gameObject).render2d(spriteBatch);
            }
        }
        spriteBatch.end();

        // TODO: Work in HUD rendering later.

        boolean debug = false;
        if (debug) {
            debugDrawer.begin(camera3d);
            physicsEngine.getWorld().debugDrawWorld();
            debugDrawer.end();
        }
    }

    public void resize(int width, int height) {
        camera2d.viewportWidth = width;
        camera2d.viewportHeight = height;
        camera3d.viewportWidth = width;
        camera3d.viewportHeight = height;
    }
}
