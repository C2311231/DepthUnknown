package io.Depth_Unknown.game.world;

import com.badlogic.gdx.Gdx;
import io.Depth_Unknown.engine.input.EngineInputProcessor;
import io.Depth_Unknown.engine.rendering.Renderer;
import io.Depth_Unknown.game.EntityManager;
import io.Depth_Unknown.game.GameObject;
import io.Depth_Unknown.game.entities.Player;
import io.Depth_Unknown.levels.level1.scripts.level1;

public class LevelManager implements GameObject {

    public Level[] levels;
    public Level currentLevel;
    private Renderer renderer;
    private EngineInputProcessor inputProcessor;
    public Player player;
    private EntityManager entityManager;

    public LevelManager(Renderer renderer, EngineInputProcessor inputProcessor, EntityManager entityManager) {
        this.renderer = renderer;
        this.inputProcessor = inputProcessor;
        this.entityManager = entityManager;
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
        player = new Player(inputProcessor, renderer);
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
}
