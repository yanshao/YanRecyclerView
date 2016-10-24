# YanRecyclerView
https://github.com/yanshao
RecyclerView问世已经很久了，但是我还没在实际的项目中使用过，所以前几天看了一下RecyclerView。 
然后在网上找了N久都没找到合适的RecyclerView下啦刷新和上啦加载更多，很多的demo都是依赖SwipeRefreshLayout去实现的，也找到一些自定义的下啦刷新，但是我感觉有点复杂（菜鸟看不懂别人的代码 ） ，所以就自己动手写了这个控件（也可以叫组合控件）。下面我说一下我的实现思路：


如上图所示黑色部分为RelativeLayout，红色部分为header的布局，绿色为footer的布局，黄色部分为RecyclerView。
默认header和footer为隐藏的。
然后去监听RecyclerView的手势并做判断：
如果手势是下拉的（endy-stary）>0并且RecyclerView是在顶部的情况下，显示header并根据手指下划的距离设置RelativeLayout的上边距。那么整个布局就会根据手指滑动的距离慢慢的下移。
上啦同理：
手势是上啦（endy-stary）<0并且RecyclerView是在最底部的情况下，显示footer并根据手指下划的距离设置RelativeLayout的下边距。那么整个布局就会根据手指滑动的距离慢慢的上移。
  
当手指松开时隐藏header或者footer 并设置RelativeLayout的上下边距都为0，那么我们就实现了手指的滑动和松开时的界面变化。


剩下的就是刷新或者加载更多的状态变化的判断。我们只需要定义3个值，当手指下拉或者上啦时为状态1，松开加载中状态2    默认什么都没有的情况下为0；


然后再根据以上状态值得定义去在手势判断里面改变为响应的值即可。


最后一点 就是自己去实现接口取做下拉刷新和上啦加载更多。








注意：在这个自定义的组合控件中 需要个给RecyclerView.setadapter();和设置RecyclerView的布局。。


效果图：
https://github.com/yanshao/YanRecyclerView/blob/master/%E6%95%88%E6%9E%9C.mp4
