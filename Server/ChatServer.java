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
 
        // 여러 스레드에서 접근할 것이므로 동기화
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
 
            // 리스너 소켓 생성
            serverSocket = new ServerSocket(7777);
            JTA_ChatServer.append(getTime() +  " " + "서버가 시작되었습니다." +"\n");
 
            // 클라이언트와 연결되면
            while (true) {
                // 통신 소켓을 생성하고 스레드 생성(소켓은 1:1로만 연결된다)
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
                // 클라이언트가 서버에 접속하면 대화방에 알린다.
            	name = input.readUTF();
               // sendToAll("#" + name + "[" + socket.getInetAddress() + ":"
                //        + socket.getPort() + "]" + "님이 대화방에 접속하였습니다.");
 
                clients.put(name, output);
                JTA_ChatServer.append(getTime() +  " " + name + "[" + socket.getInetAddress() + ":"
                        + socket.getPort() + "]" + "님이 대화방에 접속하였습니다." +"\n");
                
                
                //TODO: 단어를 찾기
                
                
                JTA_ChatServer.append(getTime() +  " " + "현재 " + clients.size() + "명이 대화방에 접속 중입니다." +"\n");
                JSB_ChatServer.setValue(JSB_ChatServer.getMaximum()); // 맨아래로 스크롤
 
                // 메세지 전송
                while (input != null) {
                	sendToClient(input.readUTF(), name);
                }
            } catch (IOException e) {
            } finally {
                // 접속이 종료되면
                clients.remove(name);
                JTA_ChatServer.append(getTime() +  " " + name + "[" + socket.getInetAddress() + ":"
                        + socket.getPort() + "]" + "님이 대화방에서 나갔습니다." +"\n");
                JTA_ChatServer.append(getTime() +  " " + "현재 " + clients.size() + "명이 대화방에 접속 중입니다." +"\n");
                JSB_ChatServer.setValue(JSB_ChatServer.getMaximum()); // 맨아래로 스크롤
            }
        }
 
        public void sendToClient(String message, String name) {

            try {
                DataOutputStream dos = clients.get(name);
                dos.writeUTF(message);
                System.out.println(message);
            } catch (Exception e) {
            }
        }
    }
    
    // 현재 시간 얻어오기
    static String getTime() {
        SimpleDateFormat f = new SimpleDateFormat("[hh:mm:ss]");
        return f.format(new Date());
    }
}
