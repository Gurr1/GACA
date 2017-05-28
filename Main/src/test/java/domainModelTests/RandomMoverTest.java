package domainModelTests;

import hills.services.generation.GenerationMediator;
import hills.services.generation.IGenerationMediator;
import hills.services.generation.RandomWalker;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by gustav on 2017-04-24.
 */
public class RandomMoverTest {
    IGenerationMediator generation;
    @Before
    public void init(){
        generation = new GenerationMediator();
    }
    @Test
    public void testSpan(){
        for(int i = 0; i<1000000; i++){
            double result = generation.generateDirection(i);
            TestCase.assertTrue(result>=-1 && result <=1);
        }
    }
    @Test
    public void testProximity(){
        RandomWalker randomWalker = new RandomWalker();
        for(int i = 0 ; i<10000; i++){
            TestCase.assertEquals(generation.generateDirection(i), generation.generateDirection(i), 0.02);
        }
    }

    @Test
    public void testIsRandom(){
        RandomWalker randomWalker = new RandomWalker();
        boolean wasBigger = false;
        boolean wasSmaller = false;
        for(int i = 0 ; i<10000; i++){
            double result = generation.generateDirection(i);
            double lastResult = generation.generateDirection(i-1);
            if(isBigger(result, lastResult)){
                wasBigger = true;
            }
            if(isSmaller(result, lastResult)){
                wasSmaller = true;
            }
        }
        TestCase.assertTrue(wasBigger && wasSmaller);
    }

    private boolean isBigger(double result, double lastResult){
        return result>lastResult;
    }
    private boolean isSmaller(double result, double lastResult){
        return result<lastResult;
    }
}
