import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.awt.*;
public class GUI{
        //Main Frame
	JFrame frame;
        //Menu BAr
        JMenuBar MenuBar;
        //Menu on menu bar
        JMenu File;
        //Menu item under the menu
        JMenuItem exit;
        //Labels for naming the boxes and copyright tag
	JLabel chatLabel,contactLabel,copyRightLabel;
        //upload and send buttons
	JButton sendButton,uploadButton;
        //chat windows and the users type in box
	JTextArea chatWindow,typeWindow;
        //List of contacts
	JList usrsList;
        //Manage contacts
	DefaultListModel contactListModel;
        //scroll for all thre boxes
	JScrollPane chatWindowPane,typeWindowPane,contactPane;
        //calculate dimensions
        Dimension dim;

             GUI()
             {
                frame=new JFrame();
                exit = new JMenuItem("Exit");
                MenuBar=new JMenuBar();
                File = new JMenu("File");
		frame.setLayout(null);
                chatLabel=new JLabel("Chat Area");
		chatLabel.setFont(new Font("Times New Roman",Font.BOLD,20));
		chatLabel.setBounds(125,15,200,30);
		frame.add(chatLabel);

		contactLabel=new JLabel("Contacts");
		contactLabel.setFont(new Font("Times New Roman",Font.BOLD,20));
		contactLabel.setBounds(390,15,200,30);
		frame.add(contactLabel);

		chatWindow=new JTextArea();
		chatWindow.setFont(new Font("Times New Roman",Font.BOLD,20));
		chatWindowPane=new JScrollPane(chatWindow);
		chatWindowPane.setBounds(10,50,350,300);
                chatWindow.setEditable(false);
		frame.add(chatWindowPane);
		
		typeWindow=new JTextArea();
                typeWindow.setSize(20,10);
		typeWindowPane=new JScrollPane(typeWindow);
		typeWindowPane.setBounds(10,380,350,55);
		frame.add(typeWindowPane);
			
		sendButton=new JButton("SEND");
		sendButton.setBounds(395,380,80,25);
		sendButton.setFont(new Font("Times New Roman",Font.BOLD,10));
		frame.add(sendButton);

                uploadButton=new JButton("UPLOAD");
		uploadButton.setBounds(395,410,80,25);
		uploadButton.setFont(new Font("Times New Roman",Font.BOLD,10));
		frame.add(uploadButton);

		contactListModel=new DefaultListModel();
		usrsList=new JList(contactListModel);
		usrsList.setFont(new Font("Times New Roman",Font.BOLD,20));
		contactPane=new JScrollPane(usrsList);
		contactPane.setBounds(370,50,130,300);
		frame.add(contactPane);

		copyRightLabel=new JLabel("copyright (c) @drakula941");
		copyRightLabel.setBounds(150,430,250,50);
		copyRightLabel.setFont(new Font("Times New Roman",Font.BOLD,20));
		frame.add(copyRightLabel);

                File.add(exit);
                MenuBar.add(File);
                frame.setJMenuBar(MenuBar);

		frame.setSize(520,520);
                dim = Toolkit.getDefaultToolkit().getScreenSize();
                frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
                frame.setVisible(true);

                exit.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                System.exit(0);
                }
                });

                sendButton.addActionListener(new ActionListener() { 
                public void actionPerformed(ActionEvent e) { 
                JOptionPane.showMessageDialog (null, "SEND", "Team OpenFire Messanger", JOptionPane.WARNING_MESSAGE);
                } 
                } );

                uploadButton.addActionListener(new ActionListener() { 
                public void actionPerformed(ActionEvent e) { 
                JOptionPane.showMessageDialog (null, "UPLOAD", "Team OpenFire Messanger", JOptionPane.WARNING_MESSAGE);
                } 
                } );
         }

	public static void main(String[] arg) 
        {
                new GUI();
               
	}

}
