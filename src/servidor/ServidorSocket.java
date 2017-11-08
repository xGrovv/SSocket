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
import java.io.DataOutputStream;
import java.util.ListIterator;
import javax.swing.JTextArea;

/**
 *
 * @author Grover
 */
public class ServidorSocket implements  ClientManagerListener, ClientListObserverListener{
    
    private ServerSocket server=null;
    private ConnectionManager connectionManager=null;
    private ClientListObserver clientListObserver;
    private JTextArea textArea;
    
    private ArrayList<ClientManager> clientManagerList;
    private int port;
    
    public ServidorSocket(int port){
        this.port=port;
        clientManagerList = new ArrayList<>();
        try{
            server= new ServerSocket(this.port);
        } catch (IOException e) {
            System.out.println("ServidorSocket.Constructor:> No se pudo abrir el servidor socket:: "+e.getMessage());
        }
    }
    
    public void setTextArea(JTextArea textArea){
        this.textArea=textArea;
    }
    
        public void connetClient_Action(ConnectionManagerEvent ev) {
            ConnectionManager con = (ConnectionManager)ev.getSource();
            Client cliente = con.getCliente();
            ClientManager clientManager= new ClientManager(cliente);
            clientManager.addListenerEvent(this);
            
            clientManagerList.add(clientManager);
            System.out.println("Nuevo Cliente Registrado:: "+ cliente.getInetAddress().toString());
            clientManager.iniciar();
        }

        @Override
        public void onDisconnectClient(ClientManagerEvent ev) {
            ClientManager cli = (ClientManager)ev.getSource();
            clientManagerList.remove(cli);
            cli.deterner();
            System.out.println("Un Cliente se desconecto:: "+ cli.getClient().getInetAddress().toString());
        }
        
        @Override
        public void onReceiveMessage(ClientManagerEvent ev){
            ClientManager cli = (ClientManager)ev.getSource();
            textArea.append("Entrada:"+ cli.getClient().getIp()+":> "+cli.getMessage()+"\n");
            //System.out.println("Mensage del Cliente:: "+ cli.getClient().getInetAddress()+"-> "+cli.getMessage());
        }
        
        @Override
        public void onLostConnection(ClientListObserverEvent ev) {
            ClientManager cli = (ClientManager)ev.getSource();
            clientManagerList.remove(cli);
            cli.deterner();
            System.out.println("Conexion perdida de un Cliente:: "+ cli.getClient().getIp()+ " [Quitado]");
        }
    
    public void iniciarServicio(){
        if (server==null)
            return;
        connectionManager= new ConnectionManager(server);
        connectionManager.addListenerEvent(new ConnectionManagertListener(){
            @Override
            public void onConnetClient(ConnectionManagerEvent ev) {
                connetClient_Action(ev);
            }
        });
        connectionManager.Iniciar();
        
        clientListObserver = new ClientListObserver(clientManagerList);
        clientListObserver.addListenerEvent(this);
        clientListObserver.iniciar();
    }
    
    public void detenerServicio() {
        connectionManager.Detener();
        try {
            server.close();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void EnviarMenasaje(String mensaje){
        ListIterator li = clientManagerList.listIterator();
        while (li.hasNext()) {
            try {
                ClientManager cli = (ClientManager) li.next();
                DataOutputStream out = new DataOutputStream (cli.getSocket().getOutputStream());
                MessageSend messageSend = new MessageSend(out, mensaje);
                messageSend.start();
            } catch (IOException ex) {
                Logger.getLogger(ServidorSocket.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

}
