////////////////////////////////////////////////////////////////////////////
//
//  $RCSfile: SimpleClient.java,v $ 
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

public class SimpleClient implements MobberRelay
{

    private MobberUI  mUI;
    private MobberClient mc;




    public SimpleClient()
    {
	mUI = new MobberUI(this);
	mc = new MobberClient(this);
    }


    public void toClient(String message)
    {
	mc.processMessage(message);
    }
    
    public void toUI(String message)
    {
	mUI.displayString(message);
    }
    
    public void exit()
    {
	System.exit(0);
    }
    
    public static void main(String[] args) throws IOException
    {
	SimpleClient sc = new SimpleClient();
	BufferedReader cl = new BufferedReader(new InputStreamReader(System.in));
	sc.toUI("This is the Simple Mobber Client \n");

	while(true){
	    String command = cl.readLine();
	    sc.toClient(command);
	}
    }






}
