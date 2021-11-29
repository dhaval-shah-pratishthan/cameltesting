package com.edgeverve.routes;

import com.edgeverve.processors.exceptionHandler;
import com.edgeverve.processors.processException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class httpRoutes extends RouteBuilder {
    private static Logger logger = LogManager.getLogger();
    @Override
    public void configure() throws Exception {
        restConfiguration().component("servlet");


        rest("/api/customers")
                .post()
                .route()
                .to("direct:callTestRoutePath");

        rest("/testhttp")
                .post()
                .route()
                .to("direct:callTestFBCUtility")
                .end();

        from("direct:callTestRoutePath").routeId("callingCorrespondingUtility")
                .doTry()
                    .process(new processException())
                .doCatch(Exception.class)
                    .process(new exceptionHandler())
                .end();
    }
}
