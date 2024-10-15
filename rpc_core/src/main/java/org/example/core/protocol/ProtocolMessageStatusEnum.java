package org.example.core.protocol;


import lombok.Getter;

@Getter
public enum ProtocolMessageStatusEnum {
    OK("ok",20),
    BAD_REQUEST("badRequest",40),
    BAD_RESPONSE("badResponse",50);

    private final String next;
    private final int value;

    ProtocolMessageStatusEnum(String next, int value) {
        this.next = next;
        this.value = value;
    }
    public static ProtocolMessageStatusEnum getEnumFromValue(int value) {
        for (ProtocolMessageStatusEnum status : ProtocolMessageStatusEnum.values()) {
            if(status.value==value){
                return status;
            }
        }
        return null;
    }
}
