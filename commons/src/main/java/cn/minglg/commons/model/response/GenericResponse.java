package cn.minglg.commons.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName:R
 * Package:cn.minglg.authentication.response
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/7/10
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenericResponse<T> {
    private Integer code;
    private String message;
    private T data;
}
