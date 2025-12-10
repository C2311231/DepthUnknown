package io.Depth_Unknown.game.world;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import io.Depth_Unknown.engine.physics.PhysicsEngine;
import io.Depth_Unknown.engine.rendering.Renderable3d;
import io.Depth_Unknown.game.GameObject;
import io.Depth_Unknown.game.entities.Player;


public class Level implements GameObject, Renderable3d {
    private String name;
    private final LevelScript script;
    private final Player player;
    LevelModel lvModel;
    PhysicsEngine physicsEngine;

    public Level(String name, Player player, LevelScript script, PhysicsEngine physicsEngine) {
        this.name = name;
        this.script = script;
        this.player = player;
        this.physicsEngine = physicsEngine;
        this.lvModel = new LevelModel(LevelLoader.loadLevelModel(script), physicsEngine);
    }

    @Override
    public void reset() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void update(float delta) {
        script.update(delta);
        lvModel.update(delta);
    }

    public void create() {
        // Starts the level
        player.setPosition(script.getPlayerSpawnPosition());

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
    public void render3d(ModelBatch modelBatch, Environment environment){
        lvModel.render3d(modelBatch, environment);
    }
}
