package com.redfox.gesha.precompiler;

public class Precompiler {

    private String src;

    public Precompiler(String src) {
        this.src = src;
    }

    public PrecompileOutputData precompile() {
        String precompiledCode;
        int lineOffset = -1;

        precompiledCode = String.format("""
                import java.lang.*;
                %s
                """, src);

        return PrecompileOutputData.builder()
                .outputSrc(precompiledCode)
                .lineOffset(lineOffset)
                .build();
    }
}
