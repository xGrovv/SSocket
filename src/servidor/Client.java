/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author rudy
 */
public class Client {
    
    private Socket socket=null;
    private String ip;
    private InetAddress inetAddress;
    
    public Client(Socket socket){
        this.socket= socket;
        inetAddress = socket.getInetAddress();
        ip = inetAddress.toString();
    }

    public Socket getSocket() {
        return socket;
    }
    
    public String getIp() {
        return ip;
    }
            
    public InetAddress getInetAddress(){
        return inetAddress;
    }
    
}
