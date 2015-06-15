import java.awt.Color;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

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
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import simmetrics.JaroWinkler;
 
public class Server extends JFrame {
	private static final long serialVersionUID = 1L;
	private HashMap<String, DataOutputStream> clients;
    static private ServerSocket serverSocket;
    
    static JTextArea JTA_ChatServer = new JTextArea("");
    JScrollPane JSP_ChatServer = new JScrollPane(JTA_ChatServer,
    		JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
    		JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    JScrollBar JSB_ChatServer  = JSP_ChatServer.getVerticalScrollBar();
    
    static Boolean is_ChatMode = false;
    
    /*
     * ==== 서버 파일 예제 ====
     * 수박/붉고 물이 많으며 달고 맛있음.
     * 사과/속살이 노란빛이 돌며 달고 맛있음.
     * 바나나/길고 달고 맛있음.
     */    
    static File Dic_file = new File("./dictionary.txt");
    static FileReader Dic_filereader = null;
    static BufferedReader Dic_bufferedreader = null;
    static Boolean Dic_File_Error_Occured = false;
    
    static Vector<String> Dic_data = new Vector<String>();
    
    static void Dic_File_Opener () {
    	String read;
    	String[] reads;
    	
    	try {
    		Dic_filereader = new FileReader(Dic_file);
        	Dic_File_Error_Occured = false;
		} catch (FileNotFoundException e1) {
			Dic_File_Error_Occured = true;
			JTA_ChatServer.append(getTime() +  " 사전 파일을 불러오는데 실패하였습니다!\n");
			JTA_ChatServer.append(getTime() +  " " + "사전 파일이 올바른 위치에 저장되어 있고"
												+ " 파일명이 \"dictionary.txt\" 가 맞는지"
												+ " 확인하고 서버를 재실행 하여 주십시오." +"\n");
			return;
		}
    	
    	Dic_bufferedreader = new BufferedReader(Dic_filereader);
    	
    	try {
			while ((read = Dic_bufferedreader.readLine()) != null) {
				reads = read.split("/");
				
				if (reads.length % 2 != 0) {
					JTA_ChatServer.append(getTime() +  " 사전 파일에 문제가 있습니다!\n");
					JTA_ChatServer.append(getTime() +  " " + "사전 파일이 정상적으로 작성되어 있는지"
														+ " 확인하고 서버를 재실행 하여 주십시오." +"\n");
					JTA_ChatServer.append("\n==== 사전 파일 예제 ====\n"
										+ "[단어]/[뜻]	(구분자 : /)\n"
										+ "수박/붉고 물이 많으며 달고 맛있음.\n"
										+ "사과/속살이 노란빛이 돌며 달고 맛있음.\n"
									    + "바나나/길고 달고 맛있음.\n");
					Dic_File_Error_Occured = true;
					Dic_bufferedreader.close();
					return;
				}
				
				Dic_data.addElement(reads[0]);
				Dic_data.addElement(reads[1]);
			}
		} catch (IOException e1) {
			JTA_ChatServer.append(getTime() +  " 사전 파일을 읽는 도중 문제가 발생하였습니다!\n");
			JTA_ChatServer.append(getTime() +  " " + "사전 파일이 다른 곳에서 사용중인지"
												+ " 확인하고 서버를 재실행 하여 주십시오." +"\n");
			Dic_File_Error_Occured = true;
		}
    }
 
    public static void main(String[] args) {
    	Dic_File_Opener();
    	
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
			
			// TODO: Server closing
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					if (serverSocket != null)
						serverSocket.close();
				} catch (IOException e1) {
					System.exit(0);
				}
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
            
            if (!Dic_File_Error_Occured)
            	JTA_ChatServer.append(getTime() +  " " + "서버가 시작되었습니다." +"\n");
 
            // 클라이언트와 연결되면
            while (true) {
                // 통신 소켓을 생성하고 스레드 생성(소켓은 1:1로만 연결된다)
                socket = serverSocket.accept();
                ServerReceiver receiver = new ServerReceiver(socket);
                receiver.start();
            }
        } catch (IOException e) {
        	JTA_ChatServer.append(getTime() +  " " + "서버 소켓 생성 중 문제가 발생하였습니다!\n");
        	JTA_ChatServer.append(getTime() +  " " + "7777 번 포트가 사용중이거나 "
        									+	"실행중인 서버가 있는지 확인하고 서버를 재실행 하여 주십시오.\n"); 
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
                // 클라이언트가 서버에 접속하면 OpenDic 에 알린다.
                name = input.readUTF();
                sendToAll("* " + name + "[" + socket.getInetAddress() + ":"
                        + socket.getPort() + "]" + "님이 OpenDic 에 접속하였습니다.");
 
                clients.put(name, output);
                JTA_ChatServer.append(getTime() +  " " + name + "[" + socket.getInetAddress() + ":"
                        + socket.getPort() + "]" + "님이 OpenDic 에 접속하였습니다." +"\n");
                JTA_ChatServer.append(getTime() +  " "
                        				+ "현재 " + clients.size() + "명이 OpenDic 에 접속 중입니다." +"\n");
                JSB_ChatServer.setValue(JSB_ChatServer.getMaximum()); // 맨아래로 스크롤
                
                // 메세지 전송
                while (input != null) {
                	message = input.readUTF();
                	
                	if (message.matches(".*CHAT_MODE.*"))
                		is_ChatMode = true;
                	else if (message.matches(".*DIC_MODE.*"))
                		is_ChatMode = false;
                	else if (message.matches(".*EXIT.*"))
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
                sendToAll("* " + name + "[" + socket.getInetAddress() + ":"
                        + socket.getPort() + "]" + "님이 OpenDic 에서 나갔습니다.");
                JTA_ChatServer.append(getTime() +  " " + name + "[" + socket.getInetAddress() + ":"
                        + socket.getPort() + "]" + "님이 OpenDic 에서 나갔습니다." +"\n");
                JTA_ChatServer.append(getTime() +  " "
                        				+ "현재 " + clients.size() + "명이 OpenDic 에 접속 중입니다." +"\n");
                JSB_ChatServer.setValue(JSB_ChatServer.getMaximum()); // 맨아래로 스크롤
            }
        }
 
