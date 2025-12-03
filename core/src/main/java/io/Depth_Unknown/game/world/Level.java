package io.Depth_Unknown.game.world;
import io.Depth_Unknown.game.GameObject;
import io.Depth_Unknown.game.entities.Player;


public class Level implements GameObject {
    public String name;
    public LevelScript script;
    public Player player;

    public Level(String name, Player player, LevelScript script) {
        this.name = name;
        this.script = script;
        this.player = player;
    }

    @Override
    public void reset() {

    }

    @Override
    public void update(float delta) {
        script.update(delta);
    }

    @Override
    public void create() {
        // Starts the level
        player.position = script.getplayerSpawnPosition();

    }

    @Override
    public void destroy() {

    }

    /**
     * @param deltaTime
     */
    @Override
    public void render(float deltaTime) {

    }
}
