package org.nullpointerid.spaceago.views.multiplayer

import com.esotericsoftware.kryonet.Client
import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.esotericsoftware.kryonet.Server
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object MultiplayerController {

    enum class State {
        NONE, WAITING_CLIENT, CONNECTING_TO_SERVER
    }

    var clientConnection: Connection? = null

    var screen = MultiplayerScreen()
    val server = Server()
    val client = Client()
    var tcpPort = 0
    var udpPort = 1
    var state = State.NONE
        set(value) {
            when (value) {
                State.NONE -> {
                    screen.clientDiv.isVisible = true
                    screen.serverDiv.isVisible = true
                    screen.waitingDiv.isVisible = false
                    screen.connectingDiv.isVisible = false
                }
                State.WAITING_CLIENT -> {
                    screen.clientDiv.isVisible = false
                    screen.serverDiv.isVisible = false
                    screen.waitingDiv.isVisible = true
                    screen.connectingDiv.isVisible = false
                }
                State.CONNECTING_TO_SERVER -> {
                    screen.clientDiv.isVisible = false
                    screen.serverDiv.isVisible = false
                    screen.waitingDiv.isVisible = false
                    screen.connectingDiv.isVisible = true
                }
            }
            field = value
        }

    fun startServer(port1: Int, port2: Int) {
        state = State.WAITING_CLIENT
        GlobalScope.launch {
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
        GlobalScope.launch {
            client.start()
            tcpPort = port1
            udpPort = port2
            val x = client.discoverHost(port2, 60000)
            client.connect(60000, x, port1, port2)
            client.addListener(object : Listener() {
                override fun received(con: Connection, data: Any) = serverReceived(con, data)
                override fun connected(p0: Connection?) = serverConnected(p0!!)
                override fun disconnected(p0: Connection?) = serverDisconnected(p0!!)
            })

        }
    }

    fun serverReceived(connection: Connection, data: Any) {
        if (clientConnection != null) {
            connection.sendTCP("Sorry, somebody already connected to this port")
            connection.close()
        }

        if (data is String) {
            println(data)
            connection.sendTCP("wow")
        }
    }

    fun serverConnected(con: Connection) {
        clientConnection = con
        println("Holy shit, " + con.remoteAddressUDP + " connected")
    }

    fun serverDisconnected(con: Connection) {
        println("User " + con.remoteAddressUDP + " was disconnected")
    }

    fun closeServer() {
        state = State.NONE
        server.close()
    }

    fun closeClient() {
        state = State.NONE
        server.close()
    }

}