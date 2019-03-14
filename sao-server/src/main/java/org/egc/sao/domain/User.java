package org.egc.sao.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Transient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class User {
    UUID id;
    String name;
    String password;
    String email;
    LocalDateTime createdOn;

    @Transient
    ArrayList<String> roles=new ArrayList<>();
}
