package io.Depth_Unknown.game.entities;

import io.Depth_Unknown.game.GameObject;

public abstract class Entity implements GameObject {

    // Entity Position
    public double pos_x;
    public double pos_y;
    public double pos_z;

    // Entity Velocity
    public double vel_x = 0;
    public double vel_y = 0;
    public double vel_z = 0;

    // Entity Acceleration
    public double acc_x = 0;
    public double acc_y = 0;
    public double acc_z = 0;

    // Entity Rotation
    public double rot_x = 0;
    public double rot_y = 0;
    public double rot_z = 0;

    // Entity Rotational Velocity
    public double rot_vel_x = 0;
    public double rot_vel_y = 0;
    public double rot_vel_z = 0;

    // Entity Rotational Acceleration
    public double rot_acc_x = 0;
    public double rot_acc_y = 0;
    public double rot_acc_z = 0;

    public void reset() {

    }
    public void render(float delta) {

    }
    public void update(float delta) {
        updateVelocity(delta);
        updatePosition(delta);
        updateRotationalVelocity(delta);
        updateRotation(delta);
    }
    public void create() {

    }
    public void destroy() {

    }

    public void updateVelocity(float delta) {
        vel_x += delta * acc_x;
        vel_y += delta * acc_y;
        vel_z += delta * acc_z;
    }

    public void updatePosition(float delta) {
        pos_x += delta * vel_x;
        pos_y += delta * vel_y;
        pos_z += delta * vel_z;
    }

    public void updateRotationalVelocity(float delta) {
        rot_vel_x += delta * rot_acc_x;
        rot_vel_y += delta * rot_acc_y;
        rot_vel_z += delta * rot_acc_z;
    }

    public void updateRotation(float delta) {
        rot_x += delta * rot_vel_x;
        rot_y += delta * rot_vel_y;
        rot_z += delta * rot_vel_z;
    }

}
