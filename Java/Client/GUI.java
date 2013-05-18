import javax.swing.*;
import javax.swing.event.*;
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
	JLabel chatLabel,contactLabel,copyRightLabel,profilePic;
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
        //array to store contact names
        ArrayList<String> _contacts = new ArrayList<String>();
        //to get the current path
        File current_path = new File (".");
        //present working directory is stored here
        String pwd="";

             GUI()
             {
                try{
                pwd=current_path.getCanonicalPath();
                }catch(Exception x){}
                frame=new JFrame();
                exit = new JMenuItem("Exit");
                MenuBar=new JMenuBar();
                File = new JMenu("File");
		frame.setLayout(null);
                //profile picture
                profilePic=new JLabel();
                profilePic.setBounds(85,5,48,48);
                frame.add(profilePic);

                //Chat label
                chatLabel=new JLabel("Chat Area");
		chatLabel.setFont(new Font("Times New Roman",Font.BOLD,20));
		chatLabel.setBounds(125,15,200,30);
		frame.add(chatLabel);

                //Contact label
		contactLabel=new JLabel("Contacts");
		contactLabel.setFont(new Font("Times New Roman",Font.BOLD,20));
		contactLabel.setBounds(390,15,200,30);
		frame.add(contactLabel);

                //Main chat display box
		chatWindow=new JTextArea();
		chatWindow.setFont(new Font("Times New Roman",Font.BOLD,20));
		chatWindowPane=new JScrollPane(chatWindow);
		chatWindowPane.setBounds(10,50,350,300);
                chatWindow.setEditable(false);
		frame.add(chatWindowPane);
		
                //User area to type
		typeWindow=new JTextArea();
                typeWindow.setSize(20,10);
		typeWindowPane=new JScrollPane(typeWindow);
		typeWindowPane.setBounds(10,380,350,55);
		frame.add(typeWindowPane);

                //Send button			
		sendButton=new JButton("SEND");
		sendButton.setBounds(395,380,80,25);
		sendButton.setFont(new Font("Times New Roman",Font.BOLD,10));
		frame.add(sendButton);

                //Upload button
                uploadButton=new JButton("UPLOAD");
		uploadButton.setBounds(395,410,80,25);
		uploadButton.setFont(new Font("Times New Roman",Font.BOLD,10));
		frame.add(uploadButton);

                //Contact display box
		contactListModel=new DefaultListModel();
		usrsList=new JList(contactListModel);
		usrsList.setFont(new Font("Times New Roman",Font.BOLD,20));
                try{
                contacts();
                }catch(Exception e){}
                for(int i=0;i<_contacts.size();i++)
                {
                  contactListModel.addElement(_contacts.get(i));
                }
		contactPane=new JScrollPane(usrsList);
		contactPane.setBounds(370,50,130,300);
                usrsList.addListSelectionListener(new ListSelectionListener() {
                
                //contact selection
                public void valueChanged(ListSelectionEvent arg0) {
                if (!arg0.getValueIsAdjusting()) {
                   profilePic.setIcon(new ImageIcon(pwd+"/contacts/pic.jpg"));
                   chatLabel.setIcon(new ImageIcon(pwd+"/icons/onlineS.png"));
                   chatLabel.setText(usrsList.getSelectedValue().toString());
                   }
                }
                });
		frame.add(contactPane);

                //Copyright label
		copyRightLabel=new JLabel("copyright (c) @drakula941");
		copyRightLabel.setBounds(150,430,250,50);
		copyRightLabel.setFont(new Font("Times New Roman",Font.BOLD,20));
		frame.add(copyRightLabel);

                //Menubar setup
                File.add(exit);
                MenuBar.add(File);
                frame.setJMenuBar(MenuBar);

		frame.setSize(520,520);
                //dimension calculation to display frame at the centre of screen
                dim = Toolkit.getDefaultToolkit().getScreenSize();
                frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
                frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                //Action listener to menu item
                exit.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                System.exit(0);
                }
                });

                //Action listener to send button
                sendButton.addActionListener(new ActionListener() { 
                public void actionPerformed(ActionEvent e) { 
                JOptionPane.showMessageDialog (null, "SEND", "Team OpenFire Messanger", JOptionPane.WARNING_MESSAGE);
                } 
                } );

                //Action listener to upload button
                uploadButton.addActionListener(new ActionListener() { 
                public void actionPerformed(ActionEvent e) { 
                JOptionPane.showMessageDialog (null, "UPLOAD", "Team OpenFire Messanger", JOptionPane.WARNING_MESSAGE);
                } 
                } );


         }
        //taking contact input
        public void contacts()throws Exception
        {
         String name="";
         pwd=current_path.getCanonicalPath();
         FileInputStream fis=new FileInputStream(pwd+"/contacts/contacts.tof");
         BufferedReader br=new BufferedReader(new InputStreamReader(fis));
         while((name=br.readLine())!=null)
               {
                  _contacts.add(name);
               }
        }

	public static void main(String[] arg) 
        {
                new GUI();
               
	}

}
