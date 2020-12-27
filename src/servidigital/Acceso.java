/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidigital;

/**
 *
 * @author Jose
 */
public class Acceso {
    private int Opcion;
    private boolean Permitido;

    public int getOpcion() {
        return Opcion;
    }

    public void setOpcion(int Opcion) {
        this.Opcion = Opcion;
    }

    public boolean isPermitido() {
        return Permitido;
    }

    public void setPermitido(boolean Permitido) {
        this.Permitido = Permitido;
    }
    
    public Acceso() {
        
    }
}
