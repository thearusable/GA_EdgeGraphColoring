/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Main;

import EGC.EdgeGraphColoringProblem;
import ec.Evolve;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author arus2
 */
public class MainClass {
     /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
        
        String ParamsFile = "EGC.params";

        //Delete last line in file
        try (RandomAccessFile f = new RandomAccessFile(ParamsFile, "rw")) {
            long length = f.length() - 1;
            byte b;
            do {
                length -= 1;
                f.seek(length);
                b = f.readByte();
            } while(b != 10 && length>0);
            f.setLength(length+1);
        }
        
        String ToAdd = "pop.subpop.0.species.genome-size    = ";
        
        ToAdd += EdgeGraphColoringProblem.GetEdgesNumber();
        
        System.out.println("ToAdd: " + ToAdd);
        
        //Adding to file
        try {
            FileWriter fw = new FileWriter(ParamsFile,true); //the true will append the new data
            fw.write(ToAdd + "\n");//appends the string to the file
            fw.close();
        }catch(IOException ioe)
        {
            System.err.println("IOException: " + ioe.getMessage());
        }

        //Run Algorithm
        String[] Params = {"-file",ParamsFile}; 
        Evolve.main(Params);
        
    }
}
