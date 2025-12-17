package io.Depth_Unknown.game.entities;
import com.badlogic.gdx.math.Vector3;
import io.Depth_Unknown.engine.physics.PhysicsObject;
import io.Depth_Unknown.game.GameObject;

public abstract class Entity extends PhysicsObject implements GameObject {
    @Override
    public void render(float delta) {

    }

    public void update(float delta) {
    }

    public void destroy() {

    }

    public void setPosition(float x, float y, float z) {
        physicsBody.setWorldTransform(physicsBody.getWorldTransform().setTranslation(x, y, z));
    }

    public void setPosition(Vector3 target) {
        physicsBody.setWorldTransform(physicsBody.getWorldTransform().setTranslation(target));
    }

}
