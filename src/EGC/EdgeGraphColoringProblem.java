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
    
 //Moja metoda
    public void loadData() {
        File dane = new File(GetDataFilePath());
        
        BufferedReader reader = null;
        EdgesData = new HashMap<>(); 
        PointsData = new HashMap<>();
      
        try {
            reader = new BufferedReader(new FileReader(dane));
            String text = null;
            while ((text = reader.readLine()) != null) {
                //text = text.replaceAll("\\s+","");
                String[] dataArray = text.split(" ");
                System.out.println(Arrays.toString(dataArray));
                if(dataArray.length == 1)
                    System.out.println("Liczba krawędzi: " + text);
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
        //algorytm zawsze wczytuje wszystkie linie, ignoruje liczbe krawedzi u gory
        loadData();
       
       
        EDGES_NUMBER = EdgesData.size();
        
        //oblicznie indexu chromatycznego
        ArrayList<Integer> temp = new ArrayList<>();
        
        for(int i = 0; i < EDGES_NUMBER; i++){
            Pair<Integer,Integer> pair = EdgesData.get(i);
            temp.add(pair.getKey());
            temp.add(pair.getValue());
        }

        Collections.sort(temp);
         
        int max = 0;
        int previous = temp.get(0);
        for(int i = 0; i< temp.size(); i++){
            if(temp.get(i) == previous) max += 1;
            else{
                previous = temp.get(i);
                if(max > INDEX) INDEX = max;
                max = 0;
            }
        }
        
        System.out.println("Liczba Krawędzi: " + EDGES_NUMBER);
        System.out.println("Index Chromatyczny: " + INDEX);
        
    }
    
        //Check is it ideal
    private boolean isIdeal(IntegerVectorIndividual vector){
        /*
        PointsData.put(0, new ArrayList());
        PointsData.get(0).add(0);
        PointsData.get(0).add(1);
        PointsData.get(0).add(2);
        */
        
        //sprawdzenie czy genome jest odpowiedniej długości
        if(vector.genome.length != EDGES_NUMBER) return false;
        
        //sprawdzanie czy sie kolory nie powtarzaja przy danym wiercholku - do poprawy
        List<Integer> temp = new ArrayList<>();
        temp.clear();
        
        List<Integer> colors = new ArrayList<>();
        
        for(int i=0; i< PointsData.size(); i++){
            colors.clear();
            temp.clear();
            
            temp = PointsData.get(i);
            for(int x=0; x < temp.size(); x++){
                colors.add(vector.genome[temp.get(x)]);
            }
            
            Set<Integer> unique_colors = new HashSet<>(colors);
            
            //System.out.println("colors: " + colors.size() + "  uniq: " + unique_colors.size());
            
            if(unique_colors.size() != colors.size()){
                return false;
            }
        }
        
        //sprawdzanie ilosci wykorzystanych kolorów - musi być <= INDEX
        // NIBY DZIALA
        List<Integer> colors2 = new ArrayList<>();
        
        for(int i = 0; i < vector.genome.length; i++){
            colors2.add(vector.genome[i]);
        }
        
        Set<Integer> all_colors = new HashSet<>(colors2);
        
        System.out.println("isIdeal: " + (all_colors.size() <= INDEX));
        
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
        
        /*
        
        for (int edge = 0; edge < vector.size(); edge++) {
            int color = vector.genome[edge];
            int vertex_start = EDGES_START_POINTS.get(edge);
            int vertex_end = EDGES_END_POINTS.get(edge);
            int color_start = vector.genome[vertex_start];
            int color_end = vector.genome[vertex_end];
            
            for(int s = 0; s < EDGES_START_POINTS.size(); s++){
                if(EDGES_START_POINTS.get(s) == vertex_start){
                    if(color_start != color){
                        fitnessValue += 1;
                    }else fitnessValue -= 1;
                }
            }

            for(int s = 0; s < EDGES_END_POINTS.size(); s++){
                if(EDGES_END_POINTS.get(s) == vertex_end){
                    if(color_end != color){
                        fitnessValue += 1;
                    }else fitnessValue -= 1;
                }
            }
        }
        
        //kara za ilosc kolorow 
        HashSet<Integer> colors = new HashSet<>();
        for(int i = 0; i < vector.size(); i++){
            colors.add(vector.genome[i]);
        }
        if(colors.size() > INDEX){
            fitnessValue -= colors.size() - 2;
        }
        */
        //Check is it ideal
        boolean isIdeal = isIdeal(vector);
        
        SimpleFitness fitness  = (SimpleFitness) vector.fitness;
        fitness.setFitness(evolutionState, fitnessValue, isIdeal);

        vector.evaluated = true;

    }
    

    
}
