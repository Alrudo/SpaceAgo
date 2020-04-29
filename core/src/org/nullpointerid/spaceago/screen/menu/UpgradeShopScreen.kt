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
import org.nullpointerid.spaceago.utils.*

class UpgradeShopScreen(private val game: SpaceShooter) : Screen {

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

    private val exitRect = Rectangle(420f, 100f, 155f, 100f)

    private var changeScreen = false

    private val batch = SpriteBatch()
    private val renderer = ShapeRenderer()
    private val layout = GlyphLayout()
    private val camera = OrthographicCamera()
    private val viewport = FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, camera)

    override fun render(delta: Float) {

        clearScreen()

        renderShop()

        if (GameConfig.DEBUG_MODE) {
            viewport.drawGrid(renderer, 100f)
            renderDebug()
        }

        if (changeScreen) game.screen = MenuScreen(game)

    }

    private fun renderShop() {
        viewport.apply()
        batch.projectionMatrix = camera.combined

        batch.use {
            // draw bg
            batch.draw(background, 0f, 0f)

            // Draw logo
            layout.setText(haloFont, "SpaceAgo")
            haloFont.draw(batch, layout, 20f, GameConfig.HUD_HEIGHT - layout.height)

            // Fix coordinates of the mouse using Vector and unproject and save them to Vector.
            val mouseVector = Vector3().set(camera.unproject(Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)))

            if (inRectangle(exitRect, mouseVector.x, mouseVector.y)) {
                layout.setText(menuFontYellow, "Menu")
                // draw active button if mouse is inside button hitbox.
                menuFontYellow.draw(batch, layout, MenuScreen.CENTER + layout.width / 2f - 20f, 150f + layout.height / 2f)
                if (Gdx.input.justTouched()) {  // if exit button is pressed - exit the app.
                    changeScreen = true
                }
            } else { // draw inactive button.
                layout.setText(menuFont, "Menu")
                menuFont.draw(batch, layout, MenuScreen.CENTER + layout.width / 2f - 20f, 150f + layout.height / 2f)
            }
        }
    }

    private fun renderDebug() {
        viewport.apply()
        renderer.projectionMatrix = camera.combined
        val oldColor = renderer.color.cpy()

        renderer.use {
            renderer.color = Color.RED
            renderer.rect(exitRect.x, exitRect.y, exitRect.width, exitRect.height)
        }

        renderer.color = oldColor
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun dispose() {
        renderer.dispose()
        batch.dispose()
        haloFont.dispose()
        menuFont.dispose()
        menuFontYellow.dispose()
    }

    override fun hide() {
        dispose()
    }

    override fun show() {}
    override fun pause() {}
    override fun resume() {}
}