package io.Depth_Unknown.game.entities;

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import io.Depth_Unknown.game.GameObject;

public abstract class Entity implements GameObject {

    public Vector3 position = new Vector3();
    public Quaternion rotation = new Quaternion();
    public Vector3 scale = new Vector3(1, 1, 1);

    public Vector3 velocity = new Vector3();
    public Vector3 acceleration = new Vector3();

    public Vector3 angularVelocity = new Vector3();
    public Vector3 angularAcceleration = new Vector3();

    public void reset() {

    }

    @Override
    public void render(float delta) {

    }

    public void update(float delta) {
        Vector3 globalVelocity = new Vector3(velocity.x, velocity.y, velocity.z);
        rotation.transform(globalVelocity);
        globalVelocity.nor();

        // Linear
        velocity.mulAdd(acceleration, delta);
        position.mulAdd(globalVelocity, delta);

        // Angular
        angularVelocity.mulAdd(angularAcceleration, delta);
        applyRotation(delta);
    }
    public void create() {

    }
    public void destroy() {

    }

    private void applyRotation(float delta) {
        rotation.mulLeft(new Quaternion(Vector3.X, angularVelocity.x * delta));
        rotation.mulLeft(new Quaternion(Vector3.Y, angularVelocity.y * delta));
        rotation.mulLeft(new Quaternion(Vector3.Z, angularVelocity.z * delta));
    }

}
