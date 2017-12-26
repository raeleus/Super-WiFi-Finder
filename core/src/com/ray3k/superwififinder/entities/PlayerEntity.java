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
import com.ray3k.superwififinder.Core;
import com.ray3k.superwififinder.Entity;
import com.ray3k.superwififinder.SpineEntity;
import com.ray3k.superwififinder.states.GameState;
import com.udojava.evalex.Expression;
import com.udojava.evalex.Expression.ExpressionException;
import java.math.BigDecimal;

public class PlayerEntity extends SpineEntity {
    private final static float MOVE_SPEED = 100.0f;
    private static final Vector2 temp = new Vector2();
    public PlayerEntity() {
        super(Core.DATA_PATH + "/spine/robot.json", "stand");
        getAnimationState().getCurrent(0).setLoop(true);
    }
    
    @Override
    public void create() {
        
    }

    @Override
    public void actSub(float delta) {
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
            if (getX() >= Gdx.graphics.getWidth() - 30.0f) {
                setX(Gdx.graphics.getWidth() - 30.0f);
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
            if (getY() >= Gdx.graphics.getHeight() - 170.0f) {
                setY(Gdx.graphics.getHeight() - 170.0f);
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
        
        setDepth((int) getY());
        
        BigDecimal x1 = new BigDecimal(getX());
        BigDecimal y1 = new BigDecimal(getY());
        BigDecimal x2 = new BigDecimal(GameState.target.getX());
        BigDecimal y2 = new BigDecimal(GameState.target.getY());
        
        Expression expression = new Expression(GameState.expression).with("x1", x1).with("y1", y1).with("x2", x2).with("y2", y2);
        
        try {
            System.out.println(expression.eval());
        } catch (ExpressionException e) {
            Gdx.app.log("PlayerEntity", "Error evaluating player expression.", e);
        }
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

}