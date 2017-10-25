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

/**
 *
 * @author Grover
 */
public class ClientManager extends Thread{
    
    private Socket socket;
    private DataOutputStream messageOut;
    private DataInputStream messageIn;
    private String message;
    private boolean connected;
    
    public ClientManager(Socket sclient){
        this.socket = sclient;
        connected = true;
        try {
            messageOut = new DataOutputStream(this.socket.getOutputStream());
            messageIn = new DataInputStream(this.socket.getInputStream());
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
            while (socket.isConnected()){
                
                message = messageIn.readUTF();  // a la espera de mensajes
                System.out.println("Nuevo Mensaje del cliente: " + message);
            }
            // si ya no esta conectado -> lanzar evento de desconeccion
            //aqui
            System.out.println("");
        } catch (IOException e) {
            System.out.println("SE DESCONECTO EL CLIENTE: "+ e.getMessage());
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
