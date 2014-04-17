#!/bin/bash

java -cp .:class:lib/* -Djava.library.path=native/linux george.George
