////////////////////////////////////////////////////////////////////////////
//
//  $RCSfile: MobProcessor.java,v $ 
//
//  Author       Stefan Toepfer (stefan_toepfer@web.de)
//
//  Copyright (C) 2001 Stefan Toepfer
//
//  $Revision: 1.1 $
//  $Date: 2001/07/01 13:53:17 $
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

import java.io.*;
import java.util.*;
import java.lang.reflect.*;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;


/**
 * MobProcessor pasrses the XML request and returns the command-object
 *
 **/
public class MobProcessor
{
    /** the message from the client */
    private String _message = null;
    /** the command-namespace to use */
    private String _namespace = null;
    /** the number of parsed command of the instance */
    private long _parsed = 0;

    public MobProcessor()
    {
	// sets the Command Namespace
	_namespace = (String)Config.config.get("Command-NS");
    }

    /**
     * sets the message of the client
     * 
     * @param message the clients message
     */
    public void setMessage(String message)
    {
	_message = null;
	if (Config.config.get("Command-VALIDATE").equals("YES") == true)
	    _message = "<!DOCTYPE mobber SYSTEM \"" + (String)Config.config.get("Command-DTD") + "\">" + message;
	else
	    _message = message;
	_parsed++;
    }                                         

    /**
     * returns the parsed commands by this instance of MobProcessor
     * 
     * @return the number of parsed commands
     */
    public long getParsed(){return _parsed;}

    /**
     * processes the message of the client and dispatches it to the commands
     *
     * @return the command-object
     */
    public Object process() 
    {
	Class command;
	String what = new String();
	Document doc = null;
	SAXBuilder builder;

	if (Config.config.get("Command-VALIDATE").equals("YES") == true)
	    builder = new SAXBuilder("org.apache.xerces.parsers.SAXParser", true);
	else
 	    builder = new SAXBuilder("org.apache.xerces.parsers.SAXParser", false);

	try{
	    doc = builder.build(new StringReader(_message));
	    Element root =  doc.getRootElement();
	    List childs = root.getChildren();
	    Iterator i = childs.iterator();
	    what = ((Element)i.next()).getName();
	}catch(Exception e){
	    System.out.println("Fehler: MobProcessor");
	    e.printStackTrace();
	    MobError err = new MobError();
	    return err;
	}

	// extract the command
	String rest = what.toLowerCase();
	String begin = (rest.substring(0,1)).toUpperCase();
	String invoke = (begin.concat(rest.substring(1,rest.length()))).trim();
	
	// try to invoke the command
	try{
	    command = Class.forName(_namespace + invoke,true , this.getClass().getClassLoader());
	    Constructor con = command.getConstructor(new Class []{org.jdom.Document.class});
	    Object o = con.newInstance(new Object[]{doc});
	    return o;
	}catch(Exception e){
	    System.out.println("Fehler: Unknown Command \n" + e );
	    e.printStackTrace();
	    MobError err = new MobError();
	    return err;
	}
    }
}
