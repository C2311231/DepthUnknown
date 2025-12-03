package io.Depth_Unknown.game;

public interface GameObject {
    void reset();
    void update(float delta);
    void create();
    void destroy();

    // Needs to be worked out and transferred to the new Renderer
    // Kept for compatibility
    void render(float deltaTime);
}
