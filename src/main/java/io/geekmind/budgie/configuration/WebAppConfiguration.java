package io.geekmind.budgie.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@EnableWebMvc
public class WebAppConfiguration implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
            .addResourceHandler("/webjars/**", "/images/**", "/css/**")
            .addResourceLocations("/webjars/", "classpath:/images/", "classpath:/css/");
    }

}
