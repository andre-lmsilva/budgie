package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.digest.MessageDigestService;
import io.geekmind.budgie.model.entity.ImportedFile;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;

@Component
public class ImportedFileMappingSettings implements OrikaMapperFactoryConfigurer {

    private final MessageDigestService messageDigestService;

    @Autowired
    public ImportedFileMappingSettings(MessageDigestService messageDigestService) {
        this.messageDigestService = messageDigestService;
    }

    @Override
    public void configure(MapperFactory orikaMapperFactory) {
        orikaMapperFactory.classMap(MultipartFile.class, ImportedFile.class)
            .customize(new CustomMapper<MultipartFile, ImportedFile>() {
                @Override
                public void mapAtoB(MultipartFile multipartFile, ImportedFile importedFile, MappingContext context) {
                    super.mapAtoB(multipartFile, importedFile, context);
                    importedFile.setImportedAt(LocalDateTime.now());
                    importedFile.setFileName(multipartFile.getOriginalFilename());
                    importedFile.setFileMimeType(multipartFile.getContentType());
                    importedFile.setFileSize(multipartFile.getSize());

                    byte[] content;
                    try {
                        content = multipartFile.getBytes();
                    } catch (IOException ex) {
                        throw new RuntimeException(
                            String.format("It was not possible to calculate the imported file hash due the following exception :%s", ex.getMessage())
                        );
                    }

                    importedFile.setMd5FileHash(messageDigestService.md5Digest(content));
                    importedFile.setEncodedContent(messageDigestService.base64Encode(content));
                }
            }).register();
    }
}
