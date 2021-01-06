package com.apiadmincore.core.config;

import nz.net.ultraq.thymeleaf.LayoutDialect;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.UrlTemplateResolver;

@EnableWebMvc
@Configuration
public class AdminViewResolverConfig implements WebMvcConfigurer 
{
   @Value("${web.assets.pattern}")
   private String assetsPattern;

   @Value("${web.assets.directory}")
   private String assetsDirectory;
   
   private static final String VIEWS = "/WEB-INF/views/";


   @Override
   public void addResourceHandlers(ResourceHandlerRegistry registry)
   {
       registry
         .addResourceHandler(assetsPattern)
         .addResourceLocations(assetsDirectory);
   }
   
   @Bean(name = "messageSource")
   public MessageSource messageSource() {
       ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
       messageSource.setBasename("/WEB-INF/i18n/messages");
       messageSource.setCacheSeconds(5);
       return messageSource;
   }

   @Bean
   public ITemplateResolver templateResolver() {
       SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
       resolver.setPrefix(VIEWS);
       resolver.setSuffix(".html");
       resolver.setTemplateMode(TemplateMode.HTML);
       resolver.setCharacterEncoding("UTF-8");
       resolver.setCacheable(false);
       return resolver;
   }

   @Bean
   public SpringTemplateEngine templateEngine() 
   {
       SpringTemplateEngine templateEngine = new SpringTemplateEngine();
       templateEngine.addTemplateResolver(new UrlTemplateResolver());
       templateEngine.addTemplateResolver(templateResolver());
       templateEngine.addDialect(new SpringSecurityDialect());
       templateEngine.addDialect(new LayoutDialect());
       templateEngine.addDialect(new Java8TimeDialect());
       return templateEngine;
   }

   @Bean
   public ViewResolver viewResolver() {
       ThymeleafViewResolver thymeleafViewResolver = new ThymeleafViewResolver();
       thymeleafViewResolver.setTemplateEngine(templateEngine());
       thymeleafViewResolver.setCharacterEncoding("UTF-8");
       return thymeleafViewResolver;
   }

   
   @Bean
   public Validator validator() 
   {
       ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
       return validatorFactory.getValidator();
   }
   
}
