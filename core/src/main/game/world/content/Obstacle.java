package main.game.world.content;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import main.game.core.Constants.ObstacleConstants;
import main.game.world.player.Player;
/*
Powerups in entities.xml? <-- probably this one, fixed locations? or randomise x and y? randomise? 
type of obstacles as enum?
obstacle type as list e.g [0, 1, 2, 3]... ?
how many of each?
some random obstacles found, some generated after objective? have some obstacles in xml others generated after objective completion
*/
public class Obstacle extends Entity {

    enum ObstacleType {
        Storm, // Slows player, 400 x 400? inverted controls???
        Rocks, // Damages player 200 x 200?
        Shipwreck // Damage and slow with chance of gold? 100 x 100?
    }

    private Texture texture;
    private int size; // Size depends on type, e.g storm > rocks > shipwreck
    private ObstacleType type;

    public Obstacle(Vector2 position, String type) {
        this.texture = new Texture(Gdx.files.internal("textures/" + type.toString().toLowerCase() + ".png"));
        sprite = new Sprite(texture);
        this.type = ObstacleType.valueOf(type);
        switch (this.type) {
            case Storm:
                this.size = 400;
                break;
            case Rocks:
                this.size = 200;
                break;
            case Shipwreck:
                this.size = 100;
                break;
        }
        sprite.setPosition(position.x, position.y);
        sprite.setRotation(0);
        System.out.println("type.toString(): " + type.toString());
        System.out.println("position: " + sprite.getX() + ", " + sprite.getY());
    }

    @Override
    public int update(float deltaTime) {
        return 0;
    }

    @Override
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public void enterObstacle(Player player) {
        // Is called from World.java collisions() when player runs into obstacle
        // Add effect to player based on type of obstacle
        switch (type) {
            case Storm: // slow
                player.setSlowEffect(true);
                break;
            case Rocks: // damage
                player.takeObstacleDamage(2);
                break;
            case Shipwreck: // slow + damage + chance of gold
                player.setSlowEffect(true);
                player.takeObstacleDamage(2);
                break;
        }
    }

    public boolean inProcess(Vector2 pos) {
        if (pos.dst(this.getPosition()) <= ObstacleConstants.PROCESS_RANGE) return true;
        else return false;
    }

    @Override
    public void dispose() {
        this.texture.dispose();
    }
    
}
