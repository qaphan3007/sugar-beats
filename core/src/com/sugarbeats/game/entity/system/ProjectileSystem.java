package com.sugarbeats.game.entity.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.sugarbeats.game.entity.component.MovementComponent;
import com.sugarbeats.game.entity.component.ProjectileComponent;
import com.sugarbeats.game.entity.component.StateComponent;
import com.sugarbeats.game.entity.component.TransformComponent;

/**
 * Created by taphan on 20.04.2018.
 */

public class ProjectileSystem extends IteratingSystem {
    private static final Family family = Family.all(
            TransformComponent.class,
            MovementComponent.class,
            ProjectileComponent.class,
            StateComponent.class).get();

    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<MovementComponent> mm;
    private ComponentMapper<ProjectileComponent> pm;
    private ComponentMapper<StateComponent> sm;

    private Vector2 velocity;

    public ProjectileSystem() {
        super(family);

        tm = ComponentMapper.getFor(TransformComponent.class);
        mm = ComponentMapper.getFor(MovementComponent.class);
        pm = ComponentMapper.getFor(ProjectileComponent.class);
        sm = ComponentMapper.getFor(StateComponent.class);

        velocity = new Vector2();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent position = tm.get(entity);
        MovementComponent movement = mm.get(entity);
        ProjectileComponent projectile = pm.get(entity);
        StateComponent state = sm.get(entity);

        // Initialize function only runs once
        if (state.get() == ProjectileComponent.STATE_START) {
            movement.velocity.x = this.velocity.x;
            movement.velocity.y = this.velocity.y;
        }

        movement.velocity.y += -9.81f * deltaTime;  // Set velocity.(x = 0)

        if (state.get() != ProjectileComponent.STATE_MIDAIR) {
            state.set(ProjectileComponent.STATE_MIDAIR);  // Shots get fired! Change state from START
        }

        if(projectile.isDead && state.get() == ProjectileComponent.STATE_MIDAIR) {
            state.set(ProjectileComponent.STATE_HIT);
        }

    }

    private Vector2 initializeVelocity(){
        Vector2 velocityOut = new Vector2();
        float v0 = 90;
        float angle = 50;
        velocityOut.x += v0 * Math.cos(angle*Math.PI / 180 );
        velocityOut.y += v0 * Math.sin(angle*Math.PI / 180 );
        return velocityOut;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = initializeVelocity();
    }
}
