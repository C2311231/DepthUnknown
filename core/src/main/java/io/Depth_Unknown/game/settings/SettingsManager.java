package io.Depth_Unknown.game.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.Depth_Unknown.game.GameObject;

import java.util.ArrayList;


public class SettingsManager implements GameObject {
    private final Preferences settings;
    private final TextButton settingsBackBtn;
    private ArrayList<Setting<?>> settingsList = new ArrayList<>();
    private final Table settingsGroup;
    private final Skin skin;
    private ChangeListener lastListener;

    public SettingsManager(Preferences settings) {
        settingsList = new ArrayList<>();
        this.settings = settings;

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
        settingsGroup.center().top();
        settingsGroup.setFillParent(true);
        settingsGroup.pad(5);
        settingsGroup.padTop(50);
        settingsGroup.padBottom(50);

        settingsBackBtn.pad(20);

        settingsGroup.add(settingsBackBtn);
        settingsGroup.row().padBottom(15);
        for (Setting setting : settingsList) {
            settingsGroup.add(setting.getLabel());
            settingsGroup.add(setting.getActor(skin));
            settingsGroup.row().padBottom(15);;
        }
    }

    public void setSettingsStage(Stage stage) {
        stage.addActor(settingsGroup);

    }

    public void detachSettingsStage() {
        settingsGroup.remove();
    }

    public void setReturnCallback(ChangeListener listener) {
        if (lastListener != null) {
            settingsBackBtn.removeListener(lastListener);
        }
        settingsBackBtn.addListener(listener);
        lastListener = listener;
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

    public Setting<?> getSetting(String key) {
        for (Setting<?> setting : settingsList) {
            if (setting.getKey().equals(key)) {
                return setting;
            }
        }
        return null;
    }
}
