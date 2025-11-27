package io.Depth_Unknown.engine.rendering;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;

public interface Renderable3d {
    public void render3d(ModelBatch modelBatch, Environment environment);
}
