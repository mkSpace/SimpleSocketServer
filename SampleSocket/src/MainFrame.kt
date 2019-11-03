import java.awt.FlowLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.*

class MainFrame(private val listener: SimpleSocketTest.ClickListener) : JFrame("Socket Server"), ActionListener {
    private var serverButtons: Pair<JButton, JButton>
    private var messageButtons = JButton("메시지 보내기") to JButton("메시지 반복 보내기")
    private var inputMessage = JTextField(10)
    private var inputPeriod = JTextField(5)

    init {
        layout = FlowLayout()
        serverButtons =
            JButton("On").apply {
                addActionListener(this@MainFrame)
            } to JButton("Off").apply {
                addActionListener(this@MainFrame)
            }
        JPanel().apply {
            add(serverButtons.first)
            add(serverButtons.second)
        }.let {
            add(it)
        }

        JPanel().apply {
            add(JLabel("Message : "))
            add(inputMessage)
        }.let { add(it) }

        JPanel().apply {
            add(JLabel("Period : "))
            add(inputPeriod)
            add(JLabel("secs"))
        }.let { add(it) }

        messageButtons.first.addActionListener(this)
        messageButtons.second.addActionListener(this)

        JPanel().apply {
            add(messageButtons.first)
            add(messageButtons.second)
        }.let { add(it) }

        setSize(250,800)
        isVisible = true
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
    }

    override fun actionPerformed(p0: ActionEvent?) {
        val o = p0?.source

        when (o) {
            serverButtons.first -> {
                listener.clickServerOn()
            }
            serverButtons.second -> {
                listener.clickServerOff()
            }
            messageButtons.first -> {
                listener.clickSendMessage(inputMessage.text)
            }
            messageButtons.second -> {
                listener.clickSendMessageWithPeriod(inputMessage.text, inputPeriod.text.toInt())
            }
        }
    }

}