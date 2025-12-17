package io.Depth_Unknown.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.Depth_Unknown.engine.rendering.Renderer;
import io.Depth_Unknown.game.Game;
import io.Depth_Unknown.game.GameObject;
import io.Depth_Unknown.engine.settings.SettingsManager;
import io.Depth_Unknown.game.world.LevelManager;


public class MenuController implements GameObject {
    private final ScreenViewport viewport = new ScreenViewport();
    private Label fpsLabel;
    private final SettingsManager settingsManager;
    private final LevelManager levelManager;
    private final Renderer renderer;
    private final LevelSelectController levelSelectController;
    private Table baseButtonGroup;

    public MenuController(SettingsManager settingsManager, LevelManager levelManager, Renderer renderer, LevelSelectController levelSelectController) {
        this.settingsManager = settingsManager;
        this.levelManager = levelManager;
        this.renderer = renderer;
        this.levelSelectController = levelSelectController;


        /*
         * Replace these with custom assets latter
         * */
        Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        TextureRegion upRegion = skin.getRegion("default-round");
        TextureRegion downRegion = skin.getRegion("default-round-down");
        BitmapFont buttonFont = new BitmapFont();

        // Create an FPS label
        Label.LabelStyle labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        fpsLabel = new Label("FPS: 0", labelStyle);

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

        baseButtonGroup = new Table();
        renderer.getStage().addActor(baseButtonGroup);

        baseButtonGroup.center().top();
        baseButtonGroup.setFillParent(true);
        baseButtonGroup.pad(5);

        baseButtonGroup.align(Align.center);
        baseButtonGroup.padTop(50);
        baseButtonGroup.padBottom(50);

        // Create Buttons
        TextButton playBtn = new TextButton("Play", style);
        TextButton levelBtn = new TextButton("Levels", style);
        TextButton settingsBtn = new TextButton("Settings", style);
        TextButton quitBtn = new TextButton("Quit", style);

        float paddingBetween = 10f;
        playBtn.pad(20);
        levelBtn.pad(20);
        settingsBtn.pad(20);
        quitBtn.pad(20);

        baseButtonGroup.add(playBtn).uniformX().fillX().padBottom(paddingBetween).row();
        baseButtonGroup.add(levelBtn).uniformX().fillX().padBottom(paddingBetween).row();
        baseButtonGroup.add(settingsBtn).uniformX().fillX().padBottom(paddingBetween).row();
        baseButtonGroup.add(quitBtn).uniformX().fillX().row();


        playBtn.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                System.out.println("Starting Game...");
                levelManager.beginLevel();
                deactivate();
            }
        });

        levelBtn.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                System.out.println("Opening Levels");
                deactivate();
                levelSelectController.activate();
                levelSelectController.setReturnCallback(new ChangeListener() {
                    @Override
                    public void changed (ChangeEvent event, Actor actor) {
                        levelSelectController.deactivate();
                        System.out.println("Back to Main Menu...");
                        activate();
                    }
                });
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
                        System.out.println("Back to Main Menu...");
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
        renderer.getStage().addActor(fpsLabel);
        renderer.getStage().addActor(baseButtonGroup);
    }

    public void deactivate() {
        fpsLabel.remove();
        baseButtonGroup.remove();
    }

    @Override
    public void destroy() {
    }

    @Override
    public void render(float delta) {
    }

    @Override
    public void update(float delta) {
        fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
        fpsLabel.setPosition(10, Gdx.graphics.getHeight() - 20);
    }

    public void resize(int width, int height) {
    }
}
