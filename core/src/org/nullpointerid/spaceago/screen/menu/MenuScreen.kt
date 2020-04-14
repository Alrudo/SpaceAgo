package org.nullpointerid.spaceago.screen.menu

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.viewport.FitViewport
import org.nullpointerid.spaceago.SpaceShooter
import org.nullpointerid.spaceago.assets.AssetDescriptors
import org.nullpointerid.spaceago.assets.RegionNames
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.screen.game.GameScreen
import org.nullpointerid.spaceago.tools.MovingBackground
import org.nullpointerid.spaceago.utils.*

class MenuScreen(private val game: SpaceShooter) : Screen {

    companion object {

        private const val BUTTON_WIDTH = 200f
        private const val BUTTON_HEIGHT = 100f
        private const val CENTER = (GameConfig.HUD_WIDTH - BUTTON_WIDTH) / 2f  // center of the screen
    }

    private lateinit var batch: SpriteBatch
//    private lateinit var movingBackground: MovingBackground

    private val camera = OrthographicCamera()
    private val renderer = ShapeRenderer()
    private val layout = GlyphLayout()
    private val viewport = FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, camera)

    // save rectangle coordinates.
    private val playRect = Rectangle(CENTER, 400f, BUTTON_WIDTH, BUTTON_HEIGHT)
    private val exitRect = Rectangle(CENTER, 200f, BUTTON_WIDTH, BUTTON_HEIGHT)

    private var changeScreen = false

    // assets variables.
    private val menuAtlas = game.assetManager[AssetDescriptors.MAIN_MENU_ATLAS]
    private val movingBackground = MovingBackground()
    private val exitButtonActive = menuAtlas[RegionNames.MENU_QUIT_BUTTON_ACTIVE]
    private val exitButtonIdle = menuAtlas[RegionNames.MENU_QUIT_BUTTON_IDLE]
    private val playButtonActive = menuAtlas[RegionNames.MENU_PLAY_BUTTON_CLICKED]
    private val playButtonIdle = menuAtlas[RegionNames.MENU_PLAY_BUTTON_IDLE]
    private val background = menuAtlas[RegionNames.MENU_BACKGROUND]
    private val haloFont = FreeTypeFontGenerator("fonts/Halo3.ttf".toInternalFile()).generateFont(
            FreeTypeFontGenerator.FreeTypeFontParameter().apply {
                size = 75
                borderWidth = 5f
                borderColor = Color.BLACK
                color = Color.GRAY
            })


    override fun show() {
        batch = SpriteBatch()
    }

    override fun render(delta: Float) {
        clearScreen()

        renderMenu(delta)

        if (GameConfig.DEBUG_MODE) {  // draw grid and other debug lines if set to true
            viewport.drawGrid(renderer, 100f)
            renderDebug()
        }

        if (changeScreen) game.screen = GameScreen(game)
    }

    private fun renderMenu(delta: Float) {
        viewport.apply()
        batch.projectionMatrix = camera.combined

        batch.use {
            // Draw background
            batch.draw(background, 0f, 0f)
            movingBackground.updateRender(delta, batch)

            // Draw logo
            layout.setText(haloFont, "SpaceAgo")
            haloFont.draw(batch, layout, 20f, GameConfig.HUD_HEIGHT - layout.height)

            // Draw buttons
            // Fix coordinates of the mouse using Vector and unproject and save them to Vector.
            val mouseVector = Vector3().set(camera.unproject(Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)))

            if (inRectangle(playRect, mouseVector.x, mouseVector.y)) {
                drawButton(playButtonActive, 400f)  // draw active button if mouse is inside button hitbox.
                if (Gdx.input.justTouched()) {  // if play button is pressed - set change screen variable to true.
                    changeScreen = true
                }
            } else drawButton(playButtonIdle, 400f)  // draw inactive button.

            // Exit button
            if (inRectangle(exitRect, mouseVector.x, mouseVector.y)) {
                drawButton(exitButtonActive, 200f)  // draw active button if mouse is inside button hitbox.
                if (Gdx.input.justTouched()) {  // if exit button is pressed - exit the app.
                    Gdx.app.exit()
                }
            } else drawButton(exitButtonIdle, 200f)  // draw inactive button.
        }
    }

    private fun renderDebug() {
        viewport.apply()
        renderer.projectionMatrix = camera.combined

        renderer.use {
            val oldColor = renderer.color.cpy()
            renderer.color = Color.RED

            // Buttons hitboxes.
            renderer.rect(playRect.x, playRect.y, playRect.width, playRect.height)
            renderer.rect(exitRect.x, exitRect.y, exitRect.width, exitRect.height)

            renderer.color = oldColor
        }
    }

    private fun drawButton(skin: TextureRegion?, y: Float) {
        batch.draw(skin, CENTER, y, BUTTON_WIDTH, BUTTON_HEIGHT)
    }

    private fun inRectangle(r: Rectangle, x: Float, y: Float): Boolean {
        return r.x <= x && r.x + r.width >= x && r.y <= y && r.y + r.height >= y
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
        movingBackground.resize(width, height)
}

    override fun dispose() {
        batch.dispose()
        renderer.dispose()
        haloFont.dispose()
    }

    override fun hide() {
        // WARNING: Screens are not disposed automatically!
        // without dispose() call screen will be not disposed.
        dispose()
    }

    override fun pause() {}
    override fun resume() {}

}