package developer.github.factory.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PreDestroy;

/**
 * @package: developer.github.factory
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月11日 上午10:46
 * @description:
 */
@Slf4j
@Data
public class ObjectMapperFactory {

    private ObjectMapper objectMapper;

    public ObjectMapper get() {
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_INDEX);
        return objectMapper;
    }

    @PreDestroy
    public void destroy() {
        objectMapper = null;
    }
}
