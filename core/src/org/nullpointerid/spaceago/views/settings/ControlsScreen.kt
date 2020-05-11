package org.nullpointerid.spaceago.views.settings

//import com.badlogic.gdx.Gdx.input
//import com.badlogic.gdx.Input
//import com.badlogic.gdx.InputAdapter
//import com.badlogic.gdx.Screen
//import com.badlogic.gdx.graphics.OrthographicCamera
//import com.badlogic.gdx.graphics.g2d.SpriteBatch
//import com.badlogic.gdx.graphics.glutils.ShapeRenderer
//import com.badlogic.gdx.scenes.scene2d.Stage
//import com.badlogic.gdx.scenes.scene2d.ui.Label
//import com.badlogic.gdx.scenes.scene2d.ui.TextButton
//import com.badlogic.gdx.utils.viewport.FitViewport
//import org.nullpointerid.spaceago.SpaceShooter
//import org.nullpointerid.spaceago.config.GameConfig
//import org.nullpointerid.spaceago.utils.*
//import org.nullpointerid.spaceago.views.game.GameController
//
//class ControlsScreen(private val game: SpaceShooter) : Screen {
//
//    var leftClicked = 0
//    var rightClicked = 0
//    var upClicked = 0
//    var downClicked = 0
//    var shootClicked = 0
//    var gc = GameController
//
//    private val batch = SpriteBatch()
//    private val renderer = ShapeRenderer()
//    private val camera = OrthographicCamera()
//    private val viewport = FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, camera)
//
//
//    private val controlsStage: Stage = Stage()
//
//    private val start: Float = 550f
//    private val step: Float = 30f
//    private val step2: Float = 50f
//    private val controlsLbl: Label
//    private val moveLeftLbl: Label
//    private val moveRightLbl: Label
//    private val moveUpLbl: Label
//    private val moveDownLbl: Label
//    private val shootLbl: Label
//    private val moveLeftBtn: TextButton
//    private val moveRightBtn: TextButton
//    private val moveUpBtn: TextButton
//    private val moveDownBtn: TextButton
//    private val shootBtn: TextButton
//    private val backBtn: TextButton
//
////    init {
////        input.inputProcessor = this.controlsStage
////
////        controlsLbl = Label("Controls:", game.COMMON_SKIN).apply {
////            setPosition(controlsStage.width / 2 - width / 2, controlsStage.height - height - 50f)
////        }.bind(controlsStage)
////
////        moveLeftLbl = Label("Move Left:", game.COMMON_SKIN2).apply {
////            setPosition(controlsStage.width / 2 - width + 30f, 560f)
////        }.bind(controlsStage)
////
////        moveRightLbl = Label("Move Right:", game.COMMON_SKIN2).apply {
////            setPosition(controlsStage.width / 2 - width + 70f, moveLeftLbl.y - moveLeftLbl.height - step2)
////        }.bind(controlsStage)
////
////        moveUpLbl = Label("Move Up:", game.COMMON_SKIN2).apply {
////            setPosition(controlsStage.width / 2 - width - 50, moveRightLbl.y - moveRightLbl.height - step2)
////        }.bind(controlsStage)
////
////        moveDownLbl = Label("Move Down:", game.COMMON_SKIN2).apply {
////            setPosition(controlsStage.width / 2 - width + 30f, moveUpLbl.y - moveUpLbl.height - step2)
////        }.bind(controlsStage)
////
////        shootLbl = Label("Shoot:", game.COMMON_SKIN2).apply {
////            setPosition(controlsStage.width / 2 - width - 135f, moveDownLbl.y - moveDownLbl.height - step2)
////        }.bind(controlsStage)
////
////
////        moveLeftBtn = TextButton(gc.moveLeft, game.COMMON_SKIN2)
////                .apply {
////                    setSize(320f, 60f)
////                    setPosition(controlsStage.width - width - 10f, start)
////                }
////                .bind(controlsStage)
////                .onClick { leftClicked += 1 }
////
////        moveRightBtn = TextButton(gc.moveRight, game.COMMON_SKIN2)
////                .apply {
////                    setSize(320f, 60f)
////                    setPosition(controlsStage.width - width - 10f, moveLeftBtn.y - moveLeftBtn.height - step)
////                }
////                .bind(controlsStage)
////                .onClick { rightClicked = 1 }
////
////        moveUpBtn = TextButton(gc.moveUp, game.COMMON_SKIN2)
////                .apply {
////                    setSize(320f, 60f)
////                    setPosition(controlsStage.width - width - 10f, moveRightBtn.y - moveRightBtn.height - step)
////                }
////                .bind(controlsStage)
////                .onClick { upClicked = 1 }
////
////        moveDownBtn = TextButton(gc.moveDown, game.COMMON_SKIN2)
////                .apply {
////                    setSize(320f, 60f)
////                    setPosition(controlsStage.width - width - 10f, moveUpBtn.y - moveUpBtn.height - step)
////                }
////                .bind(controlsStage)
////                .onClick { downClicked = 1 }
////
////        shootBtn = TextButton(gc.shoot, game.COMMON_SKIN2)
////                .apply {
////                    setSize(320f, 60f)
////                    setPosition(controlsStage.width - width - 10f, moveDownBtn.y - moveDownBtn.height - step)
////                }
////                .bind(controlsStage)
////                .onClick { shootClicked = 1 }
////
////        backBtn = TextButton("Back", game.COMMON_SKIN)
////                .extend(20f, 10f)
////                .apply {
////                    setPosition(controlsStage.width / 2 - width / 2, 50f)
////                }
////                .bind(controlsStage)
////                .onClick {
////                    game.screen = SettingsScreen(game)
////                }
////
////    }
//
//    private fun bindChanger(txt: TextButton) {
//        //Gets user input for pressed key
//        if (leftClicked + rightClicked + upClicked + downClicked + shootClicked > 0) {
//            input.inputProcessor = object : InputAdapter() {
//                override fun keyUp(keycode: Int): Boolean {
//                    val newKey = Input.Keys.toString(keycode)
//                    if (txt == moveLeftBtn && newKey != gc.moveRight && newKey != gc.moveUp && newKey != gc.moveDown && newKey != gc.shoot) {
//                        gc.moveLeft = newKey
//                        moveLeftBtn.setText(gc.moveLeft)
//                        leftClicked = 0
//                        input.inputProcessor = controlsStage
//                    } else if (txt == moveRightBtn && newKey != gc.moveLeft && newKey != gc.moveUp && newKey != gc.moveDown && newKey != gc.shoot) {
//                        gc.moveRight = newKey
//                        moveRightBtn.setText(gc.moveRight)
//                        rightClicked = 0
//                        input.inputProcessor = controlsStage
//                    } else if (txt == moveUpBtn && newKey != gc.moveLeft && newKey != gc.moveRight && newKey != gc.moveDown && newKey != gc.shoot) {
//                        gc.moveUp = newKey
//                        moveUpBtn.setText(gc.moveUp)
//                        upClicked = 0
//                        input.inputProcessor = controlsStage
//                    } else if (txt == moveDownBtn && newKey != gc.moveLeft && newKey != gc.moveRight && newKey != gc.moveUp && newKey != gc.shoot) {
//                        gc.moveDown = newKey
//                        moveDownBtn.setText(gc.moveDown)
//                        downClicked = 0
//                        input.inputProcessor = controlsStage
//                    } else if (txt == shootBtn && newKey != gc.moveLeft && newKey != gc.moveRight && newKey != gc.moveUp && newKey != gc.moveDown) {
//                        gc.shoot = newKey
//                        shootBtn.setText(gc.shoot)
//                        shootClicked = 0
//                        input.inputProcessor = controlsStage
//                    } else { //When user presses button that is already being used by other key
//                        leftClicked = 0
//                        rightClicked = 0
//                        upClicked = 0
//                        downClicked = 0
//                        shootClicked = 0
//                    }
//                    input.inputProcessor = controlsStage
//                    return true
//                }
//            }
//        }
//    }
//
//
//    override fun render(delta: Float) {
//
//        clearScreen()
//
//        batch.use {
//            batch.draw(game.background, 0f, 0f)
//            game.movingBackground.updateRender(delta, batch)
//            if (leftClicked == 1) {
//                bindChanger(moveLeftBtn)
//            } else if (rightClicked == 1) {
//                bindChanger(moveRightBtn)
//            } else if (upClicked == 1) {
//                bindChanger(moveUpBtn)
//            } else if (downClicked == 1) {
//                bindChanger(moveDownBtn)
//            } else if (shootClicked == 1) {
//                bindChanger(shootBtn)
//            }
//        }
//
//        controlsStage.act(delta)
//        controlsStage.draw()
//
//        if (GameConfig.DEBUG_MODE) {
//            viewport.drawGrid(renderer, 100f)
//            renderer.use {
//                controlsStage.actors.forEach {
//                    renderer.rect(it)
//                }
//            }
//        }
//
//    }
//
//    override fun resize(width: Int, height: Int) {
//        viewport.update(width, height, true)
//    }
//
//    override fun dispose() {
//        renderer.dispose()
//        batch.dispose()
//    }
//
//    override fun hide() {
//        dispose()
//    }
//
//    override fun show() {}
//    override fun pause() {}
//    override fun resume() {}
//
//}