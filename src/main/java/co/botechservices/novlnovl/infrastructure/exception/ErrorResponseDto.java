package co.botechservices.novlnovl.infrastructure.exception;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponseDto {
    private Integer status;
    private String message;
    private List<Error> errors;

    public void addError(String name, String msg) {
        if (ObjectUtils.isNull(this.errors)) {
            this.errors = new ArrayList<>();
        }
        Error error = Error.builder().name(name).message(msg).build();
        this.errors.add(error);
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Error {
        private String name;
        private String message;
    }
}


