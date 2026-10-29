package com.redfox.gesha.precompiler;

public class Precompiler {

    private String src;

    public Precompiler(String src) {
        this.src = src;
    }

    public String precompile() {
        String precompiledCode;

        precompiledCode = String.format("""
                import java.lang.*;
                %s
                """, src);

        return precompiledCode;
    }
}
