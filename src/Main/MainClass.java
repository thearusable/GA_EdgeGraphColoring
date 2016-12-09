
package Main;

import EGC.EdgeGraphColoringProblem;
import ec.Evolve;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;


public class MainClass {
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        //sciezka do pliku konfiguracyjnego
        String ParamsFile = "EGC.params";

        //Usuniecie ostatniej linii z pliku w ktorej jest dlugosc genomu z poprzedniego uzycia programu
        //(na wypadek zmiany pliku z danymi)
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
        
        //wpis do pliku konfiguracyjnego odnoszacy sie do dlugosci genomu
        String ToAdd = "pop.subpop.0.species.genome-size    = " + EdgeGraphColoringProblem.GetEdgesNumber();
        
        //Dodanie wpisu do pliku konfiguracyjnego
        try {
            FileWriter fw = new FileWriter(ParamsFile,true); //the true will append the new data
            fw.write(ToAdd + "\n");//appends the string to the file
            fw.close();
        }catch(IOException ioe)
        {
            System.err.println("IOException: " + ioe.getMessage());
        }
        
        
        long time = 0;
        
        for (int i = 0; i < 50; i++){
            long start = System.nanoTime();
            //Uruchomienie algorytmu
            String[] Params = {"-file",ParamsFile}; 
            Evolve.main(Params);
            
            long end = System.nanoTime();
            time += end - start;
        }

        time /= 50;

        System.out.println(
                String.format("%dm %ds %dms", 
                        TimeUnit.NANOSECONDS.toMinutes(time),
                        TimeUnit.NANOSECONDS.toSeconds(time) - TimeUnit.NANOSECONDS.toMinutes(time) * 60,
                        TimeUnit.NANOSECONDS.toMillis(time) - TimeUnit.NANOSECONDS.toSeconds(time) * 1000));
    }
}
