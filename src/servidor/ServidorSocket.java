/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import eventos.ClientListObserverEvent;
import eventos.ClientListObserverListener;
import eventos.ClientManagerEvent;
import eventos.ConnectionManagerEvent;
import eventos.ClientManagerListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import eventos.ConnectionManagertListener;

/**
 *
 * @author Grover
 */
public class ServidorSocket implements ConnectionManagertListener, ClientManagerListener, ClientListObserverListener{
    
    private ServerSocket server=null;
    private ConnectionManager connectionManager=null;
    private ClientListObserver clientListObserver;
    
    private ArrayList<ClientManager> clientManagerList;
    private int port;
    
    public ServidorSocket(int port){
        this.port=port;
        clientManagerList = new ArrayList<>();
        try{
            server= new ServerSocket(this.port);
        } catch (IOException e) {
            System.out.println("No se pudo abrir el servidor socket: "+e.getMessage());
        }
    }
    
        @Override
        public void onConnetClient(ConnectionManagerEvent ev) {
            ConnectionManager con = (ConnectionManager)ev.getSource();
            Client cliente = con.getCliente();
            ClientManager clientManager= new ClientManager(cliente);
            clientManager.addListenerEvent(this);
            clientManagerList.add(clientManager);
            System.out.println("ServidorSocket:> Nuevo Cliente Registrado:: ");
            clientManager.iniciar();
            
        }

        @Override
        public void onDisconnectClient(ClientManagerEvent ev) {
            ClientManager cli = (ClientManager)ev.getSource();
            cli.deterner();
            clientManagerList.remove(cli);
            System.out.println("ServidorSocket:> Una Cliente fue Borrado:: ");
        }
        
        @Override
        public void onReceiveMessage(ClientManagerEvent ev){
            ClientManager cli = (ClientManager)ev.getSource();
            System.out.println("Mensage del Cliente:: "+cli.getMessage());
            
        }
        
        @Override
        public void onLostConnection(ClientListObserverEvent ev) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    
    public void iniciarServicio(){
        if (server==null)
            return;
        connectionManager= new ConnectionManager(server);
        connectionManager.addListenerEvent(this);
        connectionManager.Iniciar();
        
        clientListObserver = new ClientListObserver(clientManagerList);
        clientListObserver.addListenerEvent(this);
        clientListObserver.start();
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
