package org.nullpointerid.spaceago.views.multiplayer

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Vector2
import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryonet.Client
import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.esotericsoftware.kryonet.Server
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.nullpointerid.spaceago.SpaceShooter
import org.nullpointerid.spaceago.World
import org.nullpointerid.spaceago.entities.*
import org.nullpointerid.spaceago.entities.collectables.HealthPack
import org.nullpointerid.spaceago.entities.collectables.MoneyCrate
import org.nullpointerid.spaceago.entities.collectables.UltimateWeapon
import org.nullpointerid.spaceago.entities.projectile.Bullet
import org.nullpointerid.spaceago.entities.projectile.EnemyBullet
import org.nullpointerid.spaceago.utils.gdx.GdxArray
import org.nullpointerid.spaceago.utils.KeyboardState
import org.nullpointerid.spaceago.utils.UpgradeState
import org.nullpointerid.spaceago.utils.XRectangle
import org.nullpointerid.spaceago.views.game.GameController
import org.nullpointerid.spaceago.views.game.GameScreen
import org.nullpointerid.spaceago.views.gameover.GameOverScreen
import org.nullpointerid.spaceago.views.upgrade.UpgradeShopScreen


object MultiplayerController {

    enum class State {
        NONE, WAITING_CLIENT, CONNECTING_TO_SERVER, CONNECTED
    }

    enum class Role {
        CLIENT, SERVER
    }

    var game: GameController? = null
    var clientConnection: Connection? = null
    var serverConnection: Connection? = null

    var screen = MultiplayerScreen()
    var server = Server(1000000, 1000000)
    var client = Client(1000000, 1000000)
    var tcpPort = 0
    var udpPort = 1
    var role: Role? = null
    var state = State.NONE
        set(value) {
            when (value) {
                State.NONE -> {
                    screen.clientDiv.isVisible = true
                    screen.serverDiv.isVisible = true
                    screen.waitingDiv.isVisible = false
                    screen.connectingDiv.isVisible = false
                    screen.connectedDiv.isVisible = false
                }
                State.WAITING_CLIENT -> {
                    screen.clientDiv.isVisible = false
                    screen.serverDiv.isVisible = false
                    screen.waitingDiv.isVisible = true
                    screen.connectingDiv.isVisible = false
                    screen.connectedDiv.isVisible = false
                }
                State.CONNECTING_TO_SERVER -> {
                    screen.clientDiv.isVisible = false
                    screen.serverDiv.isVisible = false
                    screen.waitingDiv.isVisible = false
                    screen.connectingDiv.isVisible = true
                    screen.connectedDiv.isVisible = false
                }
                State.CONNECTED -> {
                    screen.clientDiv.isVisible = false
                    screen.serverDiv.isVisible = false
                    screen.waitingDiv.isVisible = false
                    screen.connectingDiv.isVisible = false
                    screen.connectedDiv.isVisible = true
                    screen.startGame.isVisible = role == Role.SERVER
                }
            }
            field = value
        }

    fun startServer(port1: Int, port2: Int) {
        state = State.WAITING_CLIENT
        role = Role.SERVER
        GlobalScope.launch {
            server.kryo.apply { register(this)}
            server.start()
            tcpPort = port1
            udpPort = port2
            server.bind(port1, port2)
            server.addListener(object : Listener() {
                override fun received(con: Connection, data: Any) = serverReceived(con, data)
                override fun connected(p0: Connection?) = serverConnected(p0!!)
                override fun disconnected(p0: Connection?) = serverDisconnected(p0!!)
            })

        }
    }

    fun startClient(port1: Int, port2: Int) {
        state = State.CONNECTING_TO_SERVER
        role = Role.CLIENT
        GlobalScope.launch {
            client.kryo.apply {
                register(this)
            }
            client.start()
            tcpPort = port1
            udpPort = port2
            val x = client.discoverHost(port2, 60000)
            if (x == null) {
                state = State.NONE
                return@launch
            }
            client.addListener(object : Listener() {
                override fun received(con: Connection, data: Any) = clientReceived(con, data)
                override fun connected(p0: Connection?) = clientConnected(p0!!)
                override fun disconnected(p0: Connection?) = clientDisconnected(p0!!)
            })

            client.connect(60000, x, port1, port2)
        }
    }

    fun serverReceived(con: Connection, data: Any) {
        if(data is UpgradeState && game != null){
            game!!.players.find { it.name == "player2" }?.upgradeState = data
        }
        if(data is KeyboardState && game != null){
            game!!.players.find { it.name == "player2" }?.keyboardState = data
        }
    }

    fun serverConnected(con: Connection) {
        if (serverConnection != null) {
            con.sendTCP("Sorry, somebody already connected to this port")
            con.close()
        }

        serverConnection = con
        state = State.CONNECTED
        println("I found some friend, " + con.remoteAddressUDP + " connected")
    }

    fun serverDisconnected(con: Connection) {
        serverConnection = null
        closeSocket()
        println("He left us, " + con.remoteAddressUDP + " was disconnected")
    }

    fun clientReceived(connection: Connection, data: Any) {
        if (data is String) {
            println("got: $data")
            if(data == "Start Game"){
                Gdx.app.postRunnable{
                    SpaceShooter.screen = GameScreen(this)
                }
            }
            if (data.startsWith("GameOver:")){
                Gdx.app.postRunnable{
                    closeSocket()
                    SpaceShooter.screen = GameOverScreen(data.replace("GameOver:", "").toInt())
                }
            }
        }
        if (data is World && game != null){
            game!!.world = data
        }
    }

    fun clientConnected(con: Connection) {
        if (clientConnection != null) {
            con.sendTCP("Sorry, somebody already connected to this port")
            con.close()
        }

        clientConnection = con
        state = State.CONNECTED
        println("Connected to host " + con.remoteAddressTCP)
        clientConnection!!.sendTCP(UpgradeState())
    }

    fun clientDisconnected(con: Connection) {
        clientConnection = null
        closeSocket()
        println("Disconnected from host")
    }

    fun closeSocket() {
        when (role) {
            Role.SERVER -> {
                server.close()
                server = Server(1000000, 1000000)
            }
            Role.CLIENT -> {
                client.close()
                client = Client(1000000, 1000000)
            }
        }
        state = State.NONE
        role = null
    }

    private fun register(kryo: Kryo){
        with(kryo) {
            register(Array<Any>::class.java)
            register(GdxArray::class.java)
            register(LinkedHashMap::class.java)
            register(FloatArray::class.java)
            register(Vector2::class.java)
            register(HealthPack::class.java)
            register(UltimateWeapon::class.java)
            register(LaserBeam::class.java)
            register(MoneyCrate::class.java)
            register(KeyboardState::class.java)
            register(UpgradeState::class.java)
            register(ShootingEnemy::class.java)
            register(EnemyBullet::class.java)
            register(UpgradeShopScreen.Upgrades::class.java)
            register(Player::class.java)
            register(Polygon::class.java)
            register(XRectangle::class.java)
            register(SimpleEnemy::class.java)
            register(Bullet::class.java)
            register(CivilianShip::class.java)
            register(Explosion::class.java)
            register(EntityBase::class.java)
            register(ArrayList::class.java)
            register(World::class.java)
        }
    }
}