package org.nullpointerid.spaceago.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter
import org.nullpointerid.spaceago.SpaceShooter
import org.nullpointerid.spaceago.tools.MovingBackground

class MainMenuScreen(private val game: SpaceShooter) : Screen {
    companion object {
        private const val EXIT_BUTTON_WIDTH = 150
        private const val EXIT_BUTTON_Height = 50
        private const val PLAY_BUTTON_WIDTH = 150
        private const val PLAY_BUTTON_Height = 50
        private const val EXIT_BUTTON_Y = 250
        private const val PLAY_BUTTON_Y = 325
    }

    private val background: Texture
    private val playActive: Texture
    private val playInActive: Texture
    private val exitActive: Texture
    private val exitInActive: Texture
    private val fontGenerator: FreeTypeFontGenerator
    private val fontParameter: FreeTypeFontParameter
    private val font: BitmapFont

    override fun show() {}
    override fun render(delta: Float) {
        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        game.batch.begin()
        game.batch.draw(background, 0f, 0f)
        game.movingBackground.updateRender(delta, game.batch)
        val xExit: Int = SpaceShooter.WIDTH / 2 - EXIT_BUTTON_WIDTH / 2 //X starting point
        val xPlay: Int = SpaceShooter.WIDTH / 2 - PLAY_BUTTON_WIDTH / 2
        //Menu icon
        font.draw(game.batch, "SpaceAgo", 0f, SpaceShooter.HEIGHT - 50.toFloat())
        //Start button
        if (Gdx.input.x < xPlay + PLAY_BUTTON_WIDTH && Gdx.input.x > xPlay && SpaceShooter.HEIGHT - Gdx.input.y < PLAY_BUTTON_Y + PLAY_BUTTON_Height && SpaceShooter.HEIGHT - Gdx.input.y > PLAY_BUTTON_Y) {
            game.batch.draw(playActive, SpaceShooter.WIDTH / 2 - PLAY_BUTTON_WIDTH / 2.toFloat(), PLAY_BUTTON_Y.toFloat(), PLAY_BUTTON_WIDTH.toFloat(), PLAY_BUTTON_Height.toFloat())
            if (Gdx.input.isTouched) {
                dispose()
                game.screen = MainGameScreen(game)
            }
        } else {
            game.batch.draw(playInActive, SpaceShooter.WIDTH / 2 - PLAY_BUTTON_WIDTH / 2.toFloat(), PLAY_BUTTON_Y.toFloat(), PLAY_BUTTON_WIDTH.toFloat(), PLAY_BUTTON_Height.toFloat())
        }
        //Exit button
        if (Gdx.input.x < xExit + EXIT_BUTTON_WIDTH && Gdx.input.x > xExit && SpaceShooter.HEIGHT - Gdx.input.y < EXIT_BUTTON_Y + EXIT_BUTTON_Height && SpaceShooter.HEIGHT - Gdx.input.y > EXIT_BUTTON_Y) {
            game.batch.draw(exitActive, SpaceShooter.WIDTH / 2 - EXIT_BUTTON_WIDTH / 2.toFloat(), EXIT_BUTTON_Y.toFloat(), EXIT_BUTTON_WIDTH.toFloat(), EXIT_BUTTON_Height.toFloat())
            if (Gdx.input.isTouched) {
                Gdx.app.exit()
            }
        } else {
            game.batch.draw(exitInActive, SpaceShooter.WIDTH / 2 - EXIT_BUTTON_WIDTH / 2.toFloat(), EXIT_BUTTON_Y.toFloat(), EXIT_BUTTON_WIDTH.toFloat(), EXIT_BUTTON_Height.toFloat())
        }
        game.batch.end()
    }

    override fun resize(width: Int, height: Int) {}
    override fun pause() {}
    override fun resume() {}
    override fun hide() {}
    override fun dispose() {}

    init {
        Gdx.input.isCursorCatched = false
        background = Texture("images/menuBackg.jfif")
        playActive = Texture("images/play_button_active.png")
        playInActive = Texture("images/play_button_inactive.png")
        exitActive = Texture("images/exit_button_active.png")
        exitInActive = Texture("images/exit_button_inactive.png")
        fontGenerator = FreeTypeFontGenerator(Gdx.files.internal("fonts/Halo3.ttf"))
        fontParameter = FreeTypeFontParameter()
        fontParameter.size = 55
        fontParameter.borderWidth = 10f
        fontParameter.borderColor = Color.BLACK
        fontParameter.color = Color.LIGHT_GRAY
        font = fontGenerator.generateFont(fontParameter)
        game.movingBackground.setFixedSpeed(true)
        game.movingBackground.setSpeed(MovingBackground.DEFAULT_SPEED)
    }
}