package org.nullpointerid.spaceago.screen.menu

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
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
import org.nullpointerid.spaceago.utils.*

class MenuScreen(private val game: SpaceShooter) : Screen {

    companion object {

        private const val BUTTON_WIDTH = 200f
        private const val CENTER = (GameConfig.HUD_WIDTH - BUTTON_WIDTH) / 2f  // center of the screen
    }

    private lateinit var batch: SpriteBatch
//    private lateinit var movingBackground: MovingBackground

    private val camera = OrthographicCamera()
    private val renderer = ShapeRenderer()
    private val layout = GlyphLayout()
    private val viewport = FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, camera)

    // save rectangle coordinates.
    private val singlePlayerRect = Rectangle(350f, 550f, 300f, 100f)
    private val multiPlayerRect = Rectangle(350f, 400f, 300f, 100f)
    private val upgradesRect = Rectangle(380f, 250f, 240f, 100f)
    private val exitRect = Rectangle(450f, 100f, 100f, 100f)

    private var changeScreen = false

    // assets variables.
    private val menuAtlas = game.assetManager[AssetDescriptors.MAIN_MENU_ATLAS]
    private val background = menuAtlas[RegionNames.MENU_BACKGROUND]
    private val menuFont = BitmapFont("fonts/MenuFont.fnt".toInternalFile())
    private val menuFontYellow = BitmapFont("fonts/MenuFontYellow.fnt".toInternalFile())
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
            game.movingBackground.updateRender(delta, batch)

            // Draw logo
            layout.setText(haloFont, "SpaceAgo")
            haloFont.draw(batch, layout, 20f, GameConfig.HUD_HEIGHT - layout.height)

            // Draw buttons
            // Fix coordinates of the mouse using Vector and unproject and save them to Vector.
            val mouseVector = Vector3().set(camera.unproject(Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)))


            // play button
            if (inRectangle(singlePlayerRect, mouseVector.x, mouseVector.y)) {
                layout.setText(menuFontYellow, "Singleplayer")
                menuFontYellow.draw(batch, layout, CENTER - 45f, 600f + layout.height / 2f)  // draw active button if mouse is inside button hitbox.
                if (Gdx.input.justTouched()) {  // if play button is pressed - set change screen variable to true.
                    changeScreen = true
                }
            } else {  // draw inactive button.
                layout.setText(menuFont, "Singleplayer")
                menuFont.draw(batch, layout, CENTER - 45f, 600f + layout.height / 2f)
            }

            // Multiplayer button
            if (inRectangle(multiPlayerRect, mouseVector.x, mouseVector.y)) {
                layout.setText(menuFontYellow, "Multiplayer")
                menuFontYellow.draw(batch, layout, CENTER - 20f, 450f + layout.height / 2f)
            } else {
                layout.setText(menuFont, "Multiplayer")
                menuFont.draw(batch, layout, CENTER - 20f, 450f + layout.height / 2f)
            }

            // Upgrade button
            if (inRectangle(upgradesRect, mouseVector.x, mouseVector.y)) {
                layout.setText(menuFontYellow, "Upgrades")
                menuFontYellow.draw(batch, layout, CENTER - 10f, 300f + layout.height / 2f)
            } else {
                layout.setText(menuFont, "Upgrades")
                menuFont.draw(batch, layout, CENTER - 10f, 300f + layout.height / 2f)
            }

            // Exit button
            if (inRectangle(exitRect, mouseVector.x, mouseVector.y)) {
                layout.setText(menuFontYellow, "Exit")
                menuFontYellow.draw(batch, layout, CENTER + layout.width - 15f, 150f + layout.height / 2f)  // draw active button if mouse is inside button hitbox.
                if (Gdx.input.justTouched()) {  // if exit button is pressed - exit the app.
                    Gdx.app.exit()
                }
            } else { // draw inactive button.
                layout.setText(menuFont, "Exit")
                menuFont.draw(batch, layout, CENTER + layout.width - 15f, 150f + layout.height / 2f)
            }
        }
    }

    private fun renderDebug() {
        viewport.apply()
        renderer.projectionMatrix = camera.combined

        renderer.use {
            val oldColor = renderer.color.cpy()
            renderer.color = Color.RED

            // Buttons hitboxes.
            renderer.rect(singlePlayerRect.x, singlePlayerRect.y, singlePlayerRect.width, singlePlayerRect.height)
            renderer.rect(multiPlayerRect.x, multiPlayerRect.y, multiPlayerRect.width, multiPlayerRect.height)
            renderer.rect(upgradesRect.x, upgradesRect.y, upgradesRect.width, upgradesRect.height)
            renderer.rect(exitRect.x, exitRect.y, exitRect.width, exitRect.height)

            renderer.color = oldColor
        }
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun dispose() {
        batch.dispose()
        renderer.dispose()
        haloFont.dispose()
        menuFont.dispose()
        menuFontYellow.dispose()
    }

    override fun hide() {
        // WARNING: Screens are not disposed automatically!
        // without dispose() call screen will be not disposed.
        dispose()
    }

    override fun pause() {}
    override fun resume() {}

}