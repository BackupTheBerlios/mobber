////////////////////////////////////////////////////////////////////////////
//
//  $RCSfile: Config.java,v $ 
//
//  Author       Stefan Toepfer (stefan_toepfer@web.de)
//
//  Copyright (C) 2001 Stefan Toepfer
//
//  $Revision: 1.1 $
//  $Date: 2001/07/01 13:53:16 $
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
import java.io.*;
import org.jdom.input.*;
import org.jdom.output.*;
import org.jdom.*;

/**
 * Config parses the configurationfile and holds the values in a
 * hashmap
 *
 **/
public class Config
{

    /** The hasmap that holds the key/values **/
    public static HashMap config = new HashMap();

    /**
     * Constructor gets the ConfigFile and stores it in a hashmap
     * 
     * @param file ConfigFile 
     **/
    public Config(String file)
    {
	Document doc = null;
	SAXBuilder build = new SAXBuilder();
	try{
	    doc = build.build(new File(file));
	}catch(JDOMException jdex){
	    jdex.printStackTrace();
	}

	Element root =  doc.getRootElement();
	config.put("Log-Config", root.getChild("log-config").getText());
	config.put("Port", root.getChild("port").getText());
	config.put("MaxUsers", root.getChild("maxusers").getText());
	config.put("Command-NS", root.getChild("command-ns").getText());
	config.put("Command-VALIDATE", root.getChild("command-validate").getText());
	config.put("Command-DTD", root.getChild("command-dtd").getText());
	Element auth = root.getChild("auth");
	config.put("Auth-Module", auth.getChild("auth-module").getText());
	config.put("Auth-JDBC-URL", auth.getChild("auth-jdbc-url").getText());
	config.put("Auth-JDBC-TABLE", auth.getChild("auth-jdbc-table").getText());
	config.put("Auth-JDBC-LOGIN-ROW", auth.getChild("auth-jdbc-login-row").getText());
	config.put("Auth-JDBC-PASSWORD-ROW", auth.getChild("auth-jdbc-password-row").getText());
	Element admin = root.getChild("admin");
	config.put("Admin-User", admin.getChild("user").getText());
	config.put("Admin-Pass", admin.getChild("pass").getText());
    }

}







