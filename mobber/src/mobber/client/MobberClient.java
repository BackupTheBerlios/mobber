////////////////////////////////////////////////////////////////////////////
//
//  $RCSfile: MobberClient.java,v $ 
//
//  Author       Stefan Toepfer (stefan_toepfer@web.de)
//
//  Copyright (C) 2001 Stefan Toepfer
//
//  $Revision: 1.1 $
//  $Date: 2001/07/01 13:53:12 $
//  $Author: stefant $
//
//  This program is free software; you can redistribute it and/or modify  
//  it under the terms of the GNU General Public License as published by  
//  the Free Software Foundation; either version 2, or (at your option)   
//  any later version.                                                    
//   
//  This program is distributed in the hope that it will be useful,       
//  but WITHOUT ANY WARRANTY; without even the implied warranty of        
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         
//  GNU General Public License for more details.                          
//   
//  You should have received a copy of the GNU General Public License     
//  along with this program; see the file COPYING.  If not, write to      
//  the Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139, USA. 
/////////////////////////////////////////////////////////////////////////////

package mobber.client;

import java.io.*;
import java.net.*;
import java.util.*;
import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;

/**
 * This is the MobberClient
 * (works only with the default commandset)
 *
 **/
public class MobberClient extends Thread
{
    /** Constant maximum number of arguments to parse */
    public static final int    MAXARGC        = 5;
    /** Constant Client is not connected */
    public static final int    NOTCONNECTED   = 0;
    /** Constant Client is connected */
    public static final int    CONNECTED      = 1;
    /** Constant String to print before Servermessages */
    public static final String SERVER         = "!!";
    /** Constant String to print before Clientmessages */
    public static final String CLIENT         = "::";
    /** Constant String to print before normal messages */
    public static final String NORMAL         = ">>";
    
    /** status of client */
    private int _status = NOTCONNECTED;   
    /** Users Login */
    private String _user = "";        
    /** Users Password */
    private String _pass = "";
    /** Current group */
    private String _group = "";      
    /** The Mobber-Relay */
    private MobberRelay _mr = null;
    
    /** the socket to client */
    private Socket socket;
    /** the reader of server messages */
    private BufferedReader reader; 
    /** the writer to the server */
    private PrintWriter writer;    
    /** the thread of this client */
    private Thread thread;
    
    
    public MobberClient(MobberRelay mr)
    {
	_mr = mr;
    }
    
    /**
     * Connect to the server 
     * 
     * @param host Serverhost
     * @param port Serverport
     */
    private void connect(String host, int port)
    {
	if (_status == NOTCONNECTED){
	    try{
		socket = new Socket(host, port);
	    }catch (Exception ex){
		ex.printStackTrace();
	    }
	    try{
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
		writer = new PrintWriter(socket.getOutputStream(), true);
		_status = CONNECTED;
	    }catch (IOException ex){
		ex.printStackTrace();
	    }
	    if (_status == CONNECTED){
		thread = new Thread(this);
		thread.start();
	    }
	}
    }
    
    /**
     * Processes the message from the UI
     * 
     * @param message the command
     */
    public void processMessage(String message)
    {
	String command;
	
	if (message.equals("") == false){
	    if (message.charAt(0) == '/'){
	        try{
		    command = message.substring(1, message.indexOf(' '));
		}catch (StringIndexOutOfBoundsException e){
		    command = message.substring(1,message.length());
		}
		if (processMessageCommandText(command, message) == false){
		    if (processMessageCommand(command, message) == false)
			sendMessage(CLIENT, "Unknown command");
		}
	    }
	}
    }


