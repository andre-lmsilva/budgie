package io.geekmind.budgie.configuration;

import org.jasypt.encryption.pbe.PooledPBEBigDecimalEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEByteEncryptor;
import org.jasypt.hibernate5.encryptor.HibernatePBEBigDecimalEncryptor;
import org.jasypt.hibernate5.encryptor.HibernatePBEStringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JpaEncryptionConfiguration {

    public static final String TEXT_FIELD_ENCRYPTOR_REGISTERED_NAME = "textFieldEncryptor";

    public static final String DECIMAL_FIELD_ENCRYPTOR_REGISTERED_NAME = "decimalFieldEncryptor";

    @Value("${budgie.encrypt.textFieldSecretPhrase}")
    private String textFieldSecretPhrase;

    @Value("${budgie.encrypt.decimalFieldSecretPhrase}")
    private String decimalFieldSecretPhrase;

    @Bean
    public PooledPBEStringEncryptor getPooledPBEStringEncryptor() {
        PooledPBEStringEncryptor pooledPBEStringEncryptor = new PooledPBEStringEncryptor();
        pooledPBEStringEncryptor.setAlgorithm(StandardPBEByteEncryptor.DEFAULT_ALGORITHM);
        pooledPBEStringEncryptor.setKeyObtentionIterations(StandardPBEByteEncryptor.DEFAULT_KEY_OBTENTION_ITERATIONS);
        pooledPBEStringEncryptor.setPassword(this.textFieldSecretPhrase);
        pooledPBEStringEncryptor.setPoolSize(4);
        return pooledPBEStringEncryptor;
    }

    @Bean
    public PooledPBEBigDecimalEncryptor getPooledPBEBigDecimalEncryptor() {
        PooledPBEBigDecimalEncryptor pooledPBEBigDecimalEncryptor = new PooledPBEBigDecimalEncryptor();
        pooledPBEBigDecimalEncryptor.setAlgorithm(StandardPBEByteEncryptor.DEFAULT_ALGORITHM);
        pooledPBEBigDecimalEncryptor.setKeyObtentionIterations(StandardPBEByteEncryptor.DEFAULT_KEY_OBTENTION_ITERATIONS);
        pooledPBEBigDecimalEncryptor.setPassword(this.decimalFieldSecretPhrase);
        pooledPBEBigDecimalEncryptor.setPoolSize(4);
        return pooledPBEBigDecimalEncryptor;
    }

    @Bean
    public HibernatePBEStringEncryptor getPBEStringEncryptor(PooledPBEStringEncryptor pooledPBEStringEncryptor) {
        HibernatePBEStringEncryptor hibernatePBEStringEncryptor = new HibernatePBEStringEncryptor();
        hibernatePBEStringEncryptor.setEncryptor(pooledPBEStringEncryptor);
        hibernatePBEStringEncryptor.setRegisteredName(TEXT_FIELD_ENCRYPTOR_REGISTERED_NAME);
        return hibernatePBEStringEncryptor;
    }

    @Bean
    public HibernatePBEBigDecimalEncryptor getPBEBigDecimalEncryptor(PooledPBEBigDecimalEncryptor pooledPBEBigDecimalEncryptor) {
        HibernatePBEBigDecimalEncryptor hibernatePBEBigDecimalEncryptor = new HibernatePBEBigDecimalEncryptor();
        hibernatePBEBigDecimalEncryptor.setEncryptor(pooledPBEBigDecimalEncryptor);
        hibernatePBEBigDecimalEncryptor.setRegisteredName(DECIMAL_FIELD_ENCRYPTOR_REGISTERED_NAME);
        return hibernatePBEBigDecimalEncryptor;
    }

}
