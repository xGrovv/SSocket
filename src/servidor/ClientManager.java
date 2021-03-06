/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import eventos.ClientManagerEvent;
import eventos.ClientManagerListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Grover
 */
public class ClientManager extends Thread{
    
    private Client cliente;
    private DataInputStream messageIn;
    private String message;
    private ArrayList listeners;
    private boolean connected;
    
    public ClientManager(Client cliente){
        this.cliente= cliente;
        message = "";
        connected = true;
        listeners= new ArrayList();
        try {
            messageIn = new DataInputStream(this.cliente.getSocket().getInputStream());
        } catch (IOException e) {
            System.out.println("ERRR: ClientManager.Constructor:> : "+ e.getMessage());
        }
    }
    
    public void addListenerEvent(ClientManagerListener clientManagerListener){
        listeners.add(clientManagerListener);
    }
    
    public Socket getSocket(){
        return cliente.getSocket();
    }
    
    public void iniciar(){
        this.start();
    }
    
    public void deterner(){
        connected=false;
        try {
            messageIn.close();
            cliente.getSocket().close();
        } catch (IOException ex) {
            Logger.getLogger(ClientManager.class.getName()).log(Level.SEVERE, null, ex);
        } /*catch (Throwable ex) {
            Logger.getLogger(ClientManager.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }
    
    @Override
    public void run() {
        try {
            while (connected){
                message = messageIn.readUTF();  // a la espera de mensajes
                ListIterator li = listeners.listIterator();
                while (li.hasNext()) {
                    ClientManagerListener listener = (ClientManagerListener) li.next();
                    ClientManagerEvent evObj = new ClientManagerEvent(this);
                    (listener).onReceiveMessage(evObj);
                }
            }
            System.out.println("");
        } catch (SocketException ex) {// reset cliente
            ListIterator li = listeners.listIterator();
            while (li.hasNext()) {
                ClientManagerListener listener = (ClientManagerListener) li.next();
                ClientManagerEvent evObj = new ClientManagerEvent(this);
                (listener).onDisconnectClient(evObj);
            }
        } catch (java.io.EOFException eofEx){ // closed socket client
            System.out.println("CLIENTE SE DESCONEXTO "+ eofEx.getMessage());
            ListIterator li = listeners.listIterator();
            while (li.hasNext()) {
                ClientManagerListener listener = (ClientManagerListener) li.next();
                ClientManagerEvent evObj = new ClientManagerEvent(this);
                (listener).onDisconnectClient(evObj);
            }
        }
        catch (IOException e) {// cierre de socket cliente
            System.out.println("2222CLIENTE SE DESCONEXTO "+ e.getMessage());
            ListIterator li = listeners.listIterator();
            while (li.hasNext()) {
                ClientManagerListener listener = (ClientManagerListener) li.next();
                ClientManagerEvent evObj = new ClientManagerEvent(this);//, this);
                (listener).onDisconnectClient(evObj);
            }
        }
    }
    
    public String getMessage(){
        return message;
    }
    
    public Client getClient(){
        return cliente;
    }
    
}
