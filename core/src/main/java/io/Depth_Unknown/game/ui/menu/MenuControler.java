package io.Depth_Unknown.game.ui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.Depth_Unknown.game.GameObject;
import io.Depth_Unknown.game.ui.UiManager;

public class MenuControler implements GameObject {
    private Stage stage;
    private Table table;
    UiManager uiManager;
    TextureRegion upRegion;
    TextureRegion downRegion;
    BitmapFont buttonFont;

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



        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);


        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = new TextureRegionDrawable(upRegion);
        style.down = new TextureRegionDrawable(downRegion);
        style.over = new TextureRegionDrawable(downRegion);
        style.font = buttonFont;

        TextButton button1 = new TextButton("Button 1", style);
        table.add(button1);

        TextButton button2 = new TextButton("Button 2", style);
        table.add(button2);

        button1.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                System.out.println("Changed!");
            }
        });

        button2.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                System.out.println("Changed 2!");
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
