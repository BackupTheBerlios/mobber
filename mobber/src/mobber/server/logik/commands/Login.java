////////////////////////////////////////////////////////////////////////////
//
//  $RCSfile: Login.java,v $ 
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
import java.lang.reflect.*;
import java.net.*;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;
import mobber.server.logik.*;


public class Login extends Command
{
    private boolean _isOK = false;
    private boolean _isAdmin = false;
    private String _name;

    public Login(Document doc)
    {
	_doc = doc;
    }

    public void doIt()
    {
	Element root =  _doc.getRootElement();
	Element login =  root.getChild("login");
	_name = login.getChild("name").getText();
	String password = login.getChild("password").getText();
	if (_name.equals((String)Config.config.get("Admin-User")) == true &&
	    password.equals((String)Config.config.get("Admin-Pass")) == true){
	    _isOK = true;
	    _isAdmin = true;
	    _addUser();
	    
	} else if (_loginCheck(_name,password) == true){ 
	    _isOK = true;
	    _addUser();
	}
    }

    /**
     * Checks if user allready is logged in or if users name is logged in
     * if not creates a new User and adds User to ClientList
     * 
     */
    private void _addUser()
    {
	boolean isloggedin = false;
	PrintWriter _pout = null;
	
	for (int i = 0; i < ClientList.users.size(); i++){
	    if ((((User)ClientList.users.get(i)).getSocket().equals(_client) == true) || 
		(((User)ClientList.users.get(i)).getName().equals(_name) == true))
		isloggedin = true;
	}

	if (_isOK == true && isloggedin == false && _isAdmin == false){
	    User user = new User(_client,_name,false);
	    ClientList.addUser(user);
	}else if (_isOK == true && isloggedin == false && _isAdmin == true){
	    User user = new User(_client,_name,true);
	    ClientList.addUser(user);
	    System.out.println("Got Login from Admin\n");
	} else {
	    _isOK = false;
	}
    }


    /**
     * Logincheck for User
     * 
     * @param name      the name
     * @param password  the password
     */
    private boolean _loginCheck(String name, String password)
    {
	String authmodule = (String)Config.config.get("Auth-Module");
	Class command = null;
	AuthentificationInterface auth = null;

	try{
	    command = Class.forName(authmodule ,true , this.getClass().getClassLoader());
	    Constructor con = command.getConstructor(new Class []{});
	    Object o = con.newInstance(new Object[]{});
	    auth = (AuthentificationInterface)o;
	}catch(Exception e){}
	auth.setLogin(name);
	auth.setPass(password);
	return auth.isOK();
    }


    public String getMessage()
    {
	return null;
    }

    public String getReply()
    {
	String toOut = null;
	
	if (_isOK == true){
	    Element root = new Element("mobber");
	    Element login = new Element("login");
	    root.addContent(login);
	    Element answer = new Element("answer");
	    answer.setText(Constants.OK);
	    login.addContent(answer);

	    Document docout = new Document(root);
	    XMLOutputter send = new XMLOutputter();
	    try{
		toOut = send.outputString(docout);
	    }catch(Exception e){}
	}
	return toOut;
    }

    public Vector getRecipients()
    {
	return null;
    }

    public String getCommand()
    {
	return "Login";
    }
}


















