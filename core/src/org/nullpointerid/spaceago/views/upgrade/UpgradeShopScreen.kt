package org.nullpointerid.spaceago.views.upgrade

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
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
import org.nullpointerid.spaceago.SpaceShooter.COMMON_SKIN
import org.nullpointerid.spaceago.SpaceShooter.STORAGE
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.utils.*
import org.nullpointerid.spaceago.utils.gdx.*
import org.nullpointerid.spaceago.views.menu.MenuScreen

class UpgradeShopScreen() : Screen {

    enum class Upgrades(val upgradeName: String) {
        ATTACK_SPEED("Attack speed upgrade"),
        SHOOTING("Shooting upgrade"),
        DURABILITY("Durability upgrade"),
        MOVE_SPEED("Move speed upgrade");

        fun getLevel(): Int {
            return STORAGE.getInteger(toString(), 0)
        }
    }

    private val batch = SpriteBatch()
    private val renderer = ShapeRenderer()
    private val camera = OrthographicCamera()
    private val viewport = FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, camera)
    private var scrapAmount = STORAGE.getInteger("money", 0)

    private val menuStage: Stage = Stage()

    private val upgradesTable: Table
    private val descriptionTable: Table

    private val spaceAgo: Label
    private val exitBtn: TextButton

    init {
        Gdx.input.inputProcessor = menuStage

        spaceAgo = Label("SpaceAgo", COMMON_SKIN, "game-title").apply {
            setPosition(20f, menuStage.height - height - 27f)
        }.bind(menuStage)

        upgradesTable = Table().apply {
            defaults().left().pad(25f, 15f, 25f, 0f); setBounds(50f, 200f, 350f, 500f)
            add(TextButton("Shooting upgrade", COMMON_SKIN, "button-h3").onClick {
                renderDescription(Upgrades.SHOOTING)
            }).colspan(1).row()
            add(TextButton("Durability upgrade", COMMON_SKIN, "button-h3").onClick {
                renderDescription(Upgrades.DURABILITY)
            }).colspan(3).row()
            add(TextButton("Move speed upgrade", COMMON_SKIN, "button-h3").onClick {
                renderDescription(Upgrades.MOVE_SPEED)
            }).colspan(4).row()
            add(TextButton("Attack speed upgrade", COMMON_SKIN, "button-h3").onClick {
                renderDescription(Upgrades.ATTACK_SPEED)
            }).colspan(2).row()
        }.bind(menuStage)

        descriptionTable = Table().apply {
            defaults().center().pad(25f)
            setBounds(menuStage.width / 2f, upgradesTable.y, 450f, 500f)
        }.bind(menuStage)

        exitBtn = TextButton("Menu", COMMON_SKIN, "fancy-hover-h2").extend(0f, 2f).apply {
            setPosition(menuStage.width / 2 - width / 2, 50f)
        }.bind(menuStage).onClick {
            SpaceShooter.screen = MenuScreen()
        }
    }

    override fun render(delta: Float) {
        clearScreen(255, 255, 255, 255)

        batch.use {
            batch.draw(SpaceShooter.BACKGROUND, 0f, 0f)
            SpaceShooter.MBACKGROUND.updateRender(delta, batch)
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
        var lvl = STORAGE.getInteger(upgrade.toString(), 0)
        val price = when(lvl) {0 -> 50; 1 -> 100; 2 -> 175; 3 -> 250; 4 -> 500; else -> -1 }
        descriptionTable.apply {
            reset()
            defaults().center().pad(20f)
            add(Label(upgrade.upgradeName, COMMON_SKIN)).colspan(1).row()
            add(Label("Scrap: $scrapAmount", COMMON_SKIN)).colspan(2).row()
            add(Label("Current lvl: $lvl/5", COMMON_SKIN)).colspan(3).row()
            add(Label("Price: $price scrap", COMMON_SKIN)).colspan(4).row()
            val text = if(price == -1) "Downgrade" else "Upgrade"
            if (price <= scrapAmount) {
                add(TextButton(text, COMMON_SKIN, "fancy-hover-h3").onClick {
                    if(price == -1 || Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)){
                        STORAGE.putInteger(upgrade.toString(), --lvl)
                    }else{
                        STORAGE.putInteger(upgrade.toString(), ++lvl)
                        scrapAmount -= price
                    }
                    STORAGE.putInteger("money", scrapAmount)
                    STORAGE.flush()
                    renderDescription(upgrade)
                }).colspan(5).row()
            }
        }
    }

    override fun show() {}
    override fun pause() {}
    override fun resume() {}
}