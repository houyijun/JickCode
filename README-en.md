Usually, I often write spark code. After a long time, I have to search for relevant sample code on the Internet and start coding. It's been a long time, and I think it's very troublesome.

Later, I thought it would be more convenient to develop a spark code template system, drag and edit RDD nodes in a graphical way, and finally export the code automatically.


Then I began to compile. At the beginning, I had to customize many node templates, such as source, sink, map, flatmap and other operation nodes, which are all hard coded.

That's too much trouble, isn't it? Is there any way for users to write their own code output rules for each operation node.


Then JickCode project becomes what it is now.