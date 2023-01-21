# grammar_checker
This program checks if a word (given by the user) can be produced by a Grammar (read from a file given by the user),
using Depth-First Search [1].
<br>
A grammar example file has been provided to play with.
<br>
Note: project requires *java* to be installed.

# Usage
```
% java Main.java
```

# Execution example
```
❯ java Main.java
Give file name:
Grammar.txt
Enter a word:
12321
Word is not valid for the specific Grammar. Try again.

Enter a word:
101001
HOORAY!! The word can be produced by the specified Grammar.
```

<br>
References:
<br>
[1] https://en.wikipedia.org/wiki/Depth-first_search
