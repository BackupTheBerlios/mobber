////////////////////////////////////////////////////////////////////////////
//
//  $RCSfile: ClientList.java,v $ 
//
//  Author       Stefan Toepfer (stefan_toepfer@web.de)
//
//  Copyright (C) 2001 Stefan Toepfer
//
//  $Revision: 1.1 $
//  $Date: 2001/07/01 13:53:15 $
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

/**
 * List of all Users currently logged in
 *
 **/
public class ClientList
{
    /** Vector of all User-Objects of Mobber */
    public static  Vector users = new Vector();

    /**
     * addUser adds a user to users and reverseusers
     * 
     * @param user the User-Object
     */
    public static void addUser(User u)
    {
	users.add(u);
    }
    
    /**
     * delUser deletes a user with a given username
     * and removes him from every group
     * 
     * @param user the User-Object
     */
    public static void delUser(User u)
    {
	for (int l = 0; l < GroupList.groups.size(); l++)
	    ((Group)GroupList.groups.get(l)).delUser(u);
	users.remove(u);
    }
}
