/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import eventos.ClientListObserverEvent;
import eventos.ClientListObserverListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rudy
 */
public class ClientListObserver extends Thread {
    
    private ArrayList<ClientManager> listClients;
    private ArrayList listeners;
    //private ClientManager clientDisconnected;
    private boolean enable;

    public ClientListObserver(ArrayList<ClientManager> listClients) {
        this.listClients = listClients;
        listeners= new ArrayList();
        //clientDisconnected=null;
        enable =false;
    }

//    public ClientManager obtenerClienteDesconectado(){
//        return clientDisconnected;
//    }
    
    public void addListenerEvent(ClientListObserverListener listObserverListener){
        listeners.add(listObserverListener);
    }
    
    public void iniciar(){
        enable=true;
        start();
    }
    
    public void detener(){
        enable =false;
    }
    
    @Override
    public void run() {
        try {
            while (enable){
                for (Object obj : listClients){
                    ClientManager cli = (ClientManager)obj;
                    if(!cli.getClient().getInetAddress().isReachable(3000)){
                        //clientDisconnected=cli;
                        ListIterator li = listeners.listIterator();
                        while (li.hasNext()) {
                            ClientListObserverListener listener = (ClientListObserverListener) li.next();
                            
                            ClientListObserverEvent evObj = new ClientListObserverEvent(cli);
                            (listener).onLostConnection(evObj);
                        }
                    }
                }
            }
        } catch (java.util.ConcurrentModificationException e) {
            System.out.println("error Controlado GG> ClientListObserver.run [ava.util.ConcurrentModificationException]");
            //Logger.getLogger(ClientListObserver.class.getName()).log(Level.SEVERE, null, e);
        } catch (IOException ex) {
            Logger.getLogger(ClientListObserver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
