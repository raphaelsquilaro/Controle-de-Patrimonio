package sp.senai.org.controle_de_almoxarifado.DTO.Request;

import sp.senai.org.controle_de_almoxarifado.model.enums.TipoMovimentacao;

public class RFIDRequest {

    private String uid;

    private String deviceId;

    public RFIDRequest() {}

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}