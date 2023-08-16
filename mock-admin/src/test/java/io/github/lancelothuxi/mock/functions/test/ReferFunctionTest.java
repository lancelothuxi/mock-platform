package io.github.lancelothuxi.mock.functions.test;

import io.github.lancelothuxi.mock.functions.CompoundVariable;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/8/7 上午10:56
 */
public class ReferFunctionTest {

    //test commit
    @Test
    public void testExecute() {

        String args="[[\"3343434343\",\"dfdfdfdfd\"]]";

        String data="${__refer($[0][0])}";

        CompoundVariable compoundVariable= new CompoundVariable();
        compoundVariable.setParameters(data);
        data=compoundVariable.execute(args);
        Assert.assertEquals(data,"3343434343");
    }
}