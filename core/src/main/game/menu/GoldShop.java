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

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
public class GoldShop {
    private Stage stage;
    private BitmapFont font;
    private LabelStyle basicStyle;
    private Label title, exit;
    private TextButton option1, option2, option3;
    private Texture logoTexture;
    private Sprite logo;
    private Skin skins;
    private TextureAtlas textureAtlas;
    private int cost1, cost2, cost3;
    private int gold;
    Player player;
    public GoldShop(Player player) {

        logoTexture = new Texture(Gdx.files.internal("icons/icon128.png"));
        logo = new Sprite(logoTexture);
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        font = new BitmapFont();
        basicStyle = new LabelStyle(font, Color.BLACK);
        title = new Label("Gold!", basicStyle);
        gold =player.getGold();
        player = this.player;
        cost1 = player.getMaxHealth() - player.getHealth();
        exit = new Label("PRESS ESCAPE TO EXIT", basicStyle);

        title.setFontScale(2f);
        title.setPosition(Gdx.graphics.getWidth() / 2 - title.getWidth() / 2, 650);
        title.setAlignment(Align.center);
        exit.setPosition(Gdx.graphics.getWidth() / 2 - exit.getWidth() / 2, 70);
        logo.setScale(0.5f);
        logo.setPosition(Gdx.graphics.getWidth() / 2 - logo.getWidth() / 2, Gdx.graphics.getHeight() -logo.getWidth() -50 );

        initButtons(gold);
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
        if (Gdx.input.isKeyJustPressed(Keys.P)) {
            MainRunner.Is_Gold = false;
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
        option1.addListener(new ClickListener(gold){
            @Override
            public void clicked(InputEvent event, float x, float y){
                System.out.println("clicked");
                if(player.getHealth() == player.getMaxHealth())
                {
                    System.out.println("yer ship is in top shape already");
                }
                else if (gold >= cost1)
                {
                    gold =- cost1;
                    player.decreaseGold(cost1);
                    player.heal(player.getMaxHealth()-player.getHealth());
                    cost1 = player.getMaxHealth() - player.getHealth();
                    option1.setText("Patch ye Vessel:" + cost1);
                }            
                
            }
        });
        option2 = new TextButton("Upgrade ye Cannons", skins, "default");
        option2.setPosition(Gdx.graphics.getWidth()/2 - 140,300);
        option2.setSize(280, 60);
        option2.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                System.out.println("clicked");
            }
        });
        option3 = new TextButton("Get bigger Sails", skins, "default");
        option3.setPosition(Gdx.graphics.getWidth()/2 - 140,200);
        option3.setSize(280, 60);

        option3.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                System.out.println("clicked");
            }
        });

        stage.addActor(option1);
        stage.addActor(option2);
        stage.addActor(option3);
    }


    }

