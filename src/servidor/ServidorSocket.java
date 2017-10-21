/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import eventos.ClientEventImplement;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Grover
 */
public class ServidorSocket {
    
    private ServerSocket server=null;
    private ConnectionManager connectionManager=null;
    private ArrayList<Socket> listClientes;
    private int port;
    
    public ServidorSocket(int port){
        this.port=port;
        listClientes = new ArrayList<>();
        try{
            server= new ServerSocket(port);
        } catch (Exception e) {
            System.out.println("No se pudo abrir el servidor socket: "+e.getMessage());
        }
    }
    
    public void iniciarServicio(){
        //try {
            connectionManager= new ConnectionManager(server);
            connectionManager.addListenerEvent(new ClientEventImplement());
            connectionManager.Iniciar();
        //} catch (Exception e) {
        //}
        
        
    }
    
    public void detenerServicio() {
        connectionManager.Detener();
        try {
            server.close();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
