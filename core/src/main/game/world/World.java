package main.game.world;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;

import main.game.MainRunner;
import main.game.core.Calculations;
import main.game.core.Constants;
import main.game.core.XMLLoader;
import main.game.core.Constants.*;
import main.game.world.content.*;
import main.game.world.player.Player;
import main.game.world.ui.IGUI;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import main.game.MainRunner;
import main.game.core.Calculations;
import main.game.core.Constants;
import main.game.core.Constants.BulletConstants;
import main.game.core.Constants.CollegeConstants;
import main.game.core.Constants.NPCConstants;
import main.game.core.Constants.PlayerConstants;
import main.game.core.XMLLoader;
import main.game.world.content.Bullet;
import main.game.world.content.College;
import main.game.world.content.Entity;
import main.game.world.content.NPC;
import main.game.world.player.Player;
import main.game.world.ui.IGUI;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
public class World {
    private Player player;
    private IGUI inGameUI;
    private Set<NPC> npcs;
    private Set<College> colleges;
    private Set<Bullet> eBullets;
    private Set<Bullet> pBullets;
    private Set<Obstacle> obstacles;
    private Set<Powerups> powerups;
    private boolean ispowerup = false;
    private OrthographicCamera gameCamera, uiCamera;
    private SpriteBatch batch, uiBatch;
    private float timesinceCollision;
    public Set<NPC> getNPCs()
    {
        return npcs;
    }
    public Set<College> getCollege()
    {return colleges;}

    public Player getPlayer() {
        return player;
    }
    
    public World() {
        // Create XMLLoader to load input files
        XMLLoader loader = new XMLLoader(Gdx.files.internal("xmls/entities.xml"));

        try {
            System.out.println("try load");
            //Read input file and initalize variables
            loader.load();
        } catch (Exception e) {
            System.out.println("error");
            System.out.println(e);
        }

        //Initialise all world objects
        player = new Player(100, 100, new Vector2(1000, 1000), 270, loader.getObjectives());
        npcs = loader.getNpcs();
        colleges = loader.getColleges();
        obstacles = loader.getObstacles();
        powerups = loader.getPowerups();
        eBullets = new HashSet<>();
        pBullets = new HashSet<>();
        inGameUI = new IGUI();

        //Generate npc and college objects using XML data
        gameCamera = new OrthographicCamera();
        uiCamera = new OrthographicCamera();
        batch = new SpriteBatch();
        uiBatch = new SpriteBatch();

        gameCamera.setToOrtho(false);
        uiCamera.setToOrtho(false);
    }

    /**
     * The update, process, render loop for the {@link World}.
     */
    public void worldCycle() {
        //Update All Instances of World
        //Process All Information Returned from update
        //Render All Istances
        update();
        process();
        render();
    }

