package org.binwang.bard;

import org.binwang.bard.core.Server;
import org.binwang.bard.util.Params;
import org.binwang.bard.util.ParamsFilter;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();

        server.bind(Params.class, ParamsFilter.class);
    }
}
