package io.Depth_Unknown.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import io.Depth_Unknown.engine.input.EngineInputProcessor;
import io.Depth_Unknown.engine.rendering.Renderable3d;
import io.Depth_Unknown.engine.rendering.Renderer;
import io.Depth_Unknown.game.EntityManager;
import io.Depth_Unknown.game.GameObject;
import io.Depth_Unknown.game.entities.Player;
import io.Depth_Unknown.game.settings.SettingsManager;
import io.Depth_Unknown.levels.level1.scripts.level1;

public class LevelManager implements GameObject, Renderable3d {
    public SettingsManager settingsManager;
    public Level[] levels;
    public Level currentLevel;
    private Renderer renderer;
    private EngineInputProcessor inputProcessor;
    public Player player;
    private EntityManager entityManager;

    public LevelManager(Renderer renderer, SettingsManager settingsManager, EngineInputProcessor inputProcessor, EntityManager entityManager) {
        this.renderer = renderer;
        this.inputProcessor = inputProcessor;
        this.entityManager = entityManager;
        this.settingsManager = settingsManager;
    }

    public void beginLevel(String levelName) throws RuntimeException {
        for (Level level : levels) {
            if (level.name.equals(levelName) ) {
                Gdx.input.setCursorCatched(true);
                entityManager.reset();
                currentLevel = level;
                currentLevel.create();
                Gdx.input.setInputProcessor(inputProcessor);
                return;
            }
        }
        throw new RuntimeException("Level " + levelName + " not found");
    }

    /**
     * Starts First Level
     * */
    public void beginLevel() throws RuntimeException{
        if (levels.length == 0) {
            throw new RuntimeException("Levels array is empty");

        }
        Gdx.input.setCursorCatched(true);
        Level level = levels[0];
        entityManager.reset();
        currentLevel = level;
        currentLevel.create();
        Gdx.input.setInputProcessor(inputProcessor);
    }

    @Override
    public void reset() {

    }

    @Override
    public void update(float delta) {
        if (currentLevel != null) {
            currentLevel.update(delta);
        }
    }

    @Override
    public void create() {
        player = new Player(inputProcessor, renderer, settingsManager);
        levels = new Level[]{
            new Level("Level 1", player, new level1()),

        };
        entityManager.addEntity(player);

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
        if (currentLevel != null) {
            currentLevel.render3d(modelBatch, environment);
        }
    }
}
