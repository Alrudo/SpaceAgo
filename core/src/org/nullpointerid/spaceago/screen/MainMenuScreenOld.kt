package org.nullpointerid.spaceago.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter
import org.nullpointerid.spaceago.SpaceShooterOld
import org.nullpointerid.spaceago.tools.MovingBackground
import org.nullpointerid.spaceago.utils.clearScreen

class MainMenuScreenOld(private val game: SpaceShooterOld) : Screen {
    companion object {
        private const val EXIT_BUTTON_WIDTH = 150
        private const val EXIT_BUTTON_Height = 50
        private const val PLAY_BUTTON_WIDTH = 150
        private const val PLAY_BUTTON_Height = 50
        private const val EXIT_BUTTON_Y = 250
        private const val PLAY_BUTTON_Y = 325
    }

    private val background = Texture("images/menuBackg.jfif")
    private val playActive = Texture("images/play_button_active.png")
    private val playInActive = Texture("images/play_button_inactive.png")
    private val exitActive = Texture("images/exit_button_active.png")
    private val exitInActive = Texture("images/exit_button_inactive.png")
    private val font = FreeTypeFontGenerator(Gdx.files.internal("fonts/Halo3.ttf"))
            .generateFont(FreeTypeFontParameter().apply {
                size = 55
                borderWidth = 10f
                borderColor = Color.BLACK
                color = Color.LIGHT_GRAY
            })

    init {
        Gdx.input.isCursorCatched = false
        game.movingBackground.setFixedSpeed(true)
        game.movingBackground.setSpeed(MovingBackground.DEFAULT_SPEED)
    }

    override fun show() {}
    override fun render(delta: Float) {
        clearScreen(1, 0, 0, 1)
        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        game.batch.begin()
        game.batch.draw(background, 0f, 0f)
        game.movingBackground.updateRender(delta, game.batch)
        val xExit: Int = SpaceShooterOld.WIDTH / 2 - EXIT_BUTTON_WIDTH / 2 //X starting point
        val xPlay: Int = SpaceShooterOld.WIDTH / 2 - PLAY_BUTTON_WIDTH / 2
        //Menu icon
        font.draw(game.batch, "SpaceAgo", 0f, SpaceShooterOld.HEIGHT - 50.toFloat())
        //Start button
        if (Gdx.input.x < xPlay + PLAY_BUTTON_WIDTH && Gdx.input.x > xPlay && SpaceShooterOld.HEIGHT - Gdx.input.y < PLAY_BUTTON_Y + PLAY_BUTTON_Height && SpaceShooterOld.HEIGHT - Gdx.input.y > PLAY_BUTTON_Y) {
            game.batch.draw(playActive, SpaceShooterOld.WIDTH / 2 - PLAY_BUTTON_WIDTH / 2.toFloat(), PLAY_BUTTON_Y.toFloat(), PLAY_BUTTON_WIDTH.toFloat(), PLAY_BUTTON_Height.toFloat())
            if (Gdx.input.isTouched) {
                dispose()
                game.screen = MainGameScreenOld(game)
            }
        } else {
            game.batch.draw(playInActive, SpaceShooterOld.WIDTH / 2 - PLAY_BUTTON_WIDTH / 2.toFloat(), PLAY_BUTTON_Y.toFloat(), PLAY_BUTTON_WIDTH.toFloat(), PLAY_BUTTON_Height.toFloat())
        }
        //Exit button
        if (Gdx.input.x < xExit + EXIT_BUTTON_WIDTH && Gdx.input.x > xExit && SpaceShooterOld.HEIGHT - Gdx.input.y < EXIT_BUTTON_Y + EXIT_BUTTON_Height && SpaceShooterOld.HEIGHT - Gdx.input.y > EXIT_BUTTON_Y) {
            game.batch.draw(exitActive, SpaceShooterOld.WIDTH / 2 - EXIT_BUTTON_WIDTH / 2.toFloat(), EXIT_BUTTON_Y.toFloat(), EXIT_BUTTON_WIDTH.toFloat(), EXIT_BUTTON_Height.toFloat())
            if (Gdx.input.isTouched) {
                Gdx.app.exit()
            }
        } else {
            game.batch.draw(exitInActive, SpaceShooterOld.WIDTH / 2 - EXIT_BUTTON_WIDTH / 2.toFloat(), EXIT_BUTTON_Y.toFloat(), EXIT_BUTTON_WIDTH.toFloat(), EXIT_BUTTON_Height.toFloat())
        }
        game.batch.end()
    }

    override fun resize(width: Int, height: Int) {}
    override fun pause() {}
    override fun resume() {}
    override fun hide() {}
    override fun dispose() {}
}
