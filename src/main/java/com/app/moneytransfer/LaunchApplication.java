package com.app.moneytransfer;

import com.app.moneytransfer.exception.MoneyTransferExceptionHandler;
import com.app.moneytransfer.model.Account;
import com.app.moneytransfer.service.AccountService;
import com.app.moneytransfer.service.FundsTransferService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

import java.math.BigDecimal;

/**
 * @author Aruna
 * This is the main Class which Starts the container and Initialize AccountService,TransactionService
 */
public class LaunchApplication {
	
	static final int SERVER_PORT = 8083;
	static final String INIT_PARAM = "jersey.config.server.provider.classnames";
	static final String PATH = "/*";
	static final String CONTEXT_PATH ="/";


	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		startService();
	}

	/**
	 * 
	 * @throws Exception
	 */
	private static void startService() throws Exception {
		Server server = new Server(SERVER_PORT);
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath(CONTEXT_PATH);
		server.setHandler(context);
		ServletHolder servletHolder = context.addServlet(ServletContainer.class, PATH);
		servletHolder.setInitParameter(INIT_PARAM,					
						AccountService.class.getCanonicalName() + ","
						+  FundsTransferService.class.getCanonicalName()+","
						+  MoneyTransferExceptionHandler.class.getCanonicalName());
		try {
			server.start();
			long fromAccountId = createAccount("FromAccountName",BigDecimal.valueOf(100));
			long toAccountId = createAccount("ToAccountName",BigDecimal.valueOf(100));
			System.out.println(" fromAccountId "+fromAccountId + " toAccountId "+toAccountId );
			server.join();
		} finally {
			server.destroy();
		}
	}


	private static long createAccount(String userName, BigDecimal amount){
		AccountService service = new AccountService();
		Account account = new Account(userName,amount,"USD");
		Account createdAcc = service.createAccount(account);
		return createdAcc.getAccountId();
	}

}
