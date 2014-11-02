package com.bardframework.bard.basic.injector;

import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.Injector;
import com.bardframework.bard.core.marker.Before;
import com.github.drapostolos.typeparser.TypeParser;

import javax.ws.rs.DefaultValue;

@BindTo(DefaultValue.class)
public class DefaultValueInjector extends Injector<DefaultValue> {
    @Before public void setDefault() {
        if (injectorVariable == null) {
            TypeParser parser = TypeParser.newBuilder().build();
            injectorVariable = parser.parse(annotation.value(), injectorVariableType);
        }
    }

    @Override public void generateDoc() {
        docParameter.limitations.put("default", annotation.value());
    }
}
