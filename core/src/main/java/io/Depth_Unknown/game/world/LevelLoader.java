package io.Depth_Unknown.game.world;

import io.Depth_Unknown.levels.level1.scripts.level1;

public class LevelLoader {
    Level[] levels;

    public LevelLoader() {
        levels = new Level[]{
            new level1(),

        };
    }
}
