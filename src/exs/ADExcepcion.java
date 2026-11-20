/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exs;

/**
 *
 * @author laboratorio_computo
 */
public class ADExcepcion extends RuntimeException{
    public ADExcepcion(String message) {
        super(message);
    }

    public ADExcepcion(String message, Throwable cause) {
        super(message, cause);
    }
}
