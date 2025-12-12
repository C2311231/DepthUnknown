package io.Depth_Unknown.game;

public interface GameObject {

    void update(float delta);

    void destroy();

    // TODO Eliminate the render method from game object code
    // Needs to be worked out and transferred to the new Renderer
    // Kept for compatibility
    void render(float deltaTime);
}
