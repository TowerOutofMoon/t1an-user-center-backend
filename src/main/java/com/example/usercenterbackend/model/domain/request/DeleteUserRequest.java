package com.example.usercenterbackend.model.domain.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

import static com.example.usercenterbackend.constant.UserConstant.SERIAL_VERSION_UID;

@Data
public class DeleteUserRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;
    private long id;
}
