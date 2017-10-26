/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eventos;

import java.util.EventObject;
import servidor.ClientManager;

/**
 *
 * @author Grover
 */
public class ClientManagerEvent extends EventObject {
    
    private ClientManager cli;
    
    public ClientManagerEvent(Object source, ClientManager clientManager) {
        super(source);
        cli = clientManager;
    }
    
    
}
