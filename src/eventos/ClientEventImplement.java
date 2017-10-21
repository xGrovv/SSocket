/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eventos;

/**
 *
 * @author Grover
 */
public class ClientEventImplement implements ClientEventListener{

    @Override
    public void onConnetClient(ClientEvent ev) {
        System.out.print("nuevo cliente aceptado");
    }

    @Override
    public void onDisconnectClient(ClientEvent ev) {
        System.out.print("Un cliente se desconecto");
    }
    
}
