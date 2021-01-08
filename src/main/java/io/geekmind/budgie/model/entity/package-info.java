@TypeDefs({
    @TypeDef(
       name = "encryptedTextField",
       typeClass = EncryptedStringType.class,
       parameters = {
           @Parameter(name = "encryptorRegisteredName", value = JpaEncryptionConfiguration.TEXT_FIELD_ENCRYPTOR_REGISTERED_NAME)
       }
    ),
    @TypeDef(
        name = "encryptedDecimalField",
        typeClass = EncryptedBigDecimalType.class,
        parameters = {
            @Parameter(name = "encryptorRegisteredName", value = JpaEncryptionConfiguration.DECIMAL_FIELD_ENCRYPTOR_REGISTERED_NAME),
            @Parameter(name = "decimalScale", value = "2")
        }
    ),
    @TypeDef(
        name = "encryptedBinaryField",
        typeClass = EncryptedBinaryType.class,
        parameters = {
            @Parameter(name = "encryptorRegisteredName", value = JpaEncryptionConfiguration.BYTE_FIELD_ENCRYPTOR_REGISTERED_NAME)
        }
    )
})

package io.geekmind.budgie.model.entity;

import io.geekmind.budgie.configuration.JpaEncryptionConfiguration;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.jasypt.hibernate5.type.EncryptedBigDecimalType;
import org.jasypt.hibernate5.type.EncryptedBinaryType;
import org.jasypt.hibernate5.type.EncryptedStringType;