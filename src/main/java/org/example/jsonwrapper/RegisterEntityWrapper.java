package org.example.jsonwrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.RegisterEntity;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterEntityWrapper implements Serializable {
    public RegisterEntity data;
}
