/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 *
 * @author Grover
 */
class ClientSocket {
    
    private Socket socketCli;
    
    private DataOutputStream salida_datos;
    private DataInputStream entrada_datos;
    
}