    /**
     * Processes the command with text
     * 
     * @param command 
     * @param message
     */
    private boolean processMessageCommandText(String command, String message)
    {
	boolean processed = false;
	String args[] = new String[MAXARGC];
	int counter = 0;
	
	StringTokenizer st = new StringTokenizer(message," ");
	while (st.hasMoreTokens() && counter < MAXARGC -1) {
	    args[counter] = st.nextToken();
	    counter++;
	}
	
	// do the proccessing
	if (command.equalsIgnoreCase("MESSAGE")){
	    String toSend = message.substring(command.length() + args[1].length() + 2, message.length()); 
	    //	    System.out.println("::" + toSend + "::");
	    sendServerMessage("<mobber><message><recipient>" + args[1] + "</recipient><mesg>" + toSend + "</mesg></message></mobber>");
	    processed = true;
	} else if (command.equalsIgnoreCase("GROUPMESSAGE")){
	    String toSend = message.substring(command.length() + 2, message.length()); 
	    //	    System.out.println("::" + toSend + "::");
	    sendServerMessage("<mobber><groupmessage><group>" + _group + "</group><mesg>" + toSend + "</mesg></groupmessage></mobber>");
	    processed = true;
	} else if (command.equalsIgnoreCase("WALL")){
	    String toSend = message.substring(command.length() + 2, message.length()); 
	    System.out.println("::" + toSend + "::");
	    sendServerMessage("<mobber><wall><mesg>" + toSend + "</mesg></wall></mobber>");
	    processed = true;
	} else if (command.equalsIgnoreCase("CREATEGROUP")){
	    String toSend = message.substring(command.length() + args[1].length() + 2, message.length()); 
	    sendServerMessage("<mobber><creategroup><group>" + args[1] + "</group><desc>" + toSend + "</desc></creategroup></mobber>");
	    processed = true;
	}


	return processed;
    }

    
    /**
     * Processes the other commands
     * 
     * @param command
     * @param message
     */
    private boolean processMessageCommand(String command, String message)
    {
	boolean processed = false;
	
	String args[] = new String[MAXARGC];
	int counter = 0;
	
	StringTokenizer st = new StringTokenizer(message," ");
	while (st.hasMoreTokens() && counter < MAXARGC -1) {
	    args[counter] = st.nextToken();
	    counter++;
	}

	// do the proccessing
	if (command.equalsIgnoreCase("NAME")){
	    _user = args[1];
	    processed = true;
	} else if (command.equalsIgnoreCase("PASS")){
	    _pass = args[1];
	    processed = true;
	} else if (command.equalsIgnoreCase("LOGIN")){
	    sendServerMessage("<mobber><login><name>" + _user + "</name><password>" + _pass + "</password></login></mobber>");
	    processed = true;
	} else if (command.equalsIgnoreCase("LOGOUT")){
	    sendServerMessage("<mobber><logout /></mobber>");
	    _status = NOTCONNECTED;
	    processed = true;
	} else if (command.equalsIgnoreCase("EXIT")){
	    _mr.exit();
	    processed = true;
	} else if (command.equalsIgnoreCase("CONNECT")){
	    connect(args[1], (new Integer(args[2])).intValue());
	    sendMessage(CLIENT, "try to connect");
	    if (_status == CONNECTED)
		sendMessage(CLIENT, "connected to:" + args[1] + ":" + args[2]);
	    processed = true;
	} else if (command.equalsIgnoreCase("JOINGROUP")){
	    sendServerMessage("<mobber><joingroup><group>" + args[1] + "</group></joingroup></mobber>");
	    processed = true;
	} else if (command.equalsIgnoreCase("PARTGROUP")){
	    sendServerMessage("<mobber><partgroup><group>" + args[1] + "</group></partgroup></mobber>");
	    processed = true;
	} else if (command.equalsIgnoreCase("KICK")){
	    sendServerMessage("<mobber><kick><group>" + _group + "</group><user>" + args[1] + "</user></kick></mobber>");
	    processed = true;
	} else if (command.equalsIgnoreCase("LISTUSER")){
	    sendServerMessage("<mobber><listuser /></mobber>");
	    processed = true;
	} else if (command.equalsIgnoreCase("LISTGROUP")){
	    sendServerMessage("<mobber><listgroup /></mobber>");
	    processed = true;
	}
	return processed;
    }

