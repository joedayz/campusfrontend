package pe.joedayz.campus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication(exclude={org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration.class} )
public class CampusApplication extends WebMvcConfigurerAdapter {

	public static void main(String[] args) {
		SpringApplication.run(CampusApplication.class, args);
	}
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {

		registry.addViewController("/login").setViewName("login");
		registry.addViewController("/recover").setViewName("recover::partial-view");

	}

}
