package io.Depth_Unknown.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import io.Depth_Unknown.engine.input.InputManager;
import io.Depth_Unknown.engine.physics.PhysicsEngine;
import io.Depth_Unknown.engine.rendering.Renderable3d;
import io.Depth_Unknown.engine.rendering.Renderer;
import io.Depth_Unknown.engine.settings.SettingsManager;
import io.Depth_Unknown.game.EntityManager;
import io.Depth_Unknown.game.Game;
import io.Depth_Unknown.game.GameObject;
import io.Depth_Unknown.game.entities.Player;
import io.Depth_Unknown.levels.demoLevel;
import io.Depth_Unknown.levels.test1;

public class LevelManager implements GameObject, Renderable3d {
    private SettingsManager settingsManager;
    private Level[] levels;
    private Level currentLevel;
    private Renderer renderer;
    private InputManager inputProcessor;
    private Player player;
    private EntityManager entityManager;
    private PhysicsEngine physicsEngine;

    public Level[] getLevels() {
        return levels;
    }

    public void setLevels(Level[] levels) {
        this.levels = levels;
    }

    public LevelManager(Renderer renderer, SettingsManager settingsManager, InputManager inputProcessor, EntityManager entityManager, PhysicsEngine physicsEngine) {
        this.renderer = renderer;
        this.inputProcessor = inputProcessor;
        this.entityManager = entityManager;
        this.settingsManager = settingsManager;
        this.physicsEngine = physicsEngine;
        player = new Player(inputProcessor, physicsEngine, renderer, settingsManager);
        levels = new Level[]{
            new Level("Demo Level", player, new demoLevel(entityManager, physicsEngine, player), physicsEngine),
            new Level("Physics test", player, new test1(), physicsEngine)
        };
        entityManager.addEntity(player);
    }

    public void beginLevel(String levelName) throws RuntimeException {
        for (Level level : levels) {
            if (level.getName().equals(levelName) ) {
                Gdx.input.setCursorCatched(true);
                Game.unpauseGame();
                entityManager.reset();
                currentLevel = level;
                currentLevel.create();
                return;
            }
        }
        throw new RuntimeException("Level " + levelName + " not found");
    }

    /**
     * Starts First Level
     * */
    public void beginLevel() throws RuntimeException {
        if (levels.length == 0) {
            throw new RuntimeException("Levels array is empty");
        }
        Gdx.input.setCursorCatched(true);
        Level level = levels[0];
        entityManager.reset();
        currentLevel = level;
        currentLevel.create();
        Game.unpauseGame();
    }

    @Override
    public void update(float delta) {
        if (currentLevel != null) {
            currentLevel.update(delta);
            if (!Game.isGamePaused() && !Gdx.input.isCursorCatched())
                Gdx.input.setCursorCatched(true);
        }
        if (Game.isGamePaused()) {
            Gdx.input.setCursorCatched(false);
        }
    }

    @Override
    public void destroy() {
        unloadLevel();
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
        if (currentLevel != null) {
            currentLevel.render3d(modelBatch, environment);
        }
    }

    public void unloadLevel() {
        if (currentLevel != null) {
            currentLevel.destroy();
        }
        currentLevel = null;
        entityManager.reset();
        Game.pauseGame();
    }
}
