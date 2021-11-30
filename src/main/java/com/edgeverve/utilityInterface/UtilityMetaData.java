package com.edgeverve.utilityInterface;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.camel.util.json.JsonObject;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.json.*;

@Component(value = "UtilityMetaData")
public class UtilityMetaData {
    private static Logger logger = LogManager.getLogger();

    UtilityPluginInterface pluginBase;
    ApplicationContext appContext;
    String whichUtility;
    String camelRoute;

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
    }

    public String route(String body, @Header(Exchange.SLIP_ENDPOINT) String previousRoute){
        if (previousRoute == null) {
            return this.camelRoute;
        }
        else{
            return null;
        }
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
