package io.Depth_Unknown.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import io.Depth_Unknown.engine.rendering.Renderable2d;
import io.Depth_Unknown.engine.rendering.Renderable3d;
import io.Depth_Unknown.game.entities.Entity;
import io.Depth_Unknown.game.entities.Player;

import java.util.ArrayList;

public class EntityManager implements GameObject, Renderable3d, Renderable2d {

    private ArrayList<Entity> entities = new ArrayList<>(20);


    /**
     * Destroys all entities BESIDES the player
     * */
    public void reset() {
        Entity player = null;
        for (Entity entity : entities) {
            if (entity instanceof Player) {
                player = entity; // Save pointer to player to be readded after other entities are removed
                continue;
            }
            entity.destroy();
        }
        entities.clear();
        if (player != null)
            entities.add(player);

    }

    @Override
    public void update(float delta) {
        for (Entity entity : entities) {
            entity.update(delta);
        }
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

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    /**
     * @param spriteBatch
     */
    @Override
    public void render2d(SpriteBatch spriteBatch) {
        for (Entity entity : entities) {
            if (entity instanceof Renderable2d) {
                ((Renderable2d) entity).render2d(spriteBatch);
            }
        }
    }

    /**
     * @param modelBatch
     * @param environment
     */
    @Override
    public void render3d(ModelBatch modelBatch, Environment environment) {
        for (Entity entity : entities) {
            if (entity instanceof Renderable3d) {
                ((Renderable3d) entity).render3d(modelBatch, environment);
            }
        }

    }
}
