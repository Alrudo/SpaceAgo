package org.nullpointerid.spaceago.screen.menu.settings

import com.badlogic.gdx.*
import com.badlogic.gdx.Gdx.input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.utils.viewport.FitViewport
import org.nullpointerid.spaceago.SpaceShooter
import org.nullpointerid.spaceago.assets.AssetDescriptors
import org.nullpointerid.spaceago.assets.RegionNames
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.screen.game.GameController
import org.nullpointerid.spaceago.screen.game.GameScreen
import org.nullpointerid.spaceago.screen.menu.MenuScreen
import org.nullpointerid.spaceago.screen.menu.UpgradeShopScreen
import org.nullpointerid.spaceago.utils.*
import org.w3c.dom.Text
import java.awt.SystemColor
import java.awt.SystemColor.activeCaptionText
import java.awt.SystemColor.text


class ControlsScreen(private val game: SpaceShooter) : Screen {

    var gc = GameController
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
    private val scoreFont = BitmapFont("fonts/score.fnt".toInternalFile())

    private val backRect = Rectangle(420f, 50f, 155f, 100f)
    private val moveLeftRect = Rectangle(700f, 510f, 280f, 60f)
    private val moveRightRect = Rectangle(700f, 440f, 280f, 60f)
    private val moveUpRect = Rectangle(700f, 370f, 280f, 60f)
    private val moveDownRect = Rectangle(700f, 300f, 280f, 60f)
    private val shootRect = Rectangle(700f, 230f, 280f, 60f)

    private var changeScreen = false

