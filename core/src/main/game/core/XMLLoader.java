package main.game.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.print.attribute.Size2DSyntax;

import java.util.List;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

import main.game.world.content.College;
import main.game.world.content.NPC;
import main.game.world.content.Obstacle;
import main.game.world.content.Powerups;
import main.game.world.player.Objectives.Objective;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import main.game.world.content.College;
import main.game.world.content.NPC;
import main.game.world.player.Objectives.Objective;
import main.game.world.player.Objectives.ObjectiveManager;
import main.game.world.player.Player;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
public class XMLLoader {
    private FileHandle handle;
    private Set<College> colleges;
    private Set<NPC> npcs;
    private List<Objective> objectives;
    private Set<Obstacle> obstacles;
    private Set<Powerups> powerups;

    public XMLLoader(FileHandle handle) {
        this.handle = handle;
        this.npcs = new HashSet<>();
        this.colleges = new HashSet<>();
        this.objectives = new ArrayList<>();
        this.obstacles = new HashSet<>();
        this.powerups = new HashSet<>();
    }
    public XMLLoader(FileHandle handle, Set<NPC> npcs, Set<College> colleges, List<Objective> objectives) {
        this.handle = handle;
        this.npcs = npcs;
        this.colleges = colleges;
        this.objectives = objectives;
    }
    /**
     * Loads all the objects from the XML File given and initalises all {@link Entity}, and {@link Objective} instances present.
     * @see
     * {@link XmlReader}
     */
    public void load() {
        XmlReader reader = new XmlReader();
        System.out.println("reader init");
        System.out.println("handle: " + this.handle);
        Element root = reader.parse(this.handle);
        System.out.println("got root");
        
        Array<Element> xmlNpcs = root.getChildByName("npcs").getChildrenByName("npc");
        Array<Element> xmlColleges = root.getChildByName("colleges").getChildrenByName("college");
        Array<Element> xmlObjectives = root.getChildByName("objectives").getChildrenByName("objective");
        Array<Element> xmlObstacles = root.getChildByName("obstacles").getChildrenByName("obstacle");
        Array<Element> xmlPowerups = root.getChildByName("powerups").getChildrenByName("powerup");

        for (Element npc : xmlNpcs) {
            int health = Integer.parseInt(npc.get("health"));
            int npcx = Integer.parseInt(npc.get("x"));
            int npxy = Integer.parseInt(npc.get("y"));
            int rotation = Integer.parseInt(npc.get("rotation"));
            npcs.add(new NPC(health, new Vector2(npcx, npxy), rotation));
        }

        for (Element objective : xmlObjectives) {
            String name = objective.get("name");
            String uKey = objective.get("ukey");
            int amount = Integer.parseInt(objective.get("amount"));
            int xp = Integer.parseInt(objective.get("xp"));
            int objX = Integer.parseInt(objective.get("x"));
            int objY = Integer.parseInt(objective.get("y"));
            objectives.add(new Objective(name, uKey, amount, xp, new Vector2(objX, objY)));
        }

        for (Element college : xmlColleges) {
            int health = Integer.parseInt(college.get("health"));
            int damage = Integer.parseInt(college.get("damage"));
            int objx = Integer.parseInt(college.get("x"));
            int objy = Integer.parseInt(college.get("y"));
            String name = college.get("name");
            String key = college.get("ukey");

            if (key.equals("college-goodricke")) {
                colleges.add(new College(health, damage, name, key, new Vector2(objx, objy), true));
            } else {
                colleges.add(new College(health, damage, name, key, new Vector2(objx, objy), false));
            }
        }

        for (Element obstacle : xmlObstacles) {
            int objX = Integer.parseInt(obstacle.get("x"));
            int objY = Integer.parseInt(obstacle.get("y"));
            String type = obstacle.get("type");
            obstacles.add(new Obstacle(new Vector2(objX, objY), type));
        }

        for (Element powerup : xmlPowerups) {
            int objX = Integer.parseInt(powerup.get("x"));
            int objY = Integer.parseInt(powerup.get("y"));
            String type = powerup.get("type");
            powerups.add(new Powerups(new Vector2(objX, objY), type));
        }
    }
    
    public void write(FileHandle layoutFile, Set<NPC> npcs, Set<College> colleges, List<Objective> objectives,Player player) {
        try {
        StringWriter write = new StringWriter();
        
        XmlWriter xmlWriter = new XmlWriter(write);
        
        xmlWriter.element("entity");
        xmlWriter.element("colleges");
        for (College college : colleges) {
            xmlWriter.element("college");
            xmlWriter.attribute("x", college.getBounds().getX())
                    .attribute("y", college.getBounds().getY())
                    .attribute("health", college.getHealth())
                    .attribute("damage", college.getDamage())
                    .attribute("name", college.getName())
                    .attribute("ukey", college.getUkey());
            xmlWriter.pop();
        }
        xmlWriter.element("npcs");
        for (NPC npc : npcs) {
            xmlWriter.element("npc");
            xmlWriter.attribute("x", npc.getBounds().getX())
                    .attribute("y", npc.getBounds().getY())
                    .attribute("health", npc.getHealth())
                    .attribute("rotation", npc.getRotation());
            xmlWriter.pop();
        }

        for (Objective objective : objectives) {
                xmlWriter.element("objective");
                xmlWriter.attribute("x", objective.getLocation().x)
                        .attribute("y", objective.getLocation().y)
                        .attribute("xp", objective.getXp())
                        .attribute("rotation", objective.getAmount())
                        .attribute("name", objective.getName())
                        .attribute("ukey", objective.getuKey());
                xmlWriter.pop();

        }

        xmlWriter.element("player");
        xmlWriter.attribute("x", player.getPosition().x)
                .attribute("y", player.getPosition().y)
                .attribute("damage", player.getDamage())
                .attribute("gold", player.getGold())
                .attribute("xp", player.getLevelNXP())
                .attribute("score", player.getScore());
        xmlWriter.close();


    }
       catch (IOException e)
       {
            e.printStackTrace();
       }
    }
    public Set<College> getColleges() {
        return colleges;
    }

    public Set<NPC> getNpcs() {
        return npcs;
    }

    public List<Objective> getObjectives() {
        return objectives;
    }

    public Set<Obstacle> getObstacles() {
        return obstacles;
    }
    public Set<Powerups> getPowerups(){
        return powerups;
    }

}
