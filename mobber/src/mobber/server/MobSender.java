////////////////////////////////////////////////////////////////////////////
//
//  $RCSfile: MobSender.java,v $ 
//
//  Author       Stefan Toepfer (stefan_toepfer@web.de)
//
//  Copyright (C) 2001 Stefan Toepfer
//
//  $Revision: 1.1 $
//  $Date: 2001/07/01 13:53:15 $
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
 * This is the outputthread for the client
 *
 **/
public class MobSender extends Thread
{
    Category cat = Category.getInstance(MobSender.class.getName());

    /** The user */
    private User _user = null;
    /** The clients PrintWriter */
    private PrintWriter _pout = null;
    /** The message */
    private String _toSend = null;



    MobSender(User user)
    {
	_user = user;
	cat.info("Starting Writing-Thread for: " + _user.getName());
	try{
	    OutputStream out = (_user.getSocket()).getOutputStream();
	    _pout = new PrintWriter(new OutputStreamWriter(out, "UTF-8"), true);
	}catch(Exception e){
	    cat.error("Cannot make PrintWriter: " + e);
	}
	setPriority(NORM_PRIORITY -1);
    }
    
    /**
     * the output-loop for every client
     * 
     */
    public void run()
    {
	while(true){
	    _toSend = _user.dequeue();
	    if (_toSend != null)
		_process(_toSend);
	}
    }
    
    /**
     * processes the output to client
     * 
     * @param msg the message to the client
     */
    synchronized private void _process(String msg)
    {
	_pout.println(msg);
    }
}














