package co.botechservices.novlnovl.infrastructure.exception;

import static co.botechservices.novlnovl.infrastructure.exception.ErrorCode.PARSE_JSON_FAILED;

public class JsonException extends BusinessException {

    private String json;
    private Class clazz;

    public JsonException(String json, Class clazz) {
        super(PARSE_JSON_FAILED, "Parse json failed, json = " + json + ", clazz = " + clazz.getSimpleName());
        this.json = json;
        this.clazz = clazz;
    }

    public JsonException(String json, Class outClazz, Class innerClazz) {
        super(PARSE_JSON_FAILED,
                "Parse json failed, json = " + json
                        + ", outClazz = " + outClazz.getSimpleName()
                        + ", innerClazz = " + innerClazz);
        this.json = json;
        this.clazz = outClazz;
    }

    public JsonException(Object object) {
        super(PARSE_JSON_FAILED, String.format("Parse object=%s to string failed.", object.getClass()));
    }
}
