////////////////////////////////////////////////////////////////////////////
//
//  $RCSfile: Server.java,v $ 
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
import org.apache.log4j.xml.DOMConfigurator;
import org.apache.log4j.Category;
import mobber.server.logik.*;

/**
 * This class represents the server
 *
 **/
public class Server
{
    static Category cat = Category.getInstance(Server.class.getName());

    /** The ServerSocket the server is listening */
    private ServerSocket _ss = null;
    /** The port of the server */
    private int _port = 2404;
    /** The backlog of the ServerSocket */
    private int _maxqueue = 10; 
    
    public Server(int port)
    {
	_port = port;
	_info();
	try{
	    _listen();
	}catch(Exception e){
	    e.printStackTrace();
	}
    }

    /**
     * Prints some info on the screen
     * 
     */
    private void _info()
    {
	System.out.println("This is " + Constants.SERVERNAME + " listening on port: " + _port);
	System.out.println("Using Command-Namespace: " + Config.config.get("Command-NS"));
	cat.info(Constants.SERVERNAME + " at " + _port + " listening");
	cat.info("Command-Namespace: " + Config.config.get("Command-NS"));
	cat.info("Login-Module: " + Config.config.get("Auth-Module"));
    }

    /**
     * Listens on the given port and dispatches the connections
     * 
     */
    private void _listen() throws IOException, SocketException
    {
	_ss = new ServerSocket(_port,_maxqueue); 
	
	while (true)
	    new MobConnection(_ss.accept()).start();
    }

    /**
     * Starts the Mobber-Server 
     * 
     * @param args the config-file
     */
    public static void main(String[] args) throws IOException
    {
	// Parse the commandline port
	
	if (args.length == 1){
	    new Config(args[0]);
	} else {
	    new Config("config.xml");
	}
	
	DOMConfigurator.configure((String)Config.config.get("Log-Config"));

	// Start the Server

	Server server = new Server(Integer.parseInt((String)Config.config.get("Port")));

    }
}


