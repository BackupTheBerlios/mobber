////////////////////////////////////////////////////////////////////////////
//
//  $RCSfile: Joingroup.java,v $ 
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

public class Joingroup extends Command
{

    private String _answer = Constants.NOT_OK;
    private String _group = null;
    private User _joinUser = null;
    private Group _joinGroup = null;

    public Joingroup(Document doc)
    {
	_doc = doc;
    }

    public void doIt()
    {
	int _groupIndex = -1;
	

	Element root =  _doc.getRootElement();
	Element joingroup =  root.getChild("joingroup");
	_group = joingroup.getChild("group").getText();
	int gsize = GroupList.groups.size();
	for (int i = 0; i < gsize; i++){
	    if ((((Group)GroupList.groups.get(i)).getName().equals(_group) == true))
		_groupIndex = i;
	}
	if (_groupIndex > -1){
	    int clsize = ClientList.users.size();
	    for (int i = 0; i < clsize; i++){
		if ((((User)ClientList.users.get(i)).getSocket().equals(_client) == true))
		_joinUser = (User)ClientList.users.get(i);
	}
	_joinGroup = (Group)GroupList.groups.get(_groupIndex);
	int status = _joinGroup.addUser(_joinUser);
	if (status == 0)
	    _answer = Constants.OK;
	}
    }
    

    public String getMessage()
    {
	String toOut = null;
	
	if (_answer == Constants.OK){
	    Element root = new Element("mobber");
	    Element groupmessage = new Element("groupmessage");
	    root.addContent(groupmessage);
	    
	    Element group = new Element("group");
	    group.setText(_joinGroup.getName());
	    groupmessage.addContent(group);
	
	    Element sender = new Element("sender");
	    sender.setText(Constants.SERVERNAME);
	    groupmessage.addContent(sender);

	    Element mesg = new Element("mesg");
	    mesg.setText(_joinUser.getName() + " has joined the group");
	    groupmessage.addContent(mesg);
	    
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
	String toOut = null;
	
	Element root = new Element("mobber");
	Element joingroup = new Element("joingroup");
	root.addContent(joingroup);

	Element group = new Element("group");
	group.setText(_group);
	joingroup.addContent(group);

	Element answer = new Element("answer");
	answer.setText(_answer);
	joingroup.addContent(answer);
    
	Document docout = new Document(root);
	XMLOutputter send = new XMLOutputter();
	try{
	    toOut = send.outputString(docout);
	}catch(Exception e){}
	return toOut;
    }

    public Vector getRecipients()
    {
	Vector al = new Vector();
	Vector groupUsers = null;

	if (_answer == Constants.OK){
	    // Get Groupusers
	    if (_joinGroup.getIsProducer() == false)
		groupUsers = _joinGroup.getGroupUsers();
	    // If found add the Users to the Vector
	    if (groupUsers != null){
		int gsize = groupUsers.size();
		for (int i = 0; i < gsize; i++)
		    al.add((User)groupUsers.get(i));
	    }
	}
	return al;
    }
	
    public String getCommand()
    {
	return "Joingroup";
    }
}










