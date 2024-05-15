package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
@AllArgsConstructor
@NoArgsConstructor
@Table("register")
@Data
public class RegisterEntity implements Serializable {
    public String name;

    public @Id Long customer_id;
    public String password;

    RegisterEntity(String name,Long customer_id)
    {
        this.name = name;
        this.customer_id = customer_id;
    }
}
