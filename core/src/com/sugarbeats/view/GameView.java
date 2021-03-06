package com.sugarbeats.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.sugarbeats.SugarBeats;
import com.sugarbeats.presenter.GamePresenter;
import com.sugarbeats.service.AssetService;
import com.sugarbeats.service.AudioService;

/**
 * Created by taphan on 08.03.2018.
 */

public class GameView extends BaseView {
    SugarBeats game;
    GamePresenter presenter;
    static final int GAME_READY = 0;
    static final int GAME_RUNNING = 1;
    static final int GAME_OVER = 2;

    final OrthographicCamera cam;
    Texture backBtn;
    Rectangle backBounds;

    Vector3 touchPoint;
    Rectangle fireBound;
    Rectangle leftBound;
    Rectangle rightBound;
    Rectangle upBound;
    Rectangle downBound;
    Rectangle powerBarBound;
    boolean isTouching;
    float powerBarBtnX;
    public float angle;
    public float health;
    BitmapFont font;
    BitmapFont font2;

    public GameView(SugarBeats game, GamePresenter presenter) {
        super(game.getBatch());
        this.game = game;
        this.presenter = presenter;
        backBtn = new Texture("button_exit.png");

        cam = new OrthographicCamera();
        cam.setToOrtho(false, SugarBeats.WIDTH, SugarBeats.HEIGHT);

        fireBound = new Rectangle(SugarBeats.WIDTH - AssetService.fireBtn.getWidth() / 5, AssetService.fireBtn.getWidth() / 30, AssetService.fireBtn.getWidth() / 6, AssetService.fireBtn.getHeight() / 6);
        leftBound = new Rectangle(AssetService.fireBtn.getWidth() / 8 - 20, AssetService.fireBtn.getWidth() / 9, AssetService.leftBtn.getWidth() / 6, AssetService.leftBtn.getHeight() / 6);
        rightBound = new Rectangle(AssetService.fireBtn.getWidth() / 4 + 5, AssetService.fireBtn.getWidth() / 9, AssetService.leftBtn.getWidth() / 6, AssetService.leftBtn.getHeight() / 6);
        upBound = new Rectangle(AssetService.fireBtn.getWidth() / 6 + 5, AssetService.fireBtn.getWidth() / 6 - 5, AssetService.leftBtn.getWidth() / 6, AssetService.leftBtn.getHeight() / 6);
        downBound = new Rectangle(AssetService.fireBtn.getWidth() / 6 + 5, AssetService.fireBtn.getWidth() / 16, AssetService.leftBtn.getWidth() / 6, AssetService.leftBtn.getHeight() / 6);
        powerBarBound = new Rectangle(SugarBeats.WIDTH / 2 - 30, AssetService.fireBtn.getWidth() / 9, AssetService.powerBar.getWidth() / 5 - 20, AssetService.powerBar.getHeight() / 5);
        backBounds = new Rectangle(SugarBeats.WIDTH / 2 - backBtn.getWidth()/3 / 2 + 220, SugarBeats.HEIGHT - 70 + 20, backBtn.getWidth() / 3, backBtn.getHeight() / 3);
        touchPoint = new Vector3();

        isTouching = false;
        powerBarBtnX = SugarBeats.WIDTH / 2;
        angle = 10;
        health = AssetService.health.getWidth() / 10;
        font = new BitmapFont();
        font2 = new BitmapFont();
        AudioService.playMusic(AudioService.gameMusic);
    }

