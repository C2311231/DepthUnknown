package io.Depth_Unknown.engine.rendering;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import io.Depth_Unknown.game.GameObject;

import java.util.ArrayList;

public class Renderer {
    PerspectiveCamera camera3d;
    OrthographicCamera camera2d;
    Camera currentCamera;
    ModelBatch modelBatch;
    SpriteBatch spriteBatch;
    Environment environment;
    ArrayList<GameObject> gameObjects;

    public Renderer(ArrayList<GameObject> gameObjects) {
        camera3d = new PerspectiveCamera();
        camera2d = new OrthographicCamera();
        currentCamera = camera2d;
        environment = new Environment();
        modelBatch = new ModelBatch();
        spriteBatch = new SpriteBatch();
        this.gameObjects = gameObjects;

        // Configure soft global light
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
    }

    public void setCamera3dPosition(Vector3 position) {
        camera3d.position.set(position);
    }

    public void setCamera3dRotation(Quaternion q) {
        camera3d.rotate(q);
        // ^ Will need to be later checked to see if this adds to camera rotation or simply sets it
    }

    public void setCamera2dPosition(float x, float y) {

    }

    public void render() {
        currentCamera.update();
        modelBatch.begin(currentCamera);
        for (GameObject gameObject : gameObjects) {
            if (gameObject instanceof Renderable3d) {
                ((Renderable3d) gameObject).render3d(modelBatch, environment);
            }
        }
        modelBatch.end();
        spriteBatch.setProjectionMatrix(currentCamera.combined);
        spriteBatch.begin();
        for (GameObject gameObject : gameObjects) {
            if (gameObject instanceof Renderable2d) {
                ((Renderable2d) gameObject).render2d(spriteBatch);
            }
        }
        spriteBatch.end();

        // TODO
        // Work in HUD rendering later.
    }
}
