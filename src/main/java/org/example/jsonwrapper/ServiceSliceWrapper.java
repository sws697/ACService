package org.example.jsonwrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ServiceSlice;

@Data
@NoArgsConstructor
public class ServiceSliceWrapper implements java.io.Serializable{
    private ServiceSlice data;
    private String msg;

    public ServiceSliceWrapper(ServiceSlice data, String msg) {
        this.data = data;
        this.msg = msg;
    }
}