    @Override
    public void update (float delta) {
        for(int i = 0; i < 5; i++) {
            if(touches.get(i).touched) {
                cam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
                if (leftBound.contains(touchPoint.x, touchPoint.y)) {
                    presenter.updateKeyPress(0);
                }
                if (rightBound.contains(touchPoint.x, touchPoint.y)) {
                    presenter.updateKeyPress(1);
                }
                if (powerBarBound.contains(touchPoint.x, touchPoint.y)) {
                    powerBarBtnX = touchPoint.x;
                }
                if (upBound.contains(touchPoint.x, touchPoint.y)) {
                    if (angle + 10 < 180) {
                        presenter.updateKeyPress(2);
                        angle += 10;
                    }
                }
                if (downBound.contains(touchPoint.x, touchPoint.y)) {
                    if (angle - 10 > 0) {
                        presenter.updateKeyPress(3);
                        angle -= 10;
                    }
                }
                if(backBounds.contains(touchPoint.x, touchPoint.y)) {
                    presenter.onBack();
                }
                // Note: Can only shoot if there is one finger pressing the screen
                if (!isTouching && touches.get(0).touched) {
                    isTouching = true;
                    if(fireBound.contains(touchPoint.x, touchPoint.y)) {
                        presenter.updateFireButton(powerBarBtnX - powerBarBound.x + 20, angle);
                    }
                }
            } else if (!touches.get(0).touched){
                isTouching = false;
            }
        }
    }

    @Override
    public void draw() {
        game.batch.begin();
        game.batch.draw(backBtn,SugarBeats.WIDTH / 2 - backBtn.getWidth()/3 / 2 + 220, SugarBeats.HEIGHT - 70 + 20, backBtn.getWidth() / 3, backBtn.getHeight() / 3 );
        game.batch.draw(AssetService.fireBtn, SugarBeats.WIDTH - AssetService.fireBtn.getWidth()/5, AssetService.fireBtn.getWidth()/17, AssetService.fireBtn.getWidth() / 6, AssetService.fireBtn.getHeight() / 6);
        game.batch.draw(AssetService.leftBtn, AssetService.fireBtn.getWidth()/8 - 20, AssetService.fireBtn.getWidth()/9, AssetService.leftBtn.getWidth() / 6, AssetService.leftBtn.getHeight() / 6);
        game.batch.draw(AssetService.rightBtn, AssetService.fireBtn.getWidth()/4 + 5, AssetService.fireBtn.getWidth()/9, AssetService.leftBtn.getWidth() / 6, AssetService.leftBtn.getHeight() / 6);
        game.batch.draw(AssetService.upBtn, AssetService.fireBtn.getWidth()/6 + 5, AssetService.fireBtn.getWidth()/6 - 5, AssetService.leftBtn.getWidth() / 6, AssetService.leftBtn.getHeight() / 6);
        game.batch.draw(AssetService.downBtn, AssetService.fireBtn.getWidth()/6 + 5, AssetService.fireBtn.getWidth()/16, AssetService.leftBtn.getWidth() / 6, AssetService.leftBtn.getHeight() / 6);
        game.batch.draw(AssetService.powerBar, SugarBeats.WIDTH / 2 - 37, AssetService.fireBtn.getWidth()/9, AssetService.powerBar.getWidth() / 5, AssetService.powerBar.getHeight() / 5);
        game.batch.draw(AssetService.powerBarBtn, powerBarBtnX - 10,AssetService.fireBtn.getWidth()/11, AssetService.powerBarBtn.getWidth() / 6, AssetService.powerBarBtn.getHeight() / 3);
        game.batch.draw(AssetService.health, 85, 322, health, AssetService.health.getHeight() / 10);
        game.batch.draw(AssetService.healthBar, 10, 290, AssetService.healthBar.getWidth() / 10, AssetService.healthBar.getHeight() / 10);
        game.batch.draw(AssetService.enemyHealth, 32, 260, AssetService.enemyHealth.getWidth() / 20, AssetService.enemyHealth.getHeight() / 20);
        font.draw(game.batch, String.valueOf((int) health) + "/260", 240, 311);
        font.draw(game.batch, "Sugar", 37, 337);
        font2.getData().setScale(0.8f);
        font2.draw(game.batch, "Kahoot is da best", 32, 285);
        game.batch.end();
    }

    @Override
    public void show() {

    }
}
