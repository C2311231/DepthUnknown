package io.Depth_Unknown.levels;

import com.badlogic.gdx.math.Vector3;
import io.Depth_Unknown.engine.physics.PhysicsEngine;
import io.Depth_Unknown.game.EntityManager;
import io.Depth_Unknown.game.entities.Player;
import io.Depth_Unknown.game.entities.Trigger;
import io.Depth_Unknown.game.world.LevelScript;

public class demoLevel extends LevelScript {
    EntityManager entityManager;
    PhysicsEngine physicsEngine;
    Trigger endLevelTrigger;
    Player player;

    public demoLevel(EntityManager entityManager, PhysicsEngine physicsEngine, Player player) {
        this.entityManager = entityManager;
        this.physicsEngine = physicsEngine;
        this.player = player;
    }

    @Override
    public void create() {
        // Position taken from blender (and adjusted for y-up)
        endLevelTrigger = new Trigger(physicsEngine, -6.04833f, 3.18496f , 6.09669f, 1);
        entityManager.addEntity(endLevelTrigger);
        endLevelTrigger.registerTriggerCallback(() -> {
            System.out.println("Level Completed!!");
        }, player.getPhysicsBody());
    }


    /**
     * @return Player spawn coordinates in the level.
     */
    @Override
    public Vector3 getPlayerSpawnPosition() {
        //return new Vector3(0,0.85f,0);
        return new Vector3(0,5f,0);
    }

    /**
     * @return Name of the level model file
     */
    @Override
    public String getLevelModelName() {
        return "demo.g3db";
    }

    /**
     * @param deltaTime
     */
    @Override
    public void render(float deltaTime) {

    }
}
