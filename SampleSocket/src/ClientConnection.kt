import java.io.IOException
import java.io.PrintWriter
import java.net.Socket

class ClientConnection(private val socket: Socket) {

    private val writer = PrintWriter(socket.getOutputStream())

    fun close() {
        socket.close()
    }

    fun sendMessageWithLine(message: String) {
        try {
            writer.println(message)
        } catch (e: IOException) {
            socket.close()
        }
        writer.flush()
    }

    fun sendMessage(message: String) {
        writer.print(message)
        writer.flush()
    }

    fun isConnected(): Boolean = socket.isConnected && !socket.isClosed
}