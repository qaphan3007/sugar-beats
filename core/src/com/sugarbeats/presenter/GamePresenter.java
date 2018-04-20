package com.sugarbeats.presenter;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.sugarbeats.SugarBeats;
import com.sugarbeats.game.World;
import com.sugarbeats.game.entity.system.AnimationSystem;
import com.sugarbeats.game.entity.system.BoundsSystem;
import com.sugarbeats.game.entity.system.CollisionSystem;
import com.sugarbeats.game.entity.system.CollisionSystem.CollisionListener;
import com.sugarbeats.game.entity.system.GravitySystem;
import com.sugarbeats.game.entity.system.MovementSystem;
import com.sugarbeats.game.entity.system.PlayerSystem;
import com.sugarbeats.game.entity.system.ProjectileSystem;
import com.sugarbeats.game.entity.system.RenderSystem;
import com.sugarbeats.model.PlayerData;
import com.sugarbeats.service.IPlayService;
import com.sugarbeats.view.GameView;

import java.util.List;

/**
 * Created by taphan on 11.04.2018.
 */

public class GamePresenter extends ScreenAdapter implements IPlayService.INetworkListener {

    SugarBeats game;
    private Screen parent;
    protected final PooledEngine engine;
    World world;
    GameView view;
    IPlayService playService;

    CollisionListener collisionListener;


    public GamePresenter(SugarBeats game, Screen parent) {
        this.game = game;
        this.parent = parent;
        engine = new PooledEngine();
        world = new World(engine);
        view = new GameView(game, this);
//        playService.setNetworkListener(this);

        collisionListener = new CollisionListener() {
            @Override
            public void powerup () {
                System.out.println("Power up sound");
            }

            @Override
            public void ground () { }

            @Override
            public void hit () {
                System.out.println("Ouchie got hit..");
            }

        };
        setupEngine(engine, game.getBatch());
        world.create();

    }

    private void setupEngine(PooledEngine engine, SpriteBatch batch) {
        engine.addSystem(new AnimationSystem());
        engine.addSystem(new RenderSystem(batch));
        engine.addSystem(new PlayerSystem(world));
        engine.addSystem(new MovementSystem());
        engine.addSystem(new BoundsSystem());
        engine.addSystem(new GravitySystem());
        engine.addSystem(new ProjectileSystem());

        engine.addSystem(new CollisionSystem(world, collisionListener));
    }

    @Override
    public final void render(float delta) {
        update(delta);
        view.draw();
        view.show();
    }

    private void update(float delta) {
        if (delta > 0.1f) delta = 0.1f;
        updateInput();
        view.update(delta);
        world.update(delta);
        engine.update(delta);
    }

    private void updateInput() {
        float veloX = 0.0f;
        float veloY = 0.0f;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) veloX = -250f;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) veloX = 100f;
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            veloY = 250f;


        }

        engine.getSystem(PlayerSystem.class).setVelocity(veloX);
    }

    public void updateKeyPress(int key) {
        float veloX = 0.0f;

        switch (key) {
            case 0:
                // Left button pressed
                veloX = -250f;
                break;
            case 1:
                // Right button pressed
                veloX = 250f;
                break;
        }
        engine.getSystem(PlayerSystem.class).setVelocity(veloX);
    }

    public void updateFireButton() {
        Vector2 velocity = new Vector2();
        velocity.x = -250f;
        // Note: Cannot draw projectile on screen
        world.createProjectile(230, 215, velocity.x, velocity.y);
        //world.createPlayer(1);
        //while(! projectile.getComponent(ProjectileComponent.class).isDead)
        velocity = updateProjectileVelocity();
        engine.getSystem(ProjectileSystem.class).setVelocity(velocity);
    }

    private Vector2 updateProjectileVelocity() {
        // TODO: Update the velocity of projectile to be able to move after time frame
        return new Vector2();
    }

    @Override
    public void onRoomReady(List<PlayerData> players) {
        Gdx.app.debug("SUGAR BEATS", "onRoomReady: ");
//        addPlayers(players, true);
//        world.initialize();
    }

    @Override
    public void onReliableMessageReceived(String senderParticipantId, int describeContents, byte[] messageData) {

    }

    @Override
    public void onUnreliableMessageReceived(String senderParticipantId, int describeContents, byte[] messageData) {

    }

}
