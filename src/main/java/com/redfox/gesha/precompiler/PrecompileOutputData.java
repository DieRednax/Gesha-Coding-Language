package com.redfox.gesha.precompiler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PrecompileOutputData {
    private String outputSrc;
    private Integer lineOffset;
}
