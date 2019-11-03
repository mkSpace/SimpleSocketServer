import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Client implements Runnable {

    private Socket mSocket = null;
    private BufferedReader mIn = null;
    private ClientToFrameListener mListener;

    private ReentrantLock lock = new ReentrantLock();

    static Runnable cachedRunnable;

    public Client(String ip, int port) {
        this(ip, port, null);
    }

    public Client(String ip, int port, ClientToFrameListener listener) {
        mListener = listener;
        try {
            mSocket = new Socket(ip, port);
            if (mListener != null) {
                mListener.addMessage(ip + " 연결됨");
            } else {
                System.out.println(ip + " 연결됨");
            }

            mIn = new BufferedReader(
                    new InputStreamReader(mSocket.getInputStream())
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        synchronized (lock) {
            try {
                if (mSocket != null) {
                    System.out.println("Socket closed");
                    mSocket.close();
                }
            } catch (IOException e) {
                System.out.println("EXCEPTION!!");
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        String ip = "127.0.0.1";
        int port = 6200;

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

        MainListener listener = new MainListener() {
            @Override
            public void registThread(Runnable runnable) {
                cachedRunnable = runnable;
                service.scheduleAtFixedRate(cachedRunnable, 0, 3000, TimeUnit.MILLISECONDS);
            }

            @Override
            public void closeService() {
                if(cachedRunnable != null) {
                    service.execute(cachedRunnable);
                    cachedRunnable = null;
                }
            }
        };

        MainFrame main = new MainFrame(listener);
    }

    @Override
    public void run() {
        while (true) {
            synchronized (lock) {
                try {
                    if (mSocket != null && !mSocket.isClosed() && mSocket.isConnected() && mIn != null) {
                        if (mListener != null) {
                            String readMessage= mIn.readLine();
                            if(readMessage != null) {
                                mListener.addMessage(readMessage);
                            } else {
                                System.out.println("Server is out!");
                                close();
                            }
                        } else {
                            String readMessage= mIn.readLine();
                            if(readMessage != null) {
                                System.out.println(mIn.readLine());
                            } else {
                                System.out.println("Server is out!");
                                close();
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    interface MainListener {
        void registThread(Runnable runnable);
        void closeService();
    }

    interface ClientToFrameListener {
        void addMessage(String message);
    }
}
