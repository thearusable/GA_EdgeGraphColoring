/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import ec.Evolve;

/**
 *
 * @author arus2
 */
public class MainClass {
     /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        // TODO code application logic here
        
        String ParamsFile;
        
        ParamsFile = "src\\EGC\\EGC.params";
        
        //ParamsFile = new String[] {"-file","src\\Chars\\chars.params"};
        //ParamsFile = new String[] {"-file","src\\ecj\\ec\\app\\tutorial1\\tutorial1.params"};
        //ParamsFile = new String[] {"-file","src\\ecj\\ec\\app\\tutorial2\\tutorial2.params"};
        //ParamsFile = "src\\ecj\\ec\\app\\tutorial3\\tutorial3.params";
        //ParamsFile = "src\\ecj\\ec\\app\\tutorial4\\tutorial4.params";
        //broken ?  ParamsFile = new String[] {"-file","src\\ecj\\ec\\app\\ordertree\\ordertree.params"};
        //ParamsFile = "src\\ecj\\ec\\app\\gui\\coevolve2.params";
        //ParamsFile = new String[] {"-file","src\\ecj\\ec\\app\\mona\\mona.params"};
        //stuck?    
        //ParamsFile = "src\\ecj\\ec\\app\\royaltree\\royaltree.params";
        ///ParamsFile = new String[] {"-file","src\\ecj\\ec\\gp\\push\\push.params"};
        
        
        //file = new String[] {"-file","test\\ec\\app\\tutorial4\\tutorial4.params"};
        
        String[] Params = {"-file",ParamsFile}; 
        Evolve.main(Params);
        
    }
}
