package org.nullpointerid.spaceago.views.multiplayer

import com.esotericsoftware.kryonet.Client
import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.esotericsoftware.kryonet.Server
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object MultiplayerController {

    enum class State {
        NONE, WAITING_CLIENT, CONNECTING_TO_SERVER, CONNECTED
    }

    enum class Role {
        CLIENT, SERVER
    }

    var clientConnection: Connection? = null
    var serverConnection: Connection? = null

    var screen = MultiplayerScreen()
    var server = Server()
    var client = Client()
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
        if (data is String) {
            println(data)
            con.sendTCP("wow")
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
            println(data)
            connection.sendTCP("wow")
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
                server = Server()
            }
            Role.CLIENT -> {
                client.close()
                client = Client()
            }
        }
        state = State.NONE
        role = null
    }

}