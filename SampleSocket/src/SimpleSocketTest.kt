import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class SimpleSocketTest {

    companion object {

        @JvmStatic
        fun main(args: Array<String>?) {
            val service: ScheduledExecutorService? = Executors.newScheduledThreadPool(2)
            var server: Server? = null
            var listener = object : ClickListener {
                override fun clickServerOn() {
                    if(server == null) {
                        server = Server()
                    }
                    server?.let {
                        service?.execute(it)
                    }
                }

                override fun clickServerOff() {
                    if(server != null) {
                        server?.closeAllClients()
                        server?.closeServer()
                        server = null
                    }
                }

                override fun clickSendMessage(message: String) {
                    println("Send Message with no period")
                    service?.submit(Runnable { server?.sendMessageToAllClients(message) })
                }

                override fun clickSendMessageWithPeriod(message: String, seconds: Int) {
                    println("Send Message with period")
                    service?.scheduleAtFixedRate(
                        Runnable { server?.sendMessageToAllClients(message) },
                        0,
                        seconds * 1000L,
                        TimeUnit.MILLISECONDS
                    )
                }
            }
            MainFrame(listener)
        }
    }

    interface ClickListener {
        fun clickServerOn()
        fun clickServerOff()
        fun clickSendMessage(message: String)
        fun clickSendMessageWithPeriod(message: String, seconds: Int)
    }
}