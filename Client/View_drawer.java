import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLEncoder;

public class View_drawer extends JFrame {
	private static final long serialVersionUID = 1L;

	static int isPlayer = 0;

	// ------- 시작 창 관련 영역 --------
	BorderLayout bl_main = new BorderLayout();
	JButton button_start = new JButton("이 사항을 확인하고 시작합니다.");
	Color FrameColor = new Color(238, 238, 238, 255);
	JPanel login_panel = new JPanel(new GridLayout(4, 1, 0, 1));
	JPanel login_panel_button_area = new JPanel(new GridLayout(1, 3));
	JTextField login_ID = new JTextField();
	JPasswordField login_PD = new JPasswordField();
	JButton login_button = new JButton("로그인");
	JButton anonymous_button = new JButton("비회원");
	JButton Signin_button = new JButton("회원가입");

	// ------- 메인 프레임 관련 영역 --------
	JSplitPane sp_leftright = new JSplitPane();
	JSplitPane sp_updown = new JSplitPane();
	JTextField Finder = new JTextField();
	JTextArea sp_L_commonWord = new JTextArea("");
	JPanel sp_L_find_panel = new JPanel(new BorderLayout());
	JTextArea sp_L_Friendlist = new JTextArea("");
	JPanel Startup_upper = new JPanel(new GridLayout(3, 1));
	JPanel Startup_upper_1st = new JPanel();
	JPanel Startup_upper_2st = new JPanel(new FlowLayout());
	JPanel Startup_upper_3st = new JPanel();
	JPanel Startup_upper_4st = new JPanel();
	JPanel Startup_upper_5st = new JPanel();
	JPanel Startup_down = new JPanel(new GridLayout(3, 1));
	JPanel finder_panel = new JPanel(new BorderLayout());
	JButton find_button = new JButton("검색");
	startup_BACKGROUND_Panel startup_panel = new startup_BACKGROUND_Panel();

	// 사전 뷰 패널
	JEditorPane Dic_Broswer = new JEditorPane();
	JScrollPane Dic_JSP = new JScrollPane(Dic_Broswer);

	JLabel Startup_Label = new JLabel("OpenDIC");
	Font startup_font = new Font("Broadway", Font.BOLD, 50);

