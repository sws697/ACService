package org.example.jsonwrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.EnterEntity;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnterEntityWrapper implements Serializable {
    public EnterEntity data;
}
