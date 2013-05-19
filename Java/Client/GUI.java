import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.text.*;
import java.awt.*;

public class GUI {
	// Main Frame
	JFrame frame;
	// Menu BAr
	JMenuBar MenuBar;
	// Menu on menu bar
	JMenu File;
	// Menu item under the menu
	JMenuItem exit;
	// Labels for naming the boxes and copyright tag
	JLabel chatLabel, contactLabel, profilePic;
	// upload and send buttons
	JButton sendButton, uploadButton;
	// chat windows and the users type in box
	JTextArea chatWindow, typeWindow;
	// List of contacts
	JList<DefaultListModel<String>> usrsList;
	// Manage contacts
	DefaultListModel<String> contactListModel;
	// scroll for all thre boxes
	JScrollPane chatWindowPane, typeWindowPane, contactPane;
	// calculate dimensions
	Dimension dim;
	// array to store contact names
	ArrayList<String> _contacts = new ArrayList<String>();
	// to get the current path
	File current_path = new File(".");
	// present working directory is stored here
	String pwd = "";
	Calendar cal = Calendar.getInstance();
	String calandr = cal.getTime().toString();
	String trim = "";
	String message = "";
	String selected = "Chat Box";
	String date = calandr.substring(0, calandr.lastIndexOf(":") - 9) + " "
			+ calandr.substring(calandr.length() - 4, calandr.length());
	BreakIterator bi = BreakIterator.getWordInstance();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	GUI() {
		try {
			pwd = current_path.getCanonicalPath();
		} catch (Exception x) {
		}
		frame = new JFrame();
		exit = new JMenuItem("Exit");
		MenuBar = new JMenuBar();
		File = new JMenu("File");
		frame.setLayout(null);
		// profile picture
		profilePic = new JLabel();
		profilePic.setBounds(10, 5, 48, 48);
		frame.add(profilePic);

		// Chat label
		chatLabel = new JLabel(selected);
		chatLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
		chatLabel.setBounds(50, 15, 200, 30);
		frame.add(chatLabel);

		// Contact label
		contactLabel = new JLabel("Contacts");
		contactLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
		contactLabel.setIcon(new ImageIcon(pwd + "/icons/onlineS.png"));
		contactLabel.setBounds(370, 15, 200, 30);
		frame.add(contactLabel);

		// Main chat display box
		chatWindow = new JTextArea();
		chatWindow.setFont(new Font("Times New Roman", Font.BOLD, 15));
		chatWindowPane = new JScrollPane(chatWindow);
		chatWindowPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		chatWindowPane.setBounds(10, 50, 350, 300);
		chatWindow.setEditable(false);
		frame.add(chatWindowPane);

		// User area to type
		typeWindow = new JTextArea();
		typeWindow.setSize(20, 10);
		typeWindowPane = new JScrollPane(typeWindow);
		typeWindowPane.setBounds(10, 380, 350, 55);
		typeWindow.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent k) {
				if (k.getKeyChar() == KeyEvent.VK_ENTER) {
					if (k.isShiftDown()) {
						typeWindow.append(" \n");
					} else {
						if (selected != "Chat Box") {
							raw_msg(typeWindow.getText());
							typeWindow.setText(null);
						} else {
							JOptionPane.showMessageDialog(null,
									"Contact not slected",
									"Team OpenFire Messenger",
									JOptionPane.WARNING_MESSAGE);
						}
					}
				}
			}
		});
		frame.add(typeWindowPane);

		// Send button
		sendButton = new JButton("SEND");
		sendButton.setBounds(395, 380, 80, 25);
		sendButton.setFont(new Font("Times New Roman", Font.BOLD, 10));
		frame.add(sendButton);

		// Upload button
		uploadButton = new JButton("UPLOAD");
		uploadButton.setBounds(395, 410, 80, 25);
		uploadButton.setFont(new Font("Times New Roman", Font.BOLD, 10));
		frame.add(uploadButton);

		// Contact display box
		contactListModel = new DefaultListModel<String>();
		usrsList = new JList(contactListModel);
		usrsList.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		usrsList.setFont(new Font("Times New Roman", Font.BOLD, 15));
		try {
			contacts();
		} catch (Exception e) {
		}
		for (int i = 0; i < _contacts.size(); i++) {
			contactListModel.addElement(_contacts.get(i) + " ");
		}
		contactPane = new JScrollPane(usrsList);
		contactPane.setBounds(370, 50, 140, 300);
		usrsList.addListSelectionListener(new ListSelectionListener() {

			// contact selection
			public void valueChanged(ListSelectionEvent arg0) {
				if (!arg0.getValueIsAdjusting()) {
					profilePic
							.setIcon(new ImageIcon(pwd + "/contacts/pic.jpg"));
					selected = usrsList.getSelectedValue().toString();
					chatLabel.setText(selected);
				}
			}
		});
		frame.add(contactPane);

		// Menubar setup
		File.add(exit);
		MenuBar.add(File);
		frame.setJMenuBar(MenuBar);

		frame.setSize(520, 510);
		// dimension calculation to display frame at the centre of screen
		dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height
				/ 2 - frame.getSize().height / 2);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Action listener to menu item
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		// Action listener to send button
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "SEND",
						"Team OpenFire Messenger", JOptionPane.WARNING_MESSAGE);
			}
		});

		// Action listener to upload button
		uploadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "UPLOAD",
						"Team OpenFire Messenger", JOptionPane.WARNING_MESSAGE);
			}
		});

	}

	public String raw_msg(String raw_msg) {
		String msg = raw_msg;
		bi.setText(raw_msg);
		if (raw_msg.length() > 25) {
			int preceding = bi.following(25);
			msg = raw_msg.substring(0, preceding);
			chatWindow
					.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			chatWindow.append(msg.trim()
					+ " "
					+ calandr.substring(calandr.lastIndexOf(":") - 5,
							calandr.lastIndexOf(":")) + "\n");
			raw_msg = message(raw_msg.trim(), msg.length());
		} else {
			chatWindow
					.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			chatWindow.append(msg.trim()
					+ " "
					+ calandr.substring(calandr.lastIndexOf(":") - 5,
							calandr.lastIndexOf(":")) + "\n\n");
		}
		return raw_msg;
	}

	public String message(String message_edit, int edit) {
		trim = message_edit.substring(edit, message_edit.length());
		return raw_msg(trim);
	}

	// taking contact input
	public void contacts() throws Exception {
		String name = "";
		pwd = current_path.getCanonicalPath();
		FileInputStream fis = new FileInputStream(pwd
				+ "/contacts/contacts.tof");
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		while ((name = br.readLine()) != null) {
			_contacts.add(name);
		}
		br.close();
	}

	public static void main(String[] arg) {
		new GUI();

	}

}
