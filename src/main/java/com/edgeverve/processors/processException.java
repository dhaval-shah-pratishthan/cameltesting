package com.edgeverve.processors;

import com.edgeverve.excp.CustomException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class processException implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        throw new CustomException("This is some message for exception", "E001");
    }
}
