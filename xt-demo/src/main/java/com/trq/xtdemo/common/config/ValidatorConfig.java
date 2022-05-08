package com.trq.xtdemo.common.config;

import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * 参数校验框架（Validator）配置
 * @author trq
 * @version 1.0
 * @since 2022/5/8 15:35
 */
@Configuration
public class ValidatorConfig {

    @Bean
    public Validator validator() {
        ValidatorFactory validatorFactory =
                Validation.byProvider(HibernateValidator.class)
                .configure()
                // true:开启快速校验，判断到有一个校验不通过就返回
                .failFast(false)
                .buildValidatorFactory();

        return validatorFactory.getValidator();
    }
}
