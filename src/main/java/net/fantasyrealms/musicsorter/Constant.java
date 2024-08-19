package net.fantasyrealms.musicsorter;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class Constant {

    public static final String VERSION = "1.0.2";
    public static final String DEFAULT_INPUT = "input";
    public static final String DEFAULT_OUTPUT = "output";
    public static final String INVALID_CHAR = "[\\\\/:*?\"<>|]";

    public static final List<String> LOGO = List.of("",
            "  __  __           _       _____            _            ",
            " |  \\/  |         (_)     / ____|          | |           ",
            " | \\  / |_   _ ___ _  ___| (___   ___  _ __| |_ ___ _ __ ",
            " | |\\/| | | | / __| |/ __|\\___ \\ / _ \\| '__| __/ _ \\ '__|",
            " | |  | | |_| \\__ \\ | (__ ____) | (_) | |  | ||  __/ |   ",
            " |_|  |_|\\__,_|___/_|\\___|_____/ \\___/|_|   \\__\\___|_|          v" + Constant.VERSION,
            "                                                         ",
            "   A simple Java application for organizing your music archives.",
            "                        by HappyAreaBean                 ",
            "                      https://happyareabean.cc           ",
            "                                                         ",
            "");

}
