package main.game.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import main.game.MainRunner;
import main.game.world.player.Player;
import main.game.world.player.Stats.Gold;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
public class GoldShop {
    private Stage stage;
    private BitmapFont font;
    private LabelStyle basicStyle;
    private Label title, exit, goldsign;
    private TextButton option1, option2, option3;
    private Texture logoTexture;
    private Sprite logo;
    private Skin skins;
    private TextureAtlas textureAtlas;
    private int cost1, cost2, cost3;
    private int gold;
    private Player player;
    public GoldShop(Player player) {

        logoTexture = new Texture(Gdx.files.internal("icons/icon128.png"));
        logo = new Sprite(logoTexture);
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        font = new BitmapFont();
        basicStyle = new LabelStyle(font, Color.BLACK);
        title = new Label("Gold!", basicStyle);
        gold =player.getGold();
        this.player = player;
        goldsign = new Label("You have: " + gold, basicStyle);
        cost1 = player.getMaxHealth() - player.getHealth();
        cost2 = 400 + player.getBoughtDamage()*4;
        cost3 = 200 + (int) player.getExtraSpeed()*4;
        exit = new Label("PRESS ESCAPE TO EXIT", basicStyle);
        goldsign.setFontScale(2f);
        goldsign.setPosition(Gdx.graphics.getWidth() / 2 - goldsign.getWidth() / 2, 500);
        goldsign.setAlignment(Align.center);
        title.setFontScale(2f);
        title.setPosition(Gdx.graphics.getWidth() / 2 - title.getWidth() / 2, 650);
        title.setAlignment(Align.center);
        exit.setPosition(Gdx.graphics.getWidth() / 2 - exit.getWidth() / 2, 70);
        logo.setScale(0.5f);
        logo.setPosition(Gdx.graphics.getWidth() / 2 - logo.getWidth() / 2, Gdx.graphics.getHeight() -logo.getWidth() -50 );

        initButtons(gold);
        stage.addActor(goldsign);
        stage.addActor(title);
        stage.addActor(exit);

    }
    private void render() {
        Gdx.gl.glClearColor(0.3f, 0.6f, 0.8f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
        stage.getBatch().begin();
        logo.draw(stage.getBatch());

        stage.getBatch().end();
    }
    private void update()
    {
        float delta = Gdx.graphics.getDeltaTime();
        stage.act(delta);
        if (Gdx.input.isKeyJustPressed(Keys.G)) {
            MainRunner.Is_Gold = false;
            MainRunner.inGold = false;            
        }
    }
    public void goldCycle()
    {
        update();
        render();
    }
    public void dispose() {
        stage.dispose();
        font.dispose();
        logoTexture.dispose();
    }
    private void initButtons(int Gold)
    {
        textureAtlas = new TextureAtlas(Gdx.files.internal("ui/uiskin.atlas") );
        this.skins = new Skin();
        this.skins.addRegions(textureAtlas);
        font = new BitmapFont();
        this.skins.load(Gdx.files.internal("ui/uiskin.json"));
        option1 = new TextButton("Patch ye Vessel:" + cost1, skins, "default");
        option1.setPosition(Gdx.graphics.getWidth()/2 - 140,400);
        option1.setSize(280, 60);
        option1.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                System.out.println("clicked");
                if(player.getHealth() == player.getMaxHealth())
                {
                    System.out.println("yer ship is in top shape already");
                }
                else if (gold >= cost1)
                {
                    gold = gold - cost1;
                    System.out.println(gold);
                    player.decreaseGold(cost1);
                    goldsign.setText("You have: " + gold);
                    player.heal(player.getMaxHealth() - player.getHealth());
                    cost1 = player.getMaxHealth() - player.getHealth();
                    option1.setText("Patch ye Vessel:" + cost1);
                    
                }            
                
            }
        });
        option2 = new TextButton("Upgrade ye Cannons: "+ cost2, skins, "default");
        option2.setPosition(Gdx.graphics.getWidth()/2 - 140,300);
        option2.setSize(280, 60);
        option2.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if(gold >= cost2)
                {
                    gold =gold - cost2;
                    player.decreaseGold(cost2);
                    goldsign.setText("You have: " + gold);
                    player.increaseBoughtDamage(50);
                    cost2 = 400 + player.getBoughtDamage() *4;
                    option2.setText("Upgrade ye Cannons: "+ cost2);
                   
                }
            }
        });
        option3 = new TextButton("Get bigger Sails: "+ cost3, skins, "default");
        option3.setPosition(Gdx.graphics.getWidth()/2 - 140,200);
        option3.setSize(280, 60);

        option3.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if(gold >= cost3)
                {
                    gold = gold - cost3;
                    player.decreaseGold(cost3);
                    goldsign.setText("You have: " + gold);
                    player.increaseExtraSpeed(50);
                    cost3 = 200 + (int)player.getExtraSpeed()*4;
                    option3.setText("Get bigger Sails: "+ cost3);
                    
                }
            }
        });

        stage.addActor(option1);
        stage.addActor(option2);
        stage.addActor(option3);
    }


    }

