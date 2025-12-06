package io.Depth_Unknown.game;

public interface GameObject {

    // TODO Eliminate reset method as most classes do not make use of it
    void reset();
    void update(float delta);

    // TODO Eliminate create() and instead move everything to the construstor.
    void create();


    void destroy();

    // TODO Eliminate the render method from game object code
    // Needs to be worked out and transferred to the new Renderer
    // Kept for compatibility
    void render(float deltaTime);
}
