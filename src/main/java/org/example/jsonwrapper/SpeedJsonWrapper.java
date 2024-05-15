package org.example.jsonwrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpeedJsonWrapper implements java.io.Serializable{
    public SpeedJson data;
}
