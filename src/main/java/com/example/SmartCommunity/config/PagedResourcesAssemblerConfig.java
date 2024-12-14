package com.example.SmartCommunity.config;

import com.example.SmartCommunity.dto.UserMessageDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.PagedResourcesAssemblerArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class PagedResourcesAssemblerConfig implements WebMvcConfigurer {

        @Bean
        public PagedResourcesAssembler<UserMessageDTO> userMessageDTOAssembler() {
            return new PagedResourcesAssembler<>(null, null);
        }

        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
            resolvers.add(new PagedResourcesAssemblerArgumentResolver(new HateoasPageableHandlerMethodArgumentResolver()));
        }
}