    /**
     * Updates all assets within the {@link World} 
     * @see 
     * {@link Entity},
     */
    private void update() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) MainRunner.IS_MENU = true;
        if (Gdx.input.isKeyJustPressed(Input.Keys.G))
        {
            MainRunner.Is_Gold = true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) MainRunner.Is_Pause = true;//
        float deltaTime = Gdx.graphics.getDeltaTime();
        if(!MainRunner.Is_Pause)//
        {
            //Update Player
            int bSPawn = player.update(deltaTime);
            Vector2 playerCenter = player.getCenter();

            if (bSPawn != -1) {

                //Check if there is a shooting objective
                if (player.getCurrentObjective() != null && player.getCurrentObjective().getuKey().equals("shoot")) player.updateObjective("shoot", 1);

                //Spawn player bullet based on player update response
                pBullets.add(new Bullet(playerCenter.add(BulletConstants.BULLET_OFFET), (float) Calculations.DegToRad(bSPawn), PlayerConstants.BULLET_SPEED, player.getDamage()));
            }

            //Update all enemy bullets
            Iterator<Bullet> beIterator = eBullets.iterator();
            while (beIterator.hasNext()) {
                Bullet bullet = beIterator.next();

                //If bullet has expired then dispose and delete it
                if (bullet.update(deltaTime) == 0) {
                    bullet.dispose();
                    beIterator.remove();
                    continue;
                }
            }

            //Update all player bullets
            Iterator<Bullet> bpIterator = pBullets.iterator();
            while (bpIterator.hasNext()) {
                Bullet bullet = bpIterator.next();

                //If bullet has expired then dispose and delete it
                if (bullet.update(deltaTime) == 0) {
                    bullet.dispose();
                    bpIterator.remove();
                }
            }

            //Update all colleges
            Iterator<College> cIterable = colleges.iterator();
            while (cIterable.hasNext()) {
                College college = cIterable.next();

                //Update college when in process range
                if (college.inProcess(playerCenter)) {
                    int cRet = college.update(deltaTime);

                    // if the college is allied don't do anything
                    if (cRet == -1) continue;

                    //If college has been destroyed update player stats, and set the college to allied
                    if (cRet == 0) {
                        player.collectScore(CollegeConstants.SCORE_DEATH);
                        player.collectGold(CollegeConstants.GOLD);
                        player.collectXP(CollegeConstants.XP);

                        if (player.getCurrentObjective() != null) {
                            if (player.getCurrentObjective().getuKey().equals(college.getUkey()))
                                player.updateObjective(college.getUkey(), 1);
                        }

                        college.setAllied();
                    } else if (cRet == 2 && college.inRange(playerCenter)) {

                        //Create a bullet between the player and college
                        Vector2 collegeOrigin = college.getCenter();
                        double angle = -Math.atan2(collegeOrigin.y - playerCenter.y - BulletConstants.BULLET_OFFET.y, collegeOrigin.x - playerCenter.x - BulletConstants.BULLET_OFFET.x) - Math.PI / 2;
                        eBullets.add(new Bullet(new Vector2(collegeOrigin.x - 16, collegeOrigin.y - 16), (float) angle, CollegeConstants.BULLET_SPEED, college.getDamage()));
                    }
                }
            }
        }

        //Update all NPCs
        Iterator<NPC> nIterator = npcs.iterator();
        while (nIterator.hasNext()) {
            NPC npc = nIterator.next();
            Vector2 playerCenter = player.getCenter();
            if (npc.inProcess(playerCenter)) {
                int npcRet = npc.update(deltaTime);
                //When the NPC is dead update player stats and remove the npc
                if (npcRet == 0) {
                    player.collectScore(NPCConstants.SCORE_DEATH);
                    player.collectGold(NPCConstants.GOLD);
                    player.collectXP(NPCConstants.XP);

                    if (player.getCurrentObjective() != null) {
                        if (player.getCurrentObjective().getuKey().equals("npc")) player.updateObjective("npc", 1);
                    }

                    npc.dispose();
                    nIterator.remove();
                }

                if (npc.inVision(playerCenter)) {
                    // npc move to player
                    Vector2 npcPos = npc.getPosition();
                    Vector2 position = new Vector2(0, 0);
                    double rotX = 0;
                    double rotY = 0;
                    if (npcPos.dst(playerCenter) > 150) {
                        // Do same as player movement but get x and y from vector calc
                        if (Math.abs(npcPos.x) - Math.abs(playerCenter.x) > 0) { // Left
                            position.x = -NPCConstants.MOVE_SPEED * deltaTime;
                            rotX = Math.PI / 2;
                            rotY = Math.PI / 2;
                        } else if (Math.abs(npcPos.x) - Math.abs(playerCenter.x) < 0) { // Right
                            position.x = PlayerConstants.SPEED * deltaTime;
                            rotX = 3 * Math.PI / 2;
                            rotY = 3 * Math.PI / 2;
                        }
                
                        if (Math.abs(npcPos.y) - Math.abs(playerCenter.y) < 0) { // Up
                            position.y = PlayerConstants.SPEED * deltaTime;
                            if (rotX == 3 * Math.PI / 2) rotX = 2 * Math.PI;
                            else rotX = 0;
                        } else if (Math.abs(npcPos.y) - Math.abs(playerCenter.y) > 0) { // Down
                            position.y = -PlayerConstants.SPEED * deltaTime;
                            rotY = Math.PI;
                            if (rotX == 0) rotX = Math.PI;
                        }
                        Sprite sprite = npc.getSprite();
                        sprite.translate(position.x, position.y);
                        sprite.setRotation((float) Calculations.RadToDeg((rotX + rotY) / 2));
                    }
                } else {
                    // patrol? random movement?
                }

                if (npcRet == 2 && npc.inRange(playerCenter)) {
                    // npc start attacking player
                    Vector2 npcOrigin = npc.getCenter();
                    double angle = -Math.atan2(npcOrigin.y - playerCenter.y - BulletConstants.BULLET_OFFET.y, npcOrigin.x - playerCenter.x - BulletConstants.BULLET_OFFET.x) - Math.PI / 2;
                    eBullets.add(new Bullet(new Vector2(npcOrigin.x - 16, npcOrigin.y - 16), (float) angle, NPCConstants.BULLET_SPEED, npc.getDamage()));
                }
            }
        }

        Iterator<Obstacle> obsIterator = obstacles.iterator();
        while (obsIterator.hasNext()) {
            Obstacle obstacle = obsIterator.next();
            obstacle.update(deltaTime);
        }

        // call and apply obstacle from obstacle class
        // or add new method in player class and pass obstacle to player. <-- this one better?
        for (Obstacle obstacle : obstacles) {
            if (obstacle.inProcess(player.getCenter())) {
                if (player.getBounds().overlaps(obstacle.getBounds())) {
                    obstacle.enterObstacle(player);
                    break;
                } else {
                    player.setSlowEffect(false);
                }
            }
        }
        Iterator<Powerups> powIterator = powerups.iterator();
        while (powIterator.hasNext()) {
            Powerups powerup = powIterator.next();
            powerup.update(deltaTime);
        }
        // call and apply obstacle from obstacle class
        // or add new method in player class and pass obstacle to player. <-- this one better?
        
        for (Powerups powerup : powerups) {
            if (powerup.inProcess(player.getCenter())) {
                if (player.getBounds().overlaps(powerup.getBounds())) {
                    powerup.enterPowerup(player);
                    ispowerup = true;
                    timesinceCollision = Gdx.graphics.getDeltaTime();
                } else {
                    player.setSlowEffect(false);
                }
            }
        }
        
        if(ispowerup)  
        {
        if(timesinceCollision+10 == Gdx.graphics.getDeltaTime()){
            {
                player.powerup(false);
            } 
            ispowerup = false;
        
        }
    }
        
        
    }

    /**
     * Processes all assets within the {@link World}, this includes collisions as well as updating {@link OrthographicCamera}, and {@link SpriteBatch} matrixes.
     * @see 
     * {@link OrthographicCamera},
     * {@link SpriteBatch}.
     */
    private void process() {
        collisions();

        // Update Both Cameras
        batch.setProjectionMatrix(gameCamera.combined);
        uiBatch.setProjectionMatrix(uiCamera.combined);
        gameCamera.position.set(player.getCenter(), gameCamera.position.z);
        gameCamera.update();
    }

    /**
     * Processes all collisions within the {@link World}.
     * @see 
     * {@link Player},
     * {@link College},
     * {@link NPC},
     * {@link Bullet},
     * {@link Obstacle}.
     */
    private void collisions() {
        Vector2 playerCenter = player.getCenter();
        boolean collided = false;

        //Process Player Collisions With A College
        for (College c : colleges) {
            if (c.inProcess(playerCenter)) {
                if (c.getBounds().overlaps(player.getBounds())) {
                    player.takeDamage((int) Constants.COLLIDE_DAMAGE, c.getCenter());
                    collided = true;
                    break;
                }
            }
        }

        //Process Player Collisions With A NPC
        if (!collided) {
            for (NPC n : npcs) {
                if (n.inProcess(playerCenter)) {
                    if (n.getBounds().overlaps(player.getBounds())) {
                        player.takeDamage((int) Constants.COLLIDE_DAMAGE, n.getCenter());
                        collided = true;
                        break;
                    }
                }
            }
        }

        //Process enemy bullets towards the Player
        if (!collided) {
            for (Bullet eb : eBullets) {
                if (player.getBounds().overlaps(eb.getBounds())) {
                    eb.hit();
                    player.takeDamage(eb.getDamage(), eb.getCenter());
                    break;
                }
            }
        }

        if (collided) player.collectScore(PlayerConstants.SCORE_HIT);

        //Process player bullets for NPCs and Colleges
        for (Bullet pb : pBullets) {
            collided = false;

            //Process College collisions
            for (College c : colleges) {
                if (c.inProcess(playerCenter)) {
                    if (c.getBounds().overlaps(pb.getBounds())) {

                        //if hit queue removing the bullet and update health and score if the enemy college is not allied.
                        pb.hit();
                        collided = true;

                        if (!c.getAllied()) {
                            c.takeDamage(pb.getDamage());
                            player.collectScore(CollegeConstants.SCORE_HIT);
                            break;
                        }
                    }
                }
            }

            if (collided) continue;

            //Process NPC collisions
            for (NPC n : npcs) {
                if (n.inProcess(playerCenter)) {
                    if (n.getBounds().overlaps(pb.getBounds())) {

                        //if hit queue removing the bullet and update health and score
                        pb.hit();
                        n.takeDamage(pb.getDamage());
                        player.collectScore(NPCConstants.SCORE_HIT);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Renders all entities within {@link World}. 
     * @see 
     * {@link Entity},
     */
    private void render() {
        Gdx.gl.glClearColor(0.3f, 0.6f, 0.9f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // mapRenderer.render();
        batch.begin();

        // Render enemy bullets
        for (Bullet bullet : eBullets) {
            bullet.render(batch);
        }

        //Render player bullets
        for (Bullet bullet : pBullets) {
            bullet.render(batch);
        }

        //Render npcs
        for (NPC npcs : npcs) {
            npcs.render(batch);
        }

        //Render colleges
        for (College college : colleges) {
            college.render(batch);
        }

        for (Obstacle obstacle: obstacles) {
            obstacle.render(batch);
        }
        for (Powerups powerup: powerups)
        {
            powerup.render(batch);
        }

        player.render(batch);
        batch.end();

        //Render the in game ui
        uiBatch.begin();
        inGameUI.draw(uiBatch, player);
        uiBatch.end();
    }

    /**
     * Disposes the {@link World}.
     * @see 
     * {@link Entity},k
     */
    public void dispose() {

        //Remove All Entities
        for (NPC npc : npcs) {
            npc.dispose();
        }

        //Update Colleges
        for (College college : colleges) {
            college.dispose();
        }

        for (Bullet bullet : eBullets) {
            bullet.dispose();
        }

        for (Bullet bullet : pBullets) {
            bullet.dispose();
        }

        player.dispose();
        batch.dispose();
        uiBatch.dispose();
        inGameUI.dispose();
    }
}
