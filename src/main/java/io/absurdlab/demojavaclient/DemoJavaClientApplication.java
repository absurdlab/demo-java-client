package io.absurdlab.demojavaclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

@SpringBootApplication
public class DemoJavaClientApplication {

	@Configuration
	public static class SecurityConfiguration extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests()
				.anyRequest().authenticated()
				.and()
				.oauth2Login();
		}
	}

	private static final TrustManager[] UNQUESTIONING_TRUST_MANAGER = new TrustManager[]{
		new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers(){
				return null;
			}
			public void checkClientTrusted( X509Certificate[] certs, String authType ){}
			public void checkServerTrusted( X509Certificate[] certs, String authType ){}
		}
	};

	public static void main(String[] args) throws Exception {
		final SSLContext sc = SSLContext.getInstance("SSL");
		sc.init( null, UNQUESTIONING_TRUST_MANAGER, null );
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

		SpringApplication.run(DemoJavaClientApplication.class, args);
	}
}
