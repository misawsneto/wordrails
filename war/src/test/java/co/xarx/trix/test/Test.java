package co.xarx.trix.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by misael on 12/22/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:applicationContext-test.xml"
})
public class Test {

    @org.junit.Test
    public void doTest() {
        String strings[] = "asdfasdf".split(",");
    }
}
