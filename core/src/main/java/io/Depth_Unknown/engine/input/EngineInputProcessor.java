package io.Depth_Unknown.engine.input;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;

import java.util.ArrayList;
import java.util.Objects;

public class EngineInputProcessor implements InputProcessor, ControllerListener {
    Preferences keybinds;
    ArrayList<KeyEvent> keyEvents = new ArrayList<>(15);

    public EngineInputProcessor(Preferences keybinds) {
        this.keybinds = keybinds;
    }

    @Override
    public boolean keyDown(int keycode) {
//        System.out.println("Key Down: " + keycode);
        for (KeyEvent keyEvent : keyEvents) {
            if (keyEvent.isEnabled() && keyEvent.getKeyCode() == keycode) {
                keyEvent.down();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        for (KeyEvent keyEvent : keyEvents) {
            if (keyEvent.isEnabled() && keyEvent.getKeyCode() == keycode) {
                keyEvent.up();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        // Not used for now
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        // Not used
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // Not used
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }


    /**
     * Method to register a keyboard control into the Input Processor
     *
     * @param name Name of control
     * @param keyCode Default keycode for control
     * @param runnableDown Callback function for when the key is down
     * @param runnableUp Callback function for when the key is up
     */
    public void registerControl(String name, int keyCode, Runnable runnableDown, Runnable runnableUp) {
        keyCode = keybinds.getInteger(name, keyCode);
        keybinds.putInteger(name, keyCode);
        this.keyEvents.add(new KeyEvent(name, keyCode, runnableDown, runnableUp));
    }

    /**
     * Method to register a disabled keyboard control into the Input Processor for future use.
     *
     * @param name Name of control
     * @param keyCode Default keycode for control
     * @param runnableDown Callback function for when the key is down
     * @param runnableUp Callback function for when the key is up
     */
    public void loadControl(String name, int keyCode, Runnable runnableDown, Runnable runnableUp) {
        keyCode = keybinds.getInteger(name, keyCode);
        keybinds.putInteger(name, keyCode);
        this.keyEvents.add(new KeyEvent(name, keyCode, false, runnableDown, runnableUp));
    }

    /**
     * Method to remove a control callback (Such as for changing control schemes)
     * @param name Name of the control provided for registration
     */
    public void unregisterControl(String name) {
        for  (int i = 0; i < keyEvents.size(); i++) {
            if (keyEvents.get(i).getName().equals(name)) {
                keyEvents.remove(i);
                break;
            }
        }
    }

    /**
     * Method to change the keycode of an existing control persistently
     * @param name Name of control
     * @param keyCode Keycode of control
     */
    public void updateControl(String name, int keyCode) {
        Objects.requireNonNull(getKeyEvent(name)).setKeyCode(keyCode);
        keybinds.putInteger(name, keyCode);
    }

    /**
     * Temporarily disables a control
     * @param name Name of Control
     */
    public void disableControl(String name) {
        Objects.requireNonNull(getKeyEvent(name)).disable();
    }

    /**
     * Enables a control that was previously disabled
     * @param name Name of Control
     */
    public void enableControl(String name) {
        Objects.requireNonNull(getKeyEvent(name)).enable();
    }

    /**
     * Method to get all registered KeyEvents
     * @return An ArrayList of all registered KeyEvents
     */
    public ArrayList<KeyEvent> getKeyEvents() {
        return keyEvents;
    }

    /**
     * Method to get a registered KeyEvent
     * @param name Name of KeyEvent
     * @return A KeyEvent with the name or null if not found
     */
    private KeyEvent getKeyEvent(String name) {
        for (KeyEvent keyEvent : keyEvents) {
            if (keyEvent.getName().equals(name)) {
                return keyEvent;
            }
        }
        return null;
    }


    /**
     * Clears all currently registered controls
     */
    public void clearControls() {
        keyEvents.clear();
    }

    /**
     * Controller input handling
     * Low priority
     * */
    @Override
    public void connected(Controller controller) {
        // Do nothing
    }

    @Override
    public void disconnected(Controller controller) {

    }

    @Override
    public boolean buttonDown(Controller controller, int i) {
        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int i) {
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int i, float v) {
        return false;
    }
}
