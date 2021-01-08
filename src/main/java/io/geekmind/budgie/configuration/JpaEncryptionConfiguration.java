package io.geekmind.budgie.configuration;

import org.jasypt.encryption.pbe.PooledPBEBigDecimalEncryptor;
import org.jasypt.encryption.pbe.PooledPBEByteEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEByteEncryptor;
import org.jasypt.hibernate5.encryptor.HibernatePBEBigDecimalEncryptor;
import org.jasypt.hibernate5.encryptor.HibernatePBEByteEncryptor;
import org.jasypt.hibernate5.encryptor.HibernatePBEStringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JpaEncryptionConfiguration {

    public static final String TEXT_FIELD_ENCRYPTOR_REGISTERED_NAME = "textFieldEncryptor";

    public static final String DECIMAL_FIELD_ENCRYPTOR_REGISTERED_NAME = "decimalFieldEncryptor";

    public static final String BYTE_FIELD_ENCRYPTOR_REGISTERED_NAME = "byteFieldEncryptor";

    @Value("${budgie.encrypt.textFieldSecretPhrase}")
    private String textFieldSecretPhrase;

    @Value("${budgie.encrypt.decimalFieldSecretPhrase}")
    private String decimalFieldSecretPhrase;

    @Value("${budgie.encrypt.byteFieldSecretPhrase}")
    private String byteFieldSecretPhrase;

    @Bean
    public PooledPBEStringEncryptor getPooledPBEStringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setAlgorithm(StandardPBEByteEncryptor.DEFAULT_ALGORITHM);
        encryptor.setKeyObtentionIterations(StandardPBEByteEncryptor.DEFAULT_KEY_OBTENTION_ITERATIONS);
        encryptor.setPassword(this.textFieldSecretPhrase);
        encryptor.setPoolSize(4);
        return encryptor;
    }

    @Bean
    public PooledPBEBigDecimalEncryptor getPooledPBEBigDecimalEncryptor() {
        PooledPBEBigDecimalEncryptor encryptor = new PooledPBEBigDecimalEncryptor();
        encryptor.setAlgorithm(StandardPBEByteEncryptor.DEFAULT_ALGORITHM);
        encryptor.setKeyObtentionIterations(StandardPBEByteEncryptor.DEFAULT_KEY_OBTENTION_ITERATIONS);
        encryptor.setPassword(this.decimalFieldSecretPhrase);
        encryptor.setPoolSize(4);
        return encryptor;
    }

    @Bean
    public PooledPBEByteEncryptor getPooledPBEByteEncryptor() {
        PooledPBEByteEncryptor encryptor = new PooledPBEByteEncryptor();
        encryptor.setAlgorithm(StandardPBEByteEncryptor.DEFAULT_ALGORITHM);
        encryptor.setKeyObtentionIterations(StandardPBEByteEncryptor.DEFAULT_KEY_OBTENTION_ITERATIONS);
        encryptor.setPassword(this.byteFieldSecretPhrase);
        encryptor.setPoolSize(4);
        return encryptor;
    }

    @Bean
    public HibernatePBEStringEncryptor getPBEStringEncryptor(PooledPBEStringEncryptor stringEncryptor) {
        HibernatePBEStringEncryptor encryptor = new HibernatePBEStringEncryptor();
        encryptor.setEncryptor(stringEncryptor);
        encryptor.setRegisteredName(TEXT_FIELD_ENCRYPTOR_REGISTERED_NAME);
        return encryptor;
    }

    @Bean
    public HibernatePBEBigDecimalEncryptor getPBEBigDecimalEncryptor(PooledPBEBigDecimalEncryptor decimalEncryptor) {
        HibernatePBEBigDecimalEncryptor encryptor = new HibernatePBEBigDecimalEncryptor();
        encryptor.setEncryptor(decimalEncryptor);
        encryptor.setRegisteredName(DECIMAL_FIELD_ENCRYPTOR_REGISTERED_NAME);
        return encryptor;
    }

    @Bean
    public HibernatePBEByteEncryptor getPBEBByteEncryptor(PooledPBEByteEncryptor byteEncryptor) {
        HibernatePBEByteEncryptor encryptor = new HibernatePBEByteEncryptor();
        encryptor.setEncryptor(byteEncryptor);
        encryptor.setRegisteredName(BYTE_FIELD_ENCRYPTOR_REGISTERED_NAME);
        return encryptor;
    }

}
