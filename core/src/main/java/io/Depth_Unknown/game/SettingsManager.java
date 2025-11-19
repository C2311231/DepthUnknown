package io.Depth_Unknown.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;


public class SettingsManager implements GameObject{
    Preferences settings;
    Stage settingsStage;
    TextButton settingsBackBtn;
    ArrayList<Setting<?>> settingsList = new ArrayList<>();
    Table settingsGroup;
    Skin skin;
    ChangeListener  lastListener;

    public SettingsManager(Preferences settings) {
        settingsList = new ArrayList<>();
        this.settings = settings;
        settingsStage = new Stage();
    }

    /**
     *
     */
    @Override
    public void reset() {

    }

    public void setViewport(ScreenViewport viewport) {
        this.settingsStage.setViewport(viewport);
    }

    public Stage getStage() {
        return settingsStage;
    }

    public void setReturnCallback(ChangeListener listener) {
        if (lastListener != null) {
            settingsBackBtn.removeListener(lastListener);
        }
        settingsBackBtn.addListener(listener);
    }

    /**
     * @param delta
     */
    @Override
    public void render(float delta) {

    }

    /**
     * @param delta
     */
    @Override
    public void update(float delta) {

    }

    /**
     *
     */
    @Override
    public void create() {
        /*
         * Replace these with custom assets latter
         * */
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
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

        settingsBackBtn = new TextButton("Back", style);

        /*
         * Settings Stage set up
         * */
        settingsGroup = new Table();
        settingsStage.addActor(settingsGroup);

        settingsGroup.center().top();
        settingsGroup.setFillParent(true);
        settingsGroup.pad(5);
        settingsGroup.padTop(50);
        settingsGroup.padBottom(50);

        settingsBackBtn.pad(20);

        settingsGroup.add(settingsBackBtn);
        settingsGroup.row().padBottom(15);


        for (Setting<?> setting : settingsList) {
            settingsGroup.add(setting.getLabel());
            settingsGroup.add(setting.getActor(skin));
            settingsGroup.row().padBottom(15);;
        }
    }

    /**
     *
     */
    @Override
    public void destroy() {

    }

    public <T> Setting<T> registerSetting(String key, T defaultValue) {
        Setting<T> temp = new Setting<T>(key, defaultValue, settings);
        settingsList.add(temp);
        Label tempLabel = temp.getLabel();
        tempLabel.setTouchable(Touchable.disabled);
        settingsGroup.add(tempLabel).align(Align.left).padRight(20);
        settingsGroup.add(temp.getActor(skin)).align(Align.right);
        settingsGroup.row().padBottom(15);
        return temp;
    }
}
