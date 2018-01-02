/*
 * The MIT License
 *
 * Copyright 2018 Raymond Buckley.
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

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.ray3k.superwififinder.Core;
import com.ray3k.superwififinder.Entity;
import com.ray3k.superwififinder.states.GameState;

public class LevelChanger extends Entity {
    public static final float DEFAULT_DELAY = 5.0f;
    private float timer;
    private Array<String> levels;

    @Override
    public void create() {
        setPersistent(true);
        levels = new Array(new String[]{"level1", "level2", "level3", "level4", "level5", "level6", "level7", "level8", "level9", "level10"});
    }

    @Override
    public void act(float delta) {
        if (timer > 0) {
            timer -= delta;
            if (timer < 0) {
                timer = -1;
                
                GameState.entityManager.clear();
                
                if (levels.size > 0) {
                    String level = levels.random();
                    levels.removeValue(level, false);
                    GameState.inst().loadLevel(Core.DATA_PATH + "/spine/" + level + ".json");
                } else {
                    GameState.entityManager.addEntity(new GameOverTimerEntity(1.0f));
                }
            }
        }
    }

    @Override
    public void actEnd(float delta) {
    }

    @Override
    public void draw(SpriteBatch spriteBatch, float delta) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void collision(Entity other) {
    }
    
    public void changeLevel(float delay) {
        timer = delay;
    }
    
    public void changeLevel() {
        changeLevel(DEFAULT_DELAY);
    }
}
