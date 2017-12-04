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
import eventos.ServidorSocketEvent;
import eventos.ServidorSocketListener;
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
    private ArrayList listeners;
    private boolean deteniendo;
    
    private ArrayList<ClientManager> clientManagerList;
    private int port;
    
    public ServidorSocket(int port){
        this.port=port;
        clientManagerList = new ArrayList<>();
        listeners = new ArrayList();
        deteniendo=false;
        try{
            server= new ServerSocket(this.port);
        } catch (IOException e) {
            System.out.println("ServidorSocket.Constructor:> No se pudo abrir el servidor socket:: "+e.getMessage());
        }
    }
    
    public void setTextArea(JTextArea textArea){
        this.textArea=textArea;
    }
    
    public void addListenerEvent(ServidorSocketListener servidorSocketListener){
        listeners.add(servidorSocketListener);
    }
    
    private void connetClient_Action(ConnectionManagerEvent ev) {
        ConnectionManager con = (ConnectionManager)ev.getSource();
        Client cliente = con.getCliente();
        ClientManager clientManager= new ClientManager(cliente);
        clientManager.addListenerEvent(this);

        synchronized(this){clientManagerList.add(clientManager);}
        //clientManagerList.add(clientManager);
        System.out.println("Nuevo Cliente Registrado:: "+ cliente.getInetAddress().toString());
        clientManager.iniciar();

        ListIterator li = listeners.listIterator();
        while (li.hasNext()) {
            ServidorSocketListener listener = (ServidorSocketListener) li.next();
            ServidorSocketEvent evObj = new ServidorSocketEvent(clientManager);
            (listener).onNewConnection(evObj);
        }
    }
    
        @Override
        public void onLostConnection(ClientListObserverEvent ev) {
            deteniendo=true;
            ClientManager cli = (ClientManager)ev.getSource();
            cli.deterner();
            synchronized(this){clientManagerList.remove(cli);}
            System.out.println("LostConnection...aa");
        }

        @Override
        public void onDisconnectClient(ClientManagerEvent ev) {
            if(!deteniendo){
                ClientManager cli = (ClientManager)ev.getSource();
                cli.deterner();
                synchronized(this){clientManagerList.remove(cli);}
                System.out.println("Disconneted...gg");
            }
            deteniendo=false;
        }
        
        @Override
        public void onReceiveMessage(ClientManagerEvent ev){
            ClientManager cli = (ClientManager)ev.getSource();
            ListIterator li = listeners.listIterator();
            while (li.hasNext()) {
                ServidorSocketListener listener = (ServidorSocketListener) li.next();
                ServidorSocketEvent evObj = new ServidorSocketEvent(cli);
                (listener).onMessageReceive(evObj);
            }
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
            clientListObserver.detener();
            for (Object obj : clientManagerList){
                    ClientManager cli = (ClientManager)obj;                                
                    cli.deterner();
            }        
            clientManagerList.clear();
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
    
    public void EnviarMenasaje(Client client, String mensaje){
        //ListIterator li = clientManagerList.listIterator();
        try {
            DataOutputStream out = new DataOutputStream (client.getSocket().getOutputStream());
            MessageSend messageSend = new MessageSend(out, mensaje);
            messageSend.start(); 
            System.out.println("servidor.ServidorSocket.EnviarMenasaje()");
        } catch (IOException ex) {
            Logger.getLogger(ServidorSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
