# java-snake
java期末作业

在基本的贪吃蛇小游戏的基础上，增加了寻路算法
之前已经写过一次python版本的贪吃蛇寻路算法，但是因为刚接触python不久，代码风格奇差..所以效果也不强
这次用java重写，效果还可以，基本可以吃到满屏，当然还存在一点小bug

基本构想如下：
在能吃到食物的情况下以最短路径去吃
  模拟吃到食物后计算还能否找到尾巴，若能找到尾巴则直接去吃，若找不到，则继续追尾巴
在不能吃到食物的情况下以最长路径去追尾巴

