/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EGC;

/**
 *
 * @author carlos
 */
import ec.EvolutionState;
import ec.Individual;
import ec.Problem;
import ec.simple.SimpleFitness;
import ec.simple.SimpleProblemForm;
import ec.util.Parameter;
import ec.vector.IntegerVectorIndividual;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.util.Pair;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EdgeGraphColoringProblem extends Problem implements SimpleProblemForm {
    
    public int EDGES_NUMBER;
    public int INDEX;
    
    //EdgesData index = edge number;
    //Pair = connected points 
    Map<Integer, Pair<Integer,Integer>> EdgesData;
    
    //PoinstData index = point 
    //list - connected edges to point
    Map<Integer, List<Integer>> PointsData;
    
    public final static String GetDataFilePath()
    {
        String DataFile = "Data.txt";
        /*
        File f = new File(DataFile);
        if(!f.exists() || f.isDirectory()) { 
            DataFile = "src" + File.separator + "EGC" + File.separator + DataFile;
        }
        */
        return DataFile;
    }
    
    public final static String GetEdgesNumber() throws IOException{
        File dane = new File(GetDataFilePath());
        
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(dane));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EdgeGraphColoringProblem.class.getName()).log(Level.SEVERE, null, ex);
        }
        String text = null;
        String[] dataArray = null;
        
        while ((text = reader.readLine()) != null) {
                text = removeSpaces(text);
                if(checkLine(text) == true) {
                    dataArray = text.split(" ");
                    if(dataArray.length == 1) {
                        return text;
                    }
                }
        }
        
        System.err.println("Nie mozna odczytac ile jest krawedzi.");
        return "0";
    }
    

    public final static String removeSpaces(String text) {
        char firstChar = text.charAt(0);
        while(firstChar == ' ') {
            text = text.substring(1);
            firstChar = text.charAt(0);
        }
        return text;
    }
    
    public final static boolean checkLine(String textLine) {
        return textLine.charAt(0) != '#';         
   }

    public void loadData() {
        File dane = new File(GetDataFilePath());
        
        BufferedReader reader = null;
        EdgesData = new HashMap<>(); 
        PointsData = new HashMap<>();
      
        try {
            reader = new BufferedReader(new FileReader(dane));
            String text = null;
            String[] dataArray = null;
            Integer lineToReadCount = null;
            Integer counter = 0;

            while ((text = reader.readLine()) != null) {
                //text = text.replaceAll("\\s+","");
                text = removeSpaces(text);
                if(checkLine(text) == true) {
                    dataArray = text.split(" ");
                    //System.out.println(Arrays.toString(dataArray));
                    if(dataArray.length == 1) {
                        //System.out.println("Liczba krawędzi: " + text);
                        lineToReadCount = Integer.parseInt(text);
                    }   
                    else {
                        Integer edgeNumber = Integer.parseInt(dataArray[0]);
                        Integer sourceNumber = Integer.parseInt(dataArray[1]);
                        Integer destinationNumber = Integer.parseInt(dataArray[2]);
                        //System.out.println("Numer kr: " + edgeNumber + " source: " + sourceNumber + " destination: " + destinationNumber);
                       // System.out.println("EdgesData.put(" + edgeNumber + ", new Pair(" + sourceNumber + "," + destinationNumber + "))");
                        EdgesData.put(edgeNumber, new Pair(sourceNumber, destinationNumber));
                       // System.out.println(PointsData.containsKey(0));
                        //System.out.println(PointsData.containsKey(5));
                        if(!PointsData.containsKey(sourceNumber)) {
                            //System.out.println("PointsData.put(" + sourceNumber + ", new ArrayList());");
                            PointsData.put(sourceNumber, new ArrayList());
                        }
                        if(!PointsData.containsKey(destinationNumber)) {
                            //System.out.println("PointsData.put(" + destinationNumber + ", new ArrayList());");
                            PointsData.put(destinationNumber, new ArrayList());
                        }
                        PointsData.get(sourceNumber).add(edgeNumber);
                        PointsData.get(destinationNumber).add(edgeNumber);
                        //System.out.println("PointsData.get(" + sourceNumber + ").add(" + edgeNumber + ")");
                        //System.out.println("PointsData.get(" + destinationNumber + ").add(" + edgeNumber + ")");
                        counter++;
                        System.out.println(Arrays.toString(dataArray));
                        
                        if(counter == lineToReadCount)
                            break;
                    }
                }
            }
        } 
        catch (FileNotFoundException e) {
                e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    //-------------------------------------
    
    @Override
    public void setup(EvolutionState state, Parameter base) {
        super.setup(state, base); //To change body of generated methods, choose Tools | Templates.
        
        //reading from file
        loadData();
       
        EDGES_NUMBER = EdgesData.size();
        
        //oblicznie indexu chromatycznego - powinno byc 6 a nie 5 
        ArrayList<Integer> temp = new ArrayList<>();
        
        for(int i = 0; i < EDGES_NUMBER; i++){
            Pair<Integer,Integer> pair = EdgesData.get(i);
            temp.add(pair.getKey());
            temp.add(pair.getValue());
        }

        Collections.sort(temp);
         
        int max = 1;
        int previous = temp.get(0);
        for(int i = 0; i< temp.size(); i++){
            if(temp.get(i) == previous) max += 1;
            else{
                previous = temp.get(i);
                if(max > INDEX) INDEX = max;
                max = 1;
            }
        }
        
        System.out.println("Liczba Krawędzi: " + EDGES_NUMBER);
        System.out.println("Index Chromatyczny: " + INDEX);
        
    }
    
        //Check is it ideal
    private boolean isIdeal(IntegerVectorIndividual vector){
        
        //sprawdzenie czy genome jest odpowiedniej długości
        if(vector.genome.length != EDGES_NUMBER) return false;
        
        //sprawdzanie czy sie kolory nie powtarzaja przy danym wiercholku
        List<Integer> temp = new ArrayList<>();        
        List<Integer> colors = new ArrayList<>();
        
        for(int i=0; i< PointsData.size(); i++){
            colors.clear();
            temp.clear();
            
            temp = PointsData.getOrDefault(i, colors);
            for(int x=0; x < temp.size(); x++){
                colors.add(vector.genome[temp.get(x)]);
            }
            
            Set<Integer> unique_colors = new HashSet<>(colors);
            if(unique_colors.size() != colors.size()){
                return false;
            }
        }
        
        //sprawdzanie ilosci wykorzystanych kolorów
        List<Integer> colors2 = new ArrayList<>();
        
        for(int i = 0; i < vector.genome.length; i++){
            colors2.add(vector.genome[i]);
        }
        
        Set<Integer> all_colors = new HashSet<>(colors2);
        return all_colors.size() <= INDEX;
    }
    
    @Override
    public void evaluate(final EvolutionState evolutionState, 
                                    final Individual individual, 
                                    final int subPopulation, 
                                    final int threadNum) {
        if (individual.evaluated)
            return;
        
        if (!(individual instanceof IntegerVectorIndividual))
            evolutionState.output.fatal("It's not a IntegerVectorIndividual!!!",null);

        int fitnessValue = 0;

        IntegerVectorIndividual vector = (IntegerVectorIndividual) individual;
        
        //Zliczenie ilosci kolorow
        List<Integer> all_colors = new ArrayList<>();

        for(int i = 0; i < vector.size(); i++){
            all_colors.add(vector.genome[i]);
        }
        
        HashSet<Integer> all_unique_colors = new HashSet<>(all_colors);
        
        if(all_unique_colors.size() > INDEX){
            fitnessValue -= all_unique_colors.size() - INDEX;
        }else{
            fitnessValue += 2;
        }
        
        //Ocena
        for (Map.Entry<Integer, List<Integer>> entry: PointsData.entrySet()){
            List<Integer> edges = entry.getValue();
            List<Integer> used_colors = new ArrayList<>();
            
            for(int i = 0; i < edges.size(); i++){
                used_colors.add(vector.genome[edges.get(i)]);
            }
            
            Set<Integer> unique_colors = new HashSet<>(used_colors);
            
            if(used_colors.size() == unique_colors.size()){
                fitnessValue += 1;
            }else {
                fitnessValue -= 1;
            }
        }
        
        //Check is it ideal
        boolean isIdeal = isIdeal(vector);
        
        SimpleFitness fitness  = (SimpleFitness) vector.fitness;
        fitness.setFitness(evolutionState, fitnessValue, isIdeal);

        vector.evaluated = true;

    }
}
