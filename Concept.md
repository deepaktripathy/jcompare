#Concept of JCompare Library.

# Introduction #

JCompare is a library which provides functionality of comparing given two directories recursively and provide comparison of the folders.  This makes getting the differences of the two folder paths like newer files, orphan files


# Details #

There will be two folder paths to start with.

We will fill the tree structure with the folder name as the top node and the children of that folder as the child nodes of the root.  The trees will only be filled till here.

Each node from the left tree will be compared with corresponding node of the right tree and flagged with a status

Statuses for files:
  * UNCHECKED
  * SAME
  * OLD
  * NEW
  * ORPHAN

Status for folders:
  * UNCHECKED
  * SAME
  * ORPHAN
  * NEW
  * OLD

Folders can have multiple statuses from the status list

There will be a compare tree encompassing the left and right trees and will represent the combined tree.

Algorithms will be needed for

  * Making a combined list of folder contents
  * Comparing the folders and files and flagging states