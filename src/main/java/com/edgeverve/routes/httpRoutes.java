package com.edgeverve.routes;

import com.edgeverve.processors.exceptionHandler;
import com.edgeverve.processors.processException;
import com.edgeverve.utilityInterface.UtilityMetaData;
import org.apache.camel.BeanScope;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import static org.apache.camel.builder.Builder.bean;

@Component
public class httpRoutes extends RouteBuilder {
    private static Logger logger = LogManager.getLogger();

    @Autowired
    private ApplicationContext appContext;

    @Override
    public void configure() throws Exception {
        restConfiguration().component("servlet");

//        UtilityMetaData metaData = new UtilityMetaData(appContext);

        rest("/api/customers")
                .post()
                .route()
                .to("direct:callTestRoutePath");

        rest("/testhttp")
                .post()
                .route()
                .to("direct:callTestFBCUtility")
                .end();

        rest("/testBean")
            .post()
            .route()
            .convertBodyTo(String.class)
            .bean(UtilityMetaData.class, "setPluginBase", BeanScope.Request)
            .bean(UtilityMetaData.class, "getPluginBase", BeanScope.Request)
                .bean(UtilityMetaData.class, "prepareRequest ", BeanScope.Request)
            .toD("${exchangeProperty.camelRoute}")
//            .dynamicRouter(bean(UtilityMetaData.class, "route"))
            .end();

        from("direct:callTestRoutePath").routeId("callingCorrespondingUtility")
                .doTry()
                    .process(new processException())
                .doCatch(Exception.class)
                    .process(new exceptionHandler())
                .end();
    }
}
