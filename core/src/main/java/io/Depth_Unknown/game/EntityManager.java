package io.Depth_Unknown.game;

import io.Depth_Unknown.game.entities.Entity;
import java.util.ArrayList;

public class EntityManager implements GameObject {

    ArrayList<Entity> entities = new ArrayList<>(20);


    @Override
    public void reset() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void update(float delta) {
        for (Entity entity : entities) {
            entity.update(delta);
        }
    }

    @Override
    public void create() {

    }

    @Override
    public void destroy() {

    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }
}