    private val batch = SpriteBatch()
    private val renderer = ShapeRenderer()
    private val layout = GlyphLayout()
    private val camera = OrthographicCamera()
    private val viewport = FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, camera)



    private val controlsStage: Stage = Stage()

    private val start: Float = 550f
    private val step: Float = 30f
    private val step2: Float = 50f
    private val controlsLbl: Label
    private val moveLeftLbl: Label
    private val moveRightLbl: Label
    private val moveUpLbl: Label
    private val moveDownLbl: Label
    private val shootLbl: Label
    private val moveLeftBtn: TextButton
    private val moveRightBtn: TextButton
    private val moveUpBtn: TextButton
    private val moveDownBtn: TextButton
    private val shootBtn: TextButton
    private val backBtn: TextButton

    init {
        input.inputProcessor = this.controlsStage

        controlsLbl = Label("Controls:", game.skin).apply {
            setPosition(controlsStage.width / 2 - width / 2, controlsStage.height - height - 50f)
        }.bind(controlsStage)

        moveLeftLbl = Label("Move Left:", game.skin2).apply {
            setPosition(controlsStage.width / 2 - width + 80f, 560f)
        }.bind(controlsStage)

        moveRightLbl = Label("Move Right:", game.skin2).apply {
            setPosition(controlsStage.width / 2 - width + 120f, moveLeftLbl.y - moveLeftLbl.height - step2)
        }.bind(controlsStage)

        moveUpLbl = Label("Move Up:", game.skin2).apply {
            setPosition(controlsStage.width / 2 - width , moveRightLbl.y - moveRightLbl.height - step2)
        }.bind(controlsStage)

        moveDownLbl = Label("Move Down:", game.skin2).apply {
            setPosition(controlsStage.width / 2 - width + 80f, moveUpLbl.y - moveUpLbl.height - step2)
        }.bind(controlsStage)

        shootLbl = Label("Shoot:", game.skin2).apply {
            setPosition(controlsStage.width / 2 - width - 85f, moveDownLbl.y - moveDownLbl.height - step2)
        }.bind(controlsStage)


        moveLeftBtn = TextButton(GameController.moveLeft, game.skin2)
                .apply {
                    setSize(280f, 60f)
                    setPosition(controlsStage.width - width - 10f, start)
                }
                .bind(controlsStage)
                .onClick {
                    //text = "asd"
                }

        moveRightBtn = TextButton("D", game.skin2)
                .apply {
                    setSize(280f, 60f)
                    setPosition(controlsStage.width - width - 10f, moveLeftBtn.y - moveLeftBtn.height - step)
                }
                .bind(controlsStage)
                .onClick {
                }

        moveUpBtn = TextButton("W", game.skin2)
                .apply {
                    setSize(280f, 60f)
                    setPosition(controlsStage.width - width - 10f, moveRightBtn.y - moveRightBtn.height - step)
                }
                .bind(controlsStage)
                .onClick {
                }

        moveDownBtn = TextButton("S", game.skin2)
                .apply {
                    setSize(280f, 60f)
                    setPosition(controlsStage.width - width - 10f, moveUpBtn.y - moveUpBtn.height - step)
                }
                .bind(controlsStage)
                .onClick {
                }

        shootBtn = TextButton("Space", game.skin2)
                .apply {
                    setSize(280f, 60f)
                    setPosition(controlsStage.width - width - 10f, moveDownBtn.y - moveDownBtn.height - step)
                }
                .bind(controlsStage)
                .onClick {
                }

        backBtn = TextButton("Back", game.skin)
                .extend(20f, 10f)
                .apply {
                    setPosition(controlsStage.width / 2 - width / 2, 50f)
                }
                .bind(controlsStage)
                .onClick {
                    game.screen = SettingsScreen(game)
                }
    }


    override fun render(delta: Float) {

        clearScreen()

        batch.use {
            batch.draw(game.background, 0f, 0f)
            game.movingBackground.updateRender(delta, batch)
        }

        controlsStage.act(delta)
        controlsStage.draw()

        if (GameConfig.DEBUG_MODE) {
            viewport.drawGrid(renderer, 100f)
            renderer.use {
                controlsStage.actors.forEach {
                    renderer.rect(it)
                }
            }
        }

    }

    private fun drawButton(keyup : String, xCord : Float, yCord : Float) {
        layout.setText(scoreFont, keyup)
        scoreFont.draw(batch, layout, xCord, yCord)
    }

    private fun bindChanger(rect : Rectangle, mouseX : Float, mouseY : Float) {
        if (inRectangle(rect, mouseX, mouseY)) {
            var touched = 0
            if (input.justTouched()) {
                touched = 1

                //Gets user input for pressed key
                input.inputProcessor = object : InputAdapter() {
                    override fun keyUp(keycode: Int): Boolean {
                        if (touched == 1) {
                            var newKey = Input.Keys.toString(keycode)
                            if (rect == moveLeftBtn && newKey != gc.moveRight && newKey != gc.moveUp && newKey != gc.moveDown && newKey != gc.shoot) {
                                gc.moveLeft = newKey
                            } else if (rect == moveRightBtn && newKey != gc.moveLeft && newKey != gc.moveUp && newKey != gc.moveDown && newKey != gc.shoot) {
                                gc.moveRight = newKey
                            } else if (rect == moveUpBtn && newKey != gc.moveLeft && newKey != gc.moveRight && newKey != gc.moveDown && newKey != gc.shoot) {
                                gc.moveUp = newKey
                            } else if (rect == moveDownBtn && newKey != gc.moveLeft && newKey != gc.moveRight && newKey != gc.moveUp && newKey != gc.shoot) {
                                gc.moveDown = newKey
                            } else if (rect == shootBtn && newKey != gc.moveLeft && newKey != gc.moveRight && newKey != gc.moveUp && newKey != gc.moveDown) {
                                gc.shoot = newKey
                            }
                            touched = 0
                        }
                        return true
                    }
                }
            }
        }
    }

    /*
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
            val mouseVector = Vector3().set(camera.unproject(Vector3(input.x.toFloat(), input.y.toFloat(), 0f)))

            layout.setText(haloFont, "Controls:")
            haloFont.draw(batch, layout, 400f - 100f, GameConfig.HUD_HEIGHT - layout.height - 100f)

            layout.setText(scoreFont, "Move Left:")
            scoreFont.draw(batch, layout, 220f, 550f)

            layout.setText(scoreFont, "Move Right:")
            scoreFont.draw(batch, layout, 220f, 480f)

            layout.setText(scoreFont, "Move Up:")
            scoreFont.draw(batch, layout, 220f, 410f)

            layout.setText(scoreFont, "Move Down:")
            scoreFont.draw(batch, layout, 220f, 340f)

            layout.setText(scoreFont, "Shoot:")
            scoreFont.draw(batch, layout, 220f, 270f)

            bindChanger(moveLeftRect, mouseVector.x, mouseVector.y)
            drawButton(GameController.moveLeft, 710f, 550f)

            bindChanger(moveRightRect, mouseVector.x, mouseVector.y)
            drawButton(GameController.moveRight,710f, 480f)

            bindChanger(moveUpRect, mouseVector.x, mouseVector.y)
            drawButton(GameController.moveUp,710f, 410f)

            bindChanger(moveDownRect, mouseVector.x, mouseVector.y)
            drawButton(GameController.moveDown,710f, 340f)

            bindChanger(shootRect, mouseVector.x, mouseVector.y)
            drawButton(GameController.shoot,710f, 270f)


            if (inRectangle(backRect, mouseVector.x, mouseVector.y)) {
                layout.setText(menuFontYellow, "Back")
                // draw active button if mouse is inside button hitbox.
                menuFontYellow.draw(batch, layout, 400f + layout.width / 2f - 20f, 100f + layout.height / 2f)
                if (input.justTouched()) {  // if exit button is pressed - exit the app.
                    changeScreen = true
                }
            } else { // draw inactive button.
                layout.setText(menuFont, "Back")
                menuFont.draw(batch, layout, 400f + layout.width / 2f - 20f, 100f + layout.height / 2f)
            }
        }
    }

    private fun renderDebug() {
        viewport.apply()
        renderer.projectionMatrix = camera.combined
        val oldColor = renderer.color.cpy()

        renderer.use {
            renderer.color = Color.RED
            renderer.rect(backRect.x, backRect.y, backRect.width, backRect.height)
            renderer.rect(moveLeftRect.x, moveLeftRect.y, moveLeftRect.width, moveLeftRect.height)
            renderer.rect(moveRightRect.x, moveRightRect.y, moveRightRect.width, moveRightRect.height)
            renderer.rect(moveUpRect.x, moveUpRect.y, moveUpRect.width, moveUpRect.height)
            renderer.rect(moveDownRect.x, moveDownRect.y, moveDownRect.width, moveDownRect.height)
            renderer.rect(shootRect.x, shootRect.y, shootRect.width, shootRect.height)
        }

        renderer.color = oldColor
    }

     */

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