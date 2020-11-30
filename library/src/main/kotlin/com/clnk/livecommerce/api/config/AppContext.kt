package com.clnk.livecommerce.api.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.querydsl.jpa.impl.JPAQueryFactory
import org.modelmapper.ModelMapper
import org.modelmapper.convention.MatchingStrategies
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Configuration
class AppContext {
    @Bean
    fun modelMapper(): ModelMapper {
        val modelMapper = ModelMapper()
        modelMapper.configuration.matchingStrategy = MatchingStrategies.STANDARD
        modelMapper.configuration.isAmbiguityIgnored = false
        return modelMapper
    }

    @Bean
    fun restTemplate(): RestTemplate = RestTemplateBuilder().build()

    @PersistenceContext
    lateinit var entityManager: EntityManager

    @Bean
    fun jpaQueryFactory(): JPAQueryFactory {
        return JPAQueryFactory(entityManager);
    }

    @Bean
    fun objectMapper(): ObjectMapper {
        val mapper = ObjectMapper().registerKotlinModule()
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        return mapper
    }

}

