package com.selectionarts.projectcensio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.StringTemplateResolver;

@SpringBootApplication (exclude = { SecurityAutoConfiguration.class })
public class ProjectcensioApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectcensioApplication.class, args);
    }

    @Bean
    public TemplateEngine emailTemplateEngine() {
    	
        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();

        final StringTemplateResolver templateResolver = new StringTemplateResolver();
        templateResolver.setOrder(Integer.valueOf(3));
        // No resolvable pattern, will simply process as a String template everything not previously matched
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setCacheable(false);
        
        templateEngine.addTemplateResolver(templateResolver);

        return templateEngine;
    }
}
