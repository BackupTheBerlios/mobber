////////////////////////////////////////////////////////////////////////////
//
//  $RCSfile: Creategroup.java,v $ 
//
//  Author       Stefan Toepfer (stefan_toepfer@web.de)
//
//  Copyright (C) 2001 Stefan Toepfer
//
//  $Revision: 1.1 $
//  $Date: 2001/07/01 13:53:19 $
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
 * Command creategroup (creates a new group)
 *
 * @see Command
 */
public class Creategroup extends Command
{
    private String _group = null;
    private String _desc = null;
    private User _admin = null;
    private String _answer = Constants.NOT_OK;


    public Creategroup(Document doc)
    {
	_doc = doc;
    }

    public void doIt()
    {
	boolean groupexists = false;
	

	Element root =  _doc.getRootElement();
	Element creategroup = root.getChild("creategroup");
	_group = creategroup.getChild("group").getText();
	_desc = creategroup.getChild("desc").getText();
	for (int l = 0; l < ClientList.users.size(); l++){
	    if (((User)ClientList.users.get(l)).getSocket().equals(_client) == true)
		_admin = (User)ClientList.users.get(l);
	}
	
	
	for (int l = 0; l < GroupList.groups.size(); l++){
	    if (((Group)GroupList.groups.get(l)).getName().equals(_group) == true){
		groupexists = true;
	    }
	}
	
	if (groupexists == false){
	    Group newGroup = new Group(_group, _desc);
	    newGroup.setAdmin(_admin);
	    newGroup.addUser(_admin);
	    GroupList.addGroup(newGroup);
	    _answer = Constants.OK;
	}

    }

    public String getMessage()
    {
	return null;
    }

    public String getReply()
    {
	String toOut = null;
	
	Element root = new Element("mobber");
	Element creategroup = new Element("creategroup");
	root.addContent(creategroup);

	Element group = new Element("group");
	group.setText(_group);
	creategroup.addContent(group);

	Element answer = new Element("answer");
	answer.setText(_answer);
	creategroup.addContent(answer);

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
	return "Creategroup";
    }
}
