package io.Depth_Unknown.game.ui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.Depth_Unknown.game.GameObject;
import io.Depth_Unknown.game.SettingsManager;
import io.Depth_Unknown.game.ui.UiManager;


public class MenuControler implements GameObject {
    private Stage baseMenuStage;
    private Stage levelSelectStage;

    private Stage currentStage;

    private Table buttonGroup;
    UiManager uiManager;
    TextureRegion upRegion;
    TextureRegion downRegion;
    BitmapFont buttonFont;
    ScreenViewport viewport = new ScreenViewport();
    Label fpsLabel;
    SettingsManager settingsManager;

    public MenuControler(UiManager uiManager, SettingsManager settingsManager) {
        this.uiManager = uiManager;
        this.settingsManager = settingsManager;
    }

    @Override
    public void create() {

        /*
        * Replace these with custom assets latter
        * */
        Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        upRegion = skin.getRegion("default-round");
        downRegion = skin.getRegion("default-round-down");
        buttonFont = new BitmapFont();

        // Create an FPS label
        Label.LabelStyle labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        fpsLabel = new Label("FPS: 0", labelStyle);

        /*
         * Create the stages
         * */
        baseMenuStage = new Stage(viewport);
        levelSelectStage = new Stage(viewport);

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

        Table baseButtonGroup = new Table();
        baseMenuStage.addActor(baseButtonGroup);

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
            }
        });

        levelBtn.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                System.out.println("Showing Levels!");
                setStage(levelSelectStage);
            }
        });

        settingsBtn.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                System.out.println("Opening Settings");
                settingsManager.setViewport(viewport);
                settingsManager.setReturnCallback(new ChangeListener() {
                    @Override
                    public void changed (ChangeEvent event, Actor actor) {
                        System.out.println("Home!");
                        setStage(baseMenuStage);
                    }
                });

                setStage(settingsManager.getStage());
            }
        });

        quitBtn.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        /*
         * Set up the levels stage
         * */

        Table levelsButtonGroup = new Table();
        levelSelectStage.addActor(levelsButtonGroup);

        levelsButtonGroup.center().top();
        levelsButtonGroup.setFillParent(true);
        levelsButtonGroup.pad(5);

        levelsButtonGroup.align(Align.center);
        levelsButtonGroup.padTop(50);
        levelsButtonGroup.padBottom(50);

        // Create Buttons
        TextButton level1Btn = new TextButton("Level 1", style);
        TextButton level2Btn = new TextButton("Level 2", style);
        TextButton level3Btn = new TextButton("Level 3", style);
        TextButton backBtn = new TextButton("Back", style);

        level1Btn.pad(20);
        level2Btn.pad(20);
        level3Btn.pad(20);
        backBtn.pad(20);

        levelsButtonGroup.add(level1Btn).uniformX().fillX().padBottom(paddingBetween).row();
        levelsButtonGroup.add(level2Btn).uniformX().fillX().padBottom(paddingBetween).row();
        levelsButtonGroup.add(level3Btn).uniformX().fillX().padBottom(paddingBetween).row();
        levelsButtonGroup.add(backBtn).uniformX().fillX().row();

        level1Btn.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                System.out.println("Starting Level 1...");
            }
        });

        level2Btn.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                System.out.println("Starting Level 2...");
            }
        });

        level3Btn.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                System.out.println("Starting Level 3...");
            }
        });

        backBtn.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                setStage(baseMenuStage);
            }
        });

        setStage(baseMenuStage);
    }

    public void setStage(Stage stage) {
        currentStage = stage;
        Gdx.input.setInputProcessor(currentStage);
        currentStage.addActor(fpsLabel);
    }


    @Override
    public void destroy() {
        baseMenuStage.dispose();
        levelSelectStage.dispose();
    }

    @Override
    public void reset() {

    }

    @Override
    public void render(float delta) {
        currentStage.draw();
    }

    @Override
    public void update(float delta) {
        fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
        currentStage.act(Gdx.graphics.getDeltaTime());
    }

    public void resize(int width, int height) {
        fpsLabel.setPosition(10, Gdx.graphics.getHeight() - 20);
//        baseMenuStage.getViewport().update(width, height, true);
//        levelSelectStage.getViewport().update(width, height, true);
//        settingsStage.getViewport().update(width, height, true);
        currentStage.getViewport().update(width, height, true);
    }
}
