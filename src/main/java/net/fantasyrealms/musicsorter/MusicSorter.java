package net.fantasyrealms.musicsorter;

import jodd.net.MimeTypes;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.images.Artwork;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;

import static net.fantasyrealms.musicsorter.Constant.DEFAULT_INPUT;
import static net.fantasyrealms.musicsorter.Constant.DEFAULT_OUTPUT;
import static net.fantasyrealms.musicsorter.Constant.INVALID_CHAR;

@Slf4j
public class MusicSorter {

    public File input;
    public File output;
    public boolean generateCover;
    public boolean deleteOriginal;
    public String[] args;

    public MusicSorter(String[] args) {
        this.args = args;

        init();
        mkdir();
        checkRequirements();
        run();
    }

    private void end() {
        log.info("");
        log.info("Thank you for using MusicSorter!");
        log.info("");
    }

    @SneakyThrows
    private void run() {
        int processed = 0;

        for (File file : input.listFiles()) {

            log.info("Reading file > {}", file.getName());
            log.info("");

            AudioFile audioFile = new AudioFileIO().readFile(file);
            Tag tag = audioFile.getTag();

            log.info("");
            log.info("Processing file > {}", file.getName());

            if (tag == null) {
                log.info(" - Tag does not exist, skipping...");
                continue;
            }

            TagField artistField = tag.getFirstField(FieldKey.ARTIST);
            TagField albumField = tag.getFirstField(FieldKey.ALBUM);

            String artist = artistField.isEmpty() ? "Unknown" : artistField.toString();
            String album = albumField.isEmpty() ? "Unknown" : albumField.toString();

            log.info(" - Artist: {}", artist);
            log.info(" - Album: {}", album);

            File artistFolder = new File(output, artist.replaceAll(INVALID_CHAR, ""));
            File albumFolder = new File(artistFolder, album.replaceAll(INVALID_CHAR, ""));

            if (!artistFolder.exists())
                if (artistFolder.mkdir()) log.warn("* Created artist folder: {}", artistFolder);

            if (!albumFolder.exists())
                if (albumFolder.mkdir()) log.warn("* Created album folder: {}", albumFolder);

            if (generateCover) {
                Artwork artwork = tag.getFirstArtwork();
                if (artwork != null) {
                    String mimeType = artwork.getMimeType();
                    String ext = MimeTypes.findExtensionsByMimeTypes(mimeType, false)[0];

                    log.warn("   - Cover art found! [%s]".formatted(mimeType));
                    log.warn("   - The extension [.%s] will be used as file name.".formatted(mimeType));

                    File cover = new File(albumFolder, "cover." + ext);
                    if (!cover.exists()) {
                        log.info("   - Saving as cover.%s...".formatted(ext));
                        FileUtils.writeByteArrayToFile(cover, artwork.getBinaryData());
                    }

                }
            }

            log.info(" - Copying to the album folder: {}", albumFolder);

            FileUtils.copyFileToDirectory(file, albumFolder);

            if (deleteOriginal) {
                log.info(" - Deleting the original file in input folder: {}", file);
                Files.deleteIfExists(file.toPath());
            }

            processed++;
        }

        log.info("");
        log.info("*** All %s files has been processed. ***".formatted(processed));
        end();
    }

    private void checkRequirements() {

        if (input.listFiles().length == 0) {
            log.warn("* Your input folder is empty, put some audio file inside.");
            end();
            System.exit(0);
        }

    }

    private void mkdir() {

        if (!input.exists())
            if (input.mkdir()) log.info("* Created input folder: {}", input);

        if (!output.exists())
            if (output.mkdir()) log.info("* Created output folder: {}", output);

    }

    private void init() {
        Options options = new Options();

        Option helpOption = new Option("h", "help", false, "Show the help page");
        helpOption.setRequired(false);
        options.addOption(helpOption);

        Option inputOption = new Option("i", "input", true, "Input folder (Default: input)");
        inputOption.setRequired(false);
        inputOption.setArgName("input");
        options.addOption(inputOption);

        Option outputOption = new Option("o", "output", true, "Output folder (Default: output)");
        outputOption.setRequired(false);
        outputOption.setArgName("output");
        options.addOption(outputOption);

        Option coverOption = new Option("c", "cover", false, "Generate cover image in album folder (Default: disabled)");
        coverOption.setRequired(false);
        options.addOption(coverOption);

        Option deleteOriginalOption = new Option("d", "deleteOriginal", false, "Delete the original in input folder after copied to output folder (Default: disabled)");
        deleteOriginalOption.setRequired(false);
        options.addOption(deleteOriginalOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);

            String inputFilePath = cmd.getOptionValue(inputOption);
            String outputFilePath = cmd.getOptionValue(outputOption);
            boolean cover = cmd.hasOption(coverOption);
            boolean delete = cmd.hasOption(deleteOriginalOption);
            boolean help = cmd.hasOption(helpOption);

            if (help) {
                printHelp(options);
                System.exit(0);
            }

            if (inputFilePath == null || outputFilePath == null) {
                log.warn("* Command line not available, using default options.");
                log.warn("* You can add the following options if you want:");
                log.warn("");
                printHelp(options);
                log.info("");
            }

            input = new File(inputFilePath == null ? DEFAULT_INPUT : inputFilePath);
            output = new File(outputFilePath == null ? DEFAULT_OUTPUT : outputFilePath);
            generateCover = cover;
            deleteOriginal = delete;

        } catch (ParseException | IOException e) {
            log.info(e.getMessage());
            System.exit(0);
        }
    }

    private void printHelp(Options options) throws IOException {
        HelpFormatter formatter = new HelpFormatter();
        try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)) {
            formatter.printHelp(pw, formatter.getWidth(), "java -jar MusicSorter.jar", null, options,
                    formatter.getLeftPadding(), formatter.getDescPadding(), """
                                
                                MusicSorter  Copyright (C) 2024  HappyAreaBean
                                This program comes with ABSOLUTELY NO WARRANTY.
                                This is free software, and you are welcome to redistribute it
                                under certain conditions; check 'LICENSE' for details.""", true);
            pw.flush();
            log.info(sw.toString());
        }
    }

}
