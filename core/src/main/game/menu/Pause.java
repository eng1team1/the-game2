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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import main.game.MainRunner;
import main.game.core.XMLLoader;
import main.game.world.content.College;
import main.game.world.content.NPC;
import main.game.world.player.Objectives.Objective;
import main.game.world.player.Player;

import java.util.List;
import java.util.Set;

public class Pause {
    private Stage stage;
    private BitmapFont font;
    private LabelStyle basicStyle;
    private Label enter, exit, gold;
    private Texture logoTexture;
    private Sprite logo;
    private TextButton option1;
    private Skin skins;
    private TextureAtlas textureAtlas;
    public Pause(Set<NPC> npcs, Set<College> colleges, Player player ) {
        logoTexture = new Texture(Gdx.files.internal("icons/icon128.png"));
        logo = new Sprite(logoTexture);
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        font = new BitmapFont();
        basicStyle = new LabelStyle(font, Color.BLACK);
        enter = new Label("Game is Paused", basicStyle);
        exit = new Label("PRESS ESCAPE TO EXIT", basicStyle);

        enter.setFontScale(2f);
        enter.setPosition(Gdx.graphics.getWidth() / 2 - enter.getWidth() / 2, 100);
        enter.setAlignment(Align.center);
        exit.setPosition(Gdx.graphics.getWidth() / 2 - exit.getWidth() / 2, 70);
        logo.setScale(2f);
        logo.setPosition(Gdx.graphics.getWidth() / 2 - logo.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        initButtons( npcs, colleges, player);
        stage.addActor(enter);
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
            MainRunner.Is_Pause = false;
            MainRunner.inPause = false;
        }

    }
    public void pauseCycle()
    {
        update();
        render();
    }
    public void dispose() {
        stage.dispose();
        font.dispose();
        logoTexture.dispose();
    }
    private void initButtons(final Set<NPC> npcs, final Set<College> colleges, final Player player) {
        textureAtlas = new TextureAtlas(Gdx.files.internal("ui/uiskin.atlas"));
        this.skins = new Skin();
        this.skins.addRegions(textureAtlas);
        font = new BitmapFont();
        this.skins.load(Gdx.files.internal("ui/uiskin.json"));
        option1 = new TextButton("Save and Exit", skins, "default");
        option1.setPosition(Gdx.graphics.getWidth() / 2 - 140, 200);
        option1.setSize(280, 60);
        option1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                List<Objective> object = player.getObjective();
                XMLLoader write = new XMLLoader(Gdx.files.internal("xmls/save.xml"), npcs, colleges, object);

                write.write(Gdx.files.internal("xmls/save.xml"), npcs, colleges, object, player);

            }
        });
        stage.addActor(option1);
    }
}
