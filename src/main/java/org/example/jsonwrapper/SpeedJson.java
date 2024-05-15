package org.example.jsonwrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpeedJson implements java.io.Serializable{
    public String room_id;
    public String speed;
}
