/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

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
    
    public ClientManager(Client cliente){
        
        this.cliente= cliente;
        connected = true;
        try {
            messageOut = new DataOutputStream(this.cliente.getSocket().getOutputStream());
            messageIn = new DataInputStream(this.cliente.getSocket().getInputStream());
            message = "";
        } catch (IOException e) {
            System.out.println("error ClientManager Constructor: "+ e.getMessage());
        }
    }
    
    public void iniciar(){
        this.start();
    }
    
    public void pausar(){
        
    }
    
    public void deterner(){
        
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
            //  lanzar evento de desconeccion
            //aqui
        } catch (IOException e) {
            System.out.println("ERRR "+ e.getMessage());
        }
    }

    public void desconectar(){
        connected = false;
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
