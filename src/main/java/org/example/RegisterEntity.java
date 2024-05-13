package org.example;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("register")
public class RegisterEntity {
    public String name;
    @Id
    public Long customer_id;
    public String password;
    RegisterEntity(String name, Long customer_id, String password) {
        this.name = name;
        this.customer_id = customer_id;
        this.password = password;
    }
    RegisterEntity(String name,Long customer_id)
    {
        this.name = name;
        this.customer_id = customer_id;
    }
    RegisterEntity()
    {

    }
}
