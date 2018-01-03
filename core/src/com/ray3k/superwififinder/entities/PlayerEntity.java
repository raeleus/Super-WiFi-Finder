/*
 * The MIT License
 *
 * Copyright 2017 Raymond Buckley.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.ray3k.superwififinder.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Event;
import com.ray3k.superwififinder.Core;
import com.ray3k.superwififinder.Entity;
import com.ray3k.superwififinder.SpineEntity;
import com.ray3k.superwififinder.states.GameState;

public class PlayerEntity extends SpineEntity {
    private final static float MOVE_SPEED = 100.0f;
    private static final Vector2 temp = new Vector2();
    public static enum Mode {
        WALKING, WON, LOST
    }
    private Mode mode;
    
    public PlayerEntity() {
        super(Core.DATA_PATH + "/spine/robot.json", "stand");
        mode = Mode.WALKING;
        getAnimationState().getCurrent(0).setLoop(true);
        
        getAnimationState().addListener(new AnimationState.AnimationStateAdapter() {
            @Override
            public void event(AnimationState.TrackEntry entry, Event event) {
                if (event.getData().getName().equals("step")) {
                    GameState.inst().playSound("step", .5f);
                }
            }
        });
    }
    
    @Override
    public void create() {
        GameState.wifiEntity = new WiFiEntity();
        GameState.wifiEntity.setDepth(-100);
        GameState.entityManager.addEntity(GameState.wifiEntity);
    }

    @Override
    public void actSub(float delta) {
        if (mode == Mode.WALKING) {
            if (Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.DOWN)) {
                if (!getAnimationState().getCurrent(0).getAnimation().getName().equals("walk")) {
                    getAnimationState().setAnimation(0, "walk", true);
                }
            } else {
                if (!getAnimationState().getCurrent(0).getAnimation().getName().equals("stand")) {
                    getAnimationState().setAnimation(0, "stand", true);
                    setMotion(0.0f, 0.0f);
                }
            }

            if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
                setMotion(MOVE_SPEED, 0.0f);
                if (getX() >= GameState.GAME_WIDTH - 30.0f) {
                    setX(GameState.GAME_WIDTH - 30.0f);
                    setMotion(0.0f, 0.0f);
                } else {
                    getSkeleton().setPosition(getX() + getXspeed() * delta, getY() + getYspeed() * delta);
                    getSkeleton().updateWorldTransform();
                    getSkeletonBounds().update(getSkeleton(), true);
                    for (Entity entity : GameState.entityManager.getEntities()) {
                        if (entity instanceof ObstacleEntity) {
                            ObstacleEntity obs = (ObstacleEntity) entity;

                            if (getSkeletonBounds().aabbIntersectsSkeleton(obs.getSkeletonBounds())) {
                                setMotion(0.0f, 0.0f);
                            }
                        }
                    }
                    getSkeleton().setPosition(getX(), getY());
                    getSkeleton().updateWorldTransform();
                    getSkeletonBounds().update(getSkeleton(), true);
                }
            } else if (Gdx.input.isKeyPressed(Keys.LEFT)) {
                setMotion(MOVE_SPEED, 180.0f);
                if (getX() <= 30.0f) {
                    setX(30.0f);
                    setMotion(0.0f, 0.0f);
                } else {
                    getSkeleton().setPosition(getX() + getXspeed() * delta, getY() + getYspeed() * delta);
                    getSkeleton().updateWorldTransform();
                    getSkeletonBounds().update(getSkeleton(), true);
                    for (Entity entity : GameState.entityManager.getEntities()) {
                        if (entity instanceof ObstacleEntity) {
                            ObstacleEntity obs = (ObstacleEntity) entity;

                            if (getSkeletonBounds().aabbIntersectsSkeleton(obs.getSkeletonBounds())) {
                                setMotion(0.0f, 0.0f);
                            }
                        }
                    }
                    getSkeleton().setPosition(getX(), getY());
                    getSkeleton().updateWorldTransform();
                    getSkeletonBounds().update(getSkeleton(), true);
                }
            } else if (Gdx.input.isKeyPressed(Keys.UP)) {
                setMotion(MOVE_SPEED, 90.0f);
                if (getY() >= GameState.GAME_HEIGHT - 170.0f) {
                    setY(GameState.GAME_HEIGHT - 170.0f);
                    setMotion(0.0f, 0.0f);
                } else {
                    getSkeleton().setPosition(getX() + getXspeed() * delta, getY() + getYspeed() * delta);
                    getSkeleton().updateWorldTransform();
                    getSkeletonBounds().update(getSkeleton(), true);
                    for (Entity entity : GameState.entityManager.getEntities()) {
                        if (entity instanceof ObstacleEntity) {
                            ObstacleEntity obs = (ObstacleEntity) entity;

                            if (getSkeletonBounds().aabbIntersectsSkeleton(obs.getSkeletonBounds())) {
                                setMotion(0.0f, 0.0f);
                            }
                        }
                    }
                    getSkeleton().setPosition(getX(), getY());
                    getSkeleton().updateWorldTransform();
                    getSkeletonBounds().update(getSkeleton(), true);
                }
            } else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
                setMotion(MOVE_SPEED, 270.0f);
                if (getY() <= 0.0f) {
                    setY(0.0f);
                    setMotion(0.0f, 0.0f);
                } else {
                    getSkeleton().setPosition(getX() + getXspeed() * delta, getY() + getYspeed() * delta);
                    getSkeleton().updateWorldTransform();
                    getSkeletonBounds().update(getSkeleton(), true);
                    for (Entity entity : GameState.entityManager.getEntities()) {
                        if (entity instanceof ObstacleEntity) {
                            ObstacleEntity obs = (ObstacleEntity) entity;

                            if (getSkeletonBounds().aabbIntersectsSkeleton(obs.getSkeletonBounds())) {
                                setMotion(0.0f, 0.0f);
                            }
                        }
                    }
                    getSkeleton().setPosition(getX(), getY());
                    getSkeleton().updateWorldTransform();
                    getSkeletonBounds().update(getSkeleton(), true);
                }
            }
        } else if (mode == Mode.WON) {
            if (!getAnimationState().getCurrent(0).getAnimation().getName().equals("stand")) {
                getAnimationState().setAnimation(0, "stand", true);
                setMotion(0.0f, 0.0f);
            }
        } else {
            if (!getAnimationState().getCurrent(0).getAnimation().getName().equals("lose")) {
                getAnimationState().setAnimation(0, "lose", false);
                setMotion(0.0f, 0.0f);
            }
        }
        
        setDepth((int) getY());
    }

    @Override
    public void drawSub(SpriteBatch spriteBatch, float delta) {
    }


    @Override
    public void actEnd(float delta) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void collision(Entity other) {
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

}
