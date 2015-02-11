package com.bardframework.bard.util.spring;

import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.Filter;
import com.bardframework.bard.core.marker.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@BindTo(FilterBeanTester.class)
@Component
public class FilterBeanTesterDef extends Filter<FilterBeanTester> {

    private Embed embed = null;

    @Autowired
    public FilterBeanTesterDef(Embed embed) {
        super();
        this.embed = embed;
    }

    @Before
    public void addHeader() {
        context.getResponse().addHeader("filter-embed", embed.getMessage());
    }

    @Override public void generateDoc() {
    }
}
