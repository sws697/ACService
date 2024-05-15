package org.example.jsonwrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TempJsonWrapper implements java.io.Serializable{
    public TempJson data;
}
