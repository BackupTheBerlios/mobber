////////////////////////////////////////////////////////////////////////////
//
//  $RCSfile: MySQLAuth.java,v $ 
//
//  Author       Stefan Toepfer (stefan_toepfer@web.de)
//
//  Copyright (C) 2001 Stefan Toepfer
//
//  $Revision: 1.1 $
//  $Date: 2001/07/01 13:53:18 $
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

package mobber.server.logik.authentification;

import java.io.*;
import java.util.*;
import java.sql.*;
import mobber.server.logik.*;


/**
 * MySQL Authentification: checks Login in DB
 * (see various/config.xml for options)
 *
 * @see AuthentificationInterface
 **/
public class MySQLAuth implements AuthentificationInterface
{

    private String _login = null;
    private String _pass = null;


    public void setLogin(String login)
    {
	_login = login;
    }

    public void setPass(String pass)
    {
	_pass = pass;
    }

    public boolean isOK()
    {
	return _checkDB();
    }


    /**
     * does the database stuff (connect, lookup)
     * 
     * @return true Login is OK <br>
               false Login isnot OK (or DB-Error)
     */
    private boolean _checkDB()
    {
	boolean loginOK = false;
	String select = null;
	Connection conn = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;

	select = "select * from " + (String)Config.config.get("Auth-JDBC-TABLE") + " where " +
	    (String)Config.config.get("Auth-JDBC-LOGIN-ROW") + " = ? and " + 
	    (String)Config.config.get("Auth-JDBC-PASSWORD-ROW") + " = ?";
	try {
	    Class.forName("org.gjt.mm.mysql.Driver").newInstance(); 
	}catch(Exception e){
	    System.err.println("Unable to load driver.");
	    e.printStackTrace();
	}
	try {
	    conn = DriverManager.getConnection((String)Config.config.get("Auth-JDBC-URL"));
	    stmt = conn.prepareStatement(select);
	    stmt.setString(1, _login);
	    stmt.setString(2, _pass);
	    rs = stmt.executeQuery();
	    if (rs.next() == true)
		loginOK = true;
	    conn.close();
	}catch(SQLException sqle){
	    System.err.println("Unable lookup DB.");
	    sqle.printStackTrace();
	}
	return loginOK;
    }
}










