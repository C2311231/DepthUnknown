package io.Depth_Unknown.game.ui;

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
import io.Depth_Unknown.engine.rendering.Renderer;
import io.Depth_Unknown.game.GameObject;
import io.Depth_Unknown.game.settings.SettingsManager;
import io.Depth_Unknown.game.world.Level;
import io.Depth_Unknown.game.world.LevelManager;


public class LevelSelectController implements GameObject {
    private final ScreenViewport viewport = new ScreenViewport();
    private Label fpsLabel;
    private final SettingsManager settingsManager;
    private final LevelManager levelManager;
    private final Renderer renderer;
    private final Table levelsButtonGroup;
    private TextButton backBtn;
    private ChangeListener lastListener;


    public LevelSelectController(SettingsManager settingsManager, LevelManager levelManager, Renderer renderer) {
        this.settingsManager = settingsManager;
        this.levelManager = levelManager;
        this.renderer = renderer;
        float paddingBetween = 10f;

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
         * Set up the levels stage
         * */

        levelsButtonGroup = new Table();

        levelsButtonGroup.center().top();
        levelsButtonGroup.setFillParent(true);
        levelsButtonGroup.pad(5);

        levelsButtonGroup.align(Align.center);
        levelsButtonGroup.padTop(50);
        levelsButtonGroup.padBottom(50);

        // Create Buttons

        for (Level level: levelManager.getLevels()) {
            TextButton levelSelectBtn = new TextButton(level.getName(), style);
            levelSelectBtn.pad(20);
            levelsButtonGroup.add(levelSelectBtn).uniformX().fillX().padBottom(paddingBetween).row();
            levelSelectBtn.addListener(new ChangeListener() {
                public void changed (ChangeEvent event, Actor actor) {
                    System.out.println("Starting " + level.getName());
                    levelManager.beginLevel(level.getName());
                    deactivate();
                }
            });
        }
        backBtn = new TextButton("Back", style);
        backBtn.pad(20);
        levelsButtonGroup.add(backBtn).uniformX().fillX().row();
    }

    public void setReturnCallback(ChangeListener listener) {
        if (lastListener != null) {
            backBtn.removeListener(lastListener);
        }
        backBtn.addListener(listener);
        lastListener = listener;
    }

    public void activate() {
        renderer.getStage().addActor(levelsButtonGroup);
    }

    public void deactivate() {
        levelsButtonGroup.remove();
    }


    @Override
    public void destroy() {
    }

    @Override
    public void render(float delta) {
    }

    @Override
    public void update(float delta) {
    }

}
