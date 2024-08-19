![][logo]

# MusicSorter

MusicSorter is a simple Java command line application for organizing your music archives. It helps you sort and organize
your music files based on artist and album information.

## Features

* Sorts music files into folders by artist and album

* Generates cover art images for albums (optional)

* Supports various audio file formats

* Command-line interface for easy use

## Installation

1. Ensure you have **Java 17+** installed on your system.

2. Download the latest release of MusicSorter.jar from the [release](https://github.com/HappyAreaBean/MusicSorter/releases) page.

## Usage

```
java -jar MusicSorter.jar [options]
```

### Command-line Options

* `-h, --help`: Show the help page

* `-i, --input` : Input folder (Default: input)

* `-o, --output  `: Output folder (Default: output)

* `-c, --cover`: Generate cover image in album folder (Default: disabled)

* `-d, --deleteOriginal`: Delete the original audio files in input folder after copied to output folder (Default: disabled)

## License

MusicSorter is open-source software licensed under the [GPL-3.0 license](LICENSE).

## Author

Created by HappyAreaBean, Website: [https://happyareabean.cc](https://happyareabean.cc)

[logo]: https://image.happyareabean.cc/u/DrfqXQmOBVfXAsNZ.png