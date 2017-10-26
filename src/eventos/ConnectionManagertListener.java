/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eventos;

import java.util.EventListener;

/**
 *
 * @author Grover
 */
public interface ConnectionManagertListener extends EventListener{
    
    public void onConnetClient(ConnectionManagerEvent ev);
    //public void onDisconnectClient(ConnectionManagerEvent ev);  
}
