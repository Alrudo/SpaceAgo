package org.nullpointerid.spaceago.screen.game

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.FitViewport
import org.nullpointerid.spaceago.SpaceShooter
import org.nullpointerid.spaceago.assets.AssetDescriptors
import org.nullpointerid.spaceago.assets.AssetPaths
import org.nullpointerid.spaceago.assets.RegionNames
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.entities.Bullet
import org.nullpointerid.spaceago.entities.Explosion
import org.nullpointerid.spaceago.entities.Player
import org.nullpointerid.spaceago.entities.SimpleEnemy
import org.nullpointerid.spaceago.screen.gameover.GameOverScreen
import org.nullpointerid.spaceago.utils.*

class GameRenderer(private val assetManager: AssetManager,
                   private val game: SpaceShooter,
                   private val controller: GameController) : Disposable {

    companion object {
        const val BULLET_OFFSET_X = -0.08f
        const val BULLET_OFFSET_Y = -0.03f
    }

    private val camera = OrthographicCamera()
    private val uiCamera = OrthographicCamera()
    private val viewport = FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera)
    private val uiViewport = FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, uiCamera)
    private val renderer = ShapeRenderer()
    private val batch = SpriteBatch()
    private val layout = GlyphLayout()

    private val gameAtlas = assetManager[AssetDescriptors.GAME_PLAY_ATLAS]
    private val background = gameAtlas[RegionNames.GAMEPLAY_BACKGROUND]
    private val playerTexture = gameAtlas[RegionNames.PLAYER]
    private val simpleEnemyTexture = gameAtlas[RegionNames.SIMPLE_ENEMY]?.apply { flip(true, true) }
    private val bulletTexture = gameAtlas[RegionNames.BULLET]
    private val font = BitmapFont(AssetPaths.SCORE_FONT.toInternalFile())

    private val player = controller.player
    private val simpleEnemies = controller.simpleEnemies
    private val bullets = controller.bullets
    private val explosions = controller.explosions


    fun render(delta: Float) {
        clearScreen()

        renderGameplay(delta)
        renderUi()

        if (GameConfig.DEBUG_MODE) {
            viewport.drawGrid(renderer)
            renderDebug()
        }
    }

    private fun renderDebug() {
        viewport.apply()
        renderer.projectionMatrix = camera.combined

        val oldColor = renderer.color.cpy()

        renderer.use {
            // Draw player hitboxes
            renderer.color = Color.GREEN
            renderer.rectangle(player.bounds.get(0), Player.BOUNDS_VER_WIDTH, Player.BOUNDS_VER_HEIGHT)
            renderer.rectangle(player.bounds.get(1), Player.BOUNDS_HOR_WIDTH, Player.BOUNDS_HOR_HEIGHT)

            // Draw simpleEnemy hitboxes
            simpleEnemies.forEach {
                renderer.rectangle(it.bounds[0], SimpleEnemy.BOUNDS_WIDTH, SimpleEnemy.BOUNDS_HEIGHT)
            }

            // Draw bullet hitboxes
            bullets.forEach {
                renderer.rectangle(it.bounds[0], Bullet.BOUNDS_WIDTH, Bullet.BOUNDS_HEIGHT)
            }
            renderer.color = oldColor

        }

    }

    private fun renderUi() {
        uiViewport.apply()
        batch.projectionMatrix = uiCamera.combined

        batch.use {
            layout.setText(font, controller.score.toString())

            font.draw(batch, layout, GameConfig.HUD_WIDTH / 2f - layout.width, GameConfig.HUD_HEIGHT - layout.height)
        }
    }

    private fun renderGameplay(delta: Float) {
        viewport.apply()
        batch.projectionMatrix = camera.combined
        val oldColor = renderer.color.cpy()

        batch.use {
            // Draw background texture
            batch.draw(background, 0f, 0f, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT)

            // Draw player texture
            batch.color = Color.GREEN
            batch.draw(playerTexture, player.x, player.y, Player.TEXTURE_WIDTH, Player.TEXTURE_HEIGHT)
            batch.color = oldColor

            // Draw simpleEnemy texture
            simpleEnemies.forEach {
                batch.draw(simpleEnemyTexture, it.x, it.y, SimpleEnemy.TEXTURE_WIDTH, SimpleEnemy.TEXTURE_HEIGHT)
            }

            // Draw bullet texture
            bullets.forEach {
                batch.draw(bulletTexture, it.x + BULLET_OFFSET_X, it.y + BULLET_OFFSET_Y, Bullet.TEXTURE_WIDTH, Bullet.TEXTURE_HEIGHT)
            }

            // Draw explosion texture
            explosions.forEach {
                it.stateTime += delta
                batch.draw(it.animation.getKeyFrame(it.stateTime), it.x, it.y, Explosion.TEXTURE_WIDTH, Explosion.TEXTURE_HEIGHT)
                if (it.animation.isAnimationFinished(it.stateTime)) explosions.removeValue(it, true)
            }
        }

        // Draw player healthpoints
        renderer.projectionMatrix = camera.combined
        renderer.begin(ShapeRenderer.ShapeType.Filled)
        when {
            player.lives < 0.3f -> renderer.color = Color.RED
            player.lives < 0.6f -> renderer.color = Color.ORANGE
            player.lives < 0.8f -> renderer.color = Color.YELLOW
            else -> renderer.color = Color.GREEN
        }
        renderer.rect(0f, 0f, GameConfig.WORLD_WIDTH * player.lives, 0.2f)
        renderer.color = oldColor
        renderer.end()
    }

    fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
        uiViewport.update(width, height, true)
    }

    override fun dispose() {
    }
}