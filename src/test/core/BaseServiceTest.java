package test.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.Assert;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import test.UnitTestConstants;

import com.paypal.core.BaseService;
import com.paypal.core.ConfigManager;
import com.paypal.exception.ClientActionRequiredException;
import com.paypal.exception.HttpErrorException;
import com.paypal.exception.InvalidCredentialException;
import com.paypal.exception.InvalidResponseDataException;
import com.paypal.exception.MissingCredentialException;
import com.paypal.exception.SSLConfigurationException;
import com.paypal.sdk.exceptions.OAuthException;

public class BaseServiceTest {
	BaseService service;
	String filePath = "src/config.properties";

	@BeforeClass
	public void beforeClass() {
		service = new BaseService("Invoice", "1.6.0");
	}

	@AfterClass
	public void afterClass() {
		service = null;
	}

	@Test
	public void getServiceNameTest() {
		Assert.assertEquals("Invoice", service.getServiceName());
	}

	@Test
	public void getVersionTest() {
		Assert.assertEquals("1.6.0", service.getVersion());
	}

	@Test(dataProvider = "configParams", dataProviderClass = DataProviderClass.class)
	public void callTest(ConfigManager conf) throws HttpErrorException,
			InvalidResponseDataException, ClientActionRequiredException,
			MissingCredentialException, SSLConfigurationException,
			InvalidCredentialException, FileNotFoundException,
			InterruptedException, IOException, OAuthException {

		String response = service.call("CreateInvoice",
				UnitTestConstants.REQUEST_STRING,
				UnitTestConstants.API_USER_NAME);
		Assert.assertNotNull(response);
		assert (response.contains("Success"));
		assert (response.contains("invoiceID"));
		assert (response.contains("invoiceNumber"));
	}

	@Test(expectedExceptions = FileNotFoundException.class)
	public void initConfigTestUsingFilePath() throws Exception {
		BaseService.initConfig(filePath);
	}

	@Test(expectedExceptions = FileNotFoundException.class)
	public void initConfigTestUsingFile() throws Exception {
		File file = new File(filePath);
		BaseService.initConfig(file);
	}

	@Test(expectedExceptions = FileNotFoundException.class)
	public void initConfigTestUsingInputStream() throws Exception {
		InputStream is = new FileInputStream(new File(filePath));
		BaseService.initConfig(is);
	}

}
