package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.digest.MessageDigestService;
import io.geekmind.budgie.exception.ImportedFileContentAccessException;
import io.geekmind.budgie.model.dto.DependantAccountRecord;
import io.geekmind.budgie.model.entity.ImportedFile;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ImportedFileMappingTest {

    @Mock
    private MessageDigestService messageDigestService;

    @InjectMocks
    private ImportedFileMappingSettings importedFileMappingSettings;

    private MapperFacade mapperFacade;

    @Before
    public void setUp() {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();
        this.importedFileMappingSettings.configure(factory);
        this.mapperFacade = factory.getMapperFacade();
    }

    @Test
    public void mapping_FromMultipartFileToImportedFile_MapsFields() throws IOException {
        MultipartFile fakeMultiPartFile = mock(MultipartFile.class);
        doReturn("FakeFile.csv").when(fakeMultiPartFile).getOriginalFilename();
        doReturn("fakeContentType").when(fakeMultiPartFile).getContentType();
        doReturn(123L).when(fakeMultiPartFile).getSize();
        doReturn("fakeContent".getBytes()).when(fakeMultiPartFile).getBytes();
        doReturn("fakeHash").when(this.messageDigestService).md5Digest(eq("fakeContent".getBytes()));
        doReturn("fakeEncodedContent").when(this.messageDigestService).base64Encode(eq("fakeContent".getBytes()));

        ImportedFile result = this.mapperFacade.map(fakeMultiPartFile, ImportedFile.class);

        assertThat(result)
            .hasFieldOrPropertyWithValue("id", null)
            .hasFieldOrPropertyWithValue("fileName", "FakeFile.csv")
            .hasFieldOrPropertyWithValue("fileMimeType", "fakeContentType")
            .hasFieldOrPropertyWithValue("fileSize", 123L)
            .hasFieldOrPropertyWithValue("md5FileHash", "fakeHash")
            .hasFieldOrPropertyWithValue("encodedContent", "fakeEncodedContent");

        assertThat(result.getImportedAt()).isNotNull();
    }

    @Test
    public void mapping_WithIssuesToAccessMultipartFileContent_ThrowsImportedFileContentAccessException() throws IOException {
        MultipartFile fakeMultiPartFile = mock(MultipartFile.class);
        doReturn("FakeFile.csv").when(fakeMultiPartFile).getOriginalFilename();
        doReturn("fakeContentType").when(fakeMultiPartFile).getContentType();
        doReturn(123L).when(fakeMultiPartFile).getSize();

        IOException fakeException = new IOException("Fake Exception");
        doThrow(fakeException).when(fakeMultiPartFile).getBytes();

        assertThatThrownBy(() -> this.mapperFacade.map(fakeMultiPartFile, ImportedFile.class))
            .isInstanceOf(ImportedFileContentAccessException.class)
            .hasCause(fakeException);
    }

}
