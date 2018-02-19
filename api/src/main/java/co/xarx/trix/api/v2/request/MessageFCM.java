package co.xarx.trix.api.v2.request;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
public class MessageFCM {
    public String id;
    public String deviceCode;
}
