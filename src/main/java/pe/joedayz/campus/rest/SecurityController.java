package pe.joedayz.campus.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pe.joedayz.campus.dto.RecoverPasswordDto;

@RestController
@RequestMapping("/security")
public class SecurityController {
	Logger logger = LoggerFactory.getLogger(SecurityController.class);

	@RequestMapping(value="/recover", method=RequestMethod.POST, produces="application/json")
	public String requestResetPassword(@RequestBody String username) {
		logger.info("Recovering user name for user {}", username );
		return "Success";
	}

	@RequestMapping(value="/reset-password", method=RequestMethod.POST, produces="application/json")
	public String resetPassword(@RequestBody RecoverPasswordDto recoverPasswordDto) {
		logger.info("Recovering user name for user {}", recoverPasswordDto );
		return "Success";
	}




}
