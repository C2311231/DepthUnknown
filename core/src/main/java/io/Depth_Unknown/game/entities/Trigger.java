package io.Depth_Unknown.game.entities;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btGhostObject;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import io.Depth_Unknown.engine.physics.PhysicsEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Trigger extends Entity {
    private PhysicsEngine physicsEngine;
    private btGhostObject ghostObject;
    private HashMap<btCollisionObject, ArrayList<Runnable>> collisionCallbacks = new HashMap<>();
    private ArrayList<Runnable> anyCallbacks = new ArrayList<>();
    private btCollisionShape shape;

    public Trigger(PhysicsEngine physicsEngine, float x, float y, float z, float radius) {
        this.physicsEngine = physicsEngine;
        shape = new btSphereShape(radius);
        ghostObject = physicsEngine.addTrigger(shape, new Matrix4().translate(x, y, z));
    }

    public void update(float delta) {
        int count = ghostObject.getNumOverlappingObjects();

        if (count > 0) {
            for (Runnable runnable : anyCallbacks) {
                runnable.run();
            }
        }

        for (int i = 0; i < count; i++) {
            btCollisionObject other = ghostObject.getOverlappingObject(i);
            if (collisionCallbacks.containsKey(other)) {
                ArrayList<Runnable> callbacks = collisionCallbacks.get(other);
                for (Runnable callback : callbacks) {
                    callback.run();
                }
            }

        }

    }

    /**
     * Registers a callback that will be run each update while the collisionObject is colliding with the trigger.
     * */
    public void registerTriggerCallback(Runnable runnable, btCollisionObject collisionObject) {
        if (collisionCallbacks.containsKey(collisionObject)) {
            collisionCallbacks.get(collisionObject).add(runnable);
        } else {
            ArrayList<Runnable> callback = new ArrayList<>();
            callback.add(runnable);
            collisionCallbacks.put(collisionObject, callback);
        }
    }
    /**
     * Registers a callback that will be run each update while any collisionObject is colliding with the trigger.
     * */
    public void registerTriggerCallback(Runnable runnable) {
        anyCallbacks.add(runnable);
    }

    public void destroy() {
        super.destroy();
        physicsEngine.getWorld().removeCollisionObject(ghostObject);
        anyCallbacks.clear();
        collisionCallbacks.clear();
        ghostObject.dispose();
        shape.dispose();
    }
}
