/**
 * Class for the pop up window to read a message
 * Created using WindowBuilder extension
 * @author Daniel Weber, Alex Porter, Clay Turner
 */

package com.importteamname.simpleemail;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import javax.swing.UIManager;

public class ComposeMessage {

    private JFrame 		frame;
    private JTextField 	from;
    private JTextField 	to;
    private JTextField 	subject;
    private Account 	CurrentAccount;
    private RemoteSite 	CopyOfMasterSite;
    
   /**
    * Constructor to create a compose message dialog
    * @param a Account sending the message
    * @param CopyOfMasterSite	remote site used to send the message
    */
    public ComposeMessage(Account a, RemoteSite CopyOfMasterSite) {
    	CurrentAccount = a;
    	this.CopyOfMasterSite = CopyOfMasterSite;
        initialize();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Compose Message");
        frame.setBounds(100, 100, 600, 479);
        frame.getContentPane().setLayout(null);
        
        JPanel panel = new JPanel();
        panel.setBounds(0, 38, 600, 118);
        frame.getContentPane().add(panel);
        panel.setLayout(null);
        
        JLabel lblFrom = new JLabel("From:");
        lblFrom.setBounds(39, 11, 61, 16);
        panel.add(lblFrom);
        
        JLabel lblTo = new JLabel("To:");
        lblTo.setBounds(55, 49, 30, 16);
        panel.add(lblTo);
        
        JLabel lblSubject = new JLabel("Subject:");
        lblSubject.setBounds(24, 87, 61, 16);
        panel.add(lblSubject);
        
        from = new JTextField(CurrentAccount.getAccountname()); 
        from.setEditable(false);
        from.setBounds(89, 6, 484, 26);
        panel.add(from);
        from.setColumns(10);
        
        to = new JTextField();
        to.setColumns(10);
        to.setBounds(89, 44, 484, 26);
        panel.add(to);
        
        subject = new JTextField();
        subject.setColumns(10);
        subject.setBounds(89, 82, 484, 26);
        panel.add(subject);
        
        final JTextArea textPane = new JTextArea();
        textPane.setBounds(0, 158, 600, 280);
        textPane.setWrapStyleWord(true);
        textPane.setLineWrap(true);
        frame.getContentPane().add(textPane);
        
        JPanel panel_1 = new JPanel();
        panel_1.setBackground(UIManager.getColor("Button.background"));
        panel_1.setBounds(0, 0, 600, 39);
        frame.getContentPane().add(panel_1);
        panel_1.setLayout(null);
        
        JButton btnReply = new JButton("Send");
        btnReply.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if (to.getText().isEmpty()) {
        			JOptionPane.showMessageDialog(frame, "Please enter who the email is addressed to");
        		}
        		else {
        			//Create new Message
        			String[] rec = to.getText().split(",");
        			Vector<String> receivers = new Vector<String>(Arrays.asList(rec));
        			Message m = new Message(subject.getText(), LocalDateTime.now(), 
							                textPane.getText(), CurrentAccount, receivers);
        			
        			//Add new message to sent mailbox
        			CurrentAccount.getSent().addMessageToMailBox(m);
        			
        			//Add new message to other's mailbox
                    CopyOfMasterSite.addMessageToRecipientsInbox(m);
        			frame.dispose();
        		}
        	}
        });
        btnReply.setBounds(6, 6, 117, 29);
        panel_1.add(btnReply);
        
        JLabel lblToInstructions = new JLabel("To send to 1+ people, comma separate addresses, no spaces");
        lblToInstructions.setBounds(133, 13, 421, 14);
        panel_1.add(lblToInstructions);
    }
    
    
    /**
     * Sets the message to be a reply
     * @param receiver
     */
    public void ReplyMessage(String receiver) {
    	to.setText(receiver);
    }
}
