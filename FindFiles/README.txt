Parth Patel
20677443 pp5patel
openjdk version "11.0.8_10"
Windows 10

FindFiles is a commanline application to find files easily in directory with clogged folder structure.

Usage: java FindFiles filetofind [-option arg]
filetofind: file name of file to find
Option flags:
    -help                     :: print out a help page and exit the program.
    -r                        :: execute the command recursively in subfiles.
    -reg                      :: treat `filetofind` as a regular expression when searching.
    -dir [directory]          :: find files starting in the specified directory.
    -ext [ext1,ext2,...]      :: find files matching [filetofind] with extensions [ext1, ext2,...].
args: Argument to give to the above options
