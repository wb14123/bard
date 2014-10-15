package org.binwang.bard.basic.injector;

import com.github.drapostolos.typeparser.TypeParser;
import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.Injector;
import org.binwang.bard.core.marker.Before;

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
