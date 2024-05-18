package org.example;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("register")
@Data
public class RegisterEntity implements Persistable<Long>{
    public String name;

    public @Id Long customer_id;
    public String password;

    @Transient
    private boolean isNew;

    RegisterEntity(String name,Long customer_id)
    {
        this.name = name;
        this.customer_id = customer_id;
    }

    @Override
    public Long getId() {
        return customer_id;
    }

    @Override
    @Transient
    public boolean isNew() {
        return isNew||customer_id==null;
    }

    public RegisterEntity setAsNew() {
        this.isNew = true;
        return this;
    }
}
