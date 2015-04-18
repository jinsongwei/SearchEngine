# SearchEngine
simple search engine
===
Problems:
---
1: for the huge files, creating too many threads makes copying text of web pages to disk very slow.
solution: create the first link's threads and crawl simultaneously.(keep those threads connect to the TCP always.)
or   store documents in a single file. using HTML format <DOC> </DOC>

2: the same urls will be crawled more than once.
   solution: should use hash table to eliminate it.
	 other solution: using url as file name.
	 
3: add images (what kind of way to store )
   solution: simply store image as png which is linked in txt file. ()  
   
4:  when crawl a website, it might fail and will stop program.
   solutoin: using stable websites, eg. wikipedia.
   

question:
---
1:  when do I index? indexing when creating files or after files. (previous one more effiecincy, but not too much work)

2:  indexing title or text?



