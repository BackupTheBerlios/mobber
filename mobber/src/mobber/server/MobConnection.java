////////////////////////////////////////////////////////////////////////////
//
//  $RCSfile: MobConnection.java,v $ 
//
//  Author       Stefan Toepfer (stefan_toepfer@web.de)
//
//  Copyright (C) 2001 Stefan Toepfer
//
//  $Revision: 1.1 $
//  $Date: 2001/07/01 13:53:14 $
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

package mobber.server;

import java.net.*;
import java.io.*;
import java.util.*;
import org.apache.log4j.Category;
import mobber.server.logik.*;

/**
 * This is the serverthread for the client
 *
 **/
public class MobConnection extends Thread
{
    Category cat = Category.getInstance(MobConnection.class.getName());

    /** The clients socket */
    private Socket _client;
    /** The command-dispatcher */
    private MobProcessor _mp = new MobProcessor();
    /** The input */
    private BufferedReader _in = null;
    /** The User-Object */
    private User _user = null;
    /** The Sender */
    private MobSender _sender = null;
    /** Threads starttime */
    private long _threadStartTime = 0;
    /** Threads uptime */
    private long _threadUpTime = 0;
    /** Threads commands per second */
    private long _comsec = 0;
    /** Number of threads currently running */ 
    private static int _threadNR = 0;
    /** Holds input from client */
    private String _readclient = null;
    private static int _maxusers = Integer.parseInt((String)Config.config.get("MaxUsers"));;

    MobConnection(Socket client) throws SocketException
    {
	_client = client;
	// make the In/Outputs permanent
	try{
	    _in = new BufferedReader(new InputStreamReader(_client.getInputStream(), "UTF-8"));
	}catch(Exception e){
	    cat.error("Cannot make BufferedReader: " + e);
	    killclient();
	}
	setPriority(NORM_PRIORITY -1);

	_threadNR++;
	_threadStartTime = System.currentTimeMillis();
	cat.info("Connection from: " + _client.getInetAddress() + ":" + _client.getPort());
	cat.info("Active Listening threads: " + _threadNR);
    }
    
    /**
     * the input-loop for every client
     * 
     */
    public void run()
    {
	while(true){
	    try{
		_readclient = _in.readLine();
	    }catch(Exception ex1){
		cat.error("Exception while reading from Client. Killing it!");
		killclient();
	    }
	    _process(_readclient);
	    _readclient = null;
	}
    }
    
    /**
     * removes a client if something went wrong with the read
     * 
     */
    synchronized public void killclient()
    {
	User remove = null;
	
	getComSec();
	
	cat.info("Call to killclient(): ");
	cat.info("MobProcessor calls: " + _mp.getParsed());
	cat.info("Threads uptime: " + _threadUpTime);
	cat.info("Commands per sec: " + _comsec);
	
	_mp = null;
	
	remove = _getUser();
	
	if (remove != null){
	    cat.info("User was: " + remove.getName());
	    ClientList.delUser(remove);
	}
	_threadNR--;
	if (_sender != null) 
	    _sender.stop();
	try{
	    _client.close();
	}catch(IOException ioe){
	    cat.error("Cannot close client: " + ioe);
	}
	this.stop();
    }    
    
    /**
     * computes the commands per second to avoid flooding from client 
     * 
     * @return commands per second (whole connection)
     */
    private long getComSec()
    {
	_threadUpTime = (System.currentTimeMillis() - _threadStartTime) / 1000;
	if (_threadUpTime == 0)
	    _threadUpTime = 1;
	_comsec = _mp.getParsed() / _threadUpTime;
	return _comsec;
    }

    /**
     * process processes the input given by the client, 
     * generates the answers and sends them out in the world
     * 
     * @param msg the message of the client
     */
    synchronized private void _process(String msg)
    {
	Command com = null;
	String toSend = null;
	String reply = null;
	String what = null;
	Vector al = null;
	boolean isloggedIn = false;


	if (msg == null){
	    cat.info("Client sending null. Killing it!");
	    killclient();
	}else if (getComSec() > 3){
	    cat.info("Client: " + _client.getInetAddress() + ":" + _client.getPort() + " is trying to flood me. Killing it!");
	    killclient();
	}else {
	    _mp.setMessage(msg);
	    com = (Command)_mp.process();
	    if (_getUser() != null)
		isloggedIn = true;
	    what = com.getCommand();
	    //If this is no Login and this client is not logged in ->get rid of him
	    if (what.equals("Login") == true || isloggedIn == true){ 
		com.setClient(_client);
		com.doIt();
		toSend = com.getMessage();
		reply = com.getReply();
		if (toSend != null)
		    al = com.getRecipients();
		// Send Reply to sender
		if (what.equals("Login") == true){ 
		    if (reply == null){
			cat.info("Client sending wrong login. Killing it!");
			killclient();
		    }else{
			isloggedIn = true;
			_user = _getUser();
			_sender = new MobSender(_user);
			_sender.start();
			if (_threadNR > _maxusers && _user.isAdmin() == false){
			    cat.info("MaxUser exceeded. Killing it!");
			    killclient();
			}
		    }	
		}
		
		if (reply != null)
		    _user.enqueue(reply);
		
		// Send to all others
		if (al != null){
		    int size = al.size();
		    for (int i = 0; i < size; i++){
			User u  = (User)al.get(i);
			if (toSend != null)
			    u.enqueue(toSend);
		    }
		}
		// Logout: close socket, stop thread
		if (what.equals("Logout") == true){
		    cat.info("Client send Logout. Killing it!");
		    killclient();
		}
	    }
	}
    }


    synchronized private User _getUser()
    {
	int size = ClientList.users.size();
	for (int i = 0; i < size; i++){
	    if ((((User)ClientList.users.get(i)).getSocket().equals(_client) == true))
		return (User)ClientList.users.get(i);
	}
	return null;
    }


}















