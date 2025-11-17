package io.Depth_Unknown.engine.input;

public class KeyEvent {
    private Runnable runnable;
    private String name;
    private int keyCode;
    private KeyState state;
    private boolean enabled = true;

    public KeyEvent(String name, int keyCode, KeyState state, Runnable runnable) {
        this.runnable = runnable;
        this.name = name;
        this.keyCode = keyCode;
        this.state = state;
    }

    public KeyEvent(String name, int keyCode, KeyState state, boolean enabled, Runnable runnable) {
        this.runnable = runnable;
        this.name = name;
        this.keyCode = keyCode;
        this.state = state;
        this.enabled = enabled;
    }

    public void trigger() {
        runnable.run();
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    public KeyState getState() {
        return state;
    }

    public void setState(KeyState state) {
        this.state = state;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }
}
