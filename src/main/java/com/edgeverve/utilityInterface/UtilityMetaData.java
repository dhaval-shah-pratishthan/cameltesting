package com.edgeverve.utilityInterface;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangeProperties;
import org.apache.camel.Header;
import org.apache.camel.util.json.JsonObject;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.json.*;

import java.util.Map;

@Component(value = "UtilityMetaData")
public class UtilityMetaData {
    private static Logger logger = LogManager.getLogger();

    @Autowired
    private ApplicationContext appContext;

    UtilityPluginInterface pluginBase;
    String whichUtility;
    String camelRoute;

//    ProcessExchange defaultProcessExchange
//    String request = defaultProcessExchange.getRequest()
//    RequestUrl = request.RequestUrl
//    requestMap.get("MethodType")
//    requestMap.get("RequestBody")
//    requestMap.get("EntityId")
//    requestMap.get("DocumentDownloadPath")
//    requestMap.get("file_path"); ==> uploadFilePath
//    ParseResponse() exchange.setProperty("FBC_Response",fbcResponse)

    public ApplicationContext getAppContext() {
        return appContext;
    }

    public void setAppContext(ApplicationContext appContext) {
        this.appContext = appContext;
    }

    public UtilityPluginInterface getPluginBase() {
        return pluginBase;
    }

    public void setPluginBase1(Exchange exchange){
        logger.log(Level.INFO, "inside setPluginBase "+  exchange.getIn().getBody());
    }

    public void setPluginBase(Exchange exchange) {
        String bodyStr = (String) exchange.getIn().getBody();
        JSONObject bodyJson = new JSONObject(bodyStr);
        this.whichUtility = bodyJson.getString("whichUtility");
        this.pluginBase = (UtilityPluginInterface) appContext.getBean(this.whichUtility);
        this.camelRoute = "direct:callTest" + this.whichUtility;
        exchange.setProperty("camelRoute", this.camelRoute);
    }

    public void getPluginBase(Exchange exchange){
        logger.info("The value of obj is "+this.whichUtility);
    }
    public void SegregateRequiredDataToCallUtility(){

    }

    public String getCamelRoute() {
        return camelRoute;
    }

    public void setCamelRoute(String camelRoute) {
        this.camelRoute = camelRoute;
    }
}
