package org.nullpointerid.spaceago.tools
//
//import com.badlogic.gdx.graphics.Texture
//import com.badlogic.gdx.graphics.g2d.SpriteBatch
//import org.nullpointerid.spaceago.SpaceShooterOld
//import org.nullpointerid.spaceago.utils.toInternalFile
//
class MovingBackgroundOld {}
//
//    companion object {
//        const val DEFAULT_SPEED = 80
//        private const val ACCELERATION = 50
//        private const val REACH_ACCELERATION = 200
//    }
//
//    private val img: Texture = Texture("images/mov_background2.png".toInternalFile())
//    private var y1: Float
//    private var y2: Float
//
//    private var speed: Int // Pixels per second
//    var finalSpeed: Int
//    private var imageScale: Float
//
//    private var fixedSpeed: Boolean
//
//    init {
//        y1 = 0f
//        y2 = img.height.toFloat()
//        speed = 0
//        finalSpeed = DEFAULT_SPEED
//        imageScale = SpaceShooterOld.WIDTH / img.width.toFloat()
//        fixedSpeed = true
//    }
//
//    fun updateRender(delta: Float, batch: SpriteBatch) {
//        if (speed < finalSpeed) {
//            speed += REACH_ACCELERATION * delta.toInt()
//            if (speed > finalSpeed) {
//                speed = finalSpeed
//            }
//        } else if (speed > finalSpeed) {
//            speed -= REACH_ACCELERATION * delta.toInt()
//            if (speed < finalSpeed) {
//                speed = finalSpeed
//            }
//        }
//        if (!fixedSpeed) {
//            speed += ACCELERATION * delta.toInt()
//        }
//        y1 -= speed * delta
//        y2 -= speed * delta
//        if (y1 + img.height * imageScale <= 0) {
//            y1 = y2 + img.height * imageScale
//        }
//        if (y2 + img.height * imageScale <= 0) {
//            y2 = y1 + img.height * imageScale
//        }
//        //Render
//        batch.draw(img, 0f, y1, SpaceShooterOld.WIDTH.toFloat(), img.height * imageScale)
//        batch.draw(img, 0f, y2, SpaceShooterOld.WIDTH.toFloat(), img.height * imageScale)
//    }
//
//    fun resize(width: Int, height: Int) {
//        imageScale = width / img.height.toFloat()
//    }
//
//    fun setSpeed(finalSpeed: Int) {
//        this.finalSpeed = finalSpeed
//    }
//
//    fun setFixedSpeed(fixedSpeed: Boolean) {
//        this.fixedSpeed = fixedSpeed
//    }
//}
