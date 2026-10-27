package com.redfox.gesha.tokenizer.statics;

import java.util.List;

public class TokenStatics {
    public static List<String> getKeywords() {
        return List.of(
                "int",
                "long",
                "float",
                "double"
        );
    }
}
