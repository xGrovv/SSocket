/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import eventos.ClientListObserverListener;
import java.util.ArrayList;

/**
 *
 * @author rudy
 */
public class ClientListObserver extends Thread {
    
    private ArrayList<ClientManager> listClients;
    private ArrayList listeners;

    public ClientListObserver(ArrayList<ClientManager> listClients) {
        this.listClients = listClients;
        listeners= new ArrayList();
    }

    public void addListenerEvent(ClientListObserverListener listObserverListener){
        listeners.add(listObserverListener);
    }
    
    @Override
    public void run() {
        /*for(Object obj : listeners){
            ClientManager cli = (ClientManager)obj;
            
        
        }*/
    }
    
    
    
    
}
