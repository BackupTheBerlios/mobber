////////////////////////////////////////////////////////////////////////////
//
//  $RCSfile: Group.java,v $ 
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

import java.net.*;
import java.util.*;

/**
 * This class represents a group and its attributes
 *
 **/
public class Group
{
    /** Groups name */
    private String _name;
    /** Groups description */
    private String _desc;
    /** Groups users */
    private Vector _groupusers = new Vector();
    /** Groups Admin (creator) */
    private User _admin;
    /** Groups producer flag */
    private boolean _isProducer;

    public Group (String name, String desc)
    {
	_name = name;
	_desc = desc;
    }

    public String getName(){return _name;}
    public void setName(String name){_name = name;}

    public String getDesc(){return _desc;}
    public void setDesc(String desc){_desc = desc;}

    public User getAdmin(){return _admin;}
    public void setAdmin(User admin){_admin = admin;}

    public boolean getIsProducer(){return _isProducer;}
    public void setIsProducer(boolean isProducer){_isProducer = isProducer;}

    public Vector getGroupUsers(){return _groupusers;}

    // Not yet implemented 
    public void  getUsers(){}


    /**
     * add a user to the group
     * 
     * @param user the User-Object
     *
     * return 0  OK <br>
              -1 not OK
     */
    public int addUser(User user)
    {
	if (_groupusers.indexOf(user) == -1){
	    _groupusers.add(user);
	    return 0;
	}
	return -1;
    }

    /**
     * delete a user from the group
     * 
     * @param user the User-Object
     *
     * return 0  OK <br>
              -1 not OK
     */
    public int  delUser(User user)
    {
	int item = _groupusers.indexOf(user);
	if (item > -1){
	    _groupusers.remove(item);
	    return 0;
	}
	return -1;
    }
}
