////////////////////////////////////////////////////////////////////////////
//
//  $RCSfile: Kick.java,v $ 
//
//  Author       Stefan Toepfer (stefan_toepfer@web.de)
//
//  Copyright (C) 2001 Stefan Toepfer
//
//  $Revision: 1.1 $
//  $Date: 2001/07/01 13:53:21 $
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

/**
 * Command kick (kicks user from group)
 *
 * @see Command
 */
public class Kick extends Command
{
    /** Name of the group */
    private String _group = null;
    /** Name of the user to kick */
    private String _user = null;
    /** The answer for the reply */
    private String _answer = Constants.NOT_OK;
    /** User-Object of client */
    private User _admin = null;
    /** User-Object of user to kick */
    private User _toKick = null;
    /** Group-Object the group to kick from */
    private Group _kickGroup = null;

    public Kick(Document doc)
    {
	_doc = doc;
    }

    public void doIt()
    {
	boolean isAdmin = false;
	Vector myGroupUsers = null;

	Element root =  _doc.getRootElement();
	Element kick = root.getChild("kick");
	_group = kick.getChild("group").getText();
	_user = kick.getChild("user").getText();

	// Get the User who is sending the Kick
	for (int l = 0; l < ClientList.users.size(); l++){
	    if (((User)ClientList.users.get(l)).getSocket().equals(_client) == true)
		_admin = (User)ClientList.users.get(l);
	}
	
	// Get the group
	for (int l = 0; l < GroupList.groups.size(); l++){
	    if (((Group)GroupList.groups.get(l)).getName().equals(_group) == true){
		_kickGroup = (Group)GroupList.groups.get(l);
	    }
	}
	
	// is this the admin of the group
	if (_admin != null && _kickGroup != null){
	    if (((User)_kickGroup.getAdmin()).equals(_admin) == true)
		isAdmin = true;
	}

	// Get the User to kick
	if (isAdmin == true){
	    myGroupUsers = _kickGroup.getGroupUsers();
	    for (int l = 0; l < myGroupUsers.size(); l++){
		if (((User)myGroupUsers.get(l)).getName().equals(_user) == true)
		    _toKick = (User)ClientList.users.get(l);
	    }
	}

	// kick the User
	if (_toKick != null){
	    _kickGroup.delUser(_toKick);
	    _answer = Constants.OK;
	}

    }

    public String getMessage()
    {
	String toOut = null;

	if (_toKick != null){
	    Element root = new Element("mobber");
	    Element message = new Element("message");
	    root.addContent(message);
	    
	    Element sender = new Element("sender");
	    sender.setText(Constants.SERVERNAME);
	    message.addContent(sender);
	    
	    Element mesg = new Element("mesg");
	    mesg.setText("You were kicked from group: " + _kickGroup.getName());
	    message.addContent(mesg);
	    
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
	Element kick = new Element("kick");
	root.addContent(kick);
	Element user = new Element("user");
	user.setText(_user);
	kick.addContent(user);
	Element answer = new Element("answer");
	answer.setText(_answer);
	kick.addContent(answer);

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
	if (_toKick != null)
	    al.add(_toKick);
	return al;
    }


    public String getCommand()
    {
	return "Kick";
    }
}
