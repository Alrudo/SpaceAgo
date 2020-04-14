package org.nullpointerid.spaceago.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter
import com.badlogic.gdx.utils.Align
import org.nullpointerid.spaceago.SpaceShooterOld
import org.nullpointerid.spaceago.tools.MovingBackgroundOld
import org.nullpointerid.spaceago.utils.clearScreen
import org.nullpointerid.spaceago.utils.toInternalFile

class GameOverScreenOld(private val game: SpaceShooterOld, private val score: Int) : Screen {
    companion object {
        private const val BANNER_WIDTH = 350
        private const val BANNER_HEIGHT = 100
    }

    private val highscore: Int
    private val gameOverBanner: Texture
    private val scoreFont: BitmapFont

    private val font = FreeTypeFontGenerator("fonts/Halo3.ttf".toInternalFile())
            .generateFont(FreeTypeFontParameter().apply {
                size = 55
                borderWidth = 10f
                borderColor = Color.BLACK
                color = Color.WHITE
            })

    private val font2 = FreeTypeFontGenerator("fonts/game_over.ttf".toInternalFile())
            .generateFont(FreeTypeFontParameter().apply {
                size = 70
                borderWidth = 10f
                borderColor = Color.BLACK
                color = Color.WHITE
            })

    init {
        Gdx.input.isCursorCatched = false
        //Get and save score
        val prefs = Gdx.app.getPreferences("spaceshooter")
        highscore = prefs.getInteger("highscore", 0)
        //Check if score beats highscore
        if (score > highscore) {
            prefs.putInteger("highscore", score)
            prefs.flush()
        }
        //Textures and fonts
        gameOverBanner = Texture("images/game_over.png".toInternalFile())
        scoreFont = BitmapFont("fonts/score.fnt".toInternalFile())
        game.movingBackgroundOld.setFixedSpeed(true)
        game.movingBackgroundOld.setSpeed(MovingBackgroundOld.DEFAULT_SPEED)
    }

    override fun show() {}
    override fun render(delta: Float) {
        clearScreen()
        game.batch.begin()
        game.movingBackgroundOld.updateRender(delta, game.batch)
        /*
        game.batch.draw(gameOverBanner, SpaceShooter.WIDTH / 2 - BANNER_WIDTH / 2, SpaceShooter.HEIGHT - BANNER_HEIGHT - 15, BANNER_WIDTH, BANNER_HEIGHT);
        GlyphLayout scoreLayout = new GlyphLayout(scoreFont, "Score: \n" + score, Color.WHITE, 0, Align.left, false);
        GlyphLayout highscoreLayout = new GlyphLayout(scoreFont, "Highscore: \n" + highscore, Color.WHITE, 0, Align.left, false);
        scoreFont.draw(game.batch, scoreLayout, SpaceShooter.WIDTH / 2 - scoreLayout.width / 2, SpaceShooter.HEIGHT - BANNER_HEIGHT - 15 * 2);
        scoreFont.draw(game.batch, highscoreLayout, SpaceShooter.WIDTH / 2 - highscoreLayout.width / 2, SpaceShooter.HEIGHT - BANNER_HEIGHT - scoreLayout.height - 15 * 3);
         */
        val tryAgainLayout = GlyphLayout(scoreFont, "Try Again")
        val mainMenuLayout = GlyphLayout(scoreFont, "Main Menu")
        font.draw(game.batch, "Game over", 75f, SpaceShooterOld.HEIGHT - 60.toFloat())
        font2.draw(game.batch, "Score: \n$score", 75f, SpaceShooterOld.HEIGHT - 150.toFloat())
        font2.draw(game.batch, "Highscore: \n$highscore", 75f, SpaceShooterOld.HEIGHT - 250.toFloat())
        val tryAgainX: Float = SpaceShooterOld.WIDTH / 2 - tryAgainLayout.width / 2
        val tryAgainY: Float = SpaceShooterOld.HEIGHT / 2 - tryAgainLayout.height / 2
        val mainMenuX: Float = SpaceShooterOld.WIDTH / 2 - mainMenuLayout.width / 2
        val mainMenuY: Float = SpaceShooterOld.HEIGHT / 2 - mainMenuLayout.height / 2 - tryAgainLayout.height - 15
        val touchX = Gdx.input.x.toFloat()
        val touchY = Gdx.graphics.height - Gdx.input.y.toFloat()
        //Checks if hovering over try again button
        if (touchX >= tryAgainX && touchX < tryAgainX + tryAgainLayout.width && touchY >= tryAgainY - tryAgainLayout.height && touchY < tryAgainY) tryAgainLayout.setText(scoreFont, "Try Again", Color.YELLOW, 0f, Align.left, false)
        //Checks if hovering over main menu button
        if (touchX >= mainMenuX && touchX < mainMenuX + mainMenuLayout.width && touchY >= mainMenuY - mainMenuLayout.height && touchY < mainMenuY) mainMenuLayout.setText(scoreFont, "Main Menu", Color.YELLOW, 0f, Align.left, false)
        //If try again and main menu is being pressed
        if (Gdx.input.justTouched()) { //Try again
            if (touchX > tryAgainX && touchX < tryAgainX + tryAgainLayout.width && touchY > tryAgainY - tryAgainLayout.height && touchY < tryAgainY) {
                dispose()
                game.batch.end()
                game.screen = MainGameScreenOld(game)
                return
            }
            //main menu
            if (touchX > mainMenuX && touchX < mainMenuX + mainMenuLayout.width && touchY > mainMenuY - mainMenuLayout.height && touchY < mainMenuY) {
                dispose()
                game.batch.end()
                game.screen = MainMenuScreenOld(game)
                return
            }
        }
        //Draw buttons
        scoreFont.draw(game.batch, tryAgainLayout, tryAgainX, tryAgainY)
        scoreFont.draw(game.batch, mainMenuLayout, mainMenuX, mainMenuY)
        game.batch.end()
    }

    override fun resize(width: Int, height: Int) {}
    override fun pause() {}
    override fun resume() {}
    override fun hide() {}
    override fun dispose() {}
}
