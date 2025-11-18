package io.Depth_Unknown.game.ui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.Depth_Unknown.game.GameObject;
import io.Depth_Unknown.game.ui.UiManager;


public class MenuControler implements GameObject {
    private Stage stage;
    private Table buttonGroup;
    UiManager uiManager;
    TextureRegion upRegion;
    TextureRegion downRegion;
    BitmapFont buttonFont;
    ScreenViewport viewport = new ScreenViewport();
    Label fpsLabel;

    public MenuControler(UiManager uiManager) {
        this.uiManager = uiManager;
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

        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        Label.LabelStyle labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        fpsLabel = new Label("FPS: 0", labelStyle);

        fpsLabel.setPosition(10, Gdx.graphics.getHeight() - 20);

        stage.addActor(fpsLabel);

        Table buttonGroup = new Table();
        buttonGroup.center().top();
        buttonGroup.setFillParent(true);
        stage.addActor(buttonGroup);

        buttonGroup.center();
        buttonGroup.pad(5);

        buttonGroup.align(Align.center);
        buttonGroup.padTop(50);
        buttonGroup.padBottom(50);


        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = new TextureRegionDrawable(upRegion);
        style.down = new TextureRegionDrawable(downRegion);
        style.over = new TextureRegionDrawable(downRegion);
        style.font = buttonFont;

        TextButton button1 = new TextButton("Play", style);
        button1.pad(20);

        TextButton button2 = new TextButton("Levels", style);
        button2.pad(20);

        TextButton button3 = new TextButton("Settings", style);
        button3.pad(20);

        TextButton button4 = new TextButton("Quit", style);
        button4.pad(20);
        float paddingBetween = 10f;
        buttonGroup.add(button1).uniformX().fillX().padBottom(paddingBetween).row();
        buttonGroup.add(button2).uniformX().fillX().padBottom(paddingBetween).row();
        buttonGroup.add(button3).uniformX().fillX().padBottom(paddingBetween).row();
        buttonGroup.add(button4).uniformX().fillX().row();


        button1.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                System.out.println("Starting Game");
            }
        });

        button2.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                System.out.println("Showing Levels!");
            }
        });

        button3.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                System.out.println("Opening Settings");
            }
        });

        button4.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

    }

    @Override
    public void destroy() {
        stage.dispose();
    }

    @Override
    public void reset() {

    }

    @Override
    public void render(float delta) {
        fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void update(float delta) {

    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
}
