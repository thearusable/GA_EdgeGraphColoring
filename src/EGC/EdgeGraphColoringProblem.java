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
import java.util.HashSet;

public class EdgeGraphColoringProblem extends Problem implements SimpleProblemForm {
    
    public int EDGES_NUMBER;
    public ArrayList<Integer> EDGES_START_POINTS;
    public ArrayList<Integer> EDGES_END_POINTS;
 
    @Override
    public void setup(EvolutionState state, Parameter base) {
        super.setup(state, base); //To change body of generated methods, choose Tools | Templates.
        
        EDGES_NUMBER = 5;
        EDGES_START_POINTS = new ArrayList<>();
        EDGES_END_POINTS = new ArrayList<>();
        //1
        EDGES_START_POINTS.add(0);
        EDGES_END_POINTS.add(1);
        //2
        EDGES_START_POINTS.add(0);
        EDGES_END_POINTS.add(2);
        //3
        EDGES_START_POINTS.add(0);
        EDGES_END_POINTS.add(3);
        //4
        EDGES_START_POINTS.add(1);
        EDGES_END_POINTS.add(3);
        //5
        EDGES_START_POINTS.add(3);
        EDGES_END_POINTS.add(2);
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
        if(colors.size() > 2){
            fitnessValue -= colors.size() - 2;
        }
        
        //Check is it ideal
        boolean isIdeal = isIdeal();
        
        SimpleFitness fitness  = (SimpleFitness) vector.fitness;
        fitness.setFitness(evolutionState, fitnessValue, isIdeal);

        vector.evaluated = true;

    }
    
    //Check is it ideal
    private boolean isIdeal(){
        
        
        return false;
    }
    
}
