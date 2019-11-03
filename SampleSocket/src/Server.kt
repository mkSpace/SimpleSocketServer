import java.net.ServerSocket

class Server : Runnable {

    private val serverSocket: ServerSocket = ServerSocket(Constants.PORT)
    private val clients: MutableList<ClientConnection> = mutableListOf()

    override fun run() {
        while (true) {
            println("run!")
            val client = serverSocket.accept()
            synchronized(clients) {
                clients.add(ClientConnection(client))
                println("client-${client.hashCode()} Connected!")
            }
        }
    }

    fun sendMessageToAllClients(text: String) {
        synchronized(clients) {
            clients.forEach {
                if (it.isConnected()) {
                    it.sendMessageWithLine(text)
                    println("Send message to client-${it.hashCode()}")
                } else {
                    clients.remove(it)
                    println("Remove client-${it.hashCode()}")
                }
            }
        }
    }

    fun closeAllClients() {
        synchronized(clients) {
            clients.forEach {
                it.close()
            }
            clients.clear()
        }
    }

    fun closeServer() {
        if(!serverSocket.isClosed) {
            serverSocket.close()
        }
    }
}