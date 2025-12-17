package io.Depth_Unknown.engine.physics;

import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

public abstract class PhysicsObject {
    public btRigidBody getPhysicsBody() {
        return physicsBody;
    }

    protected btRigidBody physicsBody;

}
