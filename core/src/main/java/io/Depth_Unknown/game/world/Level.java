package io.Depth_Unknown.game.world;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import io.Depth_Unknown.engine.rendering.Renderable3d;
import io.Depth_Unknown.game.GameObject;
import io.Depth_Unknown.game.entities.Player;


public class Level implements GameObject, Renderable3d {
    public String name;
    public LevelScript script;
    public Player player;
    public ModelInstance modelInstance;

    public Level(String name, Player player, LevelScript script) {
        this.name = name;
        this.script = script;
        this.player = player;
        modelInstance = LevelLoader.loadLevelModel(script);
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
        player.position = script.getPlayerSpawnPosition();

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

    /**
     * @param modelBatch
     * @param environment
     */
    @Override
    public void render3d(ModelBatch modelBatch, Environment environment) {
        modelBatch.render(modelInstance, environment);
    }
}
