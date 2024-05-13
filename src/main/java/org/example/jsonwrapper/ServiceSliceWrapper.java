package org.example.jsonwrapper;

import lombok.Data;
import org.example.ServiceSlice;

@Data
public class ServiceSliceWrapper {
    private ServiceSlice data;
    private String msg;

    public ServiceSliceWrapper(ServiceSlice data, String msg) {
        this.data = data;
        this.msg = msg;
    }
}
