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
package com.ray3k.superwififinder.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SlotData;
import com.ray3k.superwififinder.Core;
import com.ray3k.superwififinder.Entity;
import com.ray3k.superwififinder.EntityManager;
import com.ray3k.superwififinder.InputManager;
import com.ray3k.superwififinder.State;
import com.ray3k.superwififinder.entities.LevelChanger;
import com.ray3k.superwififinder.entities.ObstacleEntity;
import com.ray3k.superwififinder.entities.PlayerEntity;
import com.ray3k.superwififinder.entities.TargetEntity;

public class GameState extends State {
    private static GameState instance;
    private int score;
    private static int highscore = 0;
    private OrthographicCamera gameCamera;
    private Viewport gameViewport;
    private OrthographicCamera uiCamera;
    private Viewport uiViewport;
    private InputManager inputManager;
    private Skin skin;
    private Stage stage;
    private Table table;
    private Label scoreLabel;
    public static EntityManager entityManager;
    public static TextureAtlas spineAtlas;
    private TiledDrawable bg, liner, wall;
    public static TargetEntity target;
    public static String expression;
    public static PlayerEntity player;
    public static LevelChanger levelChanger;
    public ProgressBar progressBar;
    
    public static GameState inst() {
        return instance;
    }
    
    public GameState(Core core) {
        super(core);
    }
    
    @Override
    public void start() {
        instance = this;
        
        spineAtlas = Core.assetManager.get(Core.DATA_PATH + "/spine/superwififinder.atlas", TextureAtlas.class);
        
        score = 0;
        
        inputManager = new InputManager(); 
        
        uiCamera = new OrthographicCamera();
        uiViewport = new ScreenViewport(uiCamera);
        uiViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        uiViewport.apply();
        
        uiCamera.position.set(uiCamera.viewportWidth / 2, uiCamera.viewportHeight / 2, 0);
        
        gameCamera = new OrthographicCamera();
        gameViewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), gameCamera);
        gameViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        gameViewport.apply();
        
        gameCamera.position.set(gameCamera.viewportWidth / 2, gameCamera.viewportHeight / 2, 0);
        
        skin = Core.assetManager.get(Core.DATA_PATH + "/ui/glassy-ui.json", Skin.class);
        stage = new Stage(new ScreenViewport());
        
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(inputManager);
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);
        
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        
        entityManager = new EntityManager();
        
        createStageElements();
        
        loadLevel(Core.DATA_PATH + "/spine/level0.json");
        
        levelChanger = new LevelChanger();
        entityManager.addEntity(levelChanger);
        
        bg = new TiledDrawable(spineAtlas.findRegion("floor"));
        liner = new TiledDrawable(spineAtlas.findRegion("liner"));
        wall = new TiledDrawable(spineAtlas.findRegion("wall"));
        
        ProgressBarStyle progressBarStyle = new ProgressBarStyle();
        progressBarStyle.background = new TextureRegionDrawable(spineAtlas.findRegion("battery-bg"));
        progressBarStyle.knobBefore = new TiledDrawable(spineAtlas.findRegion("battery-knob"));
        progressBarStyle.knobBefore.setMinWidth(0.0f);
        progressBarStyle.knobBefore.setMinHeight(36.0f);
        
        progressBar = new ProgressBar(0.0f, 100.0f, 1.0f, false, progressBarStyle);
        progressBar.setValue(50.0f);
        progressBar.setSize(68.0f, 36.0f);
        progressBar.setPosition(Gdx.graphics.getWidth() - 20.0f, Gdx.graphics.getHeight() - 20.0f, Align.topRight);
    }
    
    private void createStageElements() {
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);
        
        scoreLabel = new Label("0", skin);
        root.add(scoreLabel).expandY().padTop(25.0f).top();
    }
    
    @Override
    public void draw(SpriteBatch spriteBatch, float delta) {
        Gdx.gl.glClearColor(207.0f / 255.0f, 111.0f / 255.0f, 101.0f / 255.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        
        gameCamera.update();
        spriteBatch.setProjectionMatrix(gameCamera.combined);
        spriteBatch.begin();
        spriteBatch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA);
        bg.draw(spriteBatch, 0.0f, 0.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        wall.draw(spriteBatch, 0.0f, Gdx.graphics.getHeight() - 160.0f, Gdx.graphics.getWidth(), 160.0f);
        liner.draw(spriteBatch, 0.0f, Gdx.graphics.getHeight() - 160.0f, Gdx.graphics.getWidth(), 23.0f);
        entityManager.draw(spriteBatch, delta);
        progressBar.draw(spriteBatch, 1.0f);
        spriteBatch.end();
        
        spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        stage.draw();
    }

    @Override
    public void act(float delta) {
        entityManager.act(delta);
        
        progressBar.act(delta);
        
        stage.act(delta);
        
        if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
            Core.stateManager.loadState("menu");
        }
    }

    @Override
    public void dispose() {
    }

    @Override
    public void stop() {
        stage.dispose();
    }
    
    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height, true);
        
        uiViewport.update(width, height);
        uiCamera.position.set(uiCamera.viewportWidth / 2, uiCamera.viewportHeight / 2, 0);
        stage.getViewport().update(width, height, true);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
        scoreLabel.setText(Integer.toString(score));
        if (score > highscore) {
            highscore = score;
        }
    }
    
    public void addScore(int score) {
        this.score += score;
        scoreLabel.setText(Integer.toString(this.score));
        if (this.score > highscore) {
            highscore = this.score;
        }
    }

    public OrthographicCamera getGameCamera() {
        return gameCamera;
    }

    public void setGameCamera(OrthographicCamera gameCamera) {
        this.gameCamera = gameCamera;
    }

    public Skin getSkin() {
        return skin;
    }

    public Stage getStage() {
        return stage;
    }
    
    public void playSound(String name, float volume) {
        Core.assetManager.get(Core.DATA_PATH + "/sfx/" + name + ".wav", Sound.class).play(volume);
    }
    
    public void loadLevel(String levelPath) {
        SkeletonData skeletonData = Core.assetManager.get(levelPath, SkeletonData.class);
    
        for (SlotData slotData : skeletonData.getSlots()) {
            Entity entity = null;
            if (slotData.getAttachmentName() != null) {
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
            }
            
            if (entity != null) {
                entity.setPosition(slotData.getBoneData().getX(), slotData.getBoneData().getY());
                entity.setDepth((int) entity.getY());
                GameState.entityManager.addEntity(entity);
            }
        }
    }
}