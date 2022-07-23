package org.example;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class propertiesTest {

    // junit.jupiter.testinstance.lifecycle.default=per_class
    int value = 1;

    @Test
    public void instance_test(){
        System.out.println("value = " + value++);
    }

    @Test
    public void instance_test2(){
        System.out.println("value = " + value++);
    }
}
