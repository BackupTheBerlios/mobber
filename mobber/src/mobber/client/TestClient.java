////////////////////////////////////////////////////////////////////////////
//
//  $RCSfile: TestClient.java,v $ 
//
//  Author       Stefan Toepfer (stefan_toepfer@web.de)
//
//  Copyright (C) 2001 Stefan Toepfer
//
//  $Revision: 1.1 $
//  $Date: 2001/07/01 13:53:13 $
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

package mobber.client;

import java.io.*;

public class TestClient extends SimpleClient
{
    private MobberUI  mUI;
    private MobberClient mc;

    public TestClient()
    {
	mUI = new MobberUI(this);
	mc = new MobberClient(this);
    }

    
    public static void main(String[] args) throws IOException
    {
	TestClient sc = new TestClient();
	BufferedReader cl = new BufferedReader(new InputStreamReader(System.in));
	sc.toUI("This is the Simple Mobber TestClient \n");

	sc.toClient("/name " + args[0]);
	sc.toClient("/pass " + args[0]);
	sc.toClient("/connect localhost 2405");
	sc.toClient("/login");

	while (true){
	    sc.toClient("/message Stefan Hallo Stefan was geht!");
	    sc.toClient("/listuser");
	    sc.toClient("/listgroup");
	    sc.toClient("/joingroup test");
	    sc.toClient("/groupmessage Hallo Stefan was geht! Das ist von " + args[0]);
	    sc.toClient("/partgroup test");

	}
    }
}

