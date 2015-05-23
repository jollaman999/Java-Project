import java.awt.Color;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
 
public class Server extends JFrame {
	private static final long serialVersionUID = 1L;
	private HashMap<String, DataOutputStream> clients;
    private ServerSocket serverSocket;
    
    static JTextArea JTA_ChatServer = new JTextArea("");
    JScrollPane JSP_ChatServer = new JScrollPane(JTA_ChatServer,
    		JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
    		JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    JScrollBar JSB_ChatServer  = JSP_ChatServer.getVerticalScrollBar();
    
    /*
     * ==== 서버 파일 예제 ====
     * 수박/붉고 물이 많으며 달고 맛있음.
     * 사과/속살이 노란빛이 돌며 달고 맛있음.
     * 바나나/길고 달고 맛있음.
     */
    
    static File Dic_file = new File("./dictionary.txt");
    static FileReader Dic_filereader = null;
    static Boolean is_ChatMode = false;
    static Boolean Dic_File_NotFound = false;
    
    static Vector<String> Dic_data = new Vector<String>();
 
    public static void main(String[] args) {
    	String read;
    	String[] reads;
    	
    	try {
    		Dic_filereader = new FileReader(Dic_file);
        	System.out.println("file opened");
		} catch (FileNotFoundException e1) {
			Dic_File_NotFound = true;
			JTA_ChatServer.append(getTime() +  " 사전 파일을 불러오는데 실패하였습니다!\n");
			return;
		}
    	BufferedReader Dic_bufferedreader =
    			new BufferedReader(Dic_filereader);
    	
    	try {
			while ((read = Dic_bufferedreader.readLine()) != null) {
				reads = read.split("/");
				
				if (reads.length % 2 != 0) {
					JTA_ChatServer.append(getTime() +  " 사전 파일에 문제가 있습니다!");
					Dic_bufferedreader.close();
					return;
				}
				
				Dic_data.addElement(reads[0]);
				Dic_data.addElement(reads[1]);
			}
		} catch (IOException e1) {
			JTA_ChatServer.append(getTime() +  " 사전 파일을 읽는 도중 문제가 발생하였습니다!");
		}
    	
        new Server().start();
        
        try {
        	Dic_bufferedreader.close();
		} catch (IOException e) {			
		}
    }
 
    public Server() {
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
            String message = new String();
            try {
                // 클라이언트가 서버에 접속하면 대화방에 알린다.
                name = input.readUTF();
                sendToAll("#" + name + "[" + socket.getInetAddress() + ":"
                        + socket.getPort() + "]" + "님이 대화방에 접속하였습니다.");
 
                clients.put(name, output);
                JTA_ChatServer.append(getTime() +  " " + name + "[" + socket.getInetAddress() + ":"
                        + socket.getPort() + "]" + "님이 대화방에 접속하였습니다." +"\n");
                JTA_ChatServer.append(getTime() +  " " + "현재 " + clients.size() + "명이 대화방에 접속 중입니다." +"\n");
                JSB_ChatServer.setValue(JSB_ChatServer.getMaximum()); // 맨아래로 스크롤
                
                // 메세지 전송
                while (input != null) {
                	message = input.readUTF();
                	
                	if (message.equals("CHAT_MODE"))
                		is_ChatMode = true;
                	else if (message.equals("DIC_MODE"))
                		is_ChatMode = false;
                	else if (message.equals("EXIT"))
                		break;
                	
                	if (is_ChatMode)
                		sendToAll(message);
                	else
                		sendToClient(message, name);
                }
            } catch (IOException e) {
            } finally {
                // 접속이 종료되면
                clients.remove(name);
                sendToAll("#" + name + "[" + socket.getInetAddress() + ":"
                        + socket.getPort() + "]" + "님이 대화방에서 나갔습니다.");
                JTA_ChatServer.append(getTime() +  " " + name + "[" + socket.getInetAddress() + ":"
                        + socket.getPort() + "]" + "님이 대화방에서 나갔습니다." +"\n");
                JTA_ChatServer.append(getTime() +  " " + "현재 " + clients.size() + "명이 대화방에 접속 중입니다." +"\n");
                JSB_ChatServer.setValue(JSB_ChatServer.getMaximum()); // 맨아래로 스크롤
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
        
        public void sendToClient(String message, String name) {        	
            try {
            	DataOutputStream dos = clients.get(name);
            	
                if (Dic_File_NotFound) {
    				try {
    					dos.writeUTF("서버로 부터 사전 파일을 불러오는데 실패하였습니다!\n");
    				} catch (IOException e) {
    				}
                } else {
                	int search_index = Dic_data.indexOf(message);
                	
                	System.out.println(message);
                	
                	if (search_index == -1)
                		dos.writeUTF("해당 단어는 OpenDic 에 존재하지 않는 단어입니다.");
                	else
                		dos.writeUTF(Dic_data.get(search_index + 1));
                }
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
