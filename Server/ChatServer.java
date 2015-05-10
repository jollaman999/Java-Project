import java.awt.Color;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
 
public class ChatServer extends JFrame {
	private static final long serialVersionUID = 1L;
	private HashMap<String, DataOutputStream> clients;
    private ServerSocket serverSocket;
    
    JTextArea JTA_ChatServer = new JTextArea("");
    JScrollPane JSP_ChatServer = new JScrollPane(JTA_ChatServer,
    		JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
    		JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    JScrollBar JSB_ChatServer  = JSP_ChatServer.getVerticalScrollBar();
 
    public static void main(String[] args) {
        new ChatServer().start();
    }
 
    public ChatServer() {
        clients = new HashMap<String, DataOutputStream>();
 
        // ���� �����忡�� ������ ���̹Ƿ� ����ȭ
        Collections.synchronizedMap(clients);
        
        JTA_ChatServer.setBackground(Color.WHITE);
        JTA_ChatServer.setEditable(false);
        this.add(JSP_ChatServer);
        
        this.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
			}
		});
        
        this.setVisible(true);
        this.setSize(1280, 720);
    }
 
    public void start() {
        try {
            Socket socket;
 
            // ������ ���� ����
            serverSocket = new ServerSocket(7777);
            JTA_ChatServer.append(getTime() +  " " + "������ ���۵Ǿ����ϴ�." +"\n");
 
            // Ŭ���̾�Ʈ�� ����Ǹ�
            while (true) {
                // ��� ������ �����ϰ� ������ ����(������ 1:1�θ� ����ȴ�)
                socket = serverSocket.accept();
                ServerReceiver receiver = new ServerReceiver(socket);
                receiver.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    class ServerReceiver extends Thread {
        Socket socket;
        DataInputStream input;
        DataOutputStream output;
 
        public ServerReceiver(Socket socket) {
            this.socket = socket;
            try {
                input = new DataInputStream(socket.getInputStream());
                output = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
            }
        }
 
        @Override
        public void run() {
            String name = "";
            try {
                // Ŭ���̾�Ʈ�� ������ �����ϸ� ��ȭ�濡 �˸���.
                name = input.readUTF();
                sendToAll("#" + name + "[" + socket.getInetAddress() + ":"
                        + socket.getPort() + "]" + "���� ��ȭ�濡 �����Ͽ����ϴ�.");
 
                clients.put(name, output);
                JTA_ChatServer.append(getTime() +  " " + name + "[" + socket.getInetAddress() + ":"
                        + socket.getPort() + "]" + "���� ��ȭ�濡 �����Ͽ����ϴ�." +"\n");
                JTA_ChatServer.append(getTime() +  " " + "���� " + clients.size() + "���� ��ȭ�濡 ���� ���Դϴ�." +"\n");
                JSB_ChatServer.setValue(JSB_ChatServer.getMaximum()); // �ǾƷ��� ��ũ��
 
                // �޼��� ����
                while (input != null) {
                	sendToAll(input.readUTF());
                }
            } catch (IOException e) {
            } finally {
                // ������ ����Ǹ�
                clients.remove(name);
                sendToAll("#" + name + "[" + socket.getInetAddress() + ":"
                        + socket.getPort() + "]" + "���� ��ȭ�濡�� �������ϴ�.");
                JTA_ChatServer.append(getTime() +  " " + name + "[" + socket.getInetAddress() + ":"
                        + socket.getPort() + "]" + "���� ��ȭ�濡�� �������ϴ�." +"\n");
                JTA_ChatServer.append(getTime() +  " " + "���� " + clients.size() + "���� ��ȭ�濡 ���� ���Դϴ�." +"\n");
                JSB_ChatServer.setValue(JSB_ChatServer.getMaximum()); // �ǾƷ��� ��ũ��
            }
        }
 
        public void sendToAll(String message) {
            Iterator<String> it = clients.keySet().iterator();
 
            while (it.hasNext()) {
                try {
                    DataOutputStream dos = clients.get(it.next());
                    dos.writeUTF(message);
                } catch (Exception e) {
                }
            }
        }
    }
    
    // ���� �ð� ������
    static String getTime() {
        SimpleDateFormat f = new SimpleDateFormat("[hh:mm:ss]");
        return f.format(new Date());
    }
}