	JPanel sp_L = new JPanel(new GridLayout(2, 1));
	JScrollPane sp_L_up = new JScrollPane(sp_L_commonWord,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

	JScrollPane sp_L_down = new JScrollPane(sp_L_Friendlist,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

	JPanel sp_R = new JPanel();
	JTextArea sp_R_up_Chat_Show = new JTextArea("");
	JTextArea sp_R_down_Chat_Input = new JTextArea("");

	JScrollPane sp_R_up = new JScrollPane(sp_R_up_Chat_Show,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	JScrollPane sp_R_down = new JScrollPane(sp_R_down_Chat_Input,
			JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

	JFrame frame_startup = new JFrame("OpenDIC v1.0");
	JPanel start_up_main_panel = new JPanel();

	JFrame frame_main = new JFrame("OpenDIC v1.0");
	Start_Button_Handler Start_Button_Clicked = new Start_Button_Handler();
	Dialog_Server_Connect Dlog_server_connect = new Dialog_Server_Connect(
			frame_main, "연결");

	// 채팅 대화명, 채팅 소켓
	private String chat_name;
	private Socket chat_socket;

	private boolean is_connected = false;

	// 시작 버튼 핸들러
	class Start_Button_Handler implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent arg0) {
			frame_startup.setVisible(false);
			frame_main.setVisible(true);
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
	}

	// 서버 접속 다이얼 로그
	static JTextField JTF_ip = new JTextField();
	static JTextField JTF_name = new JTextField();
	static int JTF_ip_input_counter = 0;
	static int JTF_name_input_counter = 0;

	class Dialog_Server_Connect extends JDialog {
		private static final long serialVersionUID = 1L;
		JButton okButton = new JButton("연결 하기");

		public Dialog_Server_Connect(JFrame frame, String title) {
			super(frame, title);
			setLayout(new GridLayout(3, 1));

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

			this.addWindowListener(new WindowListener() {
				@Override
				public void windowActivated(WindowEvent e) {
				}

				@Override
				public void windowClosed(WindowEvent e) {
				}

				@Override
				public void windowClosing(WindowEvent e) {
					JTF_ip_input_counter = 0;
					JTF_name_input_counter = 0;
				}

				@Override
				public void windowDeactivated(WindowEvent e) {
				}

				@Override
				public void windowDeiconified(WindowEvent e) {
				}

				@Override
				public void windowIconified(WindowEvent e) {
				}

				@Override
				public void windowOpened(WindowEvent e) {
				}
			});

			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						chat_socket = new Socket(JTF_ip.getText(), 7777);
					} catch (IOException e) {
						JTF_ip.setText("서버에 연결할 수 없습니다!");
						sp_R_down_Chat_Input.setText("서버 연결에 실패하였습니다!");
					}

					if (chat_socket != null) {
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
			});

			add(okButton);
			add(JTF_ip);
			add(JTF_name);
			setSize(200, 200);
		}
	}

	// 메인 화면
	public View_drawer() {

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

		JPanel login_panel_1st = new JPanel(new GridLayout(1, 3, 10, 10));
		JPanel login_panel_2st = new JPanel(new GridLayout(1, 3, 10, 10));
		JPanel login_panel_3st = new JPanel(new GridLayout(1, 3, 10, 10));

		login_panel_1st.setOpaque(false);
		login_panel_2st.setOpaque(false);
		login_panel_3st.setOpaque(false);

		login_panel_1st.add(new Blank_Area(), 0);
		login_panel_1st.add(login_ID, 1);
		login_panel_1st.add(new Blank_Area(), 2);

		login_panel_2st.add(new Blank_Area(), 0);
		login_panel_2st.add(login_PD, 1);
		login_panel_2st.add(new Blank_Area(), 2);

		login_panel_3st.add(new Blank_Area(), 0);
		login_panel_3st.add(login_panel_button_area, 1);
		login_panel_3st.add(new Blank_Area(), 2);
		login_panel.add(login_panel_1st, 0);
		login_panel.add(login_panel_2st, 1);
		login_panel.add(login_panel_3st, 2);
		login_panel_button_area.add(login_button);
		login_panel_button_area.add(anonymous_button);
		login_panel_button_area.add(Signin_button);

		Startup_upper_2st.add(Startup_Label);
		Startup_Label.setFont(startup_font);
		Startup_Label.setForeground(Color.WHITE);

		login_button.setFont(new Font("굴림체", Font.BOLD, 11));
		login_button.setMargin(new Insets(0, 0, 0, 0));

		anonymous_button.setFont(new Font("굴림체", Font.BOLD, 11));
		anonymous_button.setMargin(new Insets(0, 0, 0, 0));

		anonymous_button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				isPlayer = 0;
				frame_startup.setVisible(false);
				frame_main.setVisible(true);

			}
		});

		Signin_button.setFont(new Font("굴림체", Font.BOLD, 10));
		Signin_button.setMargin(new Insets(0, 0, 0, 0));

		// 컴포넌트 투명화
		{
			Startup_upper.setOpaque(false);
			Startup_down.setOpaque(false);
			Startup_upper_1st.setOpaque(false);
			Startup_upper_2st.setOpaque(false);
			Startup_upper_3st.setOpaque(false);
			login_panel.setOpaque(false);
			login_ID.setOpaque(false);
			login_PD.setOpaque(false);
			Startup_down.setOpaque(false);
			login_panel_button_area.setOpaque(false);
			login_button.setOpaque(false);
			anonymous_button.setOpaque(false);
			frame_startup.setVisible(true);
		}

		button_start.addMouseListener(Start_Button_Clicked);

		// TODO --------- 메인 프레임 관련 영역 -----------

		JMenuBar mainMenu_bar = new JMenuBar();

		JMenu main_file = new JMenu("파일");
		JMenuItem file_connect_server = new JMenuItem("서버 접속");
		JMenuItem file_open = new JMenuItem("불러오기");
		JMenuItem file_save = new JMenuItem("저장하기");
		JMenuItem file_saveas = new JMenuItem("다른 이름으로 저장");
		JMenuItem file_exit = new JMenuItem("종료");

		mainMenu_bar.add(main_file);
		main_file.add(file_connect_server);
		main_file.add(file_open);
		main_file.add(file_save);
		main_file.add(file_saveas);
		main_file.add(file_exit);

		sp_L.add(sp_L_find_panel, 0);
		sp_L.add(sp_L_down, 1);

		sp_L_find_panel.add(sp_L_up, BorderLayout.CENTER);
		sp_L_find_panel.add(finder_panel, BorderLayout.NORTH);

		finder_panel.add(BorderLayout.CENTER, Finder);
		finder_panel.add(BorderLayout.EAST, find_button);
		frame_main.setJMenuBar(mainMenu_bar);

		file_connect_server.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Dlog_server_connect.setVisible(true);
				JTF_ip.setText("서버 ip주소를 입력하세요");
				JTF_name.setText("대화명을 입력하세요");
			}
		});

		file_exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		frame_main.setBackground(Color.WHITE);
		frame_main.setSize(1280, 720);
		frame_main.setContentPane(sp_leftright);
		frame_main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		find_button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Dic_Broswer.setPage("http://ko.wikipedia.org/wiki/"
							+ URLEncoder.encode(Finder.getText(), "UTF-8"));
				} catch (UnsupportedEncodingException e1) {

					e1.printStackTrace();
				} catch (IOException e1) {
					sp_R_down_Chat_Input.setText("사전 로드에 실패하였습니다!");

					e1.printStackTrace();
				}

			}
		});
		// TODO: Finder
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
					sp_updown.setLeftComponent(Dic_JSP);
					sp_updown.setDividerLocation(460);
					try {
						Dic_Broswer.setPage("http://ko.wikipedia.org/wiki/"
								+ URLEncoder.encode(Finder.getText(), "UTF-8"));
					} catch (IOException e) {
						sp_R_down_Chat_Input.setText("사전 로드에 실패하였습니다!");
					}
				}
			}
		});

		sp_L_commonWord.setEditable(false);
		sp_L_Friendlist.setEditable(false);
		sp_R_up.setSize(200, 200);

		sp_R_down.setSize(200, 200);

		sp_R.setLayout(bl_main);
		sp_updown.setOrientation(JSplitPane.VERTICAL_SPLIT);
		sp_updown.setLeftComponent(sp_R_up);
		sp_updown.setRightComponent(sp_R_down);
		sp_updown.setDividerLocation(460);

		sp_R_up_Chat_Show.setEditable(false);
		sp_R_up_Chat_Show.setLineWrap(true);
		sp_R_down_Chat_Input.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				if (!is_connected)
					sp_R_down_Chat_Input.setText("서버에 연결해 주십시오!");
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
			}

			@Override
			public void keyPressed(KeyEvent arg0) {
				if (!is_connected)
					sp_R_down_Chat_Input.setText("서버에 연결해 주십시오!");
			}
		});

		sp_R_down_Chat_Input.setLineWrap(true);
		sp_R.add(sp_updown);

		sp_leftright.setLeftComponent(sp_L);
		sp_leftright.setRightComponent(sp_R);
		sp_leftright.setDividerLocation(260);

		Dic_Broswer.setEditable(false);
	}

	// 채팅 클라이어트 리시버
	class ClientReceiver extends Thread {
		Socket socket;
		DataInputStream input;
		JScrollBar scrollbar = sp_R_up.getVerticalScrollBar();
		JScrollBar scrollbar_L_U = sp_L_up.getVerticalScrollBar();
		JScrollBar scrollbar_L_D = sp_L_down.getVerticalScrollBar();

		public ClientReceiver(Socket socket) {
			this.socket = socket;
			try {
				if (socket != null)
					input = new DataInputStream(socket.getInputStream());
				else
					sp_R_down_Chat_Input.setText("서버 연결에 실패하였습니다!");
			} catch (IOException e) {
			}
		}

		@Override
		public void run() {
			while (input != null) {
				try {
					sp_R_up_Chat_Show.append(input.readUTF() + "\n");
					scrollbar.setValue(scrollbar.getMaximum()); // 맨아래로 스크롤
				} catch (IOException e) {
				}
			}
		}
	}

	// 빈칸 버튼
	class Blank_Area extends JButton {
		private static final long serialVersionUID = 1L;

		public Blank_Area() {
			setVisible(false);
		}
	}

	// 빈칸 패널
	class Blank_Panel extends JPanel {
		private static final long serialVersionUID = 1L;

		public Blank_Panel() {
			setVisible(false);
		}
	}

	// 배경화면을 추가한 패널
	class startup_BACKGROUND_Panel extends JPanel {
		private static final long serialVersionUID = 1L;
		private ImageIcon bkgImg = new ImageIcon(
				"./2.png");

		/*
		 * private ImageIcon bkgImg = new ImageIcon( "F:/picture/주니엘/ju2.jpg");
		 */

		private Dimension d;

		public void paintComponent(Graphics page) // 배경 이미지 그리기
		{

			super.paintComponent(page);
			d = getSize();
			page.drawImage(bkgImg.getImage(), 0, 0, d.width, d.height, null);
		}

	}

	// 채팅 클라이언트 센더
	class ClientSender implements KeyListener {
		Socket socket;
		DataOutputStream output;
		String msg = "";
		JScrollBar scrollbar = sp_R_up.getVerticalScrollBar();
		boolean is_lineadd = false;

		public ClientSender(Socket socket) {
			this.socket = socket;
			try {
				if (socket != null) {
					output = new DataOutputStream(socket.getOutputStream());
					output.writeUTF(chat_name);

					sp_R_up_Chat_Show.append("대화방에 입장하였습니다." + "\n");
					sp_R_down_Chat_Input.setText("");

					is_connected = true;
				} else
					sp_R_down_Chat_Input.setText("서버에 연결해 주십시오!");
			} catch (Exception e) {
			}
			sp_R_down_Chat_Input.addKeyListener(this);
		}

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
				msg = sp_R_down_Chat_Input.getText();
				if (msg.equals("exit"))
					System.exit(0);
				try {
					output.writeUTF("[" + chat_name + "] " + msg);
				} catch (IOException e) {
					sp_R_up_Chat_Show.append("서버와의 연결이 끊어졌습니다!" + "\n");
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
	}
}