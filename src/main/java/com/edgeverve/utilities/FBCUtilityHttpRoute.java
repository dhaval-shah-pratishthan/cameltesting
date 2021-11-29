package com.edgeverve.utilities;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http.HttpComponent;
import org.apache.camel.support.jsse.KeyManagersParameters;
import org.apache.camel.support.jsse.KeyStoreParameters;
import org.apache.camel.support.jsse.SSLContextParameters;
import org.apache.camel.support.jsse.TrustManagersParameters;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@ConfigurationProperties
@Component
public class FBCUtilityHttpRoute extends RouteBuilder {
    private HashMap<String, Object> http;
    private static Logger logger = LogManager.getLogger();

    @Override
    public void configure() throws Exception {

        Endpoint FBCHttp = setupSSLConext(getContext());
        from("direct:callTestFBCUtility").routeId("callTestFBCUtility")
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("Accept", constant("application/json"))
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .removeHeader(Exchange.HTTP_PATH)
                .removeHeader(Exchange.HTTP_URI)
                .to(FBCHttp)
                .end();
    }

    public void setHttp(HashMap<String, Object> http) {
        this.http = http;
    }

    public HashMap<String, Object> getHttp() {
        return http;
    }

    private Endpoint setupSSLConext(CamelContext camelContext) throws Exception {

        HttpComponent httpComponent = null;
        try {

        boolean sslEnabled = (boolean) Boolean.parseBoolean((String) http.get("ssl_enabled"));
        HashMap<String, Object> sslData = (HashMap<String, Object>) http.get("ssl_data");
        logger.log(Level.INFO, sslData.get("key_store"));

        KeyStoreParameters keyStoreParameters = new KeyStoreParameters();
        // Change this path to point to your truststore/keystore as jks files
        keyStoreParameters.setResource((String) sslData.get("key_store"));
        keyStoreParameters.setPassword((String) sslData.get("key_store_password"));

        KeyManagersParameters keyManagersParameters = new KeyManagersParameters();
        keyManagersParameters.setKeyStore(keyStoreParameters);
        keyManagersParameters.setKeyPassword((String) sslData.get("key_store_password"));

        KeyStoreParameters trustKeyStoreParameters = new KeyStoreParameters();
        // Change this path to point to your truststore/keystore as jks files
        trustKeyStoreParameters.setResource((String) sslData.get("trust_store"));
        trustKeyStoreParameters.setPassword((String) sslData.get("trust_store_password"));


        TrustManagersParameters trustManagersParameters = new TrustManagersParameters();
        trustManagersParameters.setKeyStore(trustKeyStoreParameters);

        SSLContextParameters sslContextParameters = new SSLContextParameters();
        sslContextParameters.setKeyManagers(keyManagersParameters);
        sslContextParameters.setTrustManagers(trustManagersParameters);


        httpComponent = camelContext.getComponent("https", HttpComponent.class);
        httpComponent.setSslContextParameters(sslContextParameters);
//        httpComponent.setClientConnectionManager((HttpClientConnectionManager) new ThreadSafeClientConnManager());
            //This is important to make your cert skip CN/Hostname checks

            return httpComponent.createEndpoint("https://secnodeserver.com:8000");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return httpComponent.createEndpoint("https4://secnodeserver.com:8000");
    }
}
