////////////////////////////////////////////////////////////////////////////
//
//  $RCSfile: Message.java,v $ 
//
//  Author       Stefan Toepfer (stefan_toepfer@web.de)
//
//  Copyright (C) 2001 Stefan Toepfer
//
//  $Revision: 1.1 $
//  $Date: 2001/07/01 13:53:23 $
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

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;
import mobber.server.logik.*;

public class Message extends Command
{
    private String _themessage = null;
    private String _sendername = null;
    private ArrayList _recipient = null;
    private int _size = 0;

    public Message(Document doc)
    {
	_doc = doc;
	_size = ClientList.users.size();
    }

    public void doIt()
    {
	Element root =  _doc.getRootElement();
	Element message =  root.getChild("message");
	List rec = message.getChildren("recipient");
	Iterator i = rec.iterator();
	_recipient = new ArrayList();
	while (i.hasNext()){
	    _recipient.add(((Element)i.next()).getText());
	}
	_themessage = message.getChild("mesg").getText();
	for (int l = 0; l < _size; l++){
	    if (((User)ClientList.users.get(l)).getSocket().equals(_client) == true)
		_sendername = ((User)ClientList.users.get(l)).getName();
	}
    }

    public String getMessage()
    {
	String toOut = null;
	
	Element root = new Element("mobber");
	Element message = new Element("message");
	root.addContent(message);

	Element sender = new Element("sender");
	sender.setText(_sendername);
	message.addContent(sender);

	Element mesg = new Element("mesg");
	mesg.setText(_themessage);
	message.addContent(mesg);

	Document docout = new Document(root);
	XMLOutputter send = new XMLOutputter();
	try{
	    toOut = send.outputString(docout);
	}catch(Exception e){}
	return toOut;
    }



    public String getReply()
    {
	return null;
    }



    public Vector getRecipients()
    {
	Vector al = new Vector();

	int rsize = _recipient.size();
	for (int i = 0; i < rsize; i++){
	    String name = (String)_recipient.get(i);
	    for (int l = 0; l < _size; l++){	    
		if (((User)ClientList.users.get(l)).getName().equals(name) == true)
		    al.add((User)ClientList.users.get(l));
	    }
	}
	for (int l = 0; l < _size; l++){	    
	    if (((User)ClientList.users.get(l)).getSocket().equals(_client) == true)
		    al.add((User)ClientList.users.get(l));
	}
	return al;
    }

    public String getCommand()
    {
	return "Message";
    }
}




















