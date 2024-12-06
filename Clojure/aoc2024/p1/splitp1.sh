#!/bin/bash

awk '{print $1}' p1.txt > p1l.txt
awk '{print $2}' p1.txt > p1r.txt
chmod -w p1*.txt
