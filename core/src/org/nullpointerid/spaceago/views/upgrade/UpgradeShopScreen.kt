package org.nullpointerid.spaceago.views.upgrade

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.viewport.FitViewport
import org.nullpointerid.spaceago.SpaceShooter
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.utils.*
import org.nullpointerid.spaceago.views.menu.MenuScreen

class UpgradeShopScreen(private val game: SpaceShooter) : Screen {

    enum class Upgrades(val upgradeName: String) {
        ATTACK_SPEED("Attack speed upgrade"),
        ATTACK_DAMAGE("Attack upgrade"),
        DURABILITY("Durability upgrade"),
        MOVE_SPEED("Move speed upgrade")
    }

    private val batch = SpriteBatch()
    private val renderer = ShapeRenderer()
    private val camera = OrthographicCamera()
    private val viewport = FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, camera)
    private val prefs = Gdx.app.getPreferences("spaceshooter")
    private var scrapAmount = prefs.getInteger("money", 0)

    private val menuStage: Stage = Stage()

    private val upgradesTable: Table
    private val descriptionTable: Table

    private val spaceAgo: Label
    private val exitBtn: TextButton

    init {
        Gdx.input.inputProcessor = this.menuStage

        spaceAgo = Label("SpaceAgo", game.COMMON_SKIN, "game-title").apply {
            setPosition(20f, menuStage.height - height - 27f)
        }.bind(menuStage)

        upgradesTable = Table().apply {
            defaults().left().pad(25f, 15f, 25f, 0f); setBounds(50f, 200f, 350f, 500f)
            add(TextButton("Attack upgrade", game.COMMON_SKIN, "button-h3").onClick {
                renderDescription(Upgrades.ATTACK_DAMAGE)
            }).colspan(1).row()
            add(TextButton("Durability upgrade", game.COMMON_SKIN, "button-h3").onClick {
                renderDescription(Upgrades.DURABILITY)
            }).colspan(3).row()
            add(TextButton("Move speed upgrade", game.COMMON_SKIN, "button-h3").onClick {
                renderDescription(Upgrades.MOVE_SPEED)
            }).colspan(4).row()
            add(TextButton("Attack speed upgrade", game.COMMON_SKIN, "button-h3").onClick {
                renderDescription(Upgrades.ATTACK_SPEED)
            }).colspan(2).row()
        }.bind(menuStage)

        descriptionTable = Table().apply {
            defaults().center().pad(25f)
            setBounds(menuStage.width / 2f, upgradesTable.y, 450f, 500f)
        }.bind(menuStage)

        exitBtn = TextButton("Menu", game.COMMON_SKIN, "fancy-hover-h2").extend(0f, 2f).apply {
            setPosition(menuStage.width / 2 - width / 2, 50f)
        }.bind(menuStage).onClick {
            game.screen = MenuScreen(game)
        }
        prefs.putInteger(Upgrades.ATTACK_DAMAGE.toString(), 5)
        prefs.putInteger(Upgrades.ATTACK_SPEED.toString(), 5)
        prefs.putInteger(Upgrades.DURABILITY.toString(), 5)
        prefs.putInteger(Upgrades.MOVE_SPEED.toString(), 5)
    }

    override fun render(delta: Float) {
        clearScreen(255, 255, 255, 255)

        batch.use {
            batch.draw(game.background, 0f, 0f)
            game.movingBackground.updateRender(delta, batch)
        }

        menuStage.act(delta)
        menuStage.draw()

        if (GameConfig.DEBUG_MODE) {
            viewport.drawGrid(renderer, 100f)
            renderer.use {
                menuStage.actors.forEach {
                    renderer.rect(it)
                }
            }
        }
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun dispose() {
        renderer.dispose()
        batch.dispose()
    }

    override fun hide() {
        dispose()
    }

    private fun renderDescription(upgrade: Upgrades) {
        var lvl = prefs.getInteger(upgrade.toString(), 0)
        val price = when(lvl) {0 -> 50; 1 -> 100; 2 -> 175; 3 -> 250; 4 -> 500; else -> -1 }
        descriptionTable.apply {
            reset()
            defaults().center().pad(20f)
            add(Label(upgrade.upgradeName, game.COMMON_SKIN)).colspan(1).row()
            add(Label("Scrap: $scrapAmount", game.COMMON_SKIN)).colspan(2).row()
            add(Label("Current lvl: $lvl/5", game.COMMON_SKIN)).colspan(3).row()
            if (price != -1) {
                add(Label("Price: $price scrap", game.COMMON_SKIN)).colspan(4).row()
                if (price <= scrapAmount) {
                    add(TextButton("Upgrade", game.COMMON_SKIN, "fancy-hover-h3").onClick {
                        prefs.putInteger(upgrade.toString(), ++lvl)
                        scrapAmount -= price
                        renderDescription(upgrade)
                    }).colspan(5).row()
                }
            }
        }
    }

    override fun show() {}
    override fun pause() {}
    override fun resume() {}
}