    /**
     * Sends messages to UI
     * 
     * @param type the type of the message (SERVER,CLIENT,NORMAL)
     * @param message
     */
    private void sendMessage(String type, String message)
    {
	_mr.toUI(type + message);
    }
    
    /**
     * Send a message to the server
     * 
     * @param message
     */
    private void sendServerMessage(String message)
    {
	if (_status == CONNECTED)
	writer.println(message);
    }
    
    /**
     * Preprocesses the messages from the server
     * 
     * @param message
     */
    synchronized private void preprocessServerMessage(String message)
    {
	if (message == null){
	    this.stop();
	    try{
		reader.close();
		writer.close();
	    }catch(IOException ioe){
		ioe.printStackTrace();
	    }
	    sendMessage(CLIENT, "Connection Closed by Server");
	    _group = "";
	} else if (message.equals("") == false)
	    processServerMessage(message);
    }
    
    
    /**
     * Processes the messages from the server
     * 
     * @param message
     */
    synchronized private void processServerMessage(String message)
    {
	SAXBuilder builder = new SAXBuilder();
	String what = new String();
	Document doc = null;

	try{
	    doc = builder.build(new StringReader(message));
	    Element root =  doc.getRootElement();
	    List childs = root.getChildren();
	    Iterator i = childs.iterator();
	    what = ((Element)i.next()).getName();
	}catch(Exception e){}
	
	if (what.equalsIgnoreCase("LOGIN") == true)
	    _login(doc);
	else if (what.equalsIgnoreCase("LOGOUT") == true)
	    _logout(doc);
	else if (what.equalsIgnoreCase("MESSAGE") == true)
	    _message(doc);
	else if (what.equalsIgnoreCase("WALL") == true)
	    _wall(doc);
	else if (what.equalsIgnoreCase("CREATEGROUP") == true)
	    _creategroup(doc);
	else if (what.equalsIgnoreCase("JOINGROUP") == true)
	    _joingroup(doc);
	else if (what.equalsIgnoreCase("PARTGROUP") == true)
	    _partgroup(doc);
	else if (what.equalsIgnoreCase("GROUPMESSAGE") == true)
	    _groupmessage(doc);
	else if (what.equalsIgnoreCase("KICK") == true)
	    _kick(doc);
	else if (what.equalsIgnoreCase("LISTUSER") == true)
	    _listuser(doc);
	else if (what.equalsIgnoreCase("LISTGROUP") == true)
	    _listgroup(doc);
    }


    /**
     * Process Login from server
     * 
     * @param doc
     */
    private void _login(Document doc)
    {
	Element root =  doc.getRootElement();
	Element login =  root.getChild("login");
	String answer = login.getChild("answer").getText();
	sendMessage(SERVER, "Login is: " + answer);
    }
    
    /**
     * Process Logout from server
     * 
     * @param doc
     */
    private void _logout(Document doc)
    {
	Element root =  doc.getRootElement();
	Element logout =  root.getChild("logout");
	String answer = logout.getChild("answer").getText();
	sendMessage(SERVER, "Logout is: " + answer);
    }
    
    /**
     * Process Message from server
     * 
     * @param doc
     */
    private void _message(Document doc)
    {
	Element root =  doc.getRootElement();
	Element message =  root.getChild("message");
	String sender = message.getChild("sender").getText();
	String mesg = message.getChild("mesg").getText();
	sendMessage(NORMAL, sender + ": " + mesg);
    }

    /**
     * Process Wall from server
     * 
     * @param doc
     */
    private void _wall(Document doc)
    {
	Element root =  doc.getRootElement();
	Element wall =  root.getChild("wall");
	String mesg = wall.getChild("mesg").getText();
	sendMessage(SERVER, mesg);
    }

