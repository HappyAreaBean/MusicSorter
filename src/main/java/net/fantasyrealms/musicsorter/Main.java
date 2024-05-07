package net.fantasyrealms.musicsorter;

public class Main {

    public static void main(String[] args) {

        Constant.LOGO.forEach(System.out::println);

        new MusicSorter(args);
    }

}