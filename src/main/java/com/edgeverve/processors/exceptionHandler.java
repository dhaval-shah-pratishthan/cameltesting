package com.edgeverve.processors;

import com.edgeverve.excp.CustomException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class exceptionHandler implements Processor {
    private static Logger logger = LogManager.getLogger();
    @Override
    public void process(Exchange exchange) throws Exception {
        System.out.println("Here inside the handler");
        logger.info("In Class  :"+this.getClass().getSimpleName());
        Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
        if(cause instanceof CustomException){
            logger.info("The custom exception is of the type custom exception");
            exchange.getIn().setBody(cause.toString());
        }
        else if(exchange.getException() != null){
            logger.info("Its an exception but not just custom");
        }
        else{
            logger.info("No exception encountered");
        }
    }
}
