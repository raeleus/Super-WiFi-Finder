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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.spine.AnimationState;
import com.ray3k.superwififinder.Core;
import com.ray3k.superwififinder.Entity;
import com.ray3k.superwififinder.SpineEntity;
import com.ray3k.superwififinder.states.GameState;
import com.udojava.evalex.Expression;
import java.math.BigDecimal;

public class WiFiEntity extends SpineEntity {
    public static enum Mode {
        START, WORKING, END, OFF
    }
    
    private Mode mode;
    
    public WiFiEntity() {
        super(Core.DATA_PATH + "/spine/wifi.json", "start");
        getAnimationState().getCurrent(0).setLoop(false);
        mode = Mode.START;
        getAnimationState().addListener(new AnimationState.AnimationStateAdapter() {
            @Override
            public void complete(AnimationState.TrackEntry entry) {
                if (mode == Mode.START) {
                    mode = Mode.WORKING;
                }
            }
        });
    }

    @Override
    public void actSub(float delta) {
        setPosition(GameState.player.getX(), GameState.player.getY() + 135.0f);
        
        BigDecimal x1 = new BigDecimal(GameState.player.getX());
        BigDecimal y1 = new BigDecimal(GameState.player.getY());
        BigDecimal x2 = new BigDecimal(GameState.target.getX());
        BigDecimal y2 = new BigDecimal(GameState.target.getY());
        
        Expression expression = new Expression(GameState.expression).with("x1", x1).with("y1", y1).with("x2", x2).with("y2", y2);
        
        if (mode == Mode.WORKING) {
            try {
                float value = expression.eval().floatValue();
                if (value < 30.0f) {
                    mode = Mode.END;
                    getAnimationState().setAnimation(0, "win", true);
                    GameState.levelChanger.changeLevel();
                    GameState.inst().playSound("win", 1.0f);
                    GameState.player.setMode(PlayerEntity.Mode.WON);
                    GameState.inst().addScore(1);
                } else if (value < 70) {
                    getAnimationState().setAnimation(0, "4", false);
                } else if (value < 150) {
                    getAnimationState().setAnimation(0, "3", false);
                } else if (value < 250) {
                    getAnimationState().setAnimation(0, "2", false);
                } else if (value < 400) {
                    getAnimationState().setAnimation(0, "1", false);
                } else {
                    getAnimationState().setAnimation(0, "0", false);
                }
            } catch (Expression.ExpressionException e) {
                Gdx.app.log("WiFiEntity", "Error evaluating player expression.", e);
            }
        }
    }

    @Override
    public void drawSub(SpriteBatch spriteBatch, float delta) {
    }

    @Override
    public void create() {
        
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
        if (mode == Mode.OFF) {
            getAnimationState().setAnimation(0, "0", false);
        }
    }
}
