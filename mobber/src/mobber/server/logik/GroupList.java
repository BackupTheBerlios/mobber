////////////////////////////////////////////////////////////////////////////
//
//  $RCSfile: GroupList.java,v $ 
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

import java.util.*;

/**
 * List of all groups in Mobber
 *
 **/
public class GroupList
{
    public static Vector groups = new Vector();

    /**
     * addGroup adds a Group to Groups 
     * 
     * @param g the Group-Object
     */
    public static void addGroup(Group g)
    {
	groups.add(g);
    }
    
    /**
     * delGroup deletes a Group with a given Groupname
     * 
     * @param g the Group-Object
     */
    public static void delGroup(Group g)
    {
	groups.remove(g);
    }
}


