package io.Depth_Unknown.game;

public interface GameObject {
    void reset();
    void render(float delta);
    void update(float delta);
    void create();
    void destroy();
}
