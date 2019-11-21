package com.dydtjr1128.nfe.protocol;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BindingData {
    private long len;
    private byte protocol;
    private String payload;
}
