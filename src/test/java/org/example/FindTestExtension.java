package org.example;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Method;

public class FindTestExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

    public FindTestExtension(Long THRESHOLD) {
        this.THRESHOLD = THRESHOLD;
    }

    private Long THRESHOLD;

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        Method requiredTestMethod = context.getRequiredTestMethod();
        SlowTest annotation = requiredTestMethod.getAnnotation(SlowTest.class);
        String testClassName = context.getRequiredTestClass().getName();
        String testMethodName = context.getRequiredTestMethod().getName();
        ExtensionContext.Store store = context.getStore(ExtensionContext.Namespace.create(testClassName, testMethodName));
        Long start_time = store.remove("START_TIME", Long.class);
        Long duration = System.currentTimeMillis() - start_time;
        if(duration > THRESHOLD && annotation == null){ // @SlowTest annotation이 없으면 권장 한다
            System.out.println("Please consider mark method " + testMethodName +" with @SlowTest");
        }
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        String testClassName = context.getRequiredTestClass().getName();
        String testMethodName = context.getRequiredTestMethod().getName();
        ExtensionContext.Store store = context.getStore(ExtensionContext.Namespace.create(testClassName, testMethodName));
        store.put("START_TIME", System.currentTimeMillis());

    }
}
