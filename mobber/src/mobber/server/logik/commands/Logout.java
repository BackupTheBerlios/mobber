////////////////////////////////////////////////////////////////////////////
//
//  $RCSfile: Logout.java,v $ 
//
//  Author       Stefan Toepfer (stefan_toepfer@web.de)
//
//  Copyright (C) 2001 Stefan Toepfer
//
//  $Revision: 1.1 $
//  $Date: 2001/07/01 13:53:22 $
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

package mobber.server.logik.commands;

import java.io.*;
import java.util.*;
import java.net.*;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;
import mobber.server.logik.*;


public class Logout extends Command
{

    public Logout(Document doc)
    {
	_doc = doc;
    }


    public void doIt()
    {
	_delUser();
    }


    private  void _delUser()
    {
	User remove = null;
	
	// Get user
	for (int i = 0; i < ClientList.users.size(); i++){
	    if (((User)ClientList.users.get(i)).getSocket().equals(_client) == true)
		remove = (User)ClientList.users.get(i);
	}
	// Delete from ClientList
	if (remove != null)
	    ClientList.delUser(remove);
	
    }


    public String getMessage()
    {
	return null;
    }

    public String getReply()
    {
	String toOut = null;
	
	Element root = new Element("mobber");
	Element logout = new Element("logout");
	root.addContent(logout);
	Element answer = new Element("answer");
	answer.setText("OK BYE");
	logout.addContent(answer);

	Document docout = new Document(root);
	XMLOutputter send = new XMLOutputter();
	try{
	    toOut = send.outputString(docout);
	}catch(Exception e){}

	return toOut;
    }


    public Vector getRecipients()
    {
	return null;
    }

    public String getCommand()
    {
	return "Logout";
    }
}
















