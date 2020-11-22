package com.qris.payment.controller;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.qris.payment.model.ClientUserModel;
import com.qris.payment.oauth.JwtTokenUtil;
import com.qris.payment.plugin.CryptoIntra;
import com.qris.payment.service.JwtUserDetailsService;


@RestController
@CrossOrigin
@RequestMapping("/payment")
public class JwtAuthenticationController {
	private final static Logger logger = LoggerFactory.getLogger(JwtAuthenticationController.class);

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private CryptoIntra generated;
	@Autowired
	private JwtUserDetailsService clienservices;
	
	@RequestMapping(value = "/auth/token", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody String grant_type, HttpServletRequest header)   {
			String Authorization = header.getHeader("Authorization").substring(6);
			
			 ClientUserModel usersClient = clienservices.check(Authorization);
			
			return  null;
			
	}
	
	
	
	
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<?> saveUser(@RequestBody JsonNode user, HttpServletRequest request)throws Exception {
       	 		String clientIpAddress = request.getHeader("X-FORWARDED-FOR");
				if (clientIpAddress == null || clientIpAddress.equals(""))
					clientIpAddress = request.getRemoteAddr();
			return ResponseEntity.ok(clienservices.save(user,clientIpAddress));
	}
	

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}

}
