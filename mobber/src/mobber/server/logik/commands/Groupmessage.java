////////////////////////////////////////////////////////////////////////////
//
//  $RCSfile: Groupmessage.java,v $ 
//
//  Author       Stefan Toepfer (stefan_toepfer@web.de)
//
//  Copyright (C) 2001 Stefan Toepfer
//
//  $Revision: 1.1 $
//  $Date: 2001/07/01 13:53:20 $
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

public class Groupmessage extends Command
{
    private String _themessage = null;
    private String _sendername = null;
    private String _group = null;

    public Groupmessage(Document doc)
    {
	_doc = doc;
    }

    public void doIt()
    {
	Element root =  _doc.getRootElement();
	Element groupmessage = root.getChild("groupmessage");
	_group = groupmessage.getChild("group").getText();
	_themessage = groupmessage.getChild("mesg").getText();
	int size = ClientList.users.size();
	for (int l = 0; l < size; l++){
	    if (((User)ClientList.users.get(l)).getSocket().equals(_client) == true)
		_sendername = ((User)ClientList.users.get(l)).getName();
	}
    }

    public String getMessage()
    {
	String toOut = null;
	
	Element root = new Element("mobber");
	Element groupmessage = new Element("groupmessage");
	root.addContent(groupmessage);

	Element group = new Element("group");
	group.setText(_group);
	groupmessage.addContent(group);

	Element sender = new Element("sender");
	sender.setText(_sendername);
	groupmessage.addContent(sender);

	Element mesg = new Element("mesg");
	mesg.setText(_themessage);
	groupmessage.addContent(mesg);

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
	Vector myGroupUsers = null;
	int index;
	boolean isGroupMember = false;
	
	// Search the group
	int size = GroupList.groups.size();
	for (int l = 0; l < size; l++){
	    if (((Group)GroupList.groups.get(l)).getName().equals(_group) == true){
		//Is this a Producer (You can't write to a Producer)
		if (((Group)GroupList.groups.get(l)).getIsProducer() == false)
		    myGroupUsers = ((Group)GroupList.groups.get(l)).getGroupUsers();
		break;
	    }
	}
	// If found add the Users to the Vector
	if (myGroupUsers != null){
	    int gsize = myGroupUsers.size();
	    for (int i = 0; i < gsize; i++){
		if (((User)myGroupUsers.get(i)).getSocket().equals(_client) == true)
		    isGroupMember = true;
		al.add((User)myGroupUsers.get(i));
	    }
	}
	// if sender is not in Group don't send the message
	if (isGroupMember == false)
	    al.clear();
	return al;
	}


    public String getCommand()
    {
	return "Groupmessage";
    }
}




















