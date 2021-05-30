package com.appleyk.rpc.core.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User{
    private Long id ;
    private String name;
    private String nickname;
    private Integer age;
}
