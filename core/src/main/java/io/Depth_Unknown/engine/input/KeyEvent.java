package io.Depth_Unknown.engine.input;

public class KeyEvent {
    private Runnable runnableDown;
    private Runnable runnableUp;
    private String name;
    private int keyCode;
    private boolean enabled = true;

    public KeyEvent(String name, int keyCode, Runnable runnableDown, Runnable runnableUp) {
        this.runnableDown = runnableDown;
        this.runnableUp = runnableUp;
        this.name = name;
        this.keyCode = keyCode;
    }

    public KeyEvent(String name, int keyCode, boolean enabled, Runnable runnableDown, Runnable runnableUp) {
        this.runnableDown = runnableDown;
        this.runnableUp = runnableUp;
        this.name = name;
        this.keyCode = keyCode;
        this.enabled = enabled;
    }

    public void down() {
        this.runnableDown.run();
    }

    public void up() {
        this.runnableUp.run();
    }

    public Runnable getRunnableUp() {
        return runnableUp;
    }

    public Runnable getRunnableDown() {
        return runnableDown;
    }

    public void setRunnableUp(Runnable runnableUp) {
        this.runnableUp = runnableUp;
    }

    public void setRunnableDown(Runnable runnableDown) {
        this.runnableUp = runnableUp;
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
