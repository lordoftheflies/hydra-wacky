package com.ge.current.hydra.websocket;

import org.junit.Test;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

//@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PredixWebsocketServerApplication.class)
@WebAppConfiguration
@IntegrationTest
@ActiveProfiles(profiles = {
    "local"
})
public class PredixWebsocketServerApplicationTests {

//    private ServerContainer serverContainer;
////
//    private ServletContext servletContext;
////
//    private AnnotationConfigWebApplicationContext webAppContext;
////
//    private ServerEndpointExporter exporter;
////
//
//    @Before
//    public void setup() {
//        this.serverContainer = mock(ServerContainer.class);
//        this.servletContext = new MockServletContext();
//        this.servletContext.setAttribute("javax.websocket.server.ServerContainer", this.serverContainer);
//        this.webAppContext = new AnnotationConfigWebApplicationContext();
//        this.webAppContext.register(WebsocketServerConfig.class);
//        this.webAppContext.setServletContext(this.servletContext);
//        this.webAppContext.refresh();
//    }

    @Test
    public void addAnnotatedEndpointClasses() throws Exception {

//        verify(this.serverContainer).addEndpoint(DataStreamServerEndPoint.class);
    }

}
