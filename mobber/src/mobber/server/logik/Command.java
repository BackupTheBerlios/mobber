////////////////////////////////////////////////////////////////////////////
//
//  $RCSfile: Command.java,v $ 
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

package mobber.server.logik;

import java.util.*;
import java.net.*;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;

/**
 * Abstract class for Mobber-Commands
 * (your own commands should extend this class)
 *
 */
abstract public class Command
{
    /** the clients request in preprocessed form */
    protected Document _doc;
    /** the clients socket */
    protected Socket _client;

    /**
     * setClient sets the client who was sending this document
     * (this must be done by every command so this is done here)
     *
     * @param client the clients socket
     */
    public void setClient(Socket client)
    {
	_client = client;
    }

    /**
     * doIt final processing of the document
     * 
     */
    abstract public void doIt();

    /**
     * getMessage gets the answer to send to client
     * 
     * @return the message for client (can be null)
     */
    abstract public String getReply();

    /**
     * getMessage gets the answer to send to other clients
     * 
     * @return the message for the other clients (can be null)
     */
    abstract public String getMessage();

    /**
     * getRecipients get a list of users who get the generated message
     * 
     * @return a Vector with all other User-Objects
     */
    abstract public Vector getRecipients();

    /**
     * getCommand get the name of the Command invoked
     * 
     * @return the name of the command
     */
    abstract public String getCommand();
}







