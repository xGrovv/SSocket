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
    private boolean enable;
    private int index;

    public ClientListObserver(ArrayList<ClientManager> listClients) {
        this.listClients = listClients;
        listeners= new ArrayList();
        enable =false;
        index=0;
    }
    
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
    
    public ClientManager nextClient(){
        ClientManager cli = listClients.get(index);
        index++;
        return cli;
    }
    
    @Override
    public void run() {
        while (enable){
            try {
                for (Object obj : listClients){
                    ClientManager cli = (ClientManager)obj;                                
                    String address =cli.getClient().getIp().substring(1, cli.getClient().getIp().length());
                    if(!isReachable(address)){
                        ListIterator li = listeners.listIterator();
                        while (li.hasNext()) {
                            ClientListObserverListener listener = (ClientListObserverListener) li.next();
                            ClientListObserverEvent evObj = new ClientListObserverEvent(cli);
                            (listener).onLostConnection(evObj);
                            System.out.println("Conexion perdida de un Cliente:: "+ cli.getClient().getIp()+ " [Quitado]");
                        }
                    }                    
                }

            } catch (java.util.ConcurrentModificationException e) {
                System.out.println("error Controlado GG> ClientListObserver.run [ava.util.ConcurrentModificationException]");
            }
        }
    }
    
    public boolean isReachable(String address){        
        try {          
            boolean reachable;
            try {
                reachable = (java.lang.Runtime.getRuntime().exec("ping -n 1 "+address).waitFor()==0);
                if(reachable)
                    System.out.println("ranning on line ip -->"+address);
                else
                    System.out.println("break .. offline ip-->"+address);
                return reachable;
            } catch (InterruptedException ex) {
                Logger.getLogger(ClientListObserver.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientListObserver.class.getName()).log(Level.SEVERE, null, ex);
        }   return false;        
    }

}
