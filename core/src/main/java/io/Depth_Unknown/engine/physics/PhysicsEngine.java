package io.Depth_Unknown.engine.physics;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.graphics.g3d.model.data.ModelNode;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;
import io.Depth_Unknown.game.GameObject;

import java.util.HashMap;

public class PhysicsEngine implements GameObject {
    private final btBroadphaseInterface broadphase;
    private final btDiscreteDynamicsWorld world;
    private final btDefaultCollisionConfiguration collisionConfig;
    private final btCollisionDispatcher dispatcher;
    private final HashMap<Integer, PhysicsObject> physicsObjects;
    private final btSequentialImpulseConstraintSolver solver;

    public btDiscreteDynamicsWorld getWorld() {
        return world;
    }

    public PhysicsEngine() {
        Bullet.init();
        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        broadphase = new btDbvtBroadphase();
        solver = new btSequentialImpulseConstraintSolver();
        world = new btDiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfig);

        physicsObjects = new HashMap<>();
        //world.addCollisionObject(physicsObjects.body);
        world.setGravity(new Vector3(0,-9.8f,0));
        //world.setGravity(new Vector3(0,0,0));

    }

    public btCollisionShape getMapShape(Model model) {
        btConvexHullShape hull = new btConvexHullShape();
        Matrix4 zToY = new Matrix4().setToRotation(new Vector3(1, 0, 0), -90f); // Z-up → Y-up
        Vector3 temp = new Vector3();
        for (Mesh mesh : model.meshes) {
            VertexAttribute posAttr = mesh.getVertexAttribute(VertexAttributes.Usage.Position);
            int vertexSizeFloats = mesh.getVertexSize() / 4; // floats per vertex
            float[] vertices = new float[mesh.getNumVertices() * vertexSizeFloats];
            mesh.getVertices(vertices);

            for (int i = 0; i < mesh.getNumVertices(); i++) {
                int offset = i * vertexSizeFloats + posAttr.offset / 4;
                float x = vertices[offset];
                float y = vertices[offset + 1];
                float z = vertices[offset + 2];

                temp.set(x, y, z);
                temp.mul(zToY);

                hull.addPoint(temp, false);
            }
        }
        hull.recalcLocalAabb();
        return hull;
    }

    /**
     * @param delta
     */
    @Override
    public void update(float delta) {
        world.stepSimulation(delta, 5, 1f / 60f);
    }

    /**
     *
     */
    @Override
    public void destroy() {

    }

    /**
     * @param deltaTime
     */
    @Override
    public void render(float deltaTime) {

    }

    public btRigidBody addRigidBody(btCollisionShape shape, Matrix4 transform, float mass) {
        Vector3 inertia = new Vector3();
        if (mass > 0f) {
            shape.calculateLocalInertia(mass, inertia);
        }
        btDefaultMotionState motionState = new btDefaultMotionState(transform);
        btRigidBody.btRigidBodyConstructionInfo info = new btRigidBody.btRigidBodyConstructionInfo(mass, motionState, shape, inertia);
        btRigidBody body = new btRigidBody(info);
        world.addRigidBody(body);

        // Dispose of Construction Info after use
        info.dispose();
        return body;
    }

    public btRigidBody addRigidBody(btRigidBody rigidBody) {
        world.addRigidBody(rigidBody);
        return rigidBody;
    }

    public btRigidBody addKinematicBody(btCollisionShape shape, Matrix4 transform) {
        btRigidBody body = addStaticBody(shape, transform);
        int flags = body.getCollisionFlags();
        body.setCollisionFlags(flags | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
        body.setActivationState(Collision.DISABLE_DEACTIVATION);

        return body;
    }

    public btGhostObject addTrigger(btCollisionShape shape, Matrix4 transform) {
        btGhostObject ghost = new btGhostObject();
        ghost.setCollisionShape(shape);
        ghost.setWorldTransform(transform);
        world.addCollisionObject(ghost, btBroadphaseProxy.CollisionFilterGroups.SensorTrigger, btBroadphaseProxy.CollisionFilterGroups.AllFilter);
        return ghost;
    }

    public btRigidBody addStaticBody(btCollisionShape shape, Matrix4 transform) {
        return this.addRigidBody(shape, transform, 0f);
    }

    public void removeRigidBody(btRigidBody body) {
        world.removeRigidBody(body);
    }

    public boolean rayCast(Vector3 from, Vector3 to, ClosestRayResultCallback callback) {
        world.rayTest(from, to, callback);
        return callback.hasHit();
    }

}

