package org.binwang.bard.core;

import org.binwang.bard.core.marker.After;
import org.binwang.bard.core.marker.Match;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Adapter is used on Handler's methods.
 *
 * @param <AnnotationType> Which annotation is this adapter bind to.
 */
public abstract class Adapter<AnnotationType extends Annotation>
    extends GenericHandler<AnnotationType> {

    public boolean match()
        throws InvocationTargetException, NoSuchMethodException, InstantiationException,
        IllegalAccessException {
        Method[] methods = this.getClass().getMethods();
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
