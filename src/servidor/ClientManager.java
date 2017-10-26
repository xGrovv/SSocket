/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import eventos.ClientManagerEvent;
import eventos.ClientManagerListener;
import eventos.ConnectionManagertListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
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
    
    //private Socket socket;
    private Client cliente;
    private DataOutputStream messageOut;
    private DataInputStream messageIn;
    private String message;
    private boolean connected;
    
    private ArrayList listeners;
    
    public ClientManager(Client cliente){
        
        this.cliente= cliente;
        connected = true;
        listeners= new ArrayList();
        try {
            messageOut = new DataOutputStream(this.cliente.getSocket().getOutputStream());
            messageIn = new DataInputStream(this.cliente.getSocket().getInputStream());
            message = "";
        } catch (IOException e) {
            System.out.println("error ClientManager Constructor: "+ e.getMessage());
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
    
    public void pausar(){
        
    }
    
    public void deterner(){
        connected=false;
        try {
            cliente.getSocket().close();
        } catch (IOException ex) {
            Logger.getLogger(ClientManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @Override
    public void run() {
        try {
            //while (connected) {
            while (connected){
                
                message = messageIn.readUTF();  // a la espera de mensajes
                System.out.println("Nuevo Mensaje del cliente: " + message);
            }
            
            System.out.println("");
        } catch (SocketException ex) {
            System.out.println("SE DESCONECTO EL CLIENTE: "+ ex.getMessage());
            ListIterator li = listeners.listIterator();
            while (li.hasNext()) {
                 
                ClientManagerListener listener = (ClientManagerListener) li.next();
                ClientManagerEvent evObj = new ClientManagerEvent(this, this);
                (listener).onDisconnectClient(evObj);
            }
        } catch (IOException e) {
            System.out.println("ERRR "+ e.getMessage());
        }
    }
    
    public Client getClient(){
        return cliente;
    }
    
    public void enviar(String mensaje) {
        try {
            messageOut.writeUTF(mensaje);
            messageOut.flush();
        } catch (IOException e) {
            System.out.println("error ClientManager.enviar :"+ e.getMessage());
        }
    }
    
}