        public void sendToAll(String message) {
            Iterator<String> it = clients.keySet().iterator();
            
            if (message.matches("DIC_MODE")) {
            	is_ChatMode = false;
        		return;
        	}
            
        	if (message.matches("CHAT_MODE")) {
        		is_ChatMode = true;
        		return;
        	}

            while (it.hasNext()) {
                try {
                    DataOutputStream dos = clients.get(it.next());
                    
                    dos.writeUTF("CHAT_DATA" + message);
                } catch (Exception e) {
                }
            }
        }
        
        public void sendToClient(String message, String name) {        	
            try {
            	DataOutputStream dos = clients.get(name);
            	
                if (Dic_File_Error_Occured) {
    				try {
    					dos.writeUTF("서버로 부터 사전 파일을 불러오는데 실패하였습니다!\n");
    				} catch (IOException e) {
    				}
                } else {
                	int search_index = Dic_data.indexOf(message);
                	Double similarity = (double) 0.0f;
                	StringBuffer sb_to_client = new StringBuffer("");
                	Map<String, String> similarity_map = new HashMap<String, String>();
                	
                	if (message.matches("CHAT_MODE")) {
                		is_ChatMode = true;
                		return;
                	}
                		
                    if (message.matches("DIC_MODE")) {
                		return;
                	}
                	
                	if (search_index == -1) {
                		sb_to_client.append("해당 검색어는 OpenDic 에 존재하지 않는 단어 입니다.");	
                		sb_to_client.append("@");
                	} else {
                		sb_to_client.append(Dic_data.get(search_index + 1));
                		sb_to_client.append("@");
                	}
                	
                	for (int i = 0; i < Dic_data.size(); i += 2) {
                		similarity = compareStrings(Dic_data.get(i), message);
                		if (similarity > 0)
                			similarity_map.put(similarity.toString(), Dic_data.get(i));
                	}
                	
                	TreeMap<String, String> similarity_treemap = new TreeMap<String, String>(similarity_map);
                	Iterator<String> iteratorKey = similarity_treemap.descendingKeySet().iterator();
                	
                	sb_to_client.append("#");
                	
                	while(iteratorKey.hasNext()){
                		   String key = (String) iteratorKey.next();
                		   sb_to_client.append(similarity_treemap.get(key) + "#");
                	}
                	dos.writeUTF(sb_to_client.toString());
                }
            } catch (Exception e) {
            }
        }
    }
    
    // simmetrics
    // http://sourceforge.net/projects/simmetrics/
    public double compareStrings(String stringA, String stringB) {
        JaroWinkler algorithm = new JaroWinkler();
        return algorithm.getSimilarity(stringA, stringB);
    }
    
    // 현재 시간 얻어오기
    static String getTime() {
        SimpleDateFormat f = new SimpleDateFormat("[hh:mm:ss]");
        return f.format(new Date());
    }
}