    /**
     * Process Creategroup from server
     * 
     * @param doc
     */
    private void _creategroup(Document doc)
    {
	Element root =  doc.getRootElement();
	Element create =  root.getChild("creategroup");
	String group = create.getChild("group").getText();
	String answer = create.getChild("answer").getText();
	sendMessage(SERVER, "Group creation is: " + answer);
	if (answer.equals("OK") == true){
	    sendMessage(SERVER, "Group " + group + " joined.");
	    _group = group;
	}
    }

    /**
     * Process Joingroup from server
     * 
     * @param doc
     */
    private void _joingroup(Document doc)
    {
	Element root =  doc.getRootElement();
	Element join =  root.getChild("joingroup");
	String group = join.getChild("group").getText();
	String answer = join.getChild("answer").getText();
	sendMessage(SERVER, "JoinGroup: " + answer);
	if (answer.equals("OK") == true){
	    sendMessage(SERVER, "Group " + group + " joined.");
	    _group = group;
	}
    }

    /**
     * Process Partgroup from server
     * 
     * @param doc
     */
    private void _partgroup(Document doc)
    {
	Element root =  doc.getRootElement();
	Element part =  root.getChild("partgroup");
	String group = part.getChild("group").getText();
	String answer = part.getChild("answer").getText();
	if (group.equals(_group) == true){
	    sendMessage(SERVER, group + ": " + answer);
	}
    }


    /**
     * Process Groupmessage from server
     * 
     * @param doc
     */
    private void _groupmessage(Document doc)
    {
	Element root =  doc.getRootElement();
	Element message =  root.getChild("groupmessage");
	String group = message.getChild("group").getText();
	String sender = message.getChild("sender").getText();
	String mesg = message.getChild("mesg").getText();
	if (group.equals(_group) == true){
	    sendMessage(NORMAL, group + "(" + sender + "): " + mesg);
	}
    }

    /**
     * Process kick from server
     * 
     * @param doc
     */
    private void _kick(Document doc)
    {
	Element root =  doc.getRootElement();
	Element kick =  root.getChild("kick");
	String user = kick.getChild("user").getText();
	String answer = kick.getChild("answer").getText();
	if (answer.equals("OK") == true)
	    sendMessage(SERVER, user + " was kicked from " + _group);
    }

    /**
     * Process listgroup from server
     * 
     * @param doc
     */
    private void _listgroup(Document doc)
    {
	StringBuffer grouplist = new StringBuffer();

	Element root =  doc.getRootElement();
	Element list =  root.getChild("listgroup");
	List groups = list.getChildren("group");
	Iterator i = groups.iterator();
	while (i.hasNext()){
	    Element temp = (Element)i.next();
	    grouplist.append(temp.getChild("name").getText());
	    grouplist.append("\t");
	    grouplist.append(temp.getChild("description").getText());
	    grouplist.append("\n");							       }
	sendMessage(SERVER, "Groups: \n" + grouplist);
    }

    /**
     * Process listuser from server
     * 
     * @param doc
     */
    private void _listuser(Document doc)
    {
	StringBuffer userlist = new StringBuffer();

	Element root =  doc.getRootElement();
	Element list =  root.getChild("listuser");
	List users = list.getChildren("user");
	Iterator i = users.iterator();
	while (i.hasNext()){
	    userlist.append(((Element)i.next()).getText());
	    userlist.append("\n");
	}
	sendMessage(SERVER, "Users: \n" + userlist);
    }

    /**
     * This listens forever for messages from server 
     * 
     */
    public void run()
    {
	String current = "";
	
	while(_status == CONNECTED){
	    // Inner Loop for Mobber-Fragments
	    try{
		current = reader.readLine();
	    }catch(Exception ex){
		_status = NOTCONNECTED;
		current = null;
	    }
	    // End Inner Loop
	    preprocessServerMessage(current);
	    current = null;
	}
    }
}
