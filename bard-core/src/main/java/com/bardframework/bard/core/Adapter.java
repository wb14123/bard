package com.bardframework.bard.core;

import com.bardframework.bard.core.marker.After;
import com.bardframework.bard.core.marker.Match;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <p>
 * An adapter is a mechanism to tell the framework whether the current handler method should handle the HTTP request.
 * This class is a foundation that could let you implement an adapter with an annotation.
 * </p>
 * <p>
 * You can define match functions with annotation {@link com.bardframework.bard.core.marker.Match} on it.
 * These functions must return a boolean value.
 * </p>
 * <p>
 * You can also define cleanup functions with annotation {@link com.bardframework.bard.core.marker.After}.
 * </p>
 *
 * @param <AnnotationType> Which annotation is this adapter bind to. It should be the same class you define in @BindTo .
 * @see BindTo
 */
public abstract class Adapter<AnnotationType extends Annotation>
    extends GenericHandler<AnnotationType> {

    /**
     * Run all the methods with annotation {@link com.bardframework.bard.core.marker.Match}, see if all the methods return true.
     *
     * @return True if all the methods with {@link com.bardframework.bard.core.marker.Match} return true. Otherwise return false.
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public boolean match()
        throws InvocationTargetException, NoSuchMethodException, InstantiationException,
        IllegalAccessException {
        Method[] methods = this.getClass().getDeclaredMethods();
        for (Method method : methods) {
            Object result = runMethod(method, Match.class);
            // if result is match method and result is not true
            if (result != NoAdapter.NO_ADAPTER && !((Boolean) result)) {
                return false;
            }
        }
        return true;
    }

    public void after()
        throws NoSuchMethodException, InstantiationException, IllegalAccessException,
        InvocationTargetException {
        runMethods(After.class);
    }

    public abstract void generateDoc();
}
