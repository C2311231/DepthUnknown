package io.Depth_Unknown.game.world;

import com.badlogic.gdx.math.Vector3;
import io.Depth_Unknown.game.GameObject;

public abstract class LevelScript implements GameObject {
    public abstract Vector3 getPlayerSpawnPosition();
    public abstract String getLevelModelName();
    public void onTrigger() {

    }

    public void onDimensionSwitch(boolean id3D) {

    }

    @Override
    public void reset() {

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void create() {

    }

    @Override
    public void destroy() {

    }
}
