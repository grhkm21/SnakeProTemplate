# SnakeProTemplate

This is a Snake game I wrote in about two week time for my high school CS project. It uses javax.swing for graphics and java.awt for events.

## Directory Structure

This is the `src/` directory structure. Code in `src/main/java/` is separated into three packages, `Controller`, `Model` and `View`. There are also replaceable resources under `src/main/resources`, such as sprites and sound files.

There are also test files under `src/test/java/`. They are JUnit tests to check certain functionalities are working. To use them, check the below instructions.

![src directory structure][srcTree]

These are the `.sh` files under `build/`

![build/\*.sh structure][buildTree]

[srcTree]: ./srcTree.png "srcTree"
[buildTree]: ./buildTree.png "buildTree"

## What are the build/scripts/\*.sh files?
The `.sh` files are there to help the user use the code! There are currently four of them:
  * `build/scripts/compileEverything.sh` is to (re-)compile both the game source and the unit test codes.
  * `build/scripts/compileGame.sh` only compiles the game source under `src/main/java/`. Use this to test the game.
  * `build/scripts/test.sh` runs the unit tests against the code.
    * Usage: `build/scripts/test.sh [test1] [test2] [test3] ...` or `build/scripts/test.sh -all`
    * Calling `build/scripts/test.sh` without any arguments brings up a help menu.
  * `build/scripts/clean.sh` removes all class files and jar files.
    * Mainly used by me to clean up my compilation files.

## How to use the build/scripts/\*.sh files
1. Open Finder, go to `/Applications/`, go into `Utilities/` and find `Terminal.app`. Open it.
  * An alternative is to open `Spotlight` and search for `Terminal`. Hit <kbd>Return</kbd>.
2. If you haven't, download this code as a `zip`, unzip it and open it in a `Finder`. Locate the `.sh` files.
3. Now in your `Terminal`, type `chmod +x `.
4. Now drag your `.sh` file into the `Terminal`. Hit <kbd>Return</kbd>.
5. Repeat step 4 without typing anything before it. Hit <kbd>Return</kbd>.
  * If you are using `build/scripts/test.sh`, this will bring up a help menu. Repeat the drag and drop process following the instructions.
6. It should compile and run the relevant code now!
7. If you put any arguments (e.g. a dash `-`) after the command `something.sh`, it will not run the compiled code!
  * Example: `build/scripts/compileGame.sh -`
