////////////////////////////////////////////////////////////////////////////
//
//  $RCSfile: User.java,v $ 
//
//  Author       Stefan Toepfer (stefan_toepfer@web.de)
//
//  Copyright (C) 2001 Stefan Toepfer
//
//  $Revision: 1.1 $
//  $Date: 2001/07/01 13:53:18 $
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

package mobber.server.logik;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * This class represents the User and its attributes
 *
 **/
public class User
{
    /** Users socket */
    private Socket _sock;
    /** Users name */
    private String _name;
    /** Users admin flag */
    private boolean _isAdmin;
    /** Queue for messages to client */
    private Vector _sendQueue = new Vector();


    public User(Socket sock, String name, boolean isAdmin)
    {
	_sock  = sock;
	_name  = name;
	_isAdmin = isAdmin;
    }
    public Socket getSocket(){return _sock;}

    public String getName(){return _name;}

    public boolean isAdmin(){return _isAdmin;}

    public void enqueue(String toSend)
    {
	_sendQueue.add(toSend);
    }

    public String dequeue()
    {
	try{
	    return (String)_sendQueue.remove(0);
	}catch(Exception e){
	    return null;
	}
    }
}












