package main.game.world.content;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Powerups extends Entity {
    private Texture texture;
    private int size; // Size depends on type, e.g storm > rocks > shipwreck
    private PowerUpType type;
    enum PowerUpType {
        damage, // Slows player, 400 x 400? inverted controls???
        speed, // Damages player 200 x 200?
        xp,
         // Damage and slow with chance of gold? 100 x 100?
    }
    @Override
    public int update(float deltaTime) {
        // TODO Auto-generated method stub
        return 0;
    }
    public Powerups(Vector2 position, String type) {
    this.texture = new Texture(Gdx.files.internal("textures/" + type.toString().toLowerCase() + ".png"));
    sprite = new Sprite(texture);
    this.type = PowerUpType.valueOf(type);
    switch (this.type) {
        case damage:
            this.size = 400;
            break;
        case speed:
            this.size = 200;
            break;
        case xp:
            this.size = 100;
            break;
        }
    sprite.setPosition(position.x, position.y);
    sprite.setRotation(0);
    System.out.println("type.toString(): " + type.toString());
    System.out.println("position: " + sprite.getX() + ", " + sprite.getY());
    }
    @Override
    public void render(SpriteBatch batch) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        
    }
    
}
