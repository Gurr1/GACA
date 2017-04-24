package domainModelTests;

import hills.Gurra.RandomWalker;
import hills.engine.system.domainModel.Sheep;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * Created by gustav on 2017-04-24.
 */
public class RandomMoverTest {

    @Test
    public void testSpan(){
        RandomWalker randomWalker = new RandomWalker();
        for(int i = 0; i<1000000; i++){
            double result = randomWalker.generate(i);
            TestCase.assertTrue(result>=-1 && result <=1);
        }
    }
    @Test
    public void testProximity(){
        RandomWalker randomWalker = new RandomWalker();
        for(int i = 0 ; i<10000; i++){
            TestCase.assertEquals(randomWalker.generate(i), randomWalker.generate(i), 0.02);
        }
    }

    @Test
    public void testIsRandom(){
        RandomWalker randomWalker = new RandomWalker();
        boolean wasBigger = false;
        boolean wasSmaller = false;
        for(int i = 0 ; i<10000; i++){
            double result = randomWalker.generate(i);
            double lastResult = randomWalker.generate(i-1);
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
