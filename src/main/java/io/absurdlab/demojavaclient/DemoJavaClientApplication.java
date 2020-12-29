package io.absurdlab.demojavaclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.oidc.authentication.OidcIdTokenDecoderFactory;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoderFactory;

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
			// 1. By default, OidcUserService is used to resolve userinfo, and it skips fetching /userinfo. So it
			// fetching /userinfo is desired, implement OAuth2UserService
			http.authorizeRequests()
				.anyRequest().authenticated()
				.and()
				.oauth2Login();
		}

		@Bean
		public JwtDecoderFactory<ClientRegistration> idTokenDecoderFactory() {
			OidcIdTokenDecoderFactory idTokenDecoderFactory = new OidcIdTokenDecoderFactory();
			idTokenDecoderFactory.setJwsAlgorithmResolver(clientRegistration -> SignatureAlgorithm.ES256);
			return idTokenDecoderFactory;
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
