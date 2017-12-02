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
public interface ServidorSocketListener extends EventListener {
    
    public void onNewConnection(ServidorSocketEvent ev);
    public void onMessageReceive(ServidorSocketEvent ev);
    //public void onFailInitSocket(ServidorSocketEvent ev);
    
}
