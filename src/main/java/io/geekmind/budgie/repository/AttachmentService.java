package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.entity.Artifact;
import io.geekmind.budgie.model.entity.Attachment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

@Service
public class AttachmentService {

    private final RecordRepository recordRepository;
    private final AttachmentRepository attachmentRepository;
    private final ArtifactRepository artifactRepository;

    public AttachmentService(RecordRepository recordRepository,
                             AttachmentRepository attachmentRepository,
                             ArtifactRepository artifactRepository) {
        this.recordRepository = recordRepository;
        this.attachmentRepository = attachmentRepository;
        this.artifactRepository = artifactRepository;
    }

    @Transactional
    public void create(Integer recordId, MultipartFile attachmentFile) {
        Attachment attachment = new Attachment();
        attachment.setRecord(this.recordRepository.getOne(recordId));
        attachment.setAttachmentName(attachmentFile.getOriginalFilename());
        attachment.setMimeType(attachmentFile.getContentType());

        BigDecimal sizeKb = BigDecimal
            .valueOf(attachmentFile.getSize())
            .divide(BigDecimal.valueOf(1024L), RoundingMode.HALF_UP);
        attachment.setSizeKb(sizeKb);

        Artifact artifact = null;
        try {
            artifact = this.getArtifact(attachmentFile.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        attachment = this.attachmentRepository.save(attachment);
        artifact.setAttachment(attachment);
        this.artifactRepository.saveAndFlush(artifact);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    protected Artifact getArtifact(InputStream attachmentContent) throws IOException {
        Artifact artifact = new Artifact();

        ByteArrayOutputStream compressedOutput = new ByteArrayOutputStream();
        DeflaterOutputStream compressingStream = new DeflaterOutputStream(compressedOutput);
        int nByte;
        while((nByte = attachmentContent.read()) != -1) {
            compressingStream.write((byte)nByte);
            compressingStream.flush();
        }
        compressingStream.finish();
        artifact.setContent(compressedOutput.toByteArray());
        compressingStream.close();
        compressedOutput.close();
        return artifact;
    }

    @Transactional
    public Integer delete(Integer attachmentId) {
        Attachment attachment = this.attachmentRepository.getOne(attachmentId);
        this.attachmentRepository.delete(attachment);
        return attachment.getRecord().getId();
    }

    public void downloadFile(Integer attachmentId, HttpServletResponse response) throws IOException {
        Artifact artifact = this.artifactRepository.getOne(attachmentId);
        response.setContentType(artifact.getAttachment().getMimeType());
        response.addHeader("Content-Disposition", String.format("attachment; filename=%s", artifact.getAttachment().getAttachmentName()));

        ByteArrayInputStream compressedContentWrapper = new ByteArrayInputStream(artifact.getContent());
        InflaterInputStream compressedContent = new InflaterInputStream(compressedContentWrapper);
        int nByte;
        while ((nByte = compressedContent.read()) != -1) {
            response.getOutputStream().write((byte)nByte);
            response.getOutputStream().flush();
        }
        compressedContent.close();
        compressedContentWrapper.close();
    }

}
