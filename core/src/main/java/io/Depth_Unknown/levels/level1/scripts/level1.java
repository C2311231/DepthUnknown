package io.Depth_Unknown.levels.level1.scripts;

import com.badlogic.gdx.math.Vector3;
import io.Depth_Unknown.game.world.LevelScript;

public class level1 extends LevelScript {
    /**
     * @return Player spawn coordinates in the level.
     */
    @Override
    public Vector3 getPlayerSpawnPosition() {
        return new Vector3(0,0.85f,0);
    }

    /**
     * @return Name of the level model file
     */
    @Override
    public String getLevelModelName() {
        return "level1.g3db";
    }

    /**
     * @param deltaTime
     */
    @Override
    public void render(float deltaTime) {

    }
}
