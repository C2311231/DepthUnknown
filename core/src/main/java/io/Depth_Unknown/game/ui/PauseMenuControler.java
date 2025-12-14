package io.Depth_Unknown.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import io.Depth_Unknown.engine.input.InputManager;
import io.Depth_Unknown.engine.rendering.Renderer;
import io.Depth_Unknown.game.Game;
import io.Depth_Unknown.game.GameObject;
import io.Depth_Unknown.engine.settings.SettingsManager;

public class PauseMenuControler implements GameObject {
    private final Game.LevelManager levelManager;
    private final SettingsManager settingsManager;
    private final Renderer renderer;
    private final Table baseButtonGroup;
    private final MenuController menuController;

    public PauseMenuControler(SettingsManager settingsManager, Game.LevelManager levelManager, Renderer renderer, MenuController menuController, InputManager inputProcessor) {
        this.levelManager = levelManager;
        this.settingsManager = settingsManager;
        this.renderer = renderer;
        baseButtonGroup = new Table();
        this.menuController = menuController;

        registerActors();

        inputProcessor.registerControl("Pause", Input.Keys.ESCAPE, () -> {
            if (!Game.isGamePaused()) {
                Game.pauseGame();
                activate();
            } else {
                deactivate();
                Game.unpauseGame();
            }
        }, () -> {
        });
    }

    private void registerActors() {
        /*
         * Replace these with custom assets latter
         * */
        Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        TextureRegion upRegion = skin.getRegion("default-round");
        TextureRegion downRegion = skin.getRegion("default-round-down");
        BitmapFont buttonFont = new BitmapFont();

        /*
         * Set up button styles
         * */
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = new TextureRegionDrawable(upRegion);
        style.down = new TextureRegionDrawable(downRegion);
        style.over = new TextureRegionDrawable(downRegion);
        style.font = buttonFont;

        /*
         * Set up the main menu stage
         * */

        baseButtonGroup.center().top();
        baseButtonGroup.setFillParent(true);
        baseButtonGroup.pad(5);

        baseButtonGroup.align(Align.center);
        baseButtonGroup.padTop(50);
        baseButtonGroup.padBottom(50);

        // Create Buttons
        TextButton resumeBtn = new TextButton("Resume", style);
        TextButton settingsBtn = new TextButton("Settings", style);
        TextButton menuBtn = new TextButton("Menu", style);
        TextButton quitBtn = new TextButton("Quit", style);

        float paddingBetween = 10f;
        resumeBtn.pad(20);
        menuBtn.pad(20);
        settingsBtn.pad(20);
        quitBtn.pad(20);

        baseButtonGroup.add(resumeBtn).uniformX().fillX().padBottom(paddingBetween).row();
        baseButtonGroup.add(menuBtn).uniformX().fillX().padBottom(paddingBetween).row();
        baseButtonGroup.add(settingsBtn).uniformX().fillX().padBottom(paddingBetween).row();
        baseButtonGroup.add(quitBtn).uniformX().fillX().row();


        resumeBtn.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                System.out.println("Resuming Game...");
                deactivate();
                Game.unpauseGame();
            }
        });

        menuBtn.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                System.out.println("Returning To Menu...");
                levelManager.unloadLevel();
                deactivate();
                menuController.activate();
            }
        });

        settingsBtn.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                System.out.println("Opening Settings");
                deactivate();
                settingsManager.setSettingsStage(renderer.getStage());
                settingsManager.setReturnCallback(new ChangeListener() {
                    @Override
                    public void changed (ChangeEvent event, Actor actor) {
                        settingsManager.detachSettingsStage();
                        System.out.println("Back to pause menu...");
                        activate();
                    }
                });
            }
        });

        quitBtn.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
    }

    public void activate() {
        renderer.getStage().addActor(baseButtonGroup);
    }

    public void deactivate() {
        baseButtonGroup.remove();
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

    @Override
    public void update(float delta) {


    }

    public void resize(int width, int height) {
    }
}
