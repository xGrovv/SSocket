/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;

/**
 *
 * @author rudy
 */
public class Client {
    
    private Socket socket=null;
    String ip;
    Date dateConnection;
    InetAddress inetAddress;
    
    public Client(Socket socket){
        this.socket= socket;
        inetAddress = socket.getInetAddress();
        ip = inetAddress.toString();
        dateConnection = new Date (System.currentTimeMillis());
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
