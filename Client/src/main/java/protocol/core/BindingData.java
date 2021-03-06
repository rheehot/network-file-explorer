package protocol.core;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BindingData {
    private long len;
    private byte protocol;
    private String payload;
}
