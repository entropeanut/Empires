package server;


import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import terminal.Protocol;
import terminal.Terminal;
import terminal.TerminalServer;

public class EmpireServer implements TerminalServer.Listener {

	private static final Logger log = LoggerFactory.getLogger(EmpireServer.class);

    /** the port on which to listen for incoming connections */
    private static final int DEFAULT_PORT = 23;

    /**
     * Program entry point
     * @param args the command line arguments
     */
    public static void main(String[] args) throws JAXBException {
    	try {
	        TerminalServer server = new TerminalServer();
	        server.openPort(DEFAULT_PORT, Protocol.TELNET);
	        server.listen(new EmpireServer());
	        server.run();
    	} catch (Throwable t) {
    		log.error("Error in server: ", t);
    		throw t;
    	}
    }
    
    @SuppressWarnings("unused")
	@Override
    public void onConnect(Terminal terminal) {
    	new Controller(terminal);
    }

	@Override
	public void onDisconnect(Terminal terminal) {
		
	}
}
