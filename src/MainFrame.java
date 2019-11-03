import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame implements ActionListener {

    JScrollPane s;
    JTextArea area;
    JButton b1;
    JButton b2;

    JWindow w;
    Client mClient = null;

    Client.MainListener mListener;

    public MainFrame(Client.MainListener listener) {
        super("Socket Client");
        mListener = listener;

        setLayout(new FlowLayout());
        b1 = new JButton("서버 연결");
        b2 = new JButton("연결 종료");

        b1.addActionListener(this);
        b2.addActionListener(this);

        JPanel loginPanel = new JPanel();
        loginPanel.add(b1);
        loginPanel.add(b2);

        add(loginPanel);

        area = new JTextArea(17,20);
        s = new JScrollPane(area);
        add(s);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(this);
        add(clearButton);

        setSize(250,400);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void appendMessage(String message) {
        area.append(message+"\n");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if(o == b1) {
            if(mClient == null) {
                mClient = new Client("127.0.0.1", 6200, this::appendMessage);
                mListener.registThread(mClient);
            }
        } else if(o == b2){
            mListener.closeService();
            if(mClient != null) {
                mClient.close();
                mClient = null;
            }
        } else {
            area.setText("");
        }
    }
}
