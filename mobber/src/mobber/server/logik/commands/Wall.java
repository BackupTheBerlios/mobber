////////////////////////////////////////////////////////////////////////////
//
//  $RCSfile: Wall.java,v $ 
//
//  Author       Stefan Toepfer (stefan_toepfer@web.de)
//
//  Copyright (C) 2001 Stefan Toepfer
//
//  $Revision: 1.1 $
//  $Date: 2001/07/01 13:53:24 $
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

public class Wall extends Command
{
    private String _wallmessage = null;
    private String _sendername = null;
    private User _admin = null;



    public Wall(Document doc)
    {
	_doc = doc;
    }

    public void doIt()
    {
	Element root =  _doc.getRootElement();
	Element login =  root.getChild("wall");
	_wallmessage = login.getChild("mesg").getText();
	for (int i = 0; i < ClientList.users.size(); i++){
	    if (((User)ClientList.users.get(i)).getSocket().equals(_client) == true){
		_admin = (User)ClientList.users.get(i);
		_sendername = _admin.getName();
	    }
	}
    }

    public String getMessage()
    {
	String toOut = null;
	
	// Send wall if this is the Admin
	if (_admin.isAdmin() == true){
	    Element root = new Element("mobber");
	    Element wall = new Element("wall");
	    root.addContent(wall);

	    Element sender = new Element("sender");
	    sender.setText(_sendername);
	    wall.addContent(sender);
	    
	    Element answer = new Element("mesg");
	    answer.setText(_wallmessage);
	    wall.addContent(answer);

	    Document docout = new Document(root);
	    XMLOutputter send = new XMLOutputter();
	    try{
		toOut = send.outputString(docout);
	    }catch(Exception e){}
	}
	return toOut;
    }

    public String getReply()
    {
	return null;
    }
    

    public Vector getRecipients()
    {
	Vector al = new Vector();
	for (int i = 0; i < ClientList.users.size(); i++)
	    al.add((User)ClientList.users.get(i));
	return al;
    }

    public String getCommand()
    {
	return "Wall";
    }




}








