package org.nullpointerid.spaceago.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.math.Vector2
import org.nullpointerid.spaceago.SpaceShooterOld
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.entities.BulletOld
import org.nullpointerid.spaceago.entities.EnemyOld
import org.nullpointerid.spaceago.entities.EntityOld
import org.nullpointerid.spaceago.entities.PlayerOld
import org.nullpointerid.spaceago.tools.MovingBackground
import org.nullpointerid.spaceago.tools.limitByRange
import org.nullpointerid.spaceago.utils.clearScreen
import org.nullpointerid.spaceago.utils.toInternalFile
import java.awt.event.MouseEvent
import kotlin.random.Random


class MainGameScreenOld(private val game: SpaceShooterOld) : Screen {
    companion object {
        private const val SHOOT_WAIT_TIME = 0.2f
        private const val MIN_ENEMY_SPAWN_TIME = 0.3f
        private const val MAX_ENEMY_SPAWN_TIME = 0.6f
    }

    private val screenWidth = GameConfig.WIDTH
    private val screenHeight = SpaceShooterOld.HEIGHT
    private var background = Texture("images/backg.jfif".toInternalFile())
    private val blank = Texture("images/blank.png".toInternalFile())
    private val cursorLocation = Vector2()
    private val scoreFont = BitmapFont("fonts/score.fnt".toInternalFile())
    private var asteroidSpawnTimer = Random.nextFloat() * (MAX_ENEMY_SPAWN_TIME - MIN_ENEMY_SPAWN_TIME) + MIN_ENEMY_SPAWN_TIME
    private var entityAddQueue: MutableList<EntityOld> = mutableListOf()
    val player = PlayerOld(0f, 0f)
    var entities: MutableList<EntityOld> = mutableListOf(player)

    init {
        game.movingBackground.setFixedSpeed(false)
        game.movingBackground.setSpeed(MovingBackground.DEFAULT_SPEED)
    }

    fun addEntity(entity: EntityOld) {
        entityAddQueue.add(entity)
    }

    override fun show() {}

    override fun render(delta: Float) { // move
        move()
        player.shootTimer += delta
        if (Gdx.input.isButtonPressed(MouseEvent.NOBUTTON) && player.shootTimer >= SHOOT_WAIT_TIME) {
            player.shootTimer = 0f
            val xOffset = 55 // bullet exit location offset
            val yOffset = 85 // bullet exit location offset
            entities.add(BulletOld(player.posX + xOffset, player.posY + yOffset))
        }

        //Enemy spawn
        asteroidSpawnTimer -= delta
        if (asteroidSpawnTimer <= 0) {
            asteroidSpawnTimer = Random.nextFloat() * (MAX_ENEMY_SPAWN_TIME - MIN_ENEMY_SPAWN_TIME) + MIN_ENEMY_SPAWN_TIME
            entities.add(EnemyOld(Random.nextInt(Gdx.graphics.width - EnemyOld.width).toFloat()))
        }

        //Update entities
        entities.forEach { entity ->
            entity.update(delta)
            entity.action(this)
        }

        //Remove entities
        // Jätab listis alles need, kellel klapib tingimus !entity.remove
        entities.retainAll { entity -> !entity.remove }

        // Adds entities that were queued
        entities.addAll(entityAddQueue)
        entityAddQueue.clear()

        clearScreen(1, 0, 0, 1)
        game.batch.begin()
        game.batch.draw(background, 0f, 0f)
        game.movingBackground.updateRender(delta, game.batch)
        val scoreLayout = GlyphLayout(scoreFont, "" + player.score)
        scoreFont.draw(game.batch, scoreLayout, Gdx.graphics.width / 2 - scoreLayout.width / 2, Gdx.graphics.height - scoreLayout.height - 10)
        player.render(game.batch)

        entities.forEach { it.render(game.batch) }

        game.batch.color = when {
            player.health > 0.6f -> Color.GREEN
            player.health > 0.2f -> Color.ORANGE
            else -> Color.RED
        }

        game.batch.draw(blank, 0f, 0f, Gdx.graphics.width * player.health, 5f)
        game.batch.color = Color.WHITE
        game.batch.end()

        // If dead, go to game over
        if (player.health <= 0) {
            dispose()
            game.screen = GameOverScreenOld(game, player.score)
        }
    }

    private fun move() {
        Gdx.input.isCursorCatched = false
        cursorLocation.x = Gdx.input.x.toFloat()
        cursorLocation.y = screenHeight - Gdx.input.y.toFloat()
        player.changePos(
                (cursorLocation.x - player.width / 2).limitByRange(-32f, screenWidth - player.width.toFloat() + 32f),
                (cursorLocation.y - player.height / 2).limitByRange(-16f, screenHeight - player.height.toFloat())
        )
/*
            //pm = new Pixmap(Gdx.files.internal("xxx.png"));
            //cursor = Gdx.graphics.newCursor(pm, 2, 2);
            //Gdx.graphics.setCursor(cursor);
            if (cursorLocation.y <= 0) {
                player.y = 0;
            }
            if (cursorLocation.x <= 0) {
                player.x = jet.getWidth();
            }
            if (player.y > screenHeight - jet.getHeight()) {
                player.y = screenHeight - jet.getHeight();
            }
            if (player.x > screenWidth - jet.getWidth()) {
                player.x = screenWidth - jet.getWidth();
            }

             */
    }

    override fun resize(width: Int, height: Int) {}
    override fun pause() {}
    override fun resume() {}
    override fun hide() {}
    override fun dispose() {}
}
