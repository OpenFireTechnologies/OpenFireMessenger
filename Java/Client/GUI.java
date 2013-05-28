import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class GUI {
	// Main Frame
	JFrame frame,iframe;
	// Menu BAr
	JMenuBar MenuBar;
	// Menu on menu bar
	JMenu File;
	// Menu item under the menu
	JMenuItem exit,pref;
	// Labels for naming the boxes and copyright tag
	JLabel chatLabel, contactLabel, profilePic;
	// get and send buttons, get for Debugging
	JButton sendButton, getButton;
	// chat windows and the users type in box
	JTextArea chatWindow, typeWindow;
	// List of contacts
	@SuppressWarnings("rawtypes")
	JList usrsList;
	// Manage contacts
	DefaultListModel contactListModel;
	// scroll for all three boxes
	JScrollPane chatWindowPane, typeWindowPane, contactPane;
	// calculate dimensions
	Dimension dim;
	// array to store contact names
	ArrayList<String> _contacts = new ArrayList<String>();
	// to get the current path
	File current_path = new File(".");
	// present working directory is stored here
	String pwd = "";
        //Calander information
	String calandr = Calendar.getInstance().getTime().toString();
        //temporary for triming message
	String trim = "";
        //User Name
        String user_name="drakula941";
	String message = "";
        //Selecting user
	String selected = user_name;
        //Date
	String date = calandr.substring(0, calandr.lastIndexOf(":") - 9) + " "
			+ calandr.substring(calandr.length() - 4, calandr.length());
	BreakIterator bi = BreakIterator.getWordInstance();
        //Set Preference font style
        String FontStyle="";
        //Set Preference font size
	int FontSize=0;
        //Drop boxes for font style and font size
	JComboBox fontStyle,fontSize;
        //Array for all available font styles
	String []fontStyleArr={"DigifaceWide","Script MT Bold","Matura MT Script Capitals","Algerian","Times New Roman",
        "Arial Black","Arial","Bradley Hand ITC"};
        //Array for different font size
	String []fontSizeArr={"10","12","14","16","18","20","22","24","26","28","30"};
        File fl;
        FileOutputStream fos;
        PrintStream ps;
        FileInputStream fin;
	BufferedReader br;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	GUI() {
		try {
		pwd = current_path.getCanonicalPath();
                fl=new File(pwd + "/preference/Font.tof");
                fin=new FileInputStream(fl);
		br = new BufferedReader(new InputStreamReader(fin));
                FontStyle=br.readLine();
                FontSize=Integer.parseInt(br.readLine());
                System.out.println(FontSize+" "+FontStyle);
		frame = new JFrame();
		exit = new JMenuItem("Add contact");
                pref = new JMenuItem("Preference");
		MenuBar = new JMenuBar();
		File = new JMenu("Chat");
		frame.setLayout(null);
		// profile picture
		profilePic = new JLabel();
		profilePic.setBounds(10, 5, 48, 48);
	        profilePic.setIcon(new ImageIcon(pwd + "/contacts/pic.jpg"));
		frame.add(profilePic);

		// Chat label
		chatLabel = new JLabel(selected);
		chatLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
		chatLabel.setBounds(140, 15, 200, 30);
		frame.add(chatLabel);

		// Contact label
		contactLabel = new JLabel("Contacts");
		contactLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
		contactLabel.setIcon(new ImageIcon(pwd + "/icons/onlineS.png"));
		contactLabel.setBounds(370, 15, 200, 30);
		frame.add(contactLabel);

		// Main chat display box
		chatWindow = new JTextArea();
		chatWindow.setFont(new Font(FontStyle,Font.BOLD,FontSize));
		chatWindowPane = new JScrollPane(chatWindow);
		chatWindowPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		chatWindowPane.setBounds(10, 50, 350, 300);
		chatWindow.setEditable(false);

		// Dummy Message
		chatWindow.setText("Chat initiated at: " + date + "\n\n");
		frame.add(chatWindowPane);

		// User area to type
		typeWindow = new JTextArea();
		typeWindow.setFont(new Font(FontStyle,Font.BOLD,FontSize));
		typeWindow.setSize(20, 10);
		typeWindowPane = new JScrollPane(typeWindow);
		typeWindowPane.setBounds(10, 380, 350, 55);
		typeWindow.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent k) {
				if (k.getKeyChar() == KeyEvent.VK_ENTER) {
					if (k.isShiftDown()) {
						typeWindow.append(" \n");
					} else {
						if (selected != user_name) {
                                                        chatWindow.append(user_name+"\n");
                                                        chat_Out(user_name);
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
		getButton = new JButton("GET");
		getButton.setBounds(395, 410, 80, 25);
		getButton.setFont(new Font("Times New Roman", Font.BOLD, 10));
		frame.add(getButton);

		// Contact display box
		contactListModel = new DefaultListModel();
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
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				if (!arg0.getValueIsAdjusting()) {
					profilePic.setIcon(new ImageIcon(pwd + "/contacts/pic.jpg"));
					selected = usrsList.getSelectedValue().toString();
					chatLabel.setText(selected);
                                        chat_in();
				}
			}
		});
		frame.add(contactPane);

		// Menubar setup
                File.add(pref);
		File.add(exit);
		MenuBar.add(File);
		frame.setJMenuBar(MenuBar);

		frame.setSize(520, 510);
		// dimension calculation to display frame at the centre of screen
		dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((dim.width / 2) - (frame.getSize().width / 2),
				(dim.height / 2) - (frame.getSize().height / 2));
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Action listener to menu item 'Preference'
		pref.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                                fl=new File(pwd + "/preference/Font.tof");
				iframe=new JFrame();
		                iframe.setLayout(null);
                                //font style box + its action listener
                                fontStyle=new JComboBox(fontStyleArr);
	                	fontStyle.setBounds(25,10,150,20);
	                	iframe.add(fontStyle);
			        fontStyle.addActionListener(new ActionListener(){
                                public void actionPerformed(ActionEvent x){
                                try{
                                FontStyle=(String)fontStyle.getSelectedItem();
				chatWindow.setFont(new Font(FontStyle,Font.BOLD,FontSize));
				typeWindow.setFont(new Font(FontStyle,Font.BOLD,FontSize));
                                fos=new FileOutputStream(fl);
                                ps=new PrintStream(fos);
                                ps.println(FontStyle);
                                ps.println(FontSize);
                                }catch(Exception q){}
                                }});

                                //font size box + its action listener
		                fontSize=new JComboBox(fontSizeArr);
		                fontSize.setBounds(25,40,150,20);
                                iframe.add(fontSize);
			        fontSize.addActionListener(new ActionListener(){
                                public void actionPerformed(ActionEvent y){
                                try{
                                FontSize=Integer.parseInt((String)fontSize.getSelectedItem());
				chatWindow.setFont(new Font(FontStyle,Font.BOLD,FontSize));
				typeWindow.setFont(new Font(FontStyle,Font.BOLD,FontSize));
                                fos=new FileOutputStream(fl);
                                ps=new PrintStream(fos);
                                ps.println(FontStyle);
                                ps.println(FontSize);
                                }catch(Exception q){}
                                }});
                                iframe.setSize(200,300);
                  		// dimension calculation to display frame at the centre of screen
		                dim = Toolkit.getDefaultToolkit().getScreenSize();
                       		iframe.setLocation((dim.width / 2) - (iframe.getSize().width / 2),
				(dim.height / 2) - (iframe.getSize().height / 2));
	                	iframe.setVisible(true);
			}
		});

		// Action listener to send button
		sendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				new Sender("http://localhost/exchanger.php", true,
						"from=123&to=321&content=" + typeWindow.getText(),
						GUI.this).run();
				// TODO Display Sent Message in Chat Window as well
				// Temporary just adding content of the text area
				chatWindow.append(typeWindow.getText() + "\n");
			}
		});

		// Action listener to upload button
		getButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Sender("http://localhost/exchanger.php", true,
						"from=123&to=321", GUI.this).run();
			}
		});
		} catch (Exception x) {}
	}

	public String raw_msg(String raw_msg) {
		String msg = raw_msg;
		bi.setText(raw_msg);
		if (raw_msg.length() > 40) {
			int preceding = bi.following(40);
			msg = raw_msg.substring(0, preceding);
			chatWindow.append(calandr.substring(calandr.lastIndexOf(":") - 5,
							calandr.lastIndexOf(":")) + " " + msg.trim() + "\n");
                        chat_Out(msg);
			raw_msg = message(raw_msg.trim(), msg.length());
		} else {
			chatWindow.append(calandr.substring(calandr.lastIndexOf(":") - 5,
							calandr.lastIndexOf(":")) + " " + msg.trim() + "\n");
                        chat_Out(msg+"\n");
		}
		return raw_msg;
	}

	public String message(String message_edit, int edit) {
		trim = message_edit.substring(edit, message_edit.length());
		return raw_msg(trim);
	}

	/**
	 * Reads the Contacts from /contacts/contacts.tof
	 */
	public void contacts() throws Exception {
		String name = "";
		pwd = current_path.getCanonicalPath();
		fin = new FileInputStream(pwd+ "/contacts/contacts.tof");
		br = new BufferedReader(new InputStreamReader(fin));
		while ((name = br.readLine()) != null) {
			_contacts.add(name);
		}
		br.close();
	}

        public void chat_Out(String messg) {
                try{
                String msg_out=messg;
                fl=new File(pwd + "/chat/" + selected + ".tof");
                fos=new FileOutputStream(fl,true);
                ps=new PrintStream(fos);
                if(msg_out.equals(selected)||msg_out.equals(user_name))
                {
                ps.println(msg_out.trim());
                }
                else
                {
                ps.println(calandr.substring(calandr.lastIndexOf(":") - 5,calandr.lastIndexOf(":")) + " " + msg_out.trim());
                }
                System.out.println(msg_out);
                }catch(Exception z){} 
                }

        public void chat_in() {
                try{
                chatWindow.setText(null);
                String msg_in="";
                fl=new File(pwd + "/chat/" + selected + ".tof");
                fin=new FileInputStream(fl);
		br = new BufferedReader(new InputStreamReader(fin));
                while((msg_in= br.readLine()) != null)
                {
                chatWindow.append(msg_in+"\n");
                }
                }catch(Exception x){}
                }

	public static void main(String[] arg) {
		new GUI();
	}

	/**
	 * Adds the Result to the Chat Window
	 * 
	 * @result The text, which should get added to the Chat Window
	 */
	public void getResult(String result) {
		result = result.replace("<br>", "\n");
		chatWindow.append(result);
	}

}
