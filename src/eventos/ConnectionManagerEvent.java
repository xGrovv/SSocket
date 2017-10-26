/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eventos;

import java.util.EventObject;
import servidor.ConnectionManager;

/**
 *
 * @author Grover
 */
public class ConnectionManagerEvent extends EventObject{
    
    private ConnectionManager conMan;

    public ConnectionManagerEvent(Object source, ConnectionManager connectionManager) {
        super(source);
        conMan = connectionManager;
    }
    
}
