import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Client extends JFrame {
	private static final long serialVersionUID = 1L;

	static int isPlayer = 0;

	// ------- 시작 창 관련 영역 --------
	BorderLayout bl_main = new BorderLayout();
	JButton button_start = new JButton("Enter");
	Color FrameColor = new Color(238, 238, 238, 255);
	JPanel login_panel = new JPanel(new GridLayout(4, 1, 0, 1));
	JPanel login_panel_button_area = new JPanel(new GridLayout(1, 3));
	static JTextField JTF_ip = new JTextField();
	JTextField JTF_name = new JTextField();
	static int JTF_ip_input_counter = 0;
	static int JTF_name_input_counter = 0;

	// ------- 메인 프레임 관련 영역 --------
	JMenuBar mainMenu_bar = new JMenuBar();
	JMenu main_file = new JMenu("파일");
	JMenuItem file_chatmode = new JMenuItem("채팅 모드 전환");
	JMenuItem file_open = new JMenuItem("불러오기");
	JMenuItem file_save = new JMenuItem("저장하기");
	JMenuItem file_saveas = new JMenuItem("다른 이름으로 저장");
	JMenuItem file_exit = new JMenuItem("종료");	
	JSplitPane sp_leftright = new JSplitPane();
	JSplitPane sp_R_updown = new JSplitPane();
	JSplitPane sp_L_updown = new JSplitPane();
	JTextField Finder = new JTextField();
	JPanel sp_L_commonWordArea = new JPanel(new GridLayout(10,1,1,1));
	JPanel sp_L_find_panel = new JPanel(new BorderLayout());
	JPanel sp_L_WordHistoryArea = new JPanel(new GridLayout(10,1,1,1));
	JPanel Startup_upper = new JPanel(new GridLayout(3, 1));
	JPanel Startup_upper_1st = new JPanel();
	JPanel Startup_upper_2st = new JPanel(new FlowLayout());
	JPanel Startup_upper_3st = new JPanel();
	JPanel Startup_upper_4st = new JPanel();
	JPanel Startup_upper_5st = new JPanel();
	JPanel Startup_down = new JPanel(new GridLayout(3, 1));
	JPanel finder_panel = new JPanel(new BorderLayout());
	JButton find_button = new JButton("검색");
	back_Panel commonlist_background_panel_up = new back_Panel();
	back_Panel commonlist_background_panel_down = new back_Panel();
	startup_BACKGROUND_Panel startup_panel = new startup_BACKGROUND_Panel();

	JLabel Startup_Label = new JLabel("OpenDIC");
	Font startup_font = new Font("Broadway", Font.BOLD, 50);

	JScrollPane sp_L_up = new JScrollPane(sp_L_commonWordArea,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

	JScrollPane sp_L_down = new JScrollPane(sp_L_WordHistoryArea,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

	JPanel sp_R = new JPanel();
	
	// 채팅 패널
	JTextArea sp_R_up_Chat_Show = new JTextArea("");
	JTextArea sp_R_down_Chat_Input = new JTextArea("");
	Boolean is_ChatMode = false;
	
	// 사전 선택
	ButtonGroup Dic_select_btgroup = new ButtonGroup();
	JRadioButton Dic_select_Wiki = new JRadioButton("Wiki");
	JRadioButton Dic_select_OpenDIC = new JRadioButton("OpenDIC");
	JPanel Dic_select_Panel = new JPanel(new FlowLayout());
	Boolean is_Wiki = false;
	
	// wordlist
	JTextField[] wordlist = new JTextField[10];
	JTextField[] wordlist_history = new JTextField[10];
	String[] wordlist_history_queue = new String[10];
	
	// Mywordlist
	String Mywordlist_File = "./Mywordlist.txt";
	String Mywordlist_to_Save="";
	
	// Wiki 패널
	JEditorPane sp_R_up_Wiki_Broswer = new JEditorPane();
	JScrollPane sp_R_up_Wiki_JSP = new JScrollPane(sp_R_up_Wiki_Broswer);
	
	// OpenDic 패널
	JEditorPane sp_R_up_OpenDic_Show = new JEditorPane();
	JScrollPane sp_R_up_OpenDic_JSP = new JScrollPane(sp_R_up_OpenDic_Show);

	JScrollPane sp_R_up_Chat_JSP = new JScrollPane(sp_R_up_Chat_Show,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	JScrollPane sp_R_down = new JScrollPane(sp_R_down_Chat_Input,
			JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

	static JFrame frame_startup = new JFrame("OpenDIC v1.0");
	JPanel start_up_main_panel = new JPanel();

	static JFrame frame_main = new JFrame("OpenDIC v1.0");
	
	// Restart check
	static Boolean is_restarted = false;

	// 채팅 대화명, 채팅 소켓
	private String chat_name;
	static private Socket chat_socket;

	// 메인 화면
	public Client() {

		// --------- 시작 프레임 관련 영역 -----------
		// TODO 시작프레암 구성 영역
		frame_startup.setSize(500, 700);
		frame_startup.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame_startup.getContentPane().add(startup_panel);
		frame_startup.setResizable(false);

		startup_panel.setLayout(new BorderLayout());

		startup_panel.add(Startup_upper, BorderLayout.NORTH);
		startup_panel.add(Startup_down, BorderLayout.CENTER);
		Startup_upper.add(Startup_upper_1st, 0);
		Startup_upper.add(Startup_upper_2st, 1);
		Startup_upper.add(Startup_upper_3st, 2);

		Startup_down.add(new Blank_Panel(), 0);
		Startup_down.add(login_panel, 1);

		JPanel login_panel_1st = new JPanel();
		JPanel login_panel_2st = new JPanel();
		JPanel login_panel_3st = new JPanel();

		login_panel_1st.setOpaque(false);
		login_panel_2st.setOpaque(false);
		login_panel_3st.setOpaque(false);

		// TODO: Restart check
		Restart_Checker();
		if (is_restarted)
			JTF_ip.setText("서버와의 연결이 끊어졌습니다!");
		else
			JTF_ip.setText("buf.jollaman999.com");
		
		JTF_name.setText("사용자명을 입력하세요");
		
		JTF_ip.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (JTF_ip_input_counter == 0) {
					JTF_ip.setText("");
					JTF_ip_input_counter++;
				}
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
		});

		JTF_ip.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyTyped(KeyEvent e) {
				if (JTF_ip_input_counter == 0) {
					JTF_ip.setText("");
					JTF_ip_input_counter++;
				}
			}
		});

		JTF_name.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (JTF_name_input_counter == 0) {
					JTF_name.setText("");
					JTF_name_input_counter++;
				}
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
		});

		JTF_name.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				if (JTF_name_input_counter == 0) {
					JTF_name.setText("");
					JTF_name_input_counter++;
				}
				if (arg0.getKeyChar() == KeyEvent.VK_ENTER) {
					try {
						chat_socket = new Socket(JTF_ip.getText(), 7777);
					} catch (IOException e) {
						JTF_ip.setText("서버에 연결할 수 없습니다!");
						sp_R_down_Chat_Input.setText("서버 연결에 실패하였습니다!");
					}

					if (chat_socket != null) {
						frame_startup.setVisible(false);
						frame_main.setVisible(true);
						
						chat_name = JTF_name.getText();

						ClientReceiver clientReceiver = new ClientReceiver(
								chat_socket);
						new ClientSender(chat_socket);

						clientReceiver.start();

						setVisible(false);
					}

					JTF_ip_input_counter = 0;
					JTF_name_input_counter = 0;
				}
			}
		});
		

			
		JTF_ip.setPreferredSize(new Dimension(220, 30));
		JTF_name.setPreferredSize(new Dimension(220, 30));
		login_panel_button_area.setPreferredSize(new Dimension(100, 30));
		
		JTF_ip.setForeground(Color.CYAN);
		JTF_name.setForeground(Color.ORANGE);
		button_start.setForeground(Color.DARK_GRAY);
		
		JTF_ip.setHorizontalAlignment(JTextField.CENTER);
		JTF_name.setHorizontalAlignment(JTextField.CENTER);
		
		login_panel_1st.add(JTF_ip);
		login_panel_2st.add(JTF_name);
		login_panel_3st.add(login_panel_button_area);
		
		login_panel.add(login_panel_1st, 0);
		login_panel.add(login_panel_2st, 1);
		login_panel.add(login_panel_3st, 2);

		button_background_panel start_button_background = new button_background_panel();
		login_panel_button_area.add(start_button_background);
		
		start_button_background.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				try {
					chat_socket = new Socket(JTF_ip.getText(), 7777);
				} catch (IOException arg0) {
					JTF_ip.setText("서버에 연결할 수 없습니다!");
				}

				if (chat_socket != null) {
					frame_startup.setVisible(false);
					frame_main.setVisible(true);

					chat_name = JTF_name.getText();

					ClientReceiver clientReceiver = new ClientReceiver(
							chat_socket);
					new ClientSender(chat_socket);

					clientReceiver.start();
				}

				JTF_ip_input_counter = 0;
				JTF_name_input_counter = 0;
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});

		Startup_upper_2st.add(Startup_Label);
		Startup_Label.setFont(startup_font);
		Startup_Label.setForeground(Color.WHITE);

		// 컴포넌트 투명화
		Startup_upper.setOpaque(false);
		Startup_down.setOpaque(false);
		Startup_upper_1st.setOpaque(false);
		Startup_upper_2st.setOpaque(false);
		Startup_upper_3st.setOpaque(false);
		login_panel.setOpaque(false);
		JTF_ip.setOpaque(false);
		JTF_name.setOpaque(false);
		Startup_down.setOpaque(false);
		login_panel_button_area.setOpaque(false);
		frame_startup.setVisible(true);

		// TODO --------- 메인 프레임 관련 영역 -----------
		mainMenu_bar.add(main_file);
		main_file.add(file_chatmode);
		main_file.add(file_open);
		main_file.add(file_save);
		main_file.add(file_saveas);
		main_file.add(file_exit);
		
		sp_R_up_OpenDic_Show.setEditable(false);
		
		// TODO: 저장/열기
		file_open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sp_R_updown.setLeftComponent(sp_R_up_OpenDic_JSP);
				is_ChatMode = true;
				Change_Mode();
				
				try {
					File Dic_file = new File(Mywordlist_File);
					FileReader Dic_filereader = new FileReader(Dic_file);
					BufferedReader Dic_bufferedreader = new BufferedReader(
							Dic_filereader);
					StringBuffer read = new StringBuffer("");
					String tmps = new String("");

					try {
						while ((tmps = Dic_bufferedreader.readLine()) != null) {
							read.append(tmps);
							read.append("\r\n");
						}
					} catch (IOException e1) {
						sp_R_up_OpenDic_Show.setText("단어 리스트 파일을 읽어오는 중 문제가 발생하였습니다.\n"
								+ "\"Mywordlist.txt\" 파일이 다른곳에서 사용중인지 확인하고 해당 기능을 재실행 해주십시오.");
						return;
					}
					sp_R_up_OpenDic_Show.setText(read.toString());
					sp_R_up_OpenDic_Show.setEditable(true);
					try {
						Dic_bufferedreader.close();
						Dic_filereader.close();
					} catch (IOException e1) {
						sp_R_up_OpenDic_Show.setText("단어 리스트 파일을 닫는 중 문제가 발생하였습니다.");
					}
				} catch (FileNotFoundException e1) {
					sp_R_up_OpenDic_Show.setText("단어 리스트 파일이 존재 하지 않습니다.\n"
										+ "\"Mywordlist.txt\" 파일이 존재하는지 확인하고 해당 기능을 재실행 해주십시오.");
					return;
				}
			}
		});

		file_save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {StringBuffer read = new StringBuffer("");

				if ((!is_Wiki) && (!is_ChatMode)) {
					is_ChatMode = true;
					Change_Mode();
					
					try {

						File Dic_file = new File(Mywordlist_File);

						FileReader Dic_filereader = new FileReader(Dic_file);
						BufferedReader Dic_bufferedreader = new BufferedReader(
								Dic_filereader);
						String input;

						while ((input = Dic_bufferedreader.readLine()) != null) {
							read.append(input);
							read.append("\r\n");
						}

						Dic_bufferedreader.close();
						Dic_filereader.close();

						FileWriter DicFileWriter = new FileWriter(Dic_file);
						BufferedWriter Dic_bufferedWriter = new BufferedWriter(
								DicFileWriter);
						read.append(Mywordlist_to_Save);
						read.append(": ");
						read.append(sp_R_up_OpenDic_Show.getText());
						Dic_bufferedWriter.write(read.toString());
						Dic_bufferedWriter.close();

					} catch (FileNotFoundException e1) {
						sp_R_up_OpenDic_Show.setText("단어 리스트 파일이 존재 하지 않습니다.\n"
								+ "\"Mywordlist.txt\" 파일이 존재하는지 확인하고 해당 기능을 재실행 해주십시오.");
						return;
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				} else {
					is_ChatMode = true;
					Change_Mode();
					
					sp_R_up_OpenDic_Show
							.setText("저장하기: 해당 기능은 OpenDic 모드에서만 사용할수 있습니다. OpenDic모드로 전환 합니다.");
					sp_R_updown.setLeftComponent(sp_R_up_OpenDic_JSP);
					sp_R_updown.setDividerLocation(610);
					Dic_select_Wiki.setSelected(false);
					Dic_select_OpenDIC.setSelected(true);
				}
			}

		});
		
		sp_L_updown.setOrientation(JSplitPane.VERTICAL_SPLIT);
		sp_L_updown.setLeftComponent(sp_L_find_panel);
		sp_L_updown.setRightComponent(sp_L_down);
		sp_L_updown.setDividerLocation(350);

		sp_L_find_panel.add(sp_L_up, BorderLayout.CENTER);
		sp_L_find_panel.add(finder_panel, BorderLayout.NORTH);

		finder_panel.add(BorderLayout.CENTER, Finder);
		finder_panel.add(BorderLayout.EAST, find_button);

		Dic_select_OpenDIC.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				is_Wiki = false;
			}
		});
		
		Dic_select_Wiki.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				is_Wiki = true;
			}
		});

		Dic_select_OpenDIC.setSelected(true);
		Dic_select_Panel.add(Dic_select_OpenDIC);
		Dic_select_Panel.add(Dic_select_Wiki);
		Dic_select_btgroup.add(Dic_select_OpenDIC);
		Dic_select_btgroup.add(Dic_select_Wiki);
		finder_panel.add(BorderLayout.SOUTH, Dic_select_Panel);
		
		frame_main.setJMenuBar(mainMenu_bar);

		frame_main.setBackground(Color.WHITE);
		frame_main.setSize(1280, 720);
		frame_main.setContentPane(sp_leftright);
		frame_main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// TODO: wordlist 초기화
		wordlist[0] = new JTextField("====== 유사 단어 ======");
		wordlist[0].setFont(new Font("-구름L", Font.BOLD, 18));
		wordlist[0].setHorizontalAlignment(JTextField.CENTER);
		wordlist[0].setEditable(false);
		wordlist[0].setBorder(BorderFactory.createEmptyBorder());
		wordlist[0].setOpaque(false);
		commonlist_background_panel_down.add(wordlist[0]);
		sp_L_commonWordArea.add(commonlist_background_panel_down);
		
		for (int i = 1; i < 10; i++) {
			wordlist[i] = new JTextField("");
			wordlist[i].setEditable(false);
			sp_L_commonWordArea.add(wordlist[i]);
		}
		sp_L_commonWordArea.setBackground(new Color(255, 255, 255));
		
		wordlist_history[0] = new JTextField("====== 검색 기록 ======");
		wordlist_history[0].setFont(new Font("-구름L",Font.CENTER_BASELINE ,18));
		wordlist_history[0].setBackground(new Color(255, 255, 0));
		wordlist_history[0].setHorizontalAlignment(JTextField.CENTER);
		wordlist_history[0].setOpaque(false);
		wordlist_history[0].setBorder(BorderFactory.createEmptyBorder());
		wordlist_history[0].setEditable(false);
		
		sp_L_WordHistoryArea.add(commonlist_background_panel_up);
		commonlist_background_panel_up.add(wordlist_history[0]);
		
		for (int i = 1; i < 10; i++) {
			wordlist_history[i] = new JTextField("");
			wordlist_history[i].setEditable(false);
			sp_L_WordHistoryArea.add(wordlist_history[i]);
		}
		sp_L_WordHistoryArea.setBackground(new Color(255, 255, 255));

		sp_R_down.setSize(200, 200);

		sp_R.setLayout(bl_main);
		sp_R_updown.setOrientation(JSplitPane.VERTICAL_SPLIT);
		sp_R_updown.setLeftComponent(sp_R_up_OpenDic_JSP);
		sp_R_updown.setRightComponent(sp_R_down);
		sp_R_updown.setDividerLocation(610);

		sp_R_up_Chat_Show.setEditable(false);
		sp_R_up_Chat_Show.setLineWrap(true);
		sp_R_down_Chat_Input.setEditable(false);

		sp_R_down_Chat_Input.setLineWrap(true);
		sp_R.add(sp_R_updown);

		sp_leftright.setLeftComponent(sp_L_updown);
		sp_leftright.setRightComponent(sp_R);
		sp_leftright.setDividerLocation(260);

		sp_R_up_Wiki_Broswer.setEditable(false);
		
		
		
	}

	// 채팅 클라이어트 리시버
	class ClientReceiver extends Thread {
		Socket socket;
		DataInputStream input;
		JScrollBar scrollbar = sp_R_up_Chat_JSP.getVerticalScrollBar();
		JScrollBar scrollbar_L_U = sp_L_up.getVerticalScrollBar();
		JScrollBar scrollbar_L_D = sp_L_down.getVerticalScrollBar();
		
		String message = new String();

		public ClientReceiver(Socket socket) {
			this.socket = socket;
			try {
				if (socket != null)
					input = new DataInputStream(socket.getInputStream());
				else {
					Client_Disconnected();
				}
			} catch (IOException e) {
			}
		}

		@Override
		public void run() {
			while (input != null) {
				try {
					message = input.readUTF();
					message_specifier();
				} catch (IOException e) {
				}
			}
		}
		
		void message_specifier() {
			if (message.matches("CHAT_DATA.*")) {

				message = message.substring("CHAT_DATA".length());

				sp_R_up_Chat_Show.append(message + "\n");
				scrollbar.setValue(scrollbar.getMaximum()); // 맨아래로 스크롤
			} else { // TODO: 유사단어 스플릿
				String read;
				String[] reads;
				String[] reads_enter;
				String[] words;
				
				StringBuffer output = new StringBuffer("");

				read = message;

				if (read.matches(".*님이 OpenDic.*"))
					return;

				if (!read.matches(".*#.*"))
					return;

				reads = read.split("@");
				reads_enter = reads[0].split("=");
				
				for (int i = 0; i < reads_enter.length; i++)
					output.append(reads_enter[i] + "\n");
				
				sp_R_up_OpenDic_Show.setText(output.toString());

				words = reads[1].split("#");

				for (int i = 1; i < wordlist.length; i++)
					wordlist[i].setText("");

				for (int i = 1; i < words.length; i++)
					wordlist[i].setText(words[i]);
			}
		}
	}

	// 빈칸 패널
	class Blank_Panel extends JPanel {
		private static final long serialVersionUID = 1L;

		public Blank_Panel() {
			setVisible(false);
		}
	}
	
	// 배경화면을 추가한 백패널
	class back_Panel extends JPanel {
		private static final long serialVersionUID = 1L;
		private ImageIcon bkgImg = new ImageIcon("./worldlist_bg.png");

		private Dimension d;

		public void paintComponent(Graphics page) // 배경 이미지 그리기
		{

			super.paintComponent(page);
			d = getSize();
			page.drawImage(bkgImg.getImage(), 0, 0, d.width, d.height, null);
		}

	}

	class button_background_panel extends JPanel {
		private static final long serialVersionUID = 1L;
		private ImageIcon bkgImg = new ImageIcon("./enter_bg.jpg");

		private Dimension d;

		public void paintComponent(Graphics page) // 배경 이미지 그리기
		{

			super.paintComponent(page);
			d = getSize();
			page.drawImage(bkgImg.getImage(), 0, 0, d.width, d.height, null);
		}

	}

	// 배경화면을 추가한 패널
	class startup_BACKGROUND_Panel extends JPanel {
		private static final long serialVersionUID = 1L;
		private ImageIcon bkgImg = new ImageIcon("./2.png");

		private Dimension d;

		public void paintComponent(Graphics page) // 배경 이미지 그리기
		{

			super.paintComponent(page);
			d = getSize();
			page.drawImage(bkgImg.getImage(), 0, 0, d.width, d.height, null);
		}

	}

	// 채팅 클라이언트 센더
	class ClientSender {
		Socket socket;
		DataOutputStream output;
		String msg = "";
		JScrollBar scrollbar = sp_R_up_Chat_JSP.getVerticalScrollBar();
		boolean is_lineadd = false;

		void Dic_Word_Sender() {
			sp_R_updown.setDividerLocation(610);
			Finder.setText(msg);
			Mywordlist_to_Save = msg;
			
			try {
				output.writeUTF("DIC_MODE");
			} catch (IOException e) {
				Client_Disconnected();
				return;
			}
			
			if (is_ChatMode) {	// 현재 모드가 채팅 모드이며 사전 모드로 전환
				is_ChatMode = false;
				file_chatmode.setText("채팅 모드 전환");
				sp_R_down_Chat_Input.setEditable(false);
			}
			
			if (is_Wiki) {
				sp_R_up_Wiki_Broswer.setText("해당 검색어는 Wiki 에 존재하지 않는 단어 입니다.");
				sp_R_updown.setLeftComponent(sp_R_up_Wiki_JSP);
				
				for (int i = 1; i < wordlist.length; i++)
					wordlist[i].setText("");
				
				wordlist[1].setText("Wiki 에서는 유사 단어 기능이");
				wordlist[2].setText("제공되지 않습니다.");
				
				try {
					sp_R_up_Wiki_Broswer.setPage("http://ko.wikipedia.org/wiki/"
							+ URLEncoder.encode(msg, "UTF-8"));
					sp_R_down_Chat_Input.setText("Wiki 로 부터 사전 검색을 합니다.");
				} catch (UnsupportedEncodingException e1) {
					sp_R_down_Chat_Input.setText("Wiki 페이지 로드 중 주소 형식이 잘못 되었습니다!");
				} catch (FileNotFoundException e1) {
					sp_R_down_Chat_Input.setText("해당 검색어는 Wiki 에 존재하지 않는 단어 입니다.");
				} catch (IOException e1) {
					sp_R_down_Chat_Input.setText("사전 로드에 실패하였습니다!");
				}
			} else {
				sp_R_up_OpenDic_Show.setText("해당 검색어는 OpenDic 에 존재하지 않는 단어 입니다.");				
				sp_R_updown.setLeftComponent(sp_R_up_OpenDic_JSP);
				sp_R_down_Chat_Input.setText("OpenDic 으로 부터 사전 검색을 합니다.");
				try {
					output.writeUTF(msg);
				} catch (IOException e1) {
					Client_Disconnected();
				}
			}
		}
		
		// TODO: 단어 검색 큐
		void wordlist_history_allocator() {
			for (int i = 2; i < wordlist_history_queue.length; i++) {
				if (msg.equals(wordlist_history_queue[i - 1]))
					break;
				wordlist_history_queue[i] = wordlist_history[i - 1].getText();
			}

			for (int i = 1; i < wordlist_history_queue.length; i++) {
				if (msg.equals(wordlist_history_queue[i]))
					break;
				wordlist_history[i].setText(wordlist_history_queue[i]);
			}
			
			wordlist_history[1].setText(msg);
		}

		public ClientSender(Socket socket) {
			this.socket = socket;
			try {
				if (socket != null) {
					output = new DataOutputStream(socket.getOutputStream());
					output.writeUTF(chat_name);

					sp_R_up_Chat_Show.append("대화방에 입장하였습니다." + "\n");
					sp_R_down_Chat_Input.setText("");
				} else {
					Client_Disconnected();
				}
			} catch (Exception e) {
			}
			
			for (int i = 1; i < wordlist_history_queue.length; i++) {
				wordlist_history_queue[i] = "";
				wordlist_history[i].setText(wordlist_history_queue[i]);
			}
			
			sp_R_down_Chat_Input.addKeyListener(new KeyListener() {
				@Override
				public void keyPressed(KeyEvent arg0) {
					// 줄바꿈: Shift + Enter
					if (arg0.getKeyCode() == KeyEvent.VK_SHIFT) {
						is_lineadd = true;
					}
					if (arg0.getKeyCode() == KeyEvent.VK_ENTER && is_lineadd) {
						sp_R_down_Chat_Input.append("\n");
					}

					// 메세지 창에서 엔터를 치면 메세지를 전송
					if (arg0.getKeyCode() == KeyEvent.VK_ENTER && !is_lineadd) {
						try {
							output.writeUTF("CHAT_MODE");
						} catch (IOException e) {
							Client_Disconnected();
							return;
						}
						
						msg = sp_R_down_Chat_Input.getText();
						
						if (msg.equals("exit")) {
							try {
								output.writeUTF("EXIT");
							} catch (IOException e) {
							}
							System.exit(0);
						}
							
						try {
							output.writeUTF(getTime() + " [" + chat_name + "] " + msg);
						} catch (IOException e) {
							Client_Disconnected();
						}
						sp_R_down_Chat_Input.setText(""); // 입력 창 비움
						scrollbar.setValue(scrollbar.getMaximum()); // 맨아래로 스크롤
					}
				}

				@Override
				public void keyReleased(KeyEvent arg0) {
					// 입력창에 엔터를 치면서 생기는 빈 줄 제거
					if (arg0.getKeyCode() == KeyEvent.VK_ENTER && !is_lineadd)
						sp_R_down_Chat_Input.setText("");
					else if (arg0.getKeyCode() == KeyEvent.VK_SHIFT)
						is_lineadd = false;
				}

				@Override
				public void keyTyped(KeyEvent arg0) {
				}
			});
			
			// TODO: Finder
			find_button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					msg = Finder.getText();
					if (msg.equals(""))
						return;
					
					Dic_Word_Sender();
					wordlist_history_allocator();
					Finder.setText("");
					sp_R_up_OpenDic_Show.setEditable(false);
				}
			});

			Finder.addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent arg0) {
				}

				@Override
				public void keyReleased(KeyEvent arg0) {
				}

				@Override
				public void keyPressed(KeyEvent arg0) {
					if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
						msg = Finder.getText();
						if (msg.equals(""))
							return;
						
						Dic_Word_Sender();
						wordlist_history_allocator();
						Finder.setText("");
						sp_R_up_OpenDic_Show.setEditable(false);
					}
				}
			});
			
			Finder.addMouseListener(new MouseListener() {				
				@Override
				public void mouseReleased(MouseEvent e) {					
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
					Finder.setText("");
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
				}
			});
			
			// TODO: wordlist
			class Wordlist_Handler implements MouseListener {
				int index = 0;
				
				public Wordlist_Handler (int i) {
					this.index = i;
				}
				@Override
				public void mouseReleased(MouseEvent e) {
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
					msg = wordlist[index].getText();
					if (msg.equals(""))
						return;
					
					Dic_Word_Sender();
					wordlist_history_allocator();
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
				}				
			}

			wordlist[1].addMouseListener(new Wordlist_Handler(1));
			wordlist[2].addMouseListener(new Wordlist_Handler(2));
			wordlist[3].addMouseListener(new Wordlist_Handler(3));
			wordlist[4].addMouseListener(new Wordlist_Handler(4));
			wordlist[5].addMouseListener(new Wordlist_Handler(5));
			wordlist[6].addMouseListener(new Wordlist_Handler(6));
			wordlist[7].addMouseListener(new Wordlist_Handler(7));
			wordlist[8].addMouseListener(new Wordlist_Handler(8));
			wordlist[9].addMouseListener(new Wordlist_Handler(9));
			
			// TODO: wordlist_history
			class Wordlist_History_Handler implements MouseListener {
				int index = 0;
				
				public Wordlist_History_Handler (int i) {
					this.index = i;
				}
				
				@Override
				public void mouseReleased(MouseEvent e) {
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
					msg = wordlist_history[index].getText();
					if (msg.equals(""))
						return;
					
					Dic_Word_Sender();
					wordlist_history_allocator();
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
				}				
			}

			wordlist_history[1].addMouseListener(new Wordlist_History_Handler(1));
			wordlist_history[2].addMouseListener(new Wordlist_History_Handler(2));
			wordlist_history[3].addMouseListener(new Wordlist_History_Handler(3));
			wordlist_history[4].addMouseListener(new Wordlist_History_Handler(4));
			wordlist_history[5].addMouseListener(new Wordlist_History_Handler(5));
			wordlist_history[6].addMouseListener(new Wordlist_History_Handler(6));
			wordlist_history[7].addMouseListener(new Wordlist_History_Handler(7));
			wordlist_history[8].addMouseListener(new Wordlist_History_Handler(8));
			wordlist_history[9].addMouseListener(new Wordlist_History_Handler(9));
						
			file_chatmode.addActionListener(new ActionListener() {			
				@Override
				public void actionPerformed(ActionEvent arg0) {	
					Change_Mode();
				}
			});
			
			file_exit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						output.writeUTF("EXIT");
					} catch (IOException arg0) {
					}
					System.exit(0);
				}
			});
		}
	}
	
	// TODO: 채팅/사전 전환
	void Change_Mode() {
		if (is_ChatMode) {	// 현재 모드가 채팅 모드이며 사전 모드로 전환
			is_ChatMode = false;
			sp_R_updown.setDividerLocation(610);
			file_chatmode.setText("채팅 모드 전환");
			sp_R_down_Chat_Input.setEditable(false);
			if (is_Wiki)
				sp_R_updown.setLeftComponent(sp_R_up_Wiki_JSP);
			else
				sp_R_updown.setLeftComponent(sp_R_up_OpenDic_JSP);
			sp_R_down_Chat_Input.setText("사전 모드로 전환 되었습니다."
						+ "이제부터 이 곳에는 프로그램 상태가 표시되며 대화를 입력 하실 수 없습니다.");
		} else {	// 현재 모드가 사전 모드이며 채팅 모드로 전환
			is_ChatMode = true;
			sp_R_updown.setDividerLocation(460);
			file_chatmode.setText("사전 모드 전환");
			sp_R_down_Chat_Input.setEditable(true);
			sp_R_updown.setLeftComponent(sp_R_up_Chat_JSP);
			sp_R_down_Chat_Input.setText("");
			sp_R_up_Chat_Show.append("채팅 모드로 전환 되었습니다."
							+ "이제부터 아래 대화창에 대화를 입력 하실 수 있습니다.\n");
		}
	}
	
	public static void restartApplication_jar() {
		final String javaBin = System.getProperty("java.home") + File.separator
				+ "bin" + File.separator + "java";
		File currentJar = null;
		try {
			currentJar = new File(Client.class.getProtectionDomain()
					.getCodeSource().getLocation().toURI());
		} catch (URISyntaxException e) {
		}

		/* is it a jar file? */
		if (!currentJar.getName().endsWith(".jar"))
			return;

		/* Build command: java -jar application.jar */
		final ArrayList<String> command = new ArrayList<String>();
		command.add(javaBin);
		command.add("-jar");
		command.add(currentJar.getPath());

		final ProcessBuilder builder = new ProcessBuilder(command);
		try {
			builder.start();
		} catch (IOException e) {}
		System.exit(0);
	}
	
	static void Restart_Writer() {
		File Restart_WriteFile = new File("tmp.txt");

		try {
			Restart_WriteFile.createNewFile();
		} catch (IOException e) {
		}

		FileWriter RestartCheckFileWriter = null;

		try {
			RestartCheckFileWriter = new FileWriter(Restart_WriteFile);
		} catch (IOException e1) {
		}

		BufferedWriter Restart_WriteBufferedWriter = new BufferedWriter(
				RestartCheckFileWriter);

		try {
			Restart_WriteBufferedWriter.write("1");
		} catch (IOException e) {
		}

		try {
			Restart_WriteBufferedWriter.close();
		} catch (IOException e) {
		}

	}

	static void Restart_Checker() {
		String s_tmp = new String();

		File Restart_CheckFile = new File("tmp.txt");

		FileReader RestartCheckFileReader = null;

		try {
			RestartCheckFileReader = new FileReader(Restart_CheckFile);
		} catch (IOException e1) {
			return;
		}

		BufferedReader Restart_CheckBufferedReader = new BufferedReader(
				RestartCheckFileReader);

		try {
			s_tmp = Restart_CheckBufferedReader.readLine();
		} catch (IOException e) {
		}

		if (s_tmp.matches("1.*"))
			is_restarted = true;

		Restart_CheckFile.delete();
		try {
			Restart_CheckBufferedReader.close();
		} catch (IOException e) {}
	}
	
	static void Client_Disconnected() {		
		frame_startup.setVisible(true);
		frame_main.setVisible(false);
		JTF_ip.setText("서버와의 연결이 끊어졌습니다!");
		try {
			if (chat_socket != null)
				chat_socket.close();
		} catch (IOException e) {
		}
		Restart_Writer();
		restartApplication_jar();
	}
	
	// 현재 시간 얻어오기
	static String getTime() {
		SimpleDateFormat f = new SimpleDateFormat("[hh:mm:ss]");
		return f.format(new Date());
	}

	public static void main(String[] args) {
		new Client();
	}
}
