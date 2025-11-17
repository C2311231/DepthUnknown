package io.Depth_Unknown.engine.input;

public class KeyEvent {
    Runnable runnable;
    String name;
    int keyCode;
    KeyState state;

    public KeyEvent(String name, int keyCode, KeyState state, Runnable runnable) {
        this.runnable = runnable;
        this.name = name;
        this.keyCode = keyCode;
        this.state = state;
    }

    public void trigger() {
        runnable.run();
    }
}
