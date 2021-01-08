package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.model.dto.attachments.ExistingAttachment;
import io.geekmind.budgie.model.entity.Attachment;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;
import org.springframework.stereotype.Component;

@Component
public class AttachmentMappingSettings implements OrikaMapperFactoryConfigurer {
    @Override
    public void configure(MapperFactory orikaMapperFactory) {
        orikaMapperFactory.classMap(Attachment.class, ExistingAttachment.class)
            .mapNulls(false)
            .mapNullsInReverse(false)
            .customize(new CustomMapper<Attachment, ExistingAttachment>() {
                @Override
                public void mapAtoB(Attachment attachment, ExistingAttachment existingAttachment, MappingContext context) {
                    super.mapAtoB(attachment, existingAttachment, context);
                    existingAttachment.setRecordId(attachment.getRecord().getId());
                }
            })
            .byDefault()
            .register();
    }
}
