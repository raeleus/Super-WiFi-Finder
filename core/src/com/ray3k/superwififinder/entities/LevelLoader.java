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

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SlotData;
import com.ray3k.superwififinder.Core;
import com.ray3k.superwififinder.Entity;
import com.ray3k.superwififinder.states.GameState;

/**
 *
 * @author raymond
 */
public class LevelLoader extends Entity {
    private SkeletonData skeletonData;
    
    public LevelLoader(String levelPath) {
        super();
        skeletonData = Core.assetManager.get(levelPath, SkeletonData.class);
    }

    @Override
    public void act(float delta) {
    }

    @Override
    public void draw(SpriteBatch spriteBatch, float delta) {
    }

    @Override
    public void create() {
        for (SlotData slotData : skeletonData.getSlots()) {
            Entity entity = null;
            if (slotData.getAttachmentName().equals("robot")) {
                entity = new PlayerEntity();
                GameState.player = (PlayerEntity) entity;
            } else if (slotData.getAttachmentName().equals("lamp")) {
                entity = new ObstacleEntity(ObstacleEntity.Type.LAMP);
            } else if (slotData.getAttachmentName().equals("plant")) {
                entity = new ObstacleEntity(ObstacleEntity.Type.PLANT);
            } else if (slotData.getAttachmentName().equals("sofa")) {
                entity = new ObstacleEntity(ObstacleEntity.Type.SOFA);
            } else if (slotData.getAttachmentName().equals("WiFi")) {
                entity = new TargetEntity();
                GameState.target = (TargetEntity) entity;
            }
            
            if (entity != null) {
                entity.setPosition(slotData.getBoneData().getX(), slotData.getBoneData().getY());
                entity.setDepth((int) entity.getY());
                GameState.entityManager.addEntity(entity);
            }
        }
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
