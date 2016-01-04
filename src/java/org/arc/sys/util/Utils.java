/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.util;

/**
 *
 * @author angel
 */
public class Utils {

    public static double roundTwoDecinal(double cant){
        double cant1 = (double) Math.round(cant * 100) / 100;
        
        return cant1;
    }
}
