package io.Depth_Unknown.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import io.Depth_Unknown.engine.input.InputManager;
import io.Depth_Unknown.engine.physics.PhysicsEngine;
import io.Depth_Unknown.engine.rendering.Renderable3d;
import io.Depth_Unknown.engine.rendering.Renderer;
import io.Depth_Unknown.engine.settings.SettingsManager;
import io.Depth_Unknown.game.entities.Player;
import io.Depth_Unknown.game.ui.LevelSelectController;
import io.Depth_Unknown.game.ui.MenuController;
import io.Depth_Unknown.game.ui.PauseMenuControler;
import io.Depth_Unknown.game.world.Level;
import io.Depth_Unknown.levels.level1;

import java.util.ArrayList;

public class Game implements ApplicationListener {
    private static Game gameInstance;
    private static boolean paused = true;

    private Renderer renderer;
    private Preferences keybinds;
    private Preferences gameSettings;
    private LevelSelectController levelSelectController;
    private MenuController menuController;
    private PauseMenuControler pauseMenuControler;

    ArrayList<GameObject> gameObjects =  new ArrayList<>(20);

    public static boolean isGamePaused() {
        return paused;
    }

    public static void pauseGame() {
        gameInstance.pause();
    }

    public static void unpauseGame() {
        gameInstance.resume();
    }

    /**
     * Called when the application is Started
     * */
    // TODO Find a better way to avoid this mess of reference passes
    @Override
    public void create() {
        gameInstance = this;
        keybinds = Gdx.app.getPreferences("Depth_Unknown_KeyBinds");
        gameSettings = Gdx.app.getPreferences("Depth_Unknown_Game_Settings");
        PhysicsEngine physics = new PhysicsEngine();
        renderer = new Renderer(gameObjects, physics);
        InputManager input = new InputManager(keybinds);
        EntityManager entityManager = new EntityManager();
        SettingsManager settingsManager = new SettingsManager(gameSettings);
        LevelManager levelManager = new LevelManager(renderer, settingsManager, input, entityManager, physics);
        levelSelectController = new LevelSelectController(settingsManager, levelManager, renderer);
        menuController = new MenuController(settingsManager, levelManager, renderer, levelSelectController);
        pauseMenuControler = new PauseMenuControler(settingsManager, levelManager, renderer, menuController, input);

        gameObjects.add(levelManager);
        gameObjects.add(physics);
        gameObjects.add(entityManager);
        gameObjects.add(settingsManager);

        gameObjects.add(menuController);
        gameObjects.add(levelSelectController);
        gameObjects.add(pauseMenuControler);

        InputMultiplexer mux = new InputMultiplexer();

        mux.addProcessor(renderer.getStage());
        mux.addProcessor(input);

        Gdx.input.setInputProcessor(mux);

        menuController.activate();
    }

    /**
     * Called when the window is resized
     * */
    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
    }

    /**
     * Called on each frame
     * */
    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        for (GameObject gameObject : gameObjects) {
            gameObject.update(deltaTime);
        }


        for (GameObject gameObject : gameObjects) {
            gameObject.render(deltaTime);
        }
        renderer.render();
    }

    /**
     * Called when minimized and before closing
     * */
    @Override
    public void pause() {
        keybinds.flush();
        gameSettings.flush();
        paused = true;
    }

    /**
     * Called when the window is unminimized
     * */
    @Override
    public void resume() {
        paused = false;
    }

    /**
     * Called after pause when the window is closed
     * */
    @Override
    public void dispose() {
        for (GameObject gameObject : gameObjects) {
            gameObject.destroy();
        }
    }

    public static class LevelManager implements GameObject, Renderable3d {
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
                new Level("Level 1", player, new level1(), physicsEngine),

            };
            entityManager.addEntity(player);
        }

        public void beginLevel(String levelName) throws RuntimeException {
            for (Level level : levels) {
                if (level.getName().equals(levelName) ) {
                    Gdx.input.setCursorCatched(true);
                    unpauseGame();
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
            unpauseGame();
        }

        @Override
        public void update(float delta) {
            if (currentLevel != null) {
                currentLevel.update(delta);
                if (!isGamePaused() && !Gdx.input.isCursorCatched())
                    Gdx.input.setCursorCatched(true);
            }
            if (isGamePaused()) {
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
            pauseGame();
        }
    }
}
