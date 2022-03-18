package main.game.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import main.game.MainRunner;

public class Gold {
    private Stage stage;
    private BitmapFont font;
    private LabelStyle basicStyle;
    private Label title, exit, option1, option2, option3;
    private Texture logoTexture;
    private Sprite logo;

    public Gold() {
        logoTexture = new Texture(Gdx.files.internal("icons/icon128.png"));
        logo = new Sprite(logoTexture);
        stage = new Stage(new ScreenViewport());
        font = new BitmapFont();
        basicStyle = new LabelStyle(font, Color.BLACK);
        title = new Label("Gold!", basicStyle);
        option1 = new Label("Increase movement speed", basicStyle);
        option2 = new Label("Increase damage", basicStyle);
        option3 = new Label("Get healing  potion", basicStyle);
        exit = new Label("PRESS ESCAPE TO EXIT", basicStyle);

        title.setFontScale(2f);
        title.setPosition(Gdx.graphics.getWidth() / 2 - title.getWidth() / 2, 650);
        title.setAlignment(Align.center);

        exit.setPosition(Gdx.graphics.getWidth() / 2 - exit.getWidth() / 2, 70);

        logo.setScale(0.5f);
        logo.setPosition(Gdx.graphics.getWidth() / 2 - logo.getWidth() / 2, Gdx.graphics.getHeight() -logo.getWidth() -50 );

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
        if (Gdx.input.isKeyJustPressed(Keys.P)) {
            MainRunner.Is_Gold = false;
        }
    }
    public void goldCycle()
    {
        update();
        render();
    }
}
