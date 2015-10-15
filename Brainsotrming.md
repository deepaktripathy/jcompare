## There will be two folder paths to start with. ##

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
  * OLD-NEW
  * OLD-ORPHAN
  * NEW-ORPHANS

There will be a compare tree encompassing the left and right trees and will represent the combined tree.

## Algorithms will be needed for ##

  * Making a combined list of folder contents
  * Comparing the folders and files and flagging states

## Design Process to follow ##
  * Make a use case diagram
  * Component diagram
  * Analysis and class diagram
  * Start coding initial classes

## Processing Logic ##

The comparison process will require recursive processing on the given two directory paths.  This is a time consuming process and hence a UNCHECKED status is also identified for the comparison.  The comparison will set the status of all the items as UNCHECKED first and then will start the comparison process through some runnable tasks.  The process is planned to run in chunks of runnable tasks which can be kept in a queue for some processor code to take one task at a time and process.  This will provide control of the process to some external code.

This means the processing logic will be written in smaller tasks as runnable tasks which will be pushed to some processing code.  To achieve this we will need an interface which can accept the processing tasks and do the processing for us.  This processing logic will become a separate component of the library which can be replaced by any other better processing mechanism.

With each next task being processed the comparison of the whole tree will become populated with exact status of the comparison.  So the status will start with UNCHECKED state and will reach the exact status as the processing for that task is done.  There must be some mechanism to cancel the pushed processing tasks, because user may want to close the comparison or issue a different comparison in the meanwhile.

One way of doing this can be maintaining a list of all the tasks in a queue and trigger a notification through some listener to process the tasks.  Some client code can just listen to incoming tasks in the queue and when triggered process them taking them out one at a time.  Similarly this way a trigger of cancelling the tasks will also be needed.