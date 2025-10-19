package per.liyl.enums;

import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Optional;

public enum AppleUnitEnum {

    NUMBER("1", "个"),
    JIN("2", "市斤"),
    KILOGRAM("3", "公斤"),
    ;

    AppleUnitEnum(String key, String value){
        this.key = key;
        this.value = value;
    }

    private String key;

    private String value;

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static String findValueByKey(String key){
        return Optional.ofNullable(key)
                .filter((k) -> !StringUtils.isEmpty(k))
                .map((k) -> {
                    AppleUnitEnum[] enumConstants = AppleUnitEnum.class.getEnumConstants();
                    String value = Arrays.stream(enumConstants)
                            .filter((enums) -> {
                                return key.equals(enums.getKey());
                            })
                            .map(AppleUnitEnum::getValue)
                            .findFirst()
                            .orElse(null);
                    return value;
                })
                .orElse(null);
    }

    public static String findKeyByValue(String value){
        return Optional.ofNullable(value)
                .filter((k) -> !StringUtils.isEmpty(k))
                .map((k) -> {
                    AppleUnitEnum[] enumConstants = AppleUnitEnum.class.getEnumConstants();
                    String key = Arrays.stream(enumConstants)
                            .filter((enums) -> {
                                return value.equals(enums.getValue());
                            })
                            .map(AppleUnitEnum::getValue)
                            .findFirst()
                            .orElse(null);
                    return key;
                })
                .orElse(null);
    }
}
