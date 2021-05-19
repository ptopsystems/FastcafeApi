package com.rest.api.config;

import com.rest.api.config.factory.YamlPropertySourceFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Configuration
@PropertySource(value = "classpath:bankcode.yml", factory = YamlPropertySourceFactory.class)
@ConfigurationProperties(prefix = "bankcode")
@Getter
@Setter
public class BankCodeConfig {
    private List<Bank> banks;

    public static class Bank {
        private String code;
        private String bankName;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }
    }
}
