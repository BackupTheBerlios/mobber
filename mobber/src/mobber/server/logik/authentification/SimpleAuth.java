////////////////////////////////////////////////////////////////////////////
//
//  $RCSfile: SimpleAuth.java,v $ 
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

package mobber.server.logik.authentification;

import java.io.*;
import java.util.*;
import mobber.server.logik.*;


/**
 * Simple Authentification: login equals password
 *
 * @see AuthentificationInterface
 **/
public class SimpleAuth implements AuthentificationInterface
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
	return _login.equals(_pass);
    }
}



