package io.Depth_Unknown.game.world;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.UBJsonReader;

public class LevelLoader {
    public static ModelInstance loadLevelModel(LevelScript levelScript) {
        Model model;
        if (levelScript.getLevelModelName().endsWith(".g3dj")) {
            model = new G3dModelLoader(new JsonReader()).loadModel(Gdx.files.internal("levels/" + levelScript.getLevelModelName()));
        } else {
            model = new G3dModelLoader(new UBJsonReader()).loadModel(Gdx.files.internal("levels/" + levelScript.getLevelModelName()));
        }

        // Fixes an issue with the export from blender where the massiveness isn't scaled by the strength set in blender (Without this the model would always be white)
        // Alternatively can be archived by setting emission in blender to black.
        for (Material mat : model.materials) {
            mat.remove(ColorAttribute.Emissive);
        }

        return new ModelInstance(model);
    }
}
