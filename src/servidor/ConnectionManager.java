/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ListIterator;
import eventos.ConnectionManagerEvent;
import eventos.ConnectionManagertListener;

/**
 *
 * @author Grover
 */
public class ConnectionManager extends Thread{
    
    private ServerSocket sserver;
    private Socket sclient;
    private Client cliente=null;
    private ArrayList listeners;
    private boolean enable;
    //funciones de iniciar, pausar, terminar
    
    public ConnectionManager(ServerSocket ss) {
        enable=false;
        this.sserver=ss;
        listeners= new ArrayList();
    }
    
    public void addListenerEvent(ConnectionManagertListener connectionManagertListener){
        listeners.add(connectionManagertListener);
    }
    
    public void Iniciar(){
        enable=true;
        start();
    }
    
    public void Detener(){
        enable=false;
        
    }
    
    public Client getCliente(){
        return this.cliente;
    }
    
    @Override
    public void run() {
        while (enable) {
            try {
                System.out.println("Connection Manager:> Esperando conexiones.....");
                sclient = sserver.accept();  // escuchando conexiones
                
                cliente= new Client(sclient);
                // lanza evento onConnet 
                ListIterator li = listeners.listIterator();
                while (li.hasNext()) {
                    ConnectionManagertListener listener = (ConnectionManagertListener) li.next();
                    ConnectionManagerEvent evObj = new ConnectionManagerEvent(this, this);
                    (listener).onConnetClient(evObj);
                }
                
                //ClientManager clientComunication = new ClientManager(cliente); // establecemos un hilo de conexion con el cliente
                //clientComunication.start();   // inicializamos el hilo
                //System.out.println("se conecto un cliente.....");
                
                //lista_clientes.add(hilo_cliente);   // agregamos al cliente a la lista
            } catch (IOException ex) {
                System.out.println("error run HiloServidor S...");
            }
            
        }
    }